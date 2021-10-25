package com.blue.business.model;

import java.io.Serializable;

/**
 * link insert param
 *
 * @author DarkBlue
 * @date 2021/8/11
 * @apiNote
 */
@SuppressWarnings("unused")
public final class LinkInsertParam implements Serializable {

    private static final long serialVersionUID = -9009080234789649834L;

    private String linkUrl;

    private String content;

    public LinkInsertParam() {
    }

    public LinkInsertParam(String linkUrl, String content) {
        this.linkUrl = linkUrl;
        this.content = content;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "LinkInsertParam{" +
                "linkUrl='" + linkUrl + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
