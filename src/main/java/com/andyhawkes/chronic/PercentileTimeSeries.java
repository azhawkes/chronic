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

	protected TimeSlot createTimeSlot() {
        return new TimeSlot() {
            protected List<Double> values = new ArrayList<>();
            private double value = 0.0;

            public void addValue(double value) {
                values.add(value);

                Collections.sort(values);

                double[] vals = new double[values.size()];

                for (int i = 0; i < values.size(); i++) {
                    vals[i] = values.get(i);
                }

                this.value = percentile.evaluate(vals);
            }

            public double getValue() {
                return value;
            }
        };
	}
}
