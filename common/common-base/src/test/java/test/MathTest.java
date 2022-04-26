package test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.blue.base.common.base.MathProcessor.assertDisorderIntegerContinuous;

public class MathTest {

    public static void main(String[] args) {
        List<Integer> collect = Stream.of(5, 9, 7, 10, 8, 6, 4, 1, 3, 2, 11).collect(Collectors.toList());

        boolean continuous = assertDisorderIntegerContinuous(collect);

        System.err.println(continuous);
    }

}
