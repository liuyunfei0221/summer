package com.blue.marketing.api.model;


import java.io.Serializable;

/**
 * sign in reward info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class RewardInfo implements Serializable {

    private static final long serialVersionUID = 5642744602142762972L;

    /**
     * reward id
     */
    private final Long id;

    /**
     * reward name
     */
    private final String name;

    /**
     * disc
     */
    private final String detail;

    /**
     * reward link
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
