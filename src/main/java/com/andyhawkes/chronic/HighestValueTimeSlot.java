package com.andyhawkes.chronic;

public class HighestValueTimeSlot implements TimeSlot {
    private double value = 0.0;

    public void addValue(double value) {
        this.value = Math.max(this.value, value);
    }

    public double getValue() {
        return value;
    }
}
