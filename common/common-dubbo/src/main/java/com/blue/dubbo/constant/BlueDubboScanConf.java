package com.blue.dubbo.constant;

/**
 * dubbo conf schemas
 *
 * @author DarkBlue
 */
public enum BlueDubboScanConf {

    /**
     * dubbo扫描路径
     */
    BLUE_DUBBO_SCAN_PACKAGE("basePackages", "basePackageClasses", new String[]{"com"}, "dubbo conf for scan");

    /**
     * package names for scan
     */
    public final String scanPackagesAttrName;

    /**
     * class names for scan
     */
    public final String scanClassesAttrName;

    /**
     * default package name for scan
     */
    public final String[] defaultScanPackages;

    /**
     * disc
     */
    public final String disc;

    BlueDubboScanConf(String scanPackagesAttrName, String scanClassesAttrName, String[] defaultScanPackages, String disc) {
        this.scanPackagesAttrName = scanPackagesAttrName;
        this.scanClassesAttrName = scanClassesAttrName;
        this.defaultScanPackages = defaultScanPackages;
        this.disc = disc;
    }

}
