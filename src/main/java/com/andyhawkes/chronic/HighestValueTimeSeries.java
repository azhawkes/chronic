package com.andyhawkes.chronic;

/**
 * A time series that keeps only the highest value available in each slot. A max
 * function.
 */
public class HighestValueTimeSeries extends AbstractTimeSeries {
	public HighestValueTimeSeries(long interval) {
		super(interval);
	}

	public void addValue(long time, double value) {
		TimeBufferSlot slot = getOrCreateSlotAtTime(time);

		slot.value = Math.max(slot.value, value);
		slot.weight++;
	}
}
