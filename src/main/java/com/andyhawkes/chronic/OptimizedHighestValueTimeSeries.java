package com.andyhawkes.chronic;

/**
 * A time series that keeps only the highest value seen for each slot (a max function). When there are too many time
 * slots, it automatically doubles the interval and halves the number of slots.
 */
public class OptimizedHighestValueTimeSeries implements TimeSeries {
    private HighestValueTimeSeries series;
    private int maxSlots;

	public OptimizedHighestValueTimeSeries(long interval, int maxSlots) {
	    this.series = new HighestValueTimeSeries(interval);
	    this.maxSlots = maxSlots;
	}

	@Override
	public synchronized void addValue(long l, double v) {
		series.addValue(l, v);

		while (series.getSlotCount() > maxSlots) {
			HighestValueTimeSeries rebalanced = new HighestValueTimeSeries(series.getInterval() * 2);

			for (long t = series.getEarliestTime(); t <= series.getLatestTime(); t += series.getInterval()) {
				rebalanced.addValue(t, series.getValue(t));
			}

			series = rebalanced;
		}
	}

	@Override
	public double getValue(long l) {
		return series.getValue(l);
	}

	@Override
	public long getInterval() {
		return series.getInterval();
	}

	@Override
	public double getMaxDelta(long l) {
		return series.getMaxDelta(l);
	}

	@Override
	public double getMinDelta(long l) {
		return series.getMinDelta(l);
	}

	@Override
	public double getSmartSlope(long l) {
		return series.getSmartSlope(l);
	}

	@Override
	public double getDelta(long l, long l1) {
		return series.getDelta(l, l1);
	}

	@Override
	public double getMaxValue() {
		return series.getMaxValue();
	}

	@Override
	public double getAvgValue() {
		return series.getAvgValue();
	}

	@Override
	public double getMinValue() {
		return series.getMinValue();
	}

	@Override
	public long getEarliestTime() {
		return series.getEarliestTime();
	}

	@Override
	public long getLatestTime() {
		return series.getLatestTime();
	}
}
