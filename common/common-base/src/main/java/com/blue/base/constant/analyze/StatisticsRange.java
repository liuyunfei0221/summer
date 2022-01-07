package com.blue.base.constant.analyze;

import java.util.function.Supplier;

import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION;
import static java.time.LocalDate.now;

/**
 * statistics range
 *
 * @author liuyunfei
 * @date 2021/9/3
 * @apiNote
 */
public enum StatisticsRange {

    /**
     * by day
     */
    DAY("DAY", "by day", () ->
            ("D" + PAR_CONCATENATION.identity + now().getDayOfMonth()).intern()),

    /**
     * by month
     */
    MONTH("MONTH", "by month", () ->
            ("M" + PAR_CONCATENATION.identity + now().getMonthValue()).intern());

    /**
     * identity
     */
    public final String identity;

    /**
     * disc
     */
    public final String disc;

    /**
     * statistics key generator
     */
    public final Supplier<String> keyGenerator;

    StatisticsRange(String identity, String disc, Supplier<String> keyGenerator) {
        this.identity = identity;
        this.disc = disc;
        this.keyGenerator = keyGenerator;
    }

}
