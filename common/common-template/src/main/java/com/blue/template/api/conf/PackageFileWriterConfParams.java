package com.blue.template.api.conf;

/**
 * package file writer params
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc"})
public class PackageFileWriterConfParams implements PackageFileWriterConf {

    private String basePackagePath;

    public PackageFileWriterConfParams() {
    }

    @Override
    public String getBasePackagePath() {
        return basePackagePath;
    }

    public void setBasePackagePath(String basePackagePath) {
        this.basePackagePath = basePackagePath;
    }

    @Override
    public String toString() {
        return "PackageFileWriterConfParams{" +
                "basePackagePath='" + basePackagePath + '\'' +
                '}';
    }

}
