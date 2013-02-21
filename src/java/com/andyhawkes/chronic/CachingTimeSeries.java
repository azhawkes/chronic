package com.andyhawkes.chronic;

/**
 * Time series that wraps and caches another one. It leaves a rolling period at
 * the end of the series that is "mutable" and not subject to caching.
 */
public class CachingTimeSeries implements TimeSeries {
	private TimeSeries cache;
	private TimeSeries other;
	private long mutablePeriod;

	public CachingTimeSeries(TimeSeries other, long mutablePeriod) {
		this.cache = new AveragingTimeSeries(other.getInterval());
		this.other = other;
		this.mutablePeriod = mutablePeriod;
	}

	public void addValue(long time, double value) {
		other.addValue(time, value);
	}

	public double getValue(long time) {
		if (time < cache.getLatestTime()) {
			return cache.getValue(time);
		} else if (time > other.getLatestTime() - mutablePeriod) {
			for (long t = other.getLatestTime(); t < other.getLatestTime() - mutablePeriod; t += cache.getInterval()) {
				cache.addValue(t, other.getValue(t));
			}

			return cache.getValue(time);
		} else {
			return other.getValue(time);
		}
	}

	public double getMaxDelta(long period) {
		return other.getMaxDelta(period);
	}

	public double getMinDelta(long period) {
		return other.getMinDelta(period);
	}

	public double getSmartSlope(long endTime) {
		if (endTime <= cache.getLatestTime()) {
			return cache.getSmartSlope(endTime);
		} else {
			return other.getSmartSlope(endTime);
		}
	}

	public double getDelta(long startTime, long endTime) {
		return getValue(endTime) - getValue(startTime);
	}

	public double getMaxValue() {
		return other.getMaxValue();
	}

	public double getMinValue() {
		return other.getMinValue();
	}

	public long getEarliestTime() {
		return other.getEarliestTime();
	}

	public long getLatestTime() {
		return other.getLatestTime();
	}

	public long getInterval() {
		return cache.getInterval();
	}
}
