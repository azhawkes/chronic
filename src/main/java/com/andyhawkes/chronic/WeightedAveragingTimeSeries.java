package com.andyhawkes.chronic;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * An averaging time series that uses the true min/max/avg of all values, rather than an aggregate of the slots.
 */
public class WeightedAveragingTimeSeries extends AveragingTimeSeries {
    private double minValue = Double.NaN;
    private double maxValue = Double.NaN;
    private BigDecimal totalValue = new BigDecimal(0.0);
    private long totalSamples = 0;

    public WeightedAveragingTimeSeries(long interval) {
        super(interval);
    }

    public synchronized void addValue(long time, double value) {
        super.addValue(time, value);

        if (Double.isNaN(minValue) || value < minValue) {
            minValue = value;
        }

        if (Double.isNaN(maxValue) || value > maxValue) {
            maxValue = value;
        }

        totalSamples++;
        totalValue = totalValue.add(new BigDecimal(value));
    }

    public double getMaxValue() {
        return maxValue;
    }

    public synchronized double getAvgValue() {
        if (totalSamples == 0) {
            return 0.0;
        } else {
            return totalValue.divide(new BigDecimal(totalSamples), RoundingMode.HALF_EVEN).doubleValue();
        }
    }

    public double getMinValue() {
        return minValue;
    }
}
