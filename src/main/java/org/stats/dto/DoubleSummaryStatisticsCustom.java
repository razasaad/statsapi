package org.stats.dto;

import java.util.DoubleSummaryStatistics;

public class DoubleSummaryStatisticsCustom {

    private long count;

    private double min;
    private double max;
    private double average;
    private double sum;

    public long getCount() {
        return count;
    }

    private void setCount(long count) {
        this.count = count;
    }

    public double getMin() {
        return min;
    }

    private void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    private void setMax(double max) {
        this.max = max;
    }

    public double getAverage() {
        return average;
    }

    private void setAverage(double average) {
        this.average = average;
    }

    public double getSum() {
        return sum;
    }

    private void setSum(double sum) {
        this.sum = sum;
    }

    public static DoubleSummaryStatisticsCustom copyFromDoubleStatisticSummary(DoubleSummaryStatistics doubleSummaryStatistics) {
        DoubleSummaryStatisticsCustom custom = new DoubleSummaryStatisticsCustom();
        custom.setAverage(doubleSummaryStatistics.getAverage());
        custom.setCount(doubleSummaryStatistics.getCount());
        custom.setMax(doubleSummaryStatistics.getMax());
        custom.setMin(doubleSummaryStatistics.getMin());
        custom.setSum(doubleSummaryStatistics.getSum());

        return custom;
    }
}
