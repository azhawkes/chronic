package com.andyhawkes.chronic;

/**
 * A time series that keeps only the most recent value for each slot.
 */
public class LatestValueTimeSeries extends PurgeableTimeSeries {
	public LatestValueTimeSeries(long interval) {
		super(interval);
	}

	public void addValue(long time, double value) {
		TimeSlot slot = getOrCreateSlotAtTime(time);

		slot.value = value;
		slot.weight = 1;
	}
}
