package com.blue.analyze.model;

import com.blue.base.constant.analyze.StatisticsRange;
import com.blue.base.constant.analyze.StatisticsType;

import java.io.Serializable;

/**
 * summary param
 *
 * @author DarkBlue
 */
public final class SummaryParam implements Serializable {

    private static final long serialVersionUID = 3498002276452937696L;

    private StatisticsType statisticsType;

    private StatisticsRange statisticsRange;

    public SummaryParam() {
    }

    public SummaryParam(StatisticsType statisticsType, StatisticsRange statisticsRange) {
        this.statisticsType = statisticsType;
        this.statisticsRange = statisticsRange;
    }

    public StatisticsType getStatisticsType() {
        return statisticsType;
    }

    public void setStatisticsType(StatisticsType statisticsType) {
        this.statisticsType = statisticsType;
    }

    public StatisticsRange getStatisticsRange() {
        return statisticsRange;
    }

    public void setStatisticsRange(StatisticsRange statisticsRange) {
        this.statisticsRange = statisticsRange;
    }

    @Override
    public String toString() {
        return "SummaryParam{" +
                "statisticsType=" + statisticsType +
                ", statisticsRange=" + statisticsRange +
                '}';
    }

}
