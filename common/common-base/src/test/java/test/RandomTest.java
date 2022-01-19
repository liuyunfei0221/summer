package test;

import static com.blue.base.common.base.TheRandomGenerator.generate;

/**
 * @author liuyunfei
 * @date 2021/11/19
 * @apiNote
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
