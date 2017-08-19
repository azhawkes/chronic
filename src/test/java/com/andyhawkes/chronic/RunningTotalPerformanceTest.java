package com.andyhawkes.chronic;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests how long it takes to compute running totals from large data sets.
 * This is a crude measurement since it varies based on the system we are running on,
 * but sets some worst-case times to make sure we don't seriously break something.
 */
public class RunningTotalPerformanceTest {
    @Test
    public void testOptimizedRunningTotalTimeBufferPerformance() {
        long interval = 3000;
        long duration = 86400000l * 7; // 7 days

        TimeSeries b2 = new OptimizedRunningTotalTimeSeries(interval, 5);

        for (long time = 0; time < duration; time += interval) {
            b2.addValue(time, Math.random() * 18.0d);
        }

        long calculationStartTime = System.currentTimeMillis();
        b2.getValue(duration - interval);
        long calculationEndTime = System.currentTimeMillis();

        Assert.assertTrue("Should never take more than 5 ms", calculationEndTime - calculationStartTime < 50);
    }

    @Test
    public void testRunningTotalTimeBufferPerformance() {
        long interval = 3000;
        long duration = 86400000l * 7; // 7 days

        TimeSeries b2 = new RunningTotalTimeSeries(interval);

        for (long time = 0; time < duration; time += interval) {
            b2.addValue(time, Math.random() * 18.0d);
        }

        long calculationStartTime = System.currentTimeMillis();
        b2.getValue(duration - interval);
        long calculationEndTime = System.currentTimeMillis();

        Assert.assertTrue("Should never take more than 50 ms", calculationEndTime - calculationStartTime < 50);
    }
}
