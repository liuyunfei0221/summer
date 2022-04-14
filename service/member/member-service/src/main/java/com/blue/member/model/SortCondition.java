package com.blue.member.model;

import java.io.Serializable;

/**
 * base condition for select
 *
 * @author liuyunfei
 * @date 2021/8/31
 * @apiNote
 */
@SuppressWarnings("unused")
class SortCondition implements Serializable {

    private static final long serialVersionUID = 5408452099489469143L;

    protected String sortAttribute;

    protected String sortType;

    public SortCondition(String sortAttribute, String sortType) {
        this.sortAttribute = sortAttribute;
        this.sortType = sortType;
    }

    @Override
    public String toString() {
        return "SortCondition{" +
                "sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }

}
