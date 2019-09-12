package com.andyhawkes.chronic;


import com.tdunning.math.stats.TDigest;

/**
 * Time series that estimates the nth percentile value at a given slot using a
 * TDigest algorithm. By default, it returns the 50th percentile (median),
 * but you can ask for any quantile you want (between 0 and 1).
 */
public class TDigestPercentileTimeSeries extends PurgeableTimeSeries implements PercentileTimeSeries {
    private double compression;

    public TDigestPercentileTimeSeries(long interval) {
        this(interval, 100);
    }

    public TDigestPercentileTimeSeries(long interval, double compression) {
        super(interval);

        this.compression = compression;
    }

    public double getValue(long time, double quantile) {
        TDigestPercentileTimeSeriesSlot slot = (TDigestPercentileTimeSeriesSlot) getOrCreateSlotAtTime(time);

        if (slot != null) {
            return slot.getValue(quantile);
        } else {
            return 0;
        }
    }

    protected TimeSlot createTimeSlot() {
        return new TDigestPercentileTimeSeriesSlot(compression);
    }

    public static class TDigestPercentileTimeSeriesSlot implements TimeSlot {
        private TDigest digest;

        TDigestPercentileTimeSeriesSlot(double compression) {
            digest = TDigest.createMergingDigest(compression);
        }

        public void addValue(double value) {
            digest.add(value);
        }

        public double getValue() {
            return getValue(0.50);
        }

        public double getValue(double quantile) {
            return digest.quantile(quantile);
        }
    }
}
