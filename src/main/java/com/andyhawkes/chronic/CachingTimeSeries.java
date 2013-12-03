package com.andyhawkes.chronic;

import org.apache.log4j.Logger;

/**
 * Time series that wraps and caches another one. It leaves a rolling period at
 * the end of the series that is "mutable" and not subject to caching.
 */
public class CachingTimeSeries extends LatestValueTimeSeries {
    private static final Logger log = Logger.getLogger(CachingTimeSeries.class);

    private PurgeableTimeSeries other;
	private long mutablePeriod;

	public CachingTimeSeries(PurgeableTimeSeries other, long mutablePeriod) {
        super(other.getInterval());

		this.other = other;
		this.mutablePeriod = mutablePeriod;
	}

	public synchronized void addValue(long time, double value) {
        if (time > other.getLatestTime() - mutablePeriod) {
            other.addValue(time, value);
            super.addValue(time, other.getValue(time));
        } else {
            log.debug("ignoring new value at time " + time + " because it's outside our mutable window of " + mutablePeriod + " ms");
        }
	}

	public synchronized double getValue(long time) {
        if (time < other.getLatestTime() - mutablePeriod) {
            other.purgeSlotAtIndex((int) (time / other.getInterval()));

            return super.getValue(time);
        } else {
            return other.getValue(time);
        }
	}
}
