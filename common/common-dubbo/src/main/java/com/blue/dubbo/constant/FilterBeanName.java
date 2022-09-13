package com.blue.dubbo.constant;

/**
 * constant bean names
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public enum FilterBeanName {

    /**
     * no filter, will use blue exp filter
     */
    NO_EXCEPTION_FILTER("-exception"),

    /**
     * blue filter
     */
    BLUE_EXCEPTION_FILTER("blue");

    public final String name;

    FilterBeanName(String name) {
        this.name = name;
    }

}
