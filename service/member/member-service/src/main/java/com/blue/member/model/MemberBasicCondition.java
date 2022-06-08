package com.blue.member.model;

import com.blue.base.constant.common.SortType;
import com.blue.base.model.common.SortCondition;
import com.blue.member.constant.MemberBasicSortAttribute;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isNotBlank;

/**
 * member condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MemberBasicCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = -5396181043246902442L;

    private Long id;

    private String phone;

    private String email;

    private String name;

    /**
     * @see com.blue.base.constant.member.Gender
     */
    private Integer gender;

    private String source;

    /**
     * @see com.blue.base.constant.common.Status
     */
    private Integer status;

    private Long createTimeBegin;

    private Long createTimeEnd;

    private Long updateTimeBegin;

    private Long updateTimeEnd;

    public MemberBasicCondition() {
        super(MemberBasicSortAttribute.ID.attribute, SortType.DESC.identity);
    }

    public MemberBasicCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public MemberBasicCondition(Long id, String phone, String email, String name, Integer gender, String source, Integer status,
                                Long createTimeBegin, Long createTimeEnd, Long updateTimeBegin, Long updateTimeEnd, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.source = source;
        this.status = status;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
        this.updateTimeBegin = updateTimeBegin;
        this.updateTimeEnd = updateTimeEnd;
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
        this.phone = isNotBlank(phone) ? phone : null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = isNotBlank(email) ? email : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = isNotBlank(name) ? name : null;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
        return "MemberCondition{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", source=" + source +
                ", status=" + status +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", updateTimeBegin=" + updateTimeBegin +
                ", updateTimeEnd=" + updateTimeEnd +
                '}';
    }

}
