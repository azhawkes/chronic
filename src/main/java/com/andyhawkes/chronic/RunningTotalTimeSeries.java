package com.andyhawkes.chronic;

/**
 * Time series that maintains a running total. The value at any given slot is
 * the total of all slots up to and including that slot.
 */
public class RunningTotalTimeSeries extends PurgeableTimeSeries {
    public RunningTotalTimeSeries(long interval) {
        super(interval);
    }

    public double getValue(long time) {
        double total = 0.0;
        int index = getIndexAtTime(time);

        if (getSlotCount() == 0) {
            return Double.NaN; // TODO - I don't like this
        } else if (time > getLatestTime()) {
            return getValue(getLatestTime());
        }

        for (int i = 0; i <= index; i++) {
            TimeSlot slot = getSlotAtIndex(i);

            if (slot != null) {
                total += slot.getValue();
            } else {
                break;
            }
        }

        return total;
    }

    protected TimeSlot createTimeSlot() {
        return new AdditiveTimeSlot();
    }
}
