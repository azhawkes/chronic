package com.andyhawkes.chronic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Time series that maintains a running total. The value at any given slot is
 * the total of all slots up to and including that slot.
 */
public class RunningTotalTimeSeries extends PurgeableTimeSeries {
    private Map<Integer, Double> waypoints = new ConcurrentHashMap<>();
    private int waypointDistance;

    public RunningTotalTimeSeries(long interval) {
        this(interval, 10);
    }

    public RunningTotalTimeSeries(long interval, int waypointDistance) {
        super(interval);

        this.waypointDistance = waypointDistance;
    }

    public synchronized double getValue(long time) {
        int index = getIndexAtTime(time);

        if (getSlotCount() == 0) {
            return 0;
        } else if (time > getLatestTime()) {
            return getValue(getLatestTime());
        }

        double total = 0;

        for (int i = index; i >= 0; i--) {
            if (i % waypointDistance == 0) {
                Double waypointValue = waypoints.get(i);

                if (waypointValue != null) {
                    total += waypointValue;

                    break;
                }
            }

            TimeSlot slot = getSlotAtIndex(i);

            if (slot != null) {
                total += slot.getValue();
            } else {
                break;
            }
        }

        return total;
    }

    @Override
    public synchronized void addValue(long time, double value) {
        super.addValue(time, value);

        int index = getIndexAtTime(time);
        int count = getSlotCount();

        for (int i = index; i < count; i++) {
            if (i % waypointDistance == 0) {
                waypoints.remove(index);
                waypoints.put(i, getValue(time));
            }
        }
    }

    protected TimeSlot createTimeSlot() {
        return new AdditiveTimeSlot();
    }
}
