package com.blue.portal.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.ConstantProcessor.assertBulletinType;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new bulletin
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class BulletinInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = 1404149561470599216L;

    protected String title;

    protected String content;

    protected String link;

    /**
     * @see com.blue.basic.constant.portal.BulletinType
     */
    protected Integer type;

    protected Integer priority;

    protected Long activeTime;

    protected Long expireTime;

    public BulletinInsertParam() {
    }

    public BulletinInsertParam(String title, String content, String link, Integer type,
                               Integer priority, Long activeTime, Long expireTime) {
        this.title = title;
        this.content = content;
        this.link = link;
        this.type = type;
        this.priority = priority;
        this.activeTime = activeTime;
        this.expireTime = expireTime;
    }

    @Override
    public void asserts() {
        int len;
        if (isBlank(this.title) || (len = this.title.length()) < (int) TITLE_LEN_MIN.value || len > (int) TITLE_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid title");
        if (isBlank(this.content) || (len = this.content.length()) < (int) CONTENT_LEN_MIN.value || len > (int) CONTENT_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid content");
        if (isBlank(this.link) || (len = this.link.length()) < (int) LINK_LEN_MIN.value || len > (int) LINK_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid link");
        assertBulletinType(this.type, false);
        if (isNull(this.priority))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "priority can't be null");
        if (isNull(this.activeTime) || this.activeTime < 1)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "activeTime can't be null or less than 1");
        if (isNull(this.expireTime) || this.expireTime < 1)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "expireTime can't be null or less than 1");
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Long activeTime) {
        this.activeTime = activeTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "BulletinInsertParam{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                ", priority=" + priority +
                ", activeTime=" + activeTime +
                ", expireTime=" + expireTime +
                '}';
    }

}
