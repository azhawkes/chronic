package com.andyhawkes.chronic;

import org.junit.Assert;
import org.junit.Test;

public class OptimizedHighestValueTimeSeriesTest {
    @Test
    public void testRebalancing() {
        OptimizedHighestValueTimeSeries series = new OptimizedHighestValueTimeSeries(3000L, 5);

        series.addValue(0, 3);
        series.addValue(3000, 5);
        series.addValue(6000, 8);
        series.addValue(9000, 6);
        series.addValue(12000, 4);

        Assert.assertEquals(3000L, series.getInterval());
        Assert.assertEquals(3, series.getValue(0), 0.0001);
        Assert.assertEquals(5, series.getValue(3000), 0.0001);
        Assert.assertEquals(8, series.getValue(6000), 0.0001);
        Assert.assertEquals(6, series.getValue(9000), 0.0001);
        Assert.assertEquals(4, series.getValue(12000), 0.0001);

        series.addValue(15000, 7);

        Assert.assertEquals(6000L, series.getInterval());
        Assert.assertEquals(5, series.getValue(0), 0.0001);
        Assert.assertEquals(5, series.getValue(3000), 0.0001);
        Assert.assertEquals(8, series.getValue(6000), 0.0001);
        Assert.assertEquals(8, series.getValue(9000), 0.0001);
        Assert.assertEquals(7, series.getValue(12000), 0.0001);
        Assert.assertEquals(7, series.getValue(15000), 0.0001);
    }
}
