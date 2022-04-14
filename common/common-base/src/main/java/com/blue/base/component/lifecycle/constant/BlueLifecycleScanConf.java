package com.blue.base.component.lifecycle.constant;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;

/**
 * BlueLifecycle scan conf
 *
 * @author liuyunfei
 */
public enum BlueLifecycleScanConf {

    /**
     * lifecycle package
     */
    BLUE_LIFECYCLE_SCAN_PACKAGE("basePackages", new String[]{"com.blue"}, false, BlueLifecycle.class, "BlueLifecycle scan conf");

    /**
     * annotation param key
     */
    public final String scanPackagesAttrName;

    /**
     * default scan package
     */
    public final String[] defaultScanPackages;

    /**
     * use default filter
     */
    public final Boolean useDefaultFilters;

    /**
     * target type
     */
    public final Class<?> targetType;

    /**
     * disc
     */
    public final String disc;

    BlueLifecycleScanConf(String scanPackagesAttrName, String[] defaultScanPackages, Boolean useDefaultFilters, Class<?> targetType, String disc) {
        this.scanPackagesAttrName = scanPackagesAttrName;
        this.defaultScanPackages = defaultScanPackages;
        this.useDefaultFilters = useDefaultFilters;
        this.targetType = targetType;
        this.disc = disc;
    }
}
