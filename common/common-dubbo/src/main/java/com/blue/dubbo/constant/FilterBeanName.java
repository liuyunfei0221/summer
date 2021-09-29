package com.blue.dubbo.constant;

/**
 * constant bean names
 *
 * @author DarkBlue
 */
public enum FilterBeanName {

    /**
     * no filter, will use blue exp filter
     */
    NO_EXCEPTION_FILTER("-exception");

    public final String name;

    FilterBeanName(String name) {
        this.name = name;
    }

}
