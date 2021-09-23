package com.blue.base.constant.data;

import java.util.function.Supplier;

import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION;
import static java.time.LocalDate.now;

/**
 * 统计范围
 *
 * @author liuyunfei
 * @date 2021/9/3
 * @apiNote
 */
public enum StatisticsRange {

    /**
     * 统计当日
     */
    DAY("DAY", "统计当日", () ->
            ("D" + PAR_CONCATENATION.identity + now().getDayOfMonth()).intern()),

    /**
     * 统计当月
     */
    MONTH("MONTH", "统计当月", () ->
            ("M" + PAR_CONCATENATION.identity + now().getMonthValue()).intern());

    public final String identity;

    public final String disc;

    public final Supplier<String> keyGenerator;

    StatisticsRange(String identity, String disc, Supplier<String> keyGenerator) {
        this.identity = identity;
        this.disc = disc;
        this.keyGenerator = keyGenerator;
    }

}
