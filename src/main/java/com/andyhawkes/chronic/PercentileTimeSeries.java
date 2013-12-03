package com.andyhawkes.chronic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;

/**
 * Time series that calculates the nth percentile value at a given slot.
 */
public class PercentileTimeSeries extends PurgeableTimeSeries {
	private Percentile percentile;

	public PercentileTimeSeries(long interval, int percentile) {
		super(interval);

		this.percentile = new Percentile(percentile);
	}

	public synchronized void addValue(long time, double value) {
		PercentileSlot slot = (PercentileSlot) getOrCreateSlotAtTime(time);

		slot.weight++;
		slot.values.add(value);

		Collections.sort(slot.values);

		slot.value = calculatePercentile(slot);
	}

	protected TimeSlot createTimeSlot() {
        return new PercentileSlot();
	}

	private double calculatePercentile(PercentileSlot slot) {
		if (slot.weight == 0) {
			return 0.00;
		} else {
			double[] values = new double[slot.values.size()];

			for (int i = 0; i < slot.values.size(); i++) {
				values[i] = slot.values.get(i);
			}

			return percentile.evaluate(values);
		}
	}

	protected class PercentileSlot extends TimeSlot {
		protected List<Double> values = new ArrayList<>();
	}
}
