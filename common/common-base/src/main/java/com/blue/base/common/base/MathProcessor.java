package com.blue.base.common.base;

import com.blue.base.model.exps.BlueException;
import reactor.util.Logger;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

import static com.blue.base.common.base.BlueChecker.isNotNull;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;
import static java.util.stream.Collectors.toList;
import static reactor.util.Loggers.getLogger;


/**
 * math util
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class MathProcessor {

    public static final Logger LOGGER = getLogger(MathProcessor.class);

    /**
     * check the continuity of the set
     *
     * @param collection
     * @return
     */
    public static boolean assertDisorderIntegerContinuous(Collection<Integer> collection) {
        List<Integer> sorted = collection.stream()
                .sorted(Integer::compare).collect(toList());

        Integer previous = null;

        for (Integer i : sorted) {
            if (isNull(i)) {
                LOGGER.info("boolean assertDisorderIntegerContinuous(Collection<Integer> collection), collection has null element");
                return false;
            }

            if (isNotNull(previous)) {
                if ((i - previous) != 1)
                    return false;
            }

            previous = i;
        }

        return true;
    }

    /**
     * check the continuity of the set
     *
     * @param collection
     * @return
     */
    public static boolean assertDisorderLongContinuous(Collection<Long> collection) {
        List<Long> sorted = collection.stream()
                .sorted(Long::compare).collect(toList());

        Long previous = null;

        for (Long l : sorted) {
            if (isNull(l)) {
                LOGGER.info("boolean assertDisorderLongContinuous(Collection<Long> collection), collection has null element");
                return false;
            }

            if (isNull(previous)) {
                previous = l;
            } else {
                if ((l - previous) != 1L)
                    return false;
            }
        }

        return true;
    }

    /**
     * find the greatest common divisor
     *
     * @param a
     * @param b
     * @return
     */
    public static long getGreatestCommonDivisor(long a, long b) {
        if (a < 0L || b < 0L)
            throw new BlueException(BAD_REQUEST);

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
     * is power of 2
     *
     * @param num
     * @return
     */
    public static boolean isPowerOf2(long num) {
        return num > 0L && 0L == (num & (num - 1L));
    }

    /**
     * get the minimum value after deleting k numbers
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
