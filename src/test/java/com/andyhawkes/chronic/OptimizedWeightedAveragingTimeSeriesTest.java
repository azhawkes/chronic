package com.andyhawkes.chronic;

import org.junit.Assert;
import org.junit.Test;

public class OptimizedWeightedAveragingTimeSeriesTest {
    @Test
    public void testRebalancing() {
        OptimizedWeightedAveragingTimeSeries series = new OptimizedWeightedAveragingTimeSeries(3000L, 5);

        series.addValue(0, 3);
        series.addValue(3000, 5);
        series.addValue(3000, 7);
        series.addValue(6000, 8);
        series.addValue(6000, 10);
        series.addValue(9000, 6);
        series.addValue(12000, 4);

        Assert.assertEquals(3000L, series.getInterval());
        Assert.assertEquals(3, series.getValue(0), 0.0001);
        Assert.assertEquals(6, series.getValue(3000), 0.0001);
        Assert.assertEquals(9, series.getValue(6000), 0.0001);
        Assert.assertEquals(6, series.getValue(9000), 0.0001);
        Assert.assertEquals(4, series.getValue(12000), 0.0001);
        Assert.assertEquals(6.143, series.getAvgValue(), 0.0001);

        series.addValue(15000, 12);

        Assert.assertEquals(6000L, series.getInterval());
        Assert.assertEquals(5, series.getValue(0), 0.0001);
        Assert.assertEquals(5, series.getValue(3000), 0.0001);
        Assert.assertEquals(8, series.getValue(6000), 0.0001);
        Assert.assertEquals(8, series.getValue(9000), 0.0001);
        Assert.assertEquals(8, series.getValue(12000), 0.0001);
        Assert.assertEquals(8, series.getValue(15000), 0.0001);
        Assert.assertEquals(6.875, series.getAvgValue(), 0.0001);
    }
}
