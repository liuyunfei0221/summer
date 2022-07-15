package com.blue.mongo.constant;

import com.blue.basic.constant.common.SortType;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * package sort attr
 *
 * @author liuyunfei
 */
public enum SortSchema {

    /**
     * DESC
     */
    DESC(SortType.DESC, attrs -> Sort.by(attrs.stream().map(Sort.Order::desc)
            .collect(toList()))),
    /**
     * ASC
     */
    ASC(SortType.ASC, attrs -> Sort.by(attrs.stream().map(Sort.Order::asc)
            .collect(toList())));

    public final SortType sortType;

    public final Function<List<String>, Sort> converter;

    SortSchema(SortType sortType, Function<List<String>, Sort> converter) {
        this.sortType = sortType;
        this.converter = converter;
    }

}
