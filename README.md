Chronic: Speedy time series for Java
====================================

In Loadster, we needed to keep track of large amounts of time series data in memory and reference it quickly and 
efficiently.

This library takes the approach of dividing time series data into time slots and applying a function to each time slot
at the time data is inserted. Individual data points within a time slot can then be discarded, saving memory. You can
query the time series for a value at any given time and it will return the computed value for that time slot.

The following code will create a time series that averages data in 3-second slots.

```java

TimeSeries series = new AverageTimeSeries(3000);

series.addValue(1204, 59.62); // first slot
series.addValue(2161, 68.44); // first slot
series.addValue(3480, 61.57); // second slot

double firstSlotValue = series.getValue(1680); // returns the average of all values from time 0-2999

```

In this case, the first time slot (from 0-2999 milliseconds) now contains 64.03 (the average of all values in
that slot), and the second time slot (from 3000-5999 milliseconds) contains 61.57.

Supported Types of Time Series
------------------------------

[AveragingTimeSeries](https://github.com/azhawkes/chronic/blob/master/src/java/com/andyhawkes/chronic/AveragingTimeSeries.java) - 
Values are averaged by time slot, with aggregates being the aggregate of all time slot values.

[OptimizedAveragingTimeSeries](https://github.com/azhawkes/chronic/blob/master/src/java/com/andyhawkes/chronic/OptimizedAveragingTimeSeries.java) - 
Just like AveragingTimeSeries, but if it grows too large, slots will be merged (and the resolution will be decreased by half).

[WeightedAveragingTimeSeries](https://github.com/azhawkes/chronic/blob/master/src/java/com/andyhawkes/chronic/WeightedAveragingTimeSeries.java) - 
Values are averaged by time slot, but the aggregates (min/avg/max) are derived from all values instead of just being an aggregate of the time slots.

[OptimizedWeightedAveragingTimeSeries](https://github.com/azhawkes/chronic/blob/master/src/java/com/andyhawkes/chronic/OptimizedWeightedAveragingTimeSeries.java) - 
Like WeightedAveragingTimeSeries, values are averaged by time slot, but also supports merging if the number of slots grows too high.

[HighestValueTimeSeries](https://github.com/azhawkes/chronic/blob/master/src/java/com/andyhawkes/chronic/HighestValueTimeSeries.java) -
The highest value for each time slot is returned; others are discarded.

[OptimizedHighestValueTimeSeries](https://github.com/azhawkes/chronic/blob/master/src/java/com/andyhawkes/chronic/OptimizedHighestValueTimeSeries.java) -
Just like HighestValueTimeSeries, except that if it grows to have too many slots, adjacent slots will be merged.

[LatestValueTimeSeries](https://github.com/azhawkes/chronic/blob/master/src/java/com/andyhawkes/chronic/LatestValueTimeSeries.java) -
The most recently recorded value for each time slot is kept; others are discarded.

[PercentileTimeSeries](https://github.com/azhawkes/chronic/blob/master/src/java/com/andyhawkes/chronic/PercentileTimeSeries.java) -
Tracks the nth percentile of all values in the time slot.

[RunningTotalTimeSeries](https://github.com/azhawkes/chronic/blob/master/src/java/com/andyhawkes/chronic/RunningTotalTimeSeries.java) -
Keeps a running total of all previous values, up to and including the current time slot value.

[OptimizedRunningTotalTimeSeries](https://github.com/azhawkes/chronic/blob/master/src/java/com/andyhawkes/chronic/OptimizedRunningTotalTimeSeries.java) -
Just like a RunningTotalTimeSeries, but if it grows too large slots will be merged.

[CachingTimeSeries](https://github.com/azhawkes/chronic/blob/master/src/java/com/andyhawkes/chronic/CachingTimeSeries.java) -
Wraps (decorates) another time series with a cached version, keeping a specified number of milliseconds mutable (uncached)
at the end to accept new data. If the underlying time series supports it, it will purge values earlier than the mutable
window to save memory.
