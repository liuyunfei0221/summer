package com.blue.base.repository.entity;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isNull;

/**
 * state entity
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class State implements Serializable {

    private static final long serialVersionUID = 6517561062109132738L;

    @Id
    private Long id;

    private Long countryId;

    private String name;

    private String fipsCode;

    private String stateCode;

    private Integer status;

    private Long createTime;

    private Long updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = isNull(name) ? null : name.trim();
    }

    public String getFipsCode() {
        return fipsCode;
    }

    public void setFipsCode(String fipsCode) {
        this.fipsCode = isNull(fipsCode) ? null : fipsCode.trim();
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = isNull(stateCode) ? null : stateCode.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "State{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", name='" + name + '\'' +
                ", fipsCode='" + fipsCode + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}