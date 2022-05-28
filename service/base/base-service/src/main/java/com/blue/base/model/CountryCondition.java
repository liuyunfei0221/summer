package com.blue.base.model;

import java.io.Serializable;

/**
 * country condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class CountryCondition implements Serializable {

    private static final long serialVersionUID = 1172301534308346457L;

    private Long id;

    private String nameLike;

    private String nativeNameLike;

    private String numericCodeLike;

    private String countryCodeLike;

    private String phoneCodeLike;

    private String capitalLike;

    private String currency;

    private String currencySymbol;

    private String topLevelDomainLike;

    private String regionLike;

    private Integer status;

    public CountryCondition() {
    }

    public CountryCondition(Long id, String nameLike, String nativeNameLike, String numericCodeLike, String countryCodeLike, String phoneCodeLike, String capitalLike, String currency, String currencySymbol, String topLevelDomainLike, String regionLike, Integer status) {
        this.id = id;
        this.nameLike = nameLike;
        this.nativeNameLike = nativeNameLike;
        this.numericCodeLike = numericCodeLike;
        this.countryCodeLike = countryCodeLike;
        this.phoneCodeLike = phoneCodeLike;
        this.capitalLike = capitalLike;
        this.currency = currency;
        this.currencySymbol = currencySymbol;
        this.topLevelDomainLike = topLevelDomainLike;
        this.regionLike = regionLike;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    public String getNativeNameLike() {
        return nativeNameLike;
    }

    public void setNativeNameLike(String nativeNameLike) {
        this.nativeNameLike = nativeNameLike;
    }

    public String getNumericCodeLike() {
        return numericCodeLike;
    }

    public void setNumericCodeLike(String numericCodeLike) {
        this.numericCodeLike = numericCodeLike;
    }

    public String getCountryCodeLike() {
        return countryCodeLike;
    }

    public void setCountryCodeLike(String countryCodeLike) {
        this.countryCodeLike = countryCodeLike;
    }

    public String getPhoneCodeLike() {
        return phoneCodeLike;
    }

    public void setPhoneCodeLike(String phoneCodeLike) {
        this.phoneCodeLike = phoneCodeLike;
    }

    public String getCapitalLike() {
        return capitalLike;
    }

    public void setCapitalLike(String capitalLike) {
        this.capitalLike = capitalLike;
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

    public String getTopLevelDomainLike() {
        return topLevelDomainLike;
    }

    public void setTopLevelDomainLike(String topLevelDomainLike) {
        this.topLevelDomainLike = topLevelDomainLike;
    }

    public String getRegionLike() {
        return regionLike;
    }

    public void setRegionLike(String regionLike) {
        this.regionLike = regionLike;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CountryCondition{" +
                "id=" + id +
                ", nameLike='" + nameLike + '\'' +
                ", nativeNameLike='" + nativeNameLike + '\'' +
                ", numericCodeLike='" + numericCodeLike + '\'' +
                ", countryCodeLike='" + countryCodeLike + '\'' +
                ", phoneCodeLike='" + phoneCodeLike + '\'' +
                ", capitalLike='" + capitalLike + '\'' +
                ", currency='" + currency + '\'' +
                ", currencySymbol='" + currencySymbol + '\'' +
                ", topLevelDomainLike='" + topLevelDomainLike + '\'' +
                ", regionLike='" + regionLike + '\'' +
                ", status=" + status +
                '}';
    }

}
