package com.andyhawkes.chronic;

/**
 * A time series that keeps only the highest value seen for each slot (a max function).
 */
public class HighestValueTimeSeries extends PurgeableTimeSeries {
	public HighestValueTimeSeries(long interval) {
		super(interval);
	}

    protected TimeSlot createTimeSlot() {
        return new TimeSlot() {
            private double value = 0.0;

            public void addValue(double value) {
                this.value = Math.max(this.value, value);
            }

            public double getValue() {
                return value;
            }
        };
    }
}
