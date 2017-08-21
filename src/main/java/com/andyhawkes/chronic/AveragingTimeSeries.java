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
            return ((WeightedTimeSlot) slot).getWeight();
        } else {
            return 0;
        }
    }

    protected TimeSlot createTimeSlot() {
        return new WeightedTimeSlot();
    }
}


