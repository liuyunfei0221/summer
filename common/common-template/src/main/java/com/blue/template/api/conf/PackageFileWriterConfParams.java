package com.blue.template.api.conf;

import java.util.List;

/**
 * package file writer params
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc"})
public class PackageFileWriterConfParams implements StringContentWriterConf {

    private List<StringContentAttr> stringContentAttrs;

    public PackageFileWriterConfParams() {
    }

    @Override
    public List<StringContentAttr> getStringContentAttrs() {
        return stringContentAttrs;
    }

    public void setStringContentAttrs(List<StringContentAttr> stringContentAttrs) {
        this.stringContentAttrs = stringContentAttrs;
    }

    @Override
    public String toString() {
        return "PackageFileWriterConfParams{" +
                "stringContentAttrs=" + stringContentAttrs +
                '}';
    }
    
}
