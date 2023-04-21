package com.blue.base.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isNull;

/**
 * country entity
 *
 * @author liuyunfei
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"})
@Document(collection = "country")
public final class Country implements Serializable {

    private static final long serialVersionUID = 8387327953062438082L;

    @Id
    private Long id;

    private String name;

    private String nativeName;

    private String numericCode;

    private String countryCode;

    private String phoneCode;

    private String capital;

    private String currency;

    private String currencySymbol;

    private String topLevelDomain;

    private String region;

    private String emoji;

    private String emojiu;

    private Double longitude;

    private Double latitude;

    private Integer status;

    private Long createTime;

    private Long updateTime;

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
        this.name = isNull(name) ? null : name.trim();
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = isNull(nativeName) ? null : nativeName.trim();
    }

    public String getNumericCode() {
        return numericCode;
    }

    public void setNumericCode(String numericCode) {
        this.numericCode = isNull(numericCode) ? null : numericCode.trim();
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = isNull(countryCode) ? null : countryCode.trim();
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = isNull(phoneCode) ? null : phoneCode.trim();
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = isNull(capital) ? null : capital.trim();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = isNull(currency) ? null : currency.trim();
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = isNull(currencySymbol) ? null : currencySymbol.trim();
    }

    public String getTopLevelDomain() {
        return topLevelDomain;
    }

    public void setTopLevelDomain(String topLevelDomain) {
        this.topLevelDomain = isNull(topLevelDomain) ? null : topLevelDomain.trim();
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = isNull(region) ? null : region.trim();
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = isNull(emoji) ? null : emoji.trim();
    }

    public String getEmojiu() {
        return emojiu;
    }

    public void setEmojiu(String emojiu) {
        this.emojiu = isNull(emojiu) ? null : emojiu.trim();
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
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
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nativeName='" + nativeName + '\'' +
                ", numericCode='" + numericCode + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", phoneCode='" + phoneCode + '\'' +
                ", capital='" + capital + '\'' +
                ", currency='" + currency + '\'' +
                ", currencySymbol='" + currencySymbol + '\'' +
                ", topLevelDomain='" + topLevelDomain + '\'' +
                ", region='" + region + '\'' +
                ", emoji='" + emoji + '\'' +
                ", emojiu='" + emojiu + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}