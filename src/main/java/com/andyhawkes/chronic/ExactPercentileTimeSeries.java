package com.andyhawkes.chronic;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Time series that calculates the nth percentile value at a given slot.
 * By default, this is the 50th percentile (median), but you can ask for any quantile
 * you want (between 0 and 1).
 */
public class ExactPercentileTimeSeries extends PurgeableTimeSeries implements PercentileTimeSeries {
    public ExactPercentileTimeSeries(long interval) {
        super(interval);
    }

    @Override
    public double getValue(long time) {
        return getValue(time, 0.50);
    }

    public double getValue(long time, double quantile) {
        PercentileTimeSlot slot = (PercentileTimeSlot) getOrCreateSlotAtTime(time);

        if (slot != null && slot.values != null) {
            return computePercentile(slot.values, quantile);
        } else {
            return 0;
        }
    }

    protected TimeSlot createTimeSlot() {
        return new PercentileTimeSlot();
    }

    private static double computePercentile(List<Double> values, double quantile) {
        synchronized (values) {
            double[] sorted = values.stream().mapToDouble(i -> i).sorted().toArray();

            return new Percentile(quantile * 100).evaluate(sorted);
        }
    }

    public static class PercentileTimeSlot implements TimeSlot {
        private List<Double> values = Collections.synchronizedList(new LinkedList<>());

        public void addValue(double value) {
            values.add(value);
        }

        public double getValue() {
            return computePercentile(values, 0.50);
        }
    }
}
