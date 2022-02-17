package com.blue.analyze.component.statistics.inter;

import java.util.Map;

/**
 * statistics command interface
 *
 * @author liuyunfei
 * @date 2021/9/3
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public interface StatisticsCommand {

    /**
     * precedence
     *
     * @return
     */
    int getPrecedence();

    /**
     * analyze and package
     *
     * @param data
     */
    void analyzeAndPackage(Map<String, String> data);

    /**
     * summary
     *
     * @param data
     */
    void summary(Map<String, String> data);

}
