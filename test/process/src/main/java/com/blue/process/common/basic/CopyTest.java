package com.blue.process.common.basic;

import com.blue.basic.common.base.BlueBeanCopier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CopyTest {

    public static void main(String[] args) {
//        testConvert();
        testConvertList();
    }

    private static void testConvert() {
        ModelA modelA = new ModelA("a", 1L, 1);

        ModelB modelB = BlueBeanCopier.convert(modelA, ModelB.class);

        System.err.println(modelB);
    }

    private static void testConvertList() {
        List<ModelA> from = Stream.of(new ModelA("a", 1L, 1), new ModelA("b", 2L, 2), new ModelA("c", 3L, 3))
                .collect(Collectors.toList());

        List<ModelB> modelBS = BlueBeanCopier.convertList(from, ModelB.class);

        System.err.println(modelBS);
    }

}
