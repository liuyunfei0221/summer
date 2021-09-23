package com.blue.dubbo.constant;

/**
 * BlueDubbo扫描配置
 *
 * @author DarkBlue
 */
public enum BlueDubboScanConf {

    /**
     * dubbo扫描路径
     */
    BLUE_DUBBO_SCAN_PACKAGE("basePackages", "basePackageClasses", new String[]{"com"}, "BlueDubbo扫描配置");

    /**
     * 待扫描的包路径的注解key名称
     */
    public final String scanPackagesAttrName;

    /**
     * 待扫描类的注解key名称
     */
    public final String basePackageClassesAttrName;

    /**
     * 默认的扫描路径
     */
    public final String[] defaultScanPackages;

    /**
     * 描述
     */
    public final String disc;

    BlueDubboScanConf(String scanPackagesAttrName, String basePackageClassesAttrName, String[] defaultScanPackages, String disc) {
        this.scanPackagesAttrName = scanPackagesAttrName;
        this.basePackageClassesAttrName = basePackageClassesAttrName;
        this.defaultScanPackages = defaultScanPackages;
        this.disc = disc;
    }

}
