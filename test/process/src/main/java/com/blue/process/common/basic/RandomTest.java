package com.blue.process.common.basic;

import static com.blue.basic.common.base.TheRandomGenerator.generate;

/**
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class RandomTest {

    private static void test1() {
        int test = 10;

        int min = 0, max = 10000, size = 20000;
        boolean distinct = true;

        long begin = System.currentTimeMillis();

        for (int i = 0; i < test; i++)
            System.err.println(generate(min, max, size, distinct));

        System.err.println("duration: " + (System.currentTimeMillis() - begin));
    }


    public static void main(String[] args) {
        test1();
    }

}
