package com.blue.base.test;

import org.springframework.util.Assert;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * @author liuyunfei
 * @date 2021/11/19
 * @apiNote
 */
public final class RandomGenerator {

    private static final Random SEED_RANDOM;

    static {
        try {
            SEED_RANDOM = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("generate random failed, e = {}", e);
        }
    }

    private static final AtomicLong INCR = new AtomicLong(SEED_RANDOM.nextLong());

    private static final Supplier<Random> RANDOM_SUP = () -> {
        Random random = BlueRandom.current();

        //random.setSeed(~INCR.getAndIncrement() ^ ~System.currentTimeMillis() & ~SEED_RANDOM.nextLong());
        return random;
    };


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


    public static void main(String[] args) {

        int min = 0, max = 20, size = 5;
        boolean distinct = false;

        System.err.println(generate(min, max, size, distinct));
    }

}
