package com.blue.business.api.model;

import java.io.Serializable;

/**
 * 链接新增数据封装
 *
 * @author DarkBlue
 * @date 2021/8/11
 * @apiNote
 */
public final class LinkInsertParam implements Serializable {

    private static final long serialVersionUID = -9009080234789649834L;

    /**
     * 链接
     */
    private String linkUrl;

    /**
     * 内容/描述等
     */
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
