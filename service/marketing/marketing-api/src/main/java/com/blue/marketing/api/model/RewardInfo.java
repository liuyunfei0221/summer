package com.blue.marketing.api.model;


import java.io.Serializable;

/**
 * 签到奖励信息
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class RewardInfo implements Serializable {

    private static final long serialVersionUID = 5642744602142762972L;

    /**
     * 奖励id
     */
    private final Long id;

    /**
     * 奖励名称
     */
    private final String name;

    /**
     * 奖励描述
     */
    private final String detail;

    /**
     * 奖励图片链接
     */
    private final String link;

    public RewardInfo(Long id, String name, String detail, String link) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.link = link;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "RewardDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

}
