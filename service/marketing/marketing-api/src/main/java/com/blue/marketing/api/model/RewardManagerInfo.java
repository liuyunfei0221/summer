package com.blue.marketing.api.model;


import java.io.Serializable;

/**
 * sign in reward info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class RewardManagerInfo implements Serializable {

    private static final long serialVersionUID = 5642744602142762972L;

    private Long id;

    private String name;

    private String detail;

    private String link;

    /**
     * reward type
     *
     * @see com.blue.basic.constant.marketing.RewardType
     */
    private Integer type;

    /**
     * reward json
     */
    private String data;

    public RewardManagerInfo() {
    }

    public RewardManagerInfo(Long id, String name, String detail, String link, Integer type, String data) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.link = link;
        this.type = type;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RewardInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                ", data='" + data + '\'' +
                '}';
    }

}
