package com.blue.basic.model.common;

import java.io.Serializable;

/**
 * base condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class SortCondition implements Serializable {

    private static final long serialVersionUID = 5408452099489469143L;

    protected String sortAttribute;

    protected String sortType;

    public SortCondition(String sortAttribute, String sortType) {
        this.sortAttribute = sortAttribute;
        this.sortType = sortType;
    }

    public String getSortAttribute() {
        return sortAttribute;
    }

    public void setSortAttribute(String sortAttribute) {
        this.sortAttribute = sortAttribute;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
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
