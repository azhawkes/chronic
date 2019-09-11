package com.andyhawkes.chronic;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Makes sure nothing breaks when lots of threads hammer a time series at the same time.
 */
public class ThreadSafetyTest {
    private static final long DURATION = 10000;
    private static final int SLOTS = 10000;
    private static final int INTERVAL = 1000;

    private PurgeableTimeSeries series = new PercentileTimeSeries(INTERVAL);
    private long startTime = 0L;

    @Test
    public void testThreadSafety() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(20);

        startTime = System.currentTimeMillis();

        for (int i = 0; i < 10; i++) {
            pool.execute(new Adder());
        }

        for (int i = 0; i < 5; i++) {
            pool.execute(new Scanner());
        }

        pool.execute(new Purger());

        pool.shutdown();
        pool.awaitTermination(60, TimeUnit.SECONDS);
    }

    private class Adder implements Runnable {
        public void run() {
            while (System.currentTimeMillis() - startTime < DURATION) {
                int slot = (int) (SLOTS * Math.random());
                double value = 100 * Math.random();

                series.addValue(slot * INTERVAL, value);
            }

            System.out.println("adder is finished - " + Thread.currentThread().getId());
        }
    }

    private class Scanner implements Runnable {
        public void run() {
            while (System.currentTimeMillis() - startTime < DURATION) {
                for (long t = 0; t < series.getLatestTime(); t += INTERVAL) {
                    series.getValue(t);
                }
            }

            System.out.println("scanner is finished - " + Thread.currentThread().getId());
        }
    }

    private class Purger implements Runnable {
        public void run() {
            while (System.currentTimeMillis() - startTime < DURATION) {
                int slot = (int) (SLOTS * Math.random());

                series.purgeSlotAtIndex(slot);
            }

            System.out.println("purger is finished - " + Thread.currentThread().getId());
        }
    }
}
