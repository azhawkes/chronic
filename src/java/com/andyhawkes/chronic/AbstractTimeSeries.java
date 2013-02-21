package com.andyhawkes.chronic;

import java.util.ArrayList;
import java.util.List;

/**
 * A construct that keeps track of time-sensitive numeric data. Implementations
 * can perform various functions when gathering the data, such as returning the
 * average/max/min/etc.
 */
public abstract class AbstractTimeSeries implements TimeSeries {
	protected List<TimeBufferSlot> slots = new ArrayList<TimeBufferSlot>();
	protected long interval;

	public AbstractTimeSeries(long interval) {
		this.interval = interval;
	}

	public abstract void addValue(long time, double value);

	public double getValue(long time) {
		long timeLatest = getLatestTime();

		// If they are off by just a half second or so, give them the last slot
		// anyway. This is to overcome small time inconsistencies from thread
		// contention.
		if (time > timeLatest && time - 500 < timeLatest) {
			time = timeLatest;
		}

		// If they ask for something negative, give them 0 instead.
		if (time < 0L) {
			time = 0L;
		}

		TimeBufferSlot slot = getSlotAtTime(time);

		if (slot == null) {
			return Double.NaN;
		} else {
			return slot.value;
		}
	}

	/**
	 * Returns the amount of maximum change over a given period. For example,
	 * the sharpest increase over any 9000ms time period.
	 */
	public double getMaxDelta(long period) {
		if (getEarliestTime() == getLatestTime()) {
			return 0.0;
		}

		long endTime = getLatestTime();
		double max = Double.MIN_VALUE;

		for (long t = getEarliestTime(); t < endTime; t += interval) {
			double delta = getDelta(t, t + period);

			if (delta > max) {
				max = delta;
			}
		}

		return max;
	}

	/**
	 * Returns the amount of minimum change over a given period. For example,
	 * the least increase (or biggest decrease) over any 9000ms time period.
	 */
	public double getMinDelta(long period) {
		if (getEarliestTime() == getLatestTime()) {
			return 0.0;
		}

		long endTime = getLatestTime();
		double min = Double.MAX_VALUE;

		for (long t = getEarliestTime(); t < endTime; t += interval) {
			double delta = getDelta(t, t + period);

			if (delta < min) {
				min = delta;
			}
		}

		if (min == Double.MAX_VALUE) {
			min = 0;
		}

		return min;
	}

	/**
	 * Calculates the slope leading up to a certain time. The slope is
	 * calculated by going back 2 buckets, or up to 5 buckets if data is missing
	 * due to a network blip or something.
	 */
	public double getSmartSlope(long endTime) {
		for (int i = 2; i < 6; i++) {
			long period = i * interval;
			long startTime = endTime - period;

			if (endTime > getLatestTime()) {
				return 0.00;
			} else if (getValue(startTime) != 0.00 && startTime > 0) {
				return getDelta(startTime, endTime) / period;
			}
		}

		return 0.00;
	}

	public double getDelta(long startTime, long endTime) {
		double startValue = getValue(startTime);
		double endValue = getValue(endTime);

		return endValue - startValue;
	}

	public double getMaxValue() {
		if (getEarliestTime() == getLatestTime()) {
			return 0.0;
		}

		double max = Double.MIN_VALUE;
		long endTime = getLatestTime();

		for (long t = getEarliestTime(); t < endTime; t += interval) {
			double value = getValue(t);

			if (value > max) {
				max = value;
			}
		}

		return max;
	}

	public double getMinValue() {
		if (getEarliestTime() == getLatestTime()) {
			return 0.0;
		}

		double min = Double.MAX_VALUE;
		long endTime = getLatestTime();

		for (long t = getEarliestTime(); t < endTime; t += interval) {
			double value = getValue(t);

			if (value < min) {
				min = value;
			}
		}

		if (min == Double.MAX_VALUE) {
			min = 0;
		}

		return min;
	}

	public long getEarliestTime() {
		return 0;
	}

	public long getLatestTime() {
		return (slots.size() * interval) - 1;
	}

	public long getInterval() {
		return interval;
	}

	protected int getIndexAtTime(long time) {
		return (int) (time / interval);
	}

	protected TimeBufferSlot getSlotAtIndex(int index) {
		return (slots.size() > index) ? slots.get(index) : null;
	}

	protected TimeBufferSlot getSlotAtTime(long time) {
		return getSlotAtIndex(getIndexAtTime(time));
	}

	protected TimeBufferSlot getOrCreateSlotAtTime(long time) {
		int index = getIndexAtTime(time);

		while (index >= slots.size()) {
			slots.add(new TimeBufferSlot());
		}

		return getSlotAtIndex(index);
	}

	protected class TimeBufferSlot {
		protected double value;
		protected int weight;
	}
}
