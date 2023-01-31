package com.blue.basic.common.base;

import com.blue.basic.model.exps.BlueException;

import java.util.*;
import java.util.function.Supplier;

import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class TheRandomGenerator {

    private static final Supplier<Random> RANDOM_SUP = TheRandom::get;

    private static List<Integer> generateUnDistinct(int min, int max, int size) {
        List<Integer> res = new ArrayList<>(size);

        Random random = RANDOM_SUP.get();

        int mask = max - min;
        for (int i = 0; i < size; i++)
            res.add(random.nextInt(mask) + min);

        return res;
    }

    private static List<Integer> generateDistinct(int min, int max, int size) {
        Set<Integer> res = new HashSet<>(size << 1);

        Random random = RANDOM_SUP.get();

        int mask = max - min;
        int step = 0;
        while (step < size)
            if (res.add(random.nextInt(mask) + min))
                step++;

        return new ArrayList<>(res);
    }

    public static List<Integer> generate(int min, int max, int size, boolean distinct) {
        assertParam(min, max, size, distinct);
        return distinct ? generateDistinct(min, max, size) : generateUnDistinct(min, max, size);
    }

    private static void assertParam(int min, int max, int size, boolean distinct) {
        int interval = max - min;

        if (interval < 1)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "max must greater than min, max : " + max + ", min : " + min);

        if (size < 1)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "size can't be less than 1");

        if (distinct && interval < size)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "if distinct, size can't be greater than (max - min), size : " + size + ", max : " + max + ", min : " + min);
    }

}