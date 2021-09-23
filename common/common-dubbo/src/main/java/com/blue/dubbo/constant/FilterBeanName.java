package com.blue.dubbo.constant;

/**
 * 通用beanname
 *
 * @author DarkBlue
 */
@SuppressWarnings("SpellCheckingInspection")
public enum FilterBeanName {

    /**
     * dubbo异常处理器,将默认加载spi中配置的blueExceptionFilter
     */
    NO_EXCEPTION_FILTER("-exception"),

    BLUE_EXCEPTION_FILTER("blueException");

    public final String name;

    FilterBeanName(String name) {
        this.name = name;
    }

}
