package com.andyhawkes.chronic;

/**
 * A time series that keeps averages of the values in each slot.
 */
public class AveragingTimeSeries extends PurgeableTimeSeries {
	public AveragingTimeSeries(long interval) {
		super(interval);
	}

	public synchronized void addValue(long time, double value) {
		TimeSlot slot = getOrCreateSlotAtTime(time);
		double average = ((slot.value * slot.weight) + value) / (slot.weight + 1);

		slot.value = average;
		slot.weight++;
	}
}
