package com.andyhawkes.chronic;

/**
 * Time series that keeps a running total. The value at any given slot is the
 * total of all slots up to and including that slot. Furthermore, this time series is optimized so that slots will be
 * combined (and the number of slots halved) if the total number of slots reaches a specified threshold.
 */
public class OptimizedRunningTotalTimeSeries implements TimeSeries {
    private RunningTotalTimeSeries series;
    private int maxSlots;

    public OptimizedRunningTotalTimeSeries(long interval, int maxSlots) {
        this.series = new RunningTotalTimeSeries(interval);
        this.maxSlots = maxSlots;
    }

    @Override
    public synchronized void addValue(long time, double value) {
        series.addValue(time, value);

        if (series.slots.size() > maxSlots) {
            RunningTotalTimeSeries rebalanced = new RunningTotalTimeSeries(series.interval * 2);

            for (long t = series.getEarliestTime(); t <= series.getLatestTime(); t += series.interval) {
               rebalanced.addValue(t, series.getSlotAtTime(t).getValue());
            }

            series = rebalanced;
        }
    }

    @Override
    public double getValue(long l) {
        return series.getValue(l);
    }

    @Override
    public long getInterval() {
        return series.getInterval();
    }

    @Override
    public double getMaxDelta(long l) {
        return series.getMaxDelta(l);
    }

    @Override
    public double getMinDelta(long l) {
        return series.getMinDelta(l);
    }

    @Override
    public double getSmartSlope(long l) {
        return series.getSmartSlope(l);
    }

    @Override
    public double getDelta(long l, long l1) {
        return series.getDelta(l, l1);
    }

    @Override
    public double getMaxValue() {
        return series.getMaxValue();
    }

    @Override
    public double getAvgValue() {
        return series.getAvgValue();
    }

    @Override
    public double getMinValue() {
        return series.getMinValue();
    }

    @Override
    public long getEarliestTime() {
        return series.getEarliestTime();
    }

    @Override
    public long getLatestTime() {
        return series.getLatestTime();
    }
}
