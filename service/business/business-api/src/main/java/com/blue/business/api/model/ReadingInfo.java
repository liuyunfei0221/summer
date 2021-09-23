package com.blue.business.api.model;

import java.io.Serializable;

/**
 * 阅读信息封装
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class ReadingInfo implements Serializable {

    private static final long serialVersionUID = 7589886728954681798L;

    private Long favorites;

    private Long readings;

    private Long comments;

    private Long likes;

    private Long boring;

    public ReadingInfo() {
    }

    public ReadingInfo(Long favorites, Long readings, Long comments, Long likes, Long boring) {
        this.favorites = favorites;
        this.readings = readings;
        this.comments = comments;
        this.likes = likes;
        this.boring = boring;
    }

    public Long getFavorites() {
        return favorites;
    }

    public void setFavorites(Long favorites) {
        this.favorites = favorites;
    }

    public Long getReadings() {
        return readings;
    }

    public void setReadings(Long readings) {
        this.readings = readings;
    }

    public Long getComments() {
        return comments;
    }

    public void setComments(Long comments) {
        this.comments = comments;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getBoring() {
        return boring;
    }

    public void setBoring(Long boring) {
        this.boring = boring;
    }

    @Override
    public String toString() {
        return "ReadingVO{" +
                "favorites=" + favorites +
                ", readings=" + readings +
                ", comments=" + comments +
                ", likes=" + likes +
                ", boring=" + boring +
                '}';
    }

}
