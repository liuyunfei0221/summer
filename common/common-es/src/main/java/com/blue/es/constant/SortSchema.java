package com.blue.es.constant;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.blue.basic.constant.common.SortType;

import java.util.function.Function;

/**
 * package sort attr
 *
 * @author liuyunfei
 */
public enum SortSchema {

    /**
     * DESC
     */
    DESC(SortType.DESC, attr ->
            SortOptions.of(builder -> builder.field(FieldSort.of(b -> b.field(attr).order(SortOrder.Desc))))),
    /**
     * ASC
     */
    ASC(SortType.ASC, attr ->
            SortOptions.of(builder -> builder.field(FieldSort.of(b -> b.field(attr).order(SortOrder.Asc)))));

    public final SortType sortType;

    public final Function<String, SortOptions> converter;

    SortSchema(SortType sortType, Function<String, SortOptions> converter) {
        this.sortType = sortType;
        this.converter = converter;
    }

}
