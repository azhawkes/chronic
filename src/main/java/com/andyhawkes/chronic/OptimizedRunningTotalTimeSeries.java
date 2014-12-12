package com.andyhawkes.chronic;

/**
 * Time series that keeps a running total. The value at any given slot is the
 * total of all slots up to and including that slot.
 * 
 * This one is optimized for large amounts of data. It keeps a backup series at
 * 1/10th the resolution, to avoid traversing a whole bunch of buckets. This
 * backup, in turn, might have a low-res backup of itself, and so on.
 */
public class OptimizedRunningTotalTimeSeries extends PurgeableTimeSeries {
	private static final int RESOLUTION = 10;

	private OptimizedRunningTotalTimeSeries lowRes;
	private long interval = 0L;
	private long latestTime = 0L;

	public OptimizedRunningTotalTimeSeries(long interval) {
		super(interval);

		this.interval = interval;
	}

	public synchronized void addValue(long time, double value) {
		TimeSlot slot = getOrCreateSlotAtTime(time);
		int index = getIndexAtTime(time);

        slot.addValue(value);

		// If we've exceeded the resolution, create a low res buffer.
		if (index >= RESOLUTION && lowRes == null) {
			lowRes = new OptimizedRunningTotalTimeSeries(interval * RESOLUTION);

			for (int i = 0; i < index; i++) {
				slot = getOrCreateSlotAtIndex(i);

				if (slot != null) {
					lowRes.addValue(i * interval, slot.getValue());
				}
			}
		}

		// Also add to the low res buffer if available.
		if (lowRes != null) {
			lowRes.addValue(time, value);
		}

		latestTime = Math.max(latestTime, time);
	}

	public synchronized double getValue(long time) {
		double total = 0.0;
		int index = getIndexAtTime(time);

		if (index >= slots.size()) {
			return Double.NaN;
		}

		// If the low res buffer exists, use it as a waypoint.
		if (lowRes != null && index / RESOLUTION > 0) {
			int lowResIndex = index / RESOLUTION;
			long lowResTime = (lowResIndex - 1) * RESOLUTION * interval;

			total += lowRes.getValue(lowResTime);

			for (int i = lowResIndex * RESOLUTION; i <= index; i++) {
				TimeSlot slot = getOrCreateSlotAtIndex(i);

				if (slot != null) {
					total += getOrCreateSlotAtIndex(i).getValue();
				} else {
					break;
				}
			}
		} else {
			for (int i = 0; i <= index; i++) {
				TimeSlot slot = getOrCreateSlotAtIndex(i);

				if (slot != null) {
					total += getOrCreateSlotAtIndex(i).getValue();
				} else {
					break;
				}
			}
		}

		return total;
	}

	public double getMaxValue() {
		long latest = getLatestTime();

		return getValue(latest);
	}

	public double getMinValue() {
		long earliest = getEarliestTime();

		return getValue(earliest);
	}

	protected TimeSlot createTimeSlot() {
        return new TimeSlot() {
            private double value = 0.0;

            public void addValue(double value) {
                this.value += value;
            }

            public double getValue() {
                return value;
            }
        };
    }
}
