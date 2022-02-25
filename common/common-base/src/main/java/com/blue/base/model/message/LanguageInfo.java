package com.blue.base.model.message;

import java.io.Serializable;

/**
 * language info
 *
 * @author DarkBlue
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class LanguageInfo implements Serializable {

    private static final long serialVersionUID = -6952599354678520139L;

    private String name;

    private String identity;

    private String link;

    public LanguageInfo() {
    }

    public LanguageInfo(String name, String identity, String link) {
        this.name = name;
        this.identity = identity;
        this.link = link;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "LanguageInfo{" +
                "name='" + name + '\'' +
                ", identity='" + identity + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

}
