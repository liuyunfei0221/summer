package com.blue.base.api.model;

import com.blue.basic.serializer.Double2StringSerializer;
import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * country info
 *
 * @author liuyunfei
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public final class CountryInfo implements Serializable {

    private static final long serialVersionUID = 8711166488625462662L;

    @JsonSerialize(using = Long2StringSerializer.class)
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

    @JsonSerialize(using = Double2StringSerializer.class)
    private Double longitude;

    @JsonSerialize(using = Double2StringSerializer.class)
    private Double latitude;

    public CountryInfo() {
    }

    public CountryInfo(Long id, String name, String nativeName, String numericCode, String countryCode, String phoneCode,
                       String capital, String currency, String currencySymbol, String topLevelDomain, String region, String emoji, String emojiu, Double longitude, Double latitude) {
        this.id = id;
        this.name = name;
        this.nativeName = nativeName;
        this.numericCode = numericCode;
        this.countryCode = countryCode;
        this.phoneCode = phoneCode;
        this.capital = capital;
        this.currency = currency;
        this.currencySymbol = currencySymbol;
        this.topLevelDomain = topLevelDomain;
        this.region = region;
        this.emoji = emoji;
        this.emojiu = emojiu;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getNumericCode() {
        return numericCode;
    }

    public void setNumericCode(String numericCode) {
        this.numericCode = numericCode;
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

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getTopLevelDomain() {
        return topLevelDomain;
    }

    public void setTopLevelDomain(String topLevelDomain) {
        this.topLevelDomain = topLevelDomain;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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

    @Override
    public String toString() {
        return "CountryInfo{" +
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
                '}';
    }

}
