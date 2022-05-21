package com.blue.base.repository.entity;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isNull;

/**
 * city entity
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class City implements Serializable {

    private static final long serialVersionUID = 8666480880659084918L;

    @Id
    private Long id;

    private Long countryId;

    private Long stateId;

    private String name;

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

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = isNull(name) ? null : name.trim();
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
        return "City{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", stateId=" + stateId +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}