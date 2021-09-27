package com.blue.base.component.lifecycle.constant;

/**
 * BlueLifecycle scan conf
 *
 * @author DarkBlue
 */
public enum BlueLifecycleScanConf {

    /**
     * lifecycle package
     */
    BLUE_LIFECYCLE_SCAN_PACKAGE("basePackages", new String[]{"com.blue"}, false, "BlueLifecycle扫描配置");

    /**
     * anno param key
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
     * disc
     */
    public final String disc;

    BlueLifecycleScanConf(String scanPackagesAttrName, String[] defaultScanPackages, Boolean useDefaultFilters, String disc) {
        this.scanPackagesAttrName = scanPackagesAttrName;
        this.defaultScanPackages = defaultScanPackages;
        this.useDefaultFilters = useDefaultFilters;
        this.disc = disc;
    }
}
