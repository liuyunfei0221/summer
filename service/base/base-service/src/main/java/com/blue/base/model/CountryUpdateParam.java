package com.blue.base.model;

import com.blue.basic.model.exps.BlueException;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;

/**
 * params for update country
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class CountryUpdateParam extends CountryInsertParam {

    private static final long serialVersionUID = 1655107159579171633L;

    private Long id;

    public CountryUpdateParam() {
    }

    public CountryUpdateParam(Long id, String name, String nativeName, String numericCode, String countryCode, String phoneCode, String capital,
                              String currency, String currencySymbol, String topLevelDomain, String region, String emoji, String emojiu) {
        super(name, nativeName, numericCode, countryCode, phoneCode, capital, currency, currencySymbol, topLevelDomain, region, emoji, emojiu);
        this.id = id;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        if (isBlank(super.getName()) && isBlank(super.getNativeName()) && isBlank(super.getNumericCode()) &&
                isBlank(super.getCountryCode()) && isBlank(super.getPhoneCode()) && isBlank(super.getCapital()) &&
                isBlank(super.getCurrency()) && isBlank(super.getCurrencySymbol()) && isBlank(super.getTopLevelDomain()) &&
                isBlank(super.getRegion()) && isBlank(super.getEmoji()) && isBlank(super.getEmojiu()))
            throw new BlueException(EMPTY_PARAM);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CountryUpdateParam{" +
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
                '}';
    }

}
