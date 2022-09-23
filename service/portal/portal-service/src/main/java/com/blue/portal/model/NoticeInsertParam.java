package com.blue.portal.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.ConstantProcessor.assertNoticeType;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new notice
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class NoticeInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = 70024367779800231L;

    protected String title;

    protected String content;

    protected String link;

    /**
     * @see com.blue.basic.constant.portal.NoticeType
     */
    protected Integer type;


    public NoticeInsertParam() {
    }

    public NoticeInsertParam(String title, String content, String link, Integer type) {
        this.title = title;
        this.content = content;
        this.link = link;
        this.type = type;
    }

    @Override
    public void asserts() {
        int len;
        if (isBlank(this.title) || (len = this.title.length()) < (int) TITLE_LEN_MIN.value || len > (int) TITLE_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid title");
        if (isBlank(this.link) || (len = this.link.length()) < (int) LINK_LEN_MIN.value || len > (int) LINK_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid link");
        assertNoticeType(this.type, false);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "NoticeInsertParam{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                '}';
    }

}
