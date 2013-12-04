package com.andyhawkes.chronic;

/**
 * A slot within a time series. Each slot contains data for a specific time range, such as 3000-5999 milliseconds.
 */
public interface TimeSlot {
    public void addValue(double value);

    public double getValue();
}
