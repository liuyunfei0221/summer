package com.blue.member.model;

import com.blue.base.constant.common.SortType;
import com.blue.base.model.common.SortCondition;
import com.blue.member.constant.MemberBusinessSortAttribute;

import java.io.Serializable;

/**
 * member business condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MemberBusinessCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = -5961680212338876121L;

    private Long id;

    private String phone;

    private String email;

    private String name;

    private String icon;

    /**
     * @see com.blue.base.constant.member.Gender
     */
    private Integer gender;

    /**
     * @see com.blue.base.constant.common.Status
     */
    private Integer status;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long updateTimeBegin;

    private Long updateTimeEnd;

    public MemberBusinessCondition() {
        super(MemberBusinessSortAttribute.ID.attribute, SortType.DESC.identity);
    }

    public MemberBusinessCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public MemberBusinessCondition(String sortAttribute, String sortType, Long id, String phone, String email, String name, String icon, Integer gender, Integer status,
                                   Long createTimeBegin, Long createTimeEnd, Long updateTimeBegin, Long updateTimeEnd, String sortAttribute1, String sortType1) {
        super(sortAttribute, sortType);
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.icon = icon;
        this.gender = gender;
        this.status = status;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
        this.updateTimeBegin = updateTimeBegin;
        this.updateTimeEnd = updateTimeEnd;
        this.sortAttribute = sortAttribute1;
        this.sortType = sortType1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Long getUpdateTimeBegin() {
        return updateTimeBegin;
    }

    public void setUpdateTimeBegin(Long updateTimeBegin) {
        this.updateTimeBegin = updateTimeBegin;
    }

    public Long getUpdateTimeEnd() {
        return updateTimeEnd;
    }

    public void setUpdateTimeEnd(Long updateTimeEnd) {
        this.updateTimeEnd = updateTimeEnd;
    }

    @Override
    public String toString() {
        return "MemberBusinessCondition{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", gender=" + gender +
                ", status=" + status +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", updateTimeBegin=" + updateTimeBegin +
                ", updateTimeEnd=" + updateTimeEnd +
                '}';
    }

}
