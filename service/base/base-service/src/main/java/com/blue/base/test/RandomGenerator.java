package com.blue.base.test;

import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author liuyunfei
 * @date 2021/11/19
 * @apiNote
 */
@SuppressWarnings("unused")
public final class RandomGenerator {

    private static final Supplier<Random> RANDOM_SUP = BlueRandom::get;

    private static List<Integer> generateUnDistinct(int min, int max, int size, Random random) {
        List<Integer> res = new ArrayList<>(size);

        int mask = max - min;
        for (int i = 0; i < size; i++) {
            res.add(random.nextInt(mask) + min);
        }

        return res;
    }

    private static List<Integer> generateDistinct(int min, int max, int size, Random random) {
        Set<Integer> res = new HashSet<>(size);

        int mask = max - min;

        int step = 0;
        while (step < size) {
            if (res.add(random.nextInt(mask) + min)) {
                step++;
            }
        }

        return new ArrayList<>(res);
    }

    public static List<Integer> generate(int min, int max, int size, boolean distinct) {
        Assert.isTrue(max > min, "max mast greater than min, max = " + max + ", min = " + min);
        Assert.isTrue(size > 0, "size mast greater than 0, size = " + size);

        Random random = RANDOM_SUP.get();
        return distinct ? generateDistinct(min, max, size, random) : generateUnDistinct(min, max, size, random);
    }


    //public static void main(String[] args) {
    //
    //    int test = 10;
    //
    //    int min = 0, max = 20, size = 20;
    //    boolean distinct = true;
    //
    //    for (int i = 0; i < test; i++)
    //        System.err.println(generate(min, max, size, distinct));
    //
    //}

}
