package com.andyhawkes.chronic;

/**
 * A time series that keeps only the most recent value for each slot.
 */
public class LatestValueTimeSeries extends PurgeableTimeSeries {
	public LatestValueTimeSeries(long interval) {
		super(interval);
	}

    protected TimeSlot createTimeSlot() {
        return new TimeSlot() {
            private double value = 0.00;

            public void addValue(double value) {
                this.value = value;
            }

            public double getValue() {
                return value;
            }
        };
    }
}
