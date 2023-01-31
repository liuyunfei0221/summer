package com.blue.basic.common.base;


import java.util.List;
import java.util.Locale;

import static java.text.Collator.getInstance;
import static java.util.stream.Collectors.toList;

/**
 * Lexicographic order
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "WeakerAccess", "unused"})
public final class ListSorter {

    /**
     * sort
     *
     * @param list
     * @param locale
     * @return
     */
    public static List<String> sort(List<String> list, Locale locale) {
        return list.stream().sorted(getInstance(locale)).collect(toList());
    }

}