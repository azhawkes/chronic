package com.andyhawkes.chronic;

/**
 * A time series that keeps averages of the values in each slot.
 */
public class AveragingTimeSeries extends PurgeableTimeSeries {
	public AveragingTimeSeries(long interval) {
		super(interval);
	}

    protected TimeSlot createTimeSlot() {
        return new TimeSlot() {
            private int weight = 0;
            private double value = 0.00;

            public void addValue(double value) {
                double average = ((this.value * this.weight) + value) / (this.weight + 1);

                this.value = average;
                this.weight++;
            }

            public double getValue() {
                return value;
            }
        };
    }
}


