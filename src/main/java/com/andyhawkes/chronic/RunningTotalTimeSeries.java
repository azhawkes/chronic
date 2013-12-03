package com.andyhawkes.chronic;

/**
 * Time series that maintains a running total. The value at any given slot is
 * the total of all slots up to and including that slot.
 */
public class RunningTotalTimeSeries extends AbstractTimeSeries {
	public RunningTotalTimeSeries(long interval) {
		super(interval);
	}

	public void addValue(long time, double value) {
		TimeBufferSlot slot = getOrCreateSlotAtTime(time);

		slot.value += value;
		slot.weight++;
	}

	public double getValue(long time) {
		double total = 0.0;
		int index = getIndexAtTime(time);

		// TODO - optimize with some kind of waypoints to avoid scanning every
		// slot every time
		for (int i = 0; i <= index; i++) {
			TimeBufferSlot slot = getSlotAtIndex(i);

			if (slot != null) {
				total += getSlotAtIndex(i).value;
			} else {
				break;
			}
		}

		return total;
	}
}
