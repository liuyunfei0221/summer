package com.blue.article.api.model;

import com.blue.basic.serializer.IdentityDeserializer;
import com.blue.basic.serializer.IdentitySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * reading info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class ReadingInfo implements Serializable {

    private static final long serialVersionUID = 7589886728954681798L;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long favorites;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long readings;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long comments;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long likes;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
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
