package com.andyhawkes.chronic;

public class AdditiveTimeSlot implements TimeSlot {
    private double value = 0.0;

    @Override
    public void addValue(double value) {
        this.value += value;
    }

    @Override
    public double getValue() {
        return value;
    }
}
