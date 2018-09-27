package com.andyhawkes.chronic;

import static org.junit.Assert.assertTrue;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.TTCCLayout;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TimeSeriesTest {
	@Before
	public void configureLog4j() {
		Logger.getLogger("com.andyhawkes").addAppender(new ConsoleAppender(new TTCCLayout()));
		Logger.getLogger("com.andyhawkes").setLevel(Level.DEBUG);
	}

	@Test
	public void testHighestValueTimeSeries() {
		TimeSeries series = new HighestValueTimeSeries(3000);

		series.addValue(13, 3.7);
		series.addValue(1280, 7.4);
		series.addValue(1332, 0.6);
		series.addValue(2640, 2.5);
		series.addValue(3000, 3.7);
		series.addValue(3955, 6.4);
		series.addValue(4823, 3.2);
		series.addValue(5505, 3.5);
		series.addValue(5999, 2.5);
		series.addValue(9811, 6.8);

		assertTrue(series.getValue(3000) == 6.4);
		assertTrue(series.getValue(2999) == 7.4);
		assertTrue(series.getValue(5999) == 6.4);
		assertTrue(series.getValue(6000) == 0.0);
		assertTrue(series.getValue(10000) == 6.8);
		assertTrue(Double.isNaN(series.getValue(13000)));

		Assert.assertEquals(0.0, series.getMinValue(), 0.0001);
		Assert.assertEquals((7.4 + 6.4 + 0.0 + 6.8) / 4, series.getAvgValue(), 0.0001);
		Assert.assertEquals(7.4, series.getMaxValue(), 0.0001);

		Assert.assertEquals(11999, series.getLatestTime());
	}

	@Test
	public void testSummingTimeSeries() {
		TimeSeries series = new SummingTimeSeries(3000);

		series.addValue(13, 3.7);
		series.addValue(1280, 7.4);
		series.addValue(1332, 0.6);
		series.addValue(2640, 2.5);
		series.addValue(3000, 3.7);
		series.addValue(3955, 6.4);
		series.addValue(4823, 3.2);
		series.addValue(5505, 3.5);
		series.addValue(5999, 2.5);
		series.addValue(9811, 6.8);

        Assert.assertEquals(series.getValue(2999), 14.2, 0.001);
        Assert.assertEquals(series.getValue(3000), 19.3, 0.001);
		Assert.assertEquals(series.getValue(5999), 19.3, 0.001);
		Assert.assertEquals(series.getValue(6000), 0.0, 0.001);
		Assert.assertEquals(series.getValue(10000), 6.8, 0.001);
		Assert.assertTrue(Double.isNaN(series.getValue(13000)));

		Assert.assertEquals(0.0, series.getMinValue(), 0.0001);
		Assert.assertEquals((14.2 + 19.3 + 0.0 + 6.8) / 4, series.getAvgValue(), 0.0001);
		Assert.assertEquals(19.3, series.getMaxValue(), 0.0001);

		Assert.assertEquals(11999, series.getLatestTime());
	}

	@Test
	public void testAveragingTimeSeries() {
		AveragingTimeSeries series = new AveragingTimeSeries(4000);

		series.addValue(13, 3.7);
		series.addValue(1280, 7.4);
		series.addValue(1332, 0.6);
		series.addValue(2640, 2.5);
		series.addValue(3000, 3.7);
		series.addValue(3955, 6.4);
		series.addValue(4823, 3.2);
		series.addValue(5505, 3.5);
		series.addValue(5999, 2.6);
		series.addValue(9811, 6.8);

		Assert.assertEquals(series.getValue(0), 4.05, 0.0001);
		Assert.assertEquals(series.getValue(3000), 4.05, 0.0001);
		Assert.assertEquals(series.getValue(3999), 4.05, 0.0001);
		Assert.assertEquals(series.getValue(4000), 3.1, 0.0001);
		Assert.assertEquals(series.getValue(7999), 3.1, 0.0001);
		Assert.assertEquals(series.getValue(10000), 6.8, 0.0001);
		Assert.assertTrue(Double.isNaN(series.getValue(13000)));

		Assert.assertEquals(6, series.getWeight(1000));
		Assert.assertEquals(3, series.getWeight(6000));
		Assert.assertEquals(1, series.getWeight(9000));
		Assert.assertEquals(0, series.getWeight(13000));

		Assert.assertEquals(3.1, series.getMinValue(), 0.0001);
		Assert.assertEquals((4.05 + 3.1 + 6.8) / 3, series.getAvgValue(), 0.0001);
		Assert.assertEquals(6.8, series.getMaxValue(), 0.0001);

		Assert.assertEquals(11999, series.getLatestTime());
	}

	@Test
	public void testWeightedAveragingTimeSeries() {
		WeightedAveragingTimeSeries series = new WeightedAveragingTimeSeries(4000);

		series.addValue(13, 3.7);
		series.addValue(1280, 7.4);
		series.addValue(1332, 0.6);
		series.addValue(2640, 2.5);
		series.addValue(3000, 3.7);
		series.addValue(3955, 6.4);
		series.addValue(4823, 3.2);
		series.addValue(5505, 3.5);
		series.addValue(5999, 2.6);
		series.addValue(9811, 6.8);

		Assert.assertEquals(series.getValue(0), 4.05, 0.0001);
		Assert.assertEquals(series.getValue(3000), 4.05, 0.0001);
		Assert.assertEquals(series.getValue(3999), 4.05, 0.0001);
		Assert.assertEquals(series.getValue(4000), 3.1, 0.0001);
		Assert.assertEquals(series.getValue(7999), 3.1, 0.0001);
		Assert.assertEquals(series.getValue(10000), 6.8, 0.0001);
		Assert.assertTrue(Double.isNaN(series.getValue(13000)));

		Assert.assertEquals(6, series.getWeight(1000));
		Assert.assertEquals(3, series.getWeight(5000));
		Assert.assertEquals(1, series.getWeight(9000));
		Assert.assertEquals(0, series.getWeight(13000));

		Assert.assertEquals(0.6, series.getMinValue(), 0.0001);
		Assert.assertEquals(4.04, series.getAvgValue(), 0.0001);
		Assert.assertEquals(7.4, series.getMaxValue(), 0.0001);

		Assert.assertEquals(11999, series.getLatestTime());
	}

	@Test
	public void testWeightedAveragingTimeSeriesLotsOfData() {
		TimeSeries series = new WeightedAveragingTimeSeries(4000);

		for (long t = 0; t < 6 * 3600000; t += 15000) {
			series.addValue(t, 0.372);
		}

		Assert.assertEquals(0.372, series.getMinValue(), 0.0001);
		Assert.assertEquals(0.372, series.getAvgValue(), 0.0001);
		Assert.assertEquals(0.372, series.getMaxValue(), 0.0001);
	}

	@Test
	public void testRunningTotalTimeSeries() {
		TimeSeries series = new RunningTotalTimeSeries(700);

		series.addValue(13, 3.7);
		series.addValue(1280, 7.4);
		series.addValue(1332, 0.6);
		series.addValue(2640, 2.5);
		series.addValue(3000, 3.7);
		series.addValue(3955, 6.4);
		series.addValue(4823, 3.2);
		series.addValue(5505, 3.5);
		series.addValue(5999, 2.6);
		series.addValue(9811, 6.8);
		series.addValue(72300, 10.0);

		Assert.assertEquals(series.getValue(0), 3.7, 0.0001);
		Assert.assertEquals(series.getValue(2000), 11.7, 0.0001);
		Assert.assertEquals(series.getValue(2790), 14.2, 0.0001);
		Assert.assertEquals(series.getValue(10000), 40.4, 0.0001);
		Assert.assertEquals(series.getValue(30000), 40.4, 0.0001);
		Assert.assertEquals(series.getValue(72300), 50.4, 0.0001);
		Assert.assertEquals(series.getValue(100000), 50.4, 0.0001);

		Assert.assertEquals(3.7, series.getMinValue(), 0.0001);
		Assert.assertEquals(38.36154, series.getAvgValue(), 0.0001);
		Assert.assertEquals(50.4, series.getMaxValue(), 0.0001);

		Assert.assertEquals(72799, series.getLatestTime());
	}

	@Test
	public void testOptimizedRunningTotalTimeSeries() {
		TimeSeries series = new OptimizedRunningTotalTimeSeries(700, 1000);

		series.addValue(13, 3.7);
		series.addValue(1280, 7.4);
		series.addValue(1332, 0.6);
		series.addValue(2640, 2.5);
		series.addValue(3000, 3.7);
		series.addValue(3955, 6.4);
		series.addValue(4823, 3.2);
		series.addValue(5505, 3.5);
		series.addValue(5999, 2.6);
		series.addValue(9811, 6.8);
		series.addValue(72300, 10.0);

		Assert.assertEquals(series.getValue(0), 3.7, 0.0001);
		Assert.assertEquals(series.getValue(2000), 11.7, 0.0001);
		Assert.assertEquals(series.getValue(2790), 14.2, 0.0001);
		Assert.assertEquals(series.getValue(10000), 40.4, 0.0001);
		Assert.assertEquals(series.getValue(30000), 40.4, 0.0001);
		Assert.assertEquals(series.getValue(72300), 50.400, 0.0001);
		Assert.assertEquals(series.getValue(100000), 50.400, 0.0001);

		Assert.assertEquals(3.7, series.getMinValue(), 0.0001);
		Assert.assertEquals(38.36154, series.getAvgValue(), 0.0001);
		Assert.assertEquals(50.4, series.getMaxValue(), 0.0001);

		Assert.assertEquals(72799, series.getLatestTime());
	}

	@Test
	public void testSmartSlope() {
		TimeSeries series = new HighestValueTimeSeries(1000);

		series.addValue(1500, 12);

		assertTrue("Smart slope at 1500 should be 0 (too early)", series.getSmartSlope(1500) == 0);

		series.addValue(6500, 17);

		assertTrue("Smart slope at 6500 should be .001 (going back 5 slots)", series.getSmartSlope(6500) == .001);

		series.addValue(7500, 19);

		assertTrue("Smart slope at 9500 should be 0 (going back 2 slots, unchanged)", series.getSmartSlope(9500) == 0);

		series.addValue(9500, 21);

		assertTrue("Smart slope at 9900 should be .001 (going back 2 slots)", series.getSmartSlope(9900) == .001);

		series.addValue(10000, 28);

		assertTrue("Smart slope at 10500 should be .001 (going back 3 slots))", series.getSmartSlope(10500) == .003);
	}

	@Test
	public void testPercentileTimeSeries() {
		TimeSeries p50 = new PercentileTimeSeries(1000, 50);
		TimeSeries p80 = new PercentileTimeSeries(1000, 80);
		TimeSeries p90 = new PercentileTimeSeries(1000, 90);

		for (int i = 0; i < 100; i++) {
			p50.addValue(1000, i);
			p80.addValue(1000, i);
			p90.addValue(1000, i);
		}

		Assert.assertEquals("50th percentile score should be 49.5", 49.5, p50.getValue(1000), .000001);
		Assert.assertEquals("80th percentile score should be 79.8", 79.8, p80.getValue(1000), .000001);
		Assert.assertEquals("90th percentile score should be 89.9", 89.9, p90.getValue(1000), .000001);
	}

	@Test
	public void testPercentileTimeSeriesTooSmall() {
		TimeSeries p80 = new PercentileTimeSeries(1000, 80);

		p80.addValue(1000, 4);
		p80.addValue(1000, 5);
		p80.addValue(1000, 6);

		Assert.assertEquals("80th percentile should be 6", 6, p80.getValue(1000), .000001);

		p80.addValue(1000, 12);
		p80.addValue(1000, 100);

		Assert.assertEquals("80th percentile should be 82.4", 82.4, p80.getValue(1000), .000001);
	}
}
