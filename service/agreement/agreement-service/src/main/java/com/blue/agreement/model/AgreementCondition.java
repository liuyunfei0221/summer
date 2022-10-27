package com.blue.agreement.model;

import com.blue.agreement.constant.AgreementSortAttribute;
import com.blue.basic.model.common.SortCondition;

import java.io.Serializable;

import static com.blue.basic.constant.common.SortType.DESC;

/**
 * agreement condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AgreementCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = 3214118282836899302L;

    private Long id;

    private String titleLike;

    private String linkLike;

    private Integer type;

    private Long createTimeBegin;

    private Long createTimeEnd;

    public AgreementCondition() {
        super(AgreementSortAttribute.CREATE_TIME.attribute, DESC.identity);
    }

    public AgreementCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public AgreementCondition(Long id, String titleLike, String linkLike, Integer type, Long createTimeBegin, Long createTimeEnd, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.titleLike = titleLike;
        this.linkLike = linkLike;
        this.type = type;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitleLike() {
        return titleLike;
    }

    public void setTitleLike(String titleLike) {
        this.titleLike = titleLike;
    }

    public String getLinkLike() {
        return linkLike;
    }

    public void setLinkLike(String linkLike) {
        this.linkLike = linkLike;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Long createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Long getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Long createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    @Override
    public String toString() {
        return "AgreementCondition{" +
                "id=" + id +
                ", titleLike='" + titleLike + '\'' +
                ", linkLike='" + linkLike + '\'' +
                ", type=" + type +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }

}
