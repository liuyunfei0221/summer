package com.blue.data.common.statistics.inter;

import java.util.Map;

/**
 * 汇总命令
 *
 * @author liuyunfei
 * @date 2021/9/3
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public interface StatisticsCommand {

    /**
     * 优先级
     *
     * @return
     */
    int getPrecedence();

    /**
     * 分析数据并封装
     *
     * @param data
     */
    void packageAnalyzeData(Map<String, String> data);

    /**
     * 数据上报统计
     *
     * @param data
     */
    void summary(Map<String, String> data);

}
