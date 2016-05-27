package com.andyhawkes.chronic;

/**
 * A time series that keeps averages of the values in each slot.
 */
public class AveragingTimeSeries extends PurgeableTimeSeries implements WeightedTimeSeries {
    public AveragingTimeSeries(long interval) {
        super(interval);
    }

    public int getWeight(long interval) {
        TimeSlot slot = getSlotAtTime(interval);

        if (slot != null) {
            return ((WeightedSlot) slot).getWeight();
        } else {
            return 0;
        }
    }

    protected TimeSlot createTimeSlot() {
        return new WeightedSlot();
    }

    private class WeightedSlot implements TimeSlot {
        private int weight = 0;
        private double value = 0.00;

        public void addValue(double value) {
            double average = ((this.value * this.weight) + value) / (this.weight + 1);

            this.value = average;
            this.weight++;
        }

        public double getValue() {
            return value;
        }

        public int getWeight() {
            return weight;
        }
    }
}


