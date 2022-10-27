package com.blue.agreement.api.model;

import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * agreement info for rest
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class AgreementInfo implements Serializable {

    private static final long serialVersionUID = -6759908982418691117L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long id;

    private String title;

    private String content;

    private String link;

    private Integer type;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long createTime;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long creator;

    public AgreementInfo() {
    }

    public AgreementInfo(Long id, String title, String content, String link, Integer type, Long createTime, Long creator) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.link = link;
        this.type = type;
        this.createTime = createTime;
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "AgreementInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                ", createTime=" + createTime +
                ", creator=" + creator +
                '}';
    }

}
