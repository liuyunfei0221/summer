package com.blue.base.api.model;

import java.io.Serializable;

/**
 * country info
 *
 * @author liuyunfei
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public final class CountryInfo implements Serializable {

    private static final long serialVersionUID = 8711166488625462662L;

    private Long id;

    private String name;

    private String countryCode;

    private String phoneCode;

    private String emoji;

    private String emojiu;

    public CountryInfo() {
    }

    public CountryInfo(Long id, String name, String countryCode, String phoneCode, String emoji, String emojiu) {
        this.id = id;
        this.name = name;
        this.countryCode = countryCode;
        this.phoneCode = phoneCode;
        this.emoji = emoji;
        this.emojiu = emojiu;
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getEmojiu() {
        return emojiu;
    }

    public void setEmojiu(String emojiu) {
        this.emojiu = emojiu;
    }

    @Override
    public String toString() {
        return "CountryInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", phoneCode='" + phoneCode + '\'' +
                ", emoji='" + emoji + '\'' +
                ", emojiu='" + emojiu + '\'' +
                '}';
    }

}
