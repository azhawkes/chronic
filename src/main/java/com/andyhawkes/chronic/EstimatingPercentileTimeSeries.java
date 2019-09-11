package com.andyhawkes.chronic;

import org.streaminer.stream.quantile.GKQuantiles;

/**
 * Time series that estimates the nth percentile value at a given slot using a
 * Greenwald-Khanna algorithm. By default, it returns the 50th percentile (median),
 * but you can ask for any quantile you want (between 0 and 1).
 */
public class EstimatingPercentileTimeSeries extends PurgeableTimeSeries {
    private double epsilon;

    public EstimatingPercentileTimeSeries(long interval) {
        this(interval, 0.05);
    }

    public EstimatingPercentileTimeSeries(long interval, double epsilon) {
        super(interval);

        this.epsilon = epsilon;
    }

    public double getValue(long time, double quantile) {
        EstimatingPercentileTimeSlot slot = (EstimatingPercentileTimeSlot) getOrCreateSlotAtTime(time);

        if (slot != null) {
            return slot.getValue(quantile);
        } else {
            return 0;
        }
    }

    protected TimeSlot createTimeSlot() {
        return new EstimatingPercentileTimeSlot(epsilon);
    }

    public static class EstimatingPercentileTimeSlot implements TimeSlot {
        private GKQuantiles quantiles;

        EstimatingPercentileTimeSlot (double epsilon) {
            quantiles = new GKQuantiles();
            quantiles.setEpsilon(epsilon);
        }

        public void addValue(double value) {
            quantiles.offer(value);
        }

        public double getValue() {
            return getValue(0.50);
        }

        public double getValue(double quantile) {
            return quantiles.getQuantile(quantile);
        }
    }
}
