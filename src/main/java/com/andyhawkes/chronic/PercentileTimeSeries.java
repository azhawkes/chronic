package com.andyhawkes.chronic;

public interface PercentileTimeSeries extends TimeSeries {
    double getValue(long time, double quantile);
}
