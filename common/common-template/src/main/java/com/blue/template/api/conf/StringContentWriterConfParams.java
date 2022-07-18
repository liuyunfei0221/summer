package com.blue.template.api.conf;

import java.util.List;

/**
 * string content writer params
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc"})
public class StringContentWriterConfParams implements StringContentWriterConf {

    private List<StringContentAttr> stringContentAttrs;

    public StringContentWriterConfParams() {
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
        return "StringContentWriterConfParams{" +
                "stringContentAttrs=" + stringContentAttrs +
                '}';
    }

}
