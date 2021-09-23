package com.blue.base.component.lifecycle.constant;

/**
 * BlueLifecycle扫描配置
 *
 * @author DarkBlue
 */
public enum BlueLifecycleScanConf {

    /**
     * lifecycle扫描路径
     */
    BLUE_LIFECYCLE_SCAN_PACKAGE("basePackages", new String[]{"com.blue"}, false, "BlueLifecycle扫描配置");

    /**
     * 待扫描的包路径的注解key名称
     */
    public final String scanPackagesAttrName;

    /**
     * 默认的扫描路径
     */
    public final String[] defaultScanPackages;


    public final Boolean useDefaultFilters;

    /**
     * 描述
     */
    public final String disc;

    BlueLifecycleScanConf(String scanPackagesAttrName, String[] defaultScanPackages, Boolean useDefaultFilters, String disc) {
        this.scanPackagesAttrName = scanPackagesAttrName;
        this.defaultScanPackages = defaultScanPackages;
        this.useDefaultFilters = useDefaultFilters;
        this.disc = disc;
    }
}
