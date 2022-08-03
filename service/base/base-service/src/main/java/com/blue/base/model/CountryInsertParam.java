package com.blue.base.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new country
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class CountryInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = -2809095995192275441L;

    protected String name;

    protected String nativeName;

    protected String numericCode;

    protected String countryCode;

    protected String phoneCode;

    protected String capital;

    protected String currency;

    protected String currencySymbol;

    protected String topLevelDomain;

    protected String region;

    protected String emoji;

    protected String emojiu;

    public CountryInsertParam() {
    }

    public CountryInsertParam(String name, String nativeName, String numericCode, String countryCode, String phoneCode, String capital,
                              String currency, String currencySymbol, String topLevelDomain, String region, String emoji, String emojiu) {
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
    }

    @Override
    public void asserts() {
        if (isBlank(this.name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");
        if (isBlank(this.nativeName))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "nativeName can't be blank");
        if (isBlank(this.numericCode))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "numericCode can't be blank");
        if (isBlank(this.countryCode))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "countryCode can't be blank");
        if (isBlank(this.phoneCode))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "phoneCode can't be blank");
        if (isBlank(this.capital))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "capital can't be blank");
        if (isBlank(this.currency))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "currency can't be blank");
        if (isBlank(this.currencySymbol))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "currencySymbol can't be blank");
        if (isBlank(this.topLevelDomain))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "topLevelDomain can't be blank");
        if (isBlank(this.region))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "region can't be blank");
        if (isBlank(this.emoji))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "emoji can't be blank");
        if (isBlank(this.emojiu))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "emojiu can't be blank");
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

    @Override
    public String toString() {
        return "CountryInsertParam{" +
                "name='" + name + '\'' +
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
                '}';
    }

}
