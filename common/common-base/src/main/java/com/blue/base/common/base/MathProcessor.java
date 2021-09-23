package com.blue.base.common.base;

import reactor.util.Logger;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;
import static java.util.stream.Collectors.toList;
import static reactor.util.Loggers.getLogger;


/**
 * 数学工具类
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class MathProcessor {

    public static final Logger LOGGER = getLogger(MathProcessor.class);

    /**
     * 校验int集合连续性
     *
     * @param collection
     * @return
     */
    public static boolean assertDisorderIntegerContinuous(Collection<Integer> collection) {
        List<Integer> sorted = collection.stream()
                .sorted(Integer::compare).collect(toList());

        Integer previous = null;

        for (Integer i : sorted) {
            if (i == null) {
                LOGGER.info("boolean assertDisorderIntegerContinuous(Collection<Integer> collection), collection has null element");
                return false;
            }

            if (previous == null) {
                previous = i;
            } else {
                if ((i - previous) != 1)
                    return false;
            }
        }

        return true;
    }

    /**
     * 校验long集合连续性
     *
     * @param collection
     * @return
     */
    public static boolean assertDisorderLongContinuous(Collection<Long> collection) {
        List<Long> sorted = collection.stream()
                .sorted(Long::compare).collect(toList());

        Long previous = null;

        for (Long l : sorted) {
            if (l == null) {
                LOGGER.info("boolean assertDisorderLongContinuous(Collection<Long> collection), collection has null element");
                return false;
            }

            if (previous == null) {
                previous = l;
            } else {
                if ((l - previous) != 1L)
                    return false;
            }
        }

        return true;
    }

    /**
     * 求最大公约数
     *
     * @param a
     * @param b
     * @return
     */
    public static long getGreatestCommonDivisor(long a, long b) {
        if (a < 0L || b < 0L)
            throw new UnsupportedOperationException();

        if (a == 0L || b == 0L)
            return 0L;

        long tarA = a, tarB = b;
        int displacement = 0;
        boolean isEvenA, isEvenB;

        while (tarA != tarB) {
            isEvenA = 0L == (tarA & 1L);
            isEvenB = 0L == (tarB & 1L);
            if (isEvenA && isEvenB) {
                tarA >>= 1;
                tarB >>= 1;
                displacement++;
            } else if (isEvenA) {
                tarA >>= 1;
            } else if (isEvenB) {
                tarB >>= 1;
            } else {
                if (tarA > tarB) {
                    tarA = tarA - tarB;
                } else {
                    tarB = tarB - tarA;
                }
            }
        }

        return tarA << displacement;
    }

    /**
     * 是否为2的幂数
     *
     * @param num
     * @return
     */
    public static boolean isPowerOf2(long num) {
        return num > 0L && 0L == (num & (num - 1L));
    }

    /**
     * 获取删除k个数字后的最小值
     *
     * @param num
     * @param k
     * @return
     */
    public static long removeSomeDigitsForMin(long num, int k) {
        if (num == 0L)
            return 0L;

        String theNum = valueOf(num);
        int len = theNum.length();

        boolean greaterThanZero = num > 0L;

        if (!greaterThanZero) {
            theNum = theNum.substring(1, len);
            len--;
        }
        if (len <= k)
            return 0L;

        BiFunction<Character, Character, Boolean> comparator = greaterThanZero ? (a, b) -> a > b : (a, b) -> a < b;

        int newLen = len - k;
        char[] stack = new char[len];
        int top = 0;
        char c;
        for (int i = 0; i < len; i++) {
            c = theNum.charAt(i);
            while (top > 0 && comparator.apply(stack[top - 1], c) && k > 0) {
                top--;
                k--;
            }
            stack[top++] = c;
        }

        int offset = 0;
        //noinspection AlibabaUndefineMagicConstant
        while (offset < newLen && stack[offset] == '0')
            offset++;

        theNum = offset == newLen ? "0" : new String(stack, offset, newLen - offset);

        return theNum.length() > 0 ?
                parseLong(greaterThanZero ? theNum : "-" + theNum) : 0L;
    }


}
