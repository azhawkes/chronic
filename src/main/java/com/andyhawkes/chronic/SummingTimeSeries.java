package com.andyhawkes.chronic;

/**
 * A time series that keeps a sum of the values in each slot.
 */
public class SummingTimeSeries extends PurgeableTimeSeries {
    public SummingTimeSeries(long interval) {
        super(interval);
    }

    protected TimeSlot createTimeSlot() {
        return new TimeSlot() {
            private double value = 0.00;

            public void addValue(double value) {
                if (!Double.isNaN(value)) {
                    this.value += value;
                }
            }

            public double getValue() {
                return value;
            }
        };
    }
}


