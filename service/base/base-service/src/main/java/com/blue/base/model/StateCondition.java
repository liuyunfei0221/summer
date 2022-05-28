package com.blue.base.model;

import java.io.Serializable;

/**
 * state condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class StateCondition implements Serializable {

    private static final long serialVersionUID = -7819442947347864810L;

    private Long id;

    private Long countryId;

    private String nameLike;

    private Integer status;

    public StateCondition() {
    }

    public StateCondition(Long id, Long countryId, String nameLike, Integer status) {
        this.id = id;
        this.countryId = countryId;
        this.nameLike = nameLike;
        this.status = status;
    }

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

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StateCondition{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", nameLike='" + nameLike + '\'' +
                ", status=" + status +
                '}';
    }

}
