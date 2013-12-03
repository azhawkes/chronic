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
	public void testHighestValueTimeBuffer() {
		TimeSeries buffer = new HighestValueTimeSeries(3000);

		buffer.addValue(13, 3.7);
		buffer.addValue(1280, 7.4);
		buffer.addValue(1332, 0.6);
		buffer.addValue(2640, 2.5);
		buffer.addValue(3000, 3.7);
		buffer.addValue(3955, 6.4);
		buffer.addValue(4823, 3.2);
		buffer.addValue(5505, 3.5);
		buffer.addValue(5999, 2.5);
		buffer.addValue(9811, 6.8);

		assertTrue(buffer.getValue(3000) == 6.4);
		assertTrue(buffer.getValue(2999) == 7.4);
		assertTrue(buffer.getValue(5999) == 6.4);
		assertTrue(buffer.getValue(6000) == 0.0);
		assertTrue(buffer.getValue(10000) == 6.8);
	}

	@Test
	public void testAveragingTimeBuffer() {
		TimeSeries buffer = new AveragingTimeSeries(4000);

		buffer.addValue(13, 3.7);
		buffer.addValue(1280, 7.4);
		buffer.addValue(1332, 0.6);
		buffer.addValue(2640, 2.5);
		buffer.addValue(3000, 3.7);
		buffer.addValue(3955, 6.4);
		buffer.addValue(4823, 3.2);
		buffer.addValue(5505, 3.5);
		buffer.addValue(5999, 2.6);
		buffer.addValue(9811, 6.8);

		Assert.assertEquals(buffer.getValue(0), 4.05, 0.0001);
		Assert.assertEquals(buffer.getValue(3000), 4.05, 0.0001);
		Assert.assertEquals(buffer.getValue(3999), 4.05, 0.0001);
		Assert.assertEquals(buffer.getValue(4000), 3.1, 0.0001);
		Assert.assertEquals(buffer.getValue(7999), 3.1, 0.0001);
		Assert.assertEquals(buffer.getValue(10000), 6.8, 0.0001);
	}

	@Test
	public void testRunningTotalTimeBuffer() {
		TimeSeries buffer = new RunningTotalTimeSeries(700);

		buffer.addValue(13, 3.7);
		buffer.addValue(1280, 7.4);
		buffer.addValue(1332, 0.6);
		buffer.addValue(2640, 2.5);
		buffer.addValue(3000, 3.7);
		buffer.addValue(3955, 6.4);
		buffer.addValue(4823, 3.2);
		buffer.addValue(5505, 3.5);
		buffer.addValue(5999, 2.6);
		buffer.addValue(9811, 6.8);
		buffer.addValue(72300, 10.0);

		Assert.assertEquals(buffer.getValue(0), 3.7, 0.0001);
		Assert.assertEquals(buffer.getValue(2000), 11.7, 0.0001);
		Assert.assertEquals(buffer.getValue(2790), 14.2, 0.0001);
		Assert.assertEquals(buffer.getValue(10000), 40.4, 0.0001);
		Assert.assertEquals(buffer.getValue(30000), 40.4, 0.0001);
		Assert.assertEquals(buffer.getValue(73000), 50.4, 0.0001);
	}

	@Test
	public void testOptimizedRunningTotalTimeBuffer() {
		TimeSeries buffer = new OptimizedRunningTotalTimeSeries(700);

		buffer.addValue(13, 3.7);
		buffer.addValue(1280, 7.4);
		buffer.addValue(1332, 0.6);
		buffer.addValue(2640, 2.5);
		buffer.addValue(3000, 3.7);
		buffer.addValue(3955, 6.4);
		buffer.addValue(4823, 3.2);
		buffer.addValue(5505, 3.5);
		buffer.addValue(5999, 2.6);
		buffer.addValue(9811, 6.8);
		buffer.addValue(72300, 10.0);

		Assert.assertEquals(buffer.getValue(0), 3.7, 0.0001);
		Assert.assertEquals(buffer.getValue(2000), 11.7, 0.0001);
		Assert.assertEquals(buffer.getValue(2790), 14.2, 0.0001);
		Assert.assertEquals(buffer.getValue(10000), 40.4, 0.0001);
		Assert.assertEquals(buffer.getValue(30000), 40.4, 0.0001);
		Assert.assertEquals(buffer.getValue(74000), 50.4, 0.0001);
	}

	@Test
	public void testOptimizedRunningTotalTimeBufferPerformance() {
		TimeSeries b2 = new OptimizedRunningTotalTimeSeries(3000);

		for (int i = 0; i < 42000; i++) {
			long time = i * 1000;
			double value = Math.random() * 18.0d;

			b2.addValue(time, value);
			b2.getValue(time);
		}
	}

	@Test
	public void testRunningTotalTimeBufferPerformance() {
		TimeSeries b2 = new RunningTotalTimeSeries(3000);

		for (int i = 0; i < 42000; i++) {
			long time = i * 1000;
			double value = Math.random() * 18.0d;

			b2.addValue(time, value);
			b2.getValue(time);
		}
	}

	@Test
	public void testBothTotalTimeBuffers() {
		TimeSeries b1 = new RunningTotalTimeSeries(739);
		TimeSeries b2 = new OptimizedRunningTotalTimeSeries(739);

		for (int i = 0; i < 4200; i++) {
			long time = Math.round(Math.random() * 42000000L);
			double value = Math.random() * 18.0d;

			b1.addValue(time, value);
			b2.addValue(time, value);

			Assert.assertEquals("Optimized and regular total should be equal", b1.getValue(time), b2.getValue(time), 0.0001);
		}
	}

	@Test
	public void testSmartSlope() {
		TimeSeries buffer = new HighestValueTimeSeries(1000);

		buffer.addValue(1500, 12);

		assertTrue("Smart slope at 1500 should be 0 (too early)", buffer.getSmartSlope(1500) == 0);

		buffer.addValue(6500, 17);

		assertTrue("Smart slope at 6500 should be .001 (going back 5 slots)", buffer.getSmartSlope(6500) == .001);

		buffer.addValue(7500, 19);

		assertTrue("Smart slope at 9500 should be 0 (going back 2 slots, unchanged)", buffer.getSmartSlope(9500) == 0);

		buffer.addValue(9500, 21);

		assertTrue("Smart slope at 9900 should be .001 (going back 2 slots)", buffer.getSmartSlope(9900) == .001);

		buffer.addValue(10000, 28);

		assertTrue("Smart slope at 10500 should be .001 (going back 3 slots))", buffer.getSmartSlope(10500) == .003);
	}

	@Test
	public void testPercentileTimeBuffer() {
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
	public void testPercentileTimeBufferTooSmall() {
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
