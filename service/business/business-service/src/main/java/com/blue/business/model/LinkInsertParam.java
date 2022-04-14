package com.blue.business.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;

/**
 * link insert param
 *
 * @author liuyunfei
 * @date 2021/8/11
 * @apiNote
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class LinkInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = -9009080234789649834L;

    private String linkUrl;

    private String content;

    public LinkInsertParam() {
    }

    public LinkInsertParam(String linkUrl, String content) {
        this.linkUrl = linkUrl;
        this.content = content;
    }

    @Override
    public void asserts() {
        if (isBlank(this.linkUrl))
            throw new BlueException(BAD_REQUEST);
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
