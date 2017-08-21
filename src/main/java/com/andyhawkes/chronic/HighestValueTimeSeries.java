package com.andyhawkes.chronic;

/**
 * A time series that keeps only the highest value seen for each slot (a max function).
 */
public class HighestValueTimeSeries extends PurgeableTimeSeries {
	public HighestValueTimeSeries(long interval) {
		super(interval);
	}

    protected TimeSlot createTimeSlot() {
	    return new HighestValueTimeSlot();
    }
}
