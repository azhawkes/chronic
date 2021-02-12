package com.andyhawkes.chronic;

public interface PercentileTimeSeries extends WeightedTimeSeries {
    double getValue(long time, double quantile);
}
