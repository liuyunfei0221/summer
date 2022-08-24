package com.blue.mongo.constant;

import com.blue.basic.constant.common.SortType;
import org.springframework.data.domain.Sort;

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
    DESC(SortType.DESC, Sort.Order::desc),

    /**
     * ASC
     */
    ASC(SortType.ASC, Sort.Order::asc);

    public final SortType sortType;

    public final Function<String, Sort.Order> sortOrder;

    SortSchema(SortType sortType, Function<String, Sort.Order> sortOrder) {
        this.sortType = sortType;
        this.sortOrder = sortOrder;
    }

}
