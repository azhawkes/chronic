package com.andyhawkes.chronic;

import org.junit.Assert;
import org.junit.Test;

public class PurgeTest {
    @Test
    public void testPurgeAndReuse() {
        AveragingTimeSeries series = new AveragingTimeSeries(3000);

        series.addValue(20000, 1);
        series.addValue(20000, 3);

        Assert.assertEquals("Average at 20000 should be 2", 2, series.getValue(20000), 0.00001);

        series.purgeSlotAtIndex(6);

        Assert.assertEquals("Average at 20000 should be 0", 0, series.getValue(20000), 0.00001);

        series.addValue(20000, 9);

        Assert.assertEquals("Average at 20000 should be 9", 9, series.getValue(20000), 0.00001);
    }
}
