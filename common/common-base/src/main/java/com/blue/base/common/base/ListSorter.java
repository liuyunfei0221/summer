package com.blue.base.common.base;


import java.text.Collator;
import java.util.List;
import java.util.Locale;

import static java.text.Collator.getInstance;
import static java.util.stream.Collectors.toList;

/**
 * Lexicographic order
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "WeakerAccess"})
public final class ListSorter {

    /**
     * 按语言对字符集合做字典排序
     *
     * @param list
     * @param locale
     * @return
     */
    public static List<String> sort(List<String> list, Locale locale) {
        Collator collator = getInstance(locale);
        return list.stream().sorted(collator).collect(toList());
    }

}
