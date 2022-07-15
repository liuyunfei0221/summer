package com.blue.basic.model.message;

import java.io.Serializable;

/**
 * language info
 *
 * @author liuyunfei
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class LanguageInfo implements Serializable {

    private static final long serialVersionUID = -6952599354678520139L;

    private String name;

    private String identity;

    private String icon;

    public LanguageInfo() {
    }

    public LanguageInfo(String name, String identity, String icon) {
        this.name = name;
        this.identity = identity;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "LanguageInfo{" +
                "name='" + name + '\'' +
                ", identity='" + identity + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }

}
