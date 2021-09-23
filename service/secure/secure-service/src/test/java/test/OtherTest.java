package test;

import java.util.function.UnaryOperator;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class OtherTest {

    private static final UnaryOperator<Integer> HASH_DISCRETE_PROCESSOR = hash ->
            hash ^ (hash >>> 16);

    private static void show(int hash, int tar) {
        int h = HASH_DISCRETE_PROCESSOR.apply(hash);

        if((h & tar) == (~h & tar)){
            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        //System.err.println((h & tar) + " --- " + (~h & tar));
    }

    public static void main(String[] args) {

        int tar = 64 - 1;

        for (int i = 0; i < 10000000; i++) {
            show(randomAlphanumeric(16).hashCode(), tar);
        }

    }


}
