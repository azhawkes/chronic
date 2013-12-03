package com.andyhawkes.chronic;

/**
 * A time series that stores values and aggregates them into buckets, each
 * bucket representing a specific interval.
 */
public interface TimeSeries {
	/**
	 * Adds a value to the time series.
	 */
	public void addValue(long time, double value);

	/**
	 * Gets a value at a specific time.
	 */
	public double getValue(long time);

	/**
	 * Returns the interval (the duration of each bucket).
	 */
	public long getInterval();

	/**
	 * Returns the amount of maximum change over a given period. For example,
	 * the sharpest increase over any 9000ms time period.
	 */
	public double getMaxDelta(long period);

	/**
	 * Returns the amount of minimum change over a given period. For example,
	 * the least increase (or biggest decrease) over any 9000ms time period.
	 */
	public double getMinDelta(long period);

	/**
	 * Calculates the slope leading up to a certain time. The slope is
	 * calculated by going back 2 buckets, or up to 5 buckets if data is missing
	 * due to a network blip or something.
	 */
	public double getSmartSlope(long endTime);

	public double getDelta(long startTime, long endTime);

	public double getMaxValue();

	public double getMinValue();

	public long getEarliestTime();

	public long getLatestTime();
}
