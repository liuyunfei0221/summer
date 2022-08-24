package com.blue.basic.model.common;

import java.io.Serializable;

/**
 * sort element
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class SortElement implements Serializable {

    private static final long serialVersionUID = -7830270811900844222L;

    private String sortAttribute;

    private String sortType;

    public SortElement() {
    }

    public SortElement(String sortAttribute, String sortType) {
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
        return "Sort{" +
                "sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }

}
