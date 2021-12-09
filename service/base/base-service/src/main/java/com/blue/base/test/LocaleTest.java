package com.blue.base.test;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liuyunfei
 * @date 2021/12/10
 * @apiNote
 */
public class LocaleTest {
    public static void main(String[] args) {
        Map<String, Locale> collect = Stream.of(Locale.getAvailableLocales())
                .collect(Collectors.toMap(Locale::getLanguage, l -> l, (a, b) -> a));

        System.err.println(collect);

    }

}
