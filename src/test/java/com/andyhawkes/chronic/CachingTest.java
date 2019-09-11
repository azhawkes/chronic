package com.andyhawkes.chronic;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the caching time series correctly wraps another time series and returns valid measurements.
 */
public class CachingTest {
    private PercentileTimeSeries percentileTimeSeries = new PercentileTimeSeries(3000);
    private CachingTimeSeries cachingTimeSeries = new CachingTimeSeries(percentileTimeSeries, 60000);

    @Test
    public void testCachingIsAccurate() {
        cachingTimeSeries.addValue(40000, 3);
        cachingTimeSeries.addValue(40000, 4);
        cachingTimeSeries.addValue(40000, 5);

        Assert.assertEquals("50th percentile at 40000 should be 4", 4, cachingTimeSeries.getValue(40000), 0.0001);

        cachingTimeSeries.addValue(40000, 6);
        cachingTimeSeries.addValue(40000, 7);

        Assert.assertEquals("50th percentile at 40000 should be 5", 5, cachingTimeSeries.getValue(40000), 0.0001);

        cachingTimeSeries.addValue(140000, 7);

        Assert.assertEquals("50th percentile at 140000 should be 7", 7, cachingTimeSeries.getValue(140000), 0.0001);
        Assert.assertEquals("50th percentile at 40000 should still be 5", 5, cachingTimeSeries.getValue(40000), 0.0001);
        Assert.assertEquals("50th percentile at 180000 should be NaN", Double.NaN, cachingTimeSeries.getValue(180000), 0.0001);

        Assert.assertTrue("Values should be the same at 140000", cachingTimeSeries.getValue(140000) == percentileTimeSeries.getValue(140000));
        Assert.assertTrue("Values should not be the same at 40000 because the underlying series was purged", cachingTimeSeries.getValue(40000) != percentileTimeSeries.getValue(40000));
    }
}
