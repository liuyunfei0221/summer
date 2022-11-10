package com.blue.analyze.component.statistics.inter;

import com.blue.basic.constant.analyze.StatisticsType;
import com.blue.basic.model.event.DataEvent;

/**
 * statistics command interface
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface StatisticsCommand {

    /**
     * analyze
     *
     * @param dataEvent
     */
    void analyze(DataEvent dataEvent);

    /**
     * summary
     *
     * @param dataEvent
     */
    void summary(DataEvent dataEvent);

    /**
     * handler type
     *
     * @return
     */
    StatisticsType targetType();

}
