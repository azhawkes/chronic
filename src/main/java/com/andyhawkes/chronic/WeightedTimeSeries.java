package com.andyhawkes.chronic;

/**
 * A time series that keeps track of the "weight" of each time slot (the number of samples that contributed to the
 * aggregate). This is useful for computing averages of averages.
 */
public interface WeightedTimeSeries extends TimeSeries {
    int getWeight(long time);
}
