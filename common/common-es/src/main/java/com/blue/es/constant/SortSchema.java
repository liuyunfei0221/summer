package com.blue.es.constant;

import co.elastic.clients.elasticsearch._types.SortOrder;
import com.blue.basic.constant.common.SortType;

/**
 * package sort attr
 *
 * @author liuyunfei
 */
public enum SortSchema {

    /**
     * DESC
     */
    DESC(SortType.DESC, SortOrder.Desc),
    /**
     * ASC
     */
    ASC(SortType.ASC, SortOrder.Asc);

    public final SortType sortType;

    public final SortOrder sortOrder;

    SortSchema(SortType sortType, SortOrder sortOrder) {
        this.sortType = sortType;
        this.sortOrder = sortOrder;
    }

}
