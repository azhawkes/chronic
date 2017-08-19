package com.andyhawkes.chronic;

import org.junit.Assert;
import org.junit.Test;

public class OptimizedRunningTotalTimeSeriesTest {
    @Test
    public void testRebalancing() {
        OptimizedRunningTotalTimeSeries series = new OptimizedRunningTotalTimeSeries(3000L, 5);

        series.addValue(0, 3);
        series.addValue(0, 4);
        series.addValue(3000, 5);
        series.addValue(3000, 8);
        series.addValue(6000, 8);
        series.addValue(9000, 6);
        series.addValue(9000, 9);
        series.addValue(12000, 4);
        series.addValue(12000, 6);

        Assert.assertEquals(3000L, series.getInterval());
        Assert.assertEquals(7, series.getValue(0), 0.0001);
        Assert.assertEquals(20, series.getValue(3000), 0.0001);
        Assert.assertEquals(28, series.getValue(6000), 0.0001);
        Assert.assertEquals(43, series.getValue(9000), 0.0001);
        Assert.assertEquals(53, series.getValue(12000), 0.0001);

        series.addValue(15000, 7);

        Assert.assertEquals(6000L, series.getInterval());
        Assert.assertEquals(20, series.getValue(0), 0.0001);
        Assert.assertEquals(20, series.getValue(3000), 0.0001);
        Assert.assertEquals(43, series.getValue(6000), 0.0001);
        Assert.assertEquals(43, series.getValue(9000), 0.0001);
        Assert.assertEquals(60, series.getValue(12000), 0.0001);
        Assert.assertEquals(60, series.getValue(15000), 0.0001);
    }
}
