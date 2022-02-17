package com.blue.analyze.model;

import com.blue.base.constant.analyze.StatisticsRange;
import com.blue.base.constant.analyze.StatisticsType;

import java.io.Serializable;
import java.util.List;

/**
 * merge summary param
 *
 * @author DarkBlue
 */
public class MergeSummaryParam implements Serializable {

    private static final long serialVersionUID = -916119028631172684L;
    
    List<StatisticsType> statisticsTypes;

    List<StatisticsRange> statisticsRanges;

    public MergeSummaryParam() {
    }

    public MergeSummaryParam(List<StatisticsType> statisticsTypes, List<StatisticsRange> statisticsRanges) {
        this.statisticsTypes = statisticsTypes;
        this.statisticsRanges = statisticsRanges;
    }

    public List<StatisticsType> getStatisticsTypes() {
        return statisticsTypes;
    }

    public void setStatisticsTypes(List<StatisticsType> statisticsTypes) {
        this.statisticsTypes = statisticsTypes;
    }

    public List<StatisticsRange> getStatisticsRanges() {
        return statisticsRanges;
    }

    public void setStatisticsRanges(List<StatisticsRange> statisticsRanges) {
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
