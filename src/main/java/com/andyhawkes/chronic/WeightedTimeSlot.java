package com.andyhawkes.chronic;

public class WeightedTimeSlot implements TimeSlot {
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
