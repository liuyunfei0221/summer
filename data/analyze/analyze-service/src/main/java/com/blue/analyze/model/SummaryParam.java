package com.blue.analyze.model;

import java.io.Serializable;

/**
 * summary param
 *
 * @author DarkBlue
 */
public final class SummaryParam implements Serializable {

    private static final long serialVersionUID = 3498002276452937696L;

    /**
     * @see com.blue.base.constant.analyze.StatisticsType
     */
    private String statisticsType;

    /**
     * @see com.blue.base.constant.analyze.StatisticsRange
     */
    private String statisticsRange;

    public SummaryParam() {
    }

    public SummaryParam(String statisticsType, String statisticsRange) {
        this.statisticsType = statisticsType;
        this.statisticsRange = statisticsRange;
    }

    public String getStatisticsType() {
        return statisticsType;
    }

    public void setStatisticsType(String statisticsType) {
        this.statisticsType = statisticsType;
    }

    public String getStatisticsRange() {
        return statisticsRange;
    }

    public void setStatisticsRange(String statisticsRange) {
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
