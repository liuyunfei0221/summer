package com.blue.analyze.model;

import java.io.Serializable;
import java.util.List;

/**
 * merge summary param
 *
 * @author liuyunfei
 */
public class MergeSummaryParam implements Serializable {

    private static final long serialVersionUID = -916119028631172684L;

    /**
     * @see com.blue.base.constant.analyze.StatisticsType
     */
    List<String> statisticsTypes;

    /**
     * @see com.blue.base.constant.analyze.StatisticsRange
     */
    List<String> statisticsRanges;

    public MergeSummaryParam() {
    }

    public MergeSummaryParam(List<String> statisticsTypes, List<String> statisticsRanges) {
        this.statisticsTypes = statisticsTypes;
        this.statisticsRanges = statisticsRanges;
    }

    public List<String> getStatisticsTypes() {
        return statisticsTypes;
    }

    public void setStatisticsTypes(List<String> statisticsTypes) {
        this.statisticsTypes = statisticsTypes;
    }

    public List<String> getStatisticsRanges() {
        return statisticsRanges;
    }

    public void setStatisticsRanges(List<String> statisticsRanges) {
        this.statisticsRanges = statisticsRanges;
    }

    @Override
    public String toString() {
        return "MergeSummaryParam{" +
                "statisticsTypes=" + statisticsTypes +
                ", statisticsRanges=" + statisticsRanges +
                '}';
    }

}
