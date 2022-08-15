package com.blue.member.converter;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.common.BlueCommonThreshold;
import com.blue.basic.constant.common.Status;
import com.blue.basic.model.exps.BlueException;
import com.blue.member.api.model.*;
import com.blue.member.repository.entity.*;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.assertGender;
import static com.blue.basic.common.base.ConstantProcessor.assertSource;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.basic.constant.member.Gender.UNKNOWN;
import static com.blue.basic.constant.member.SourceType.APP;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * model converters in member project
 *
 * @author liuyunfei
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class MemberModelConverters {

    public static final Function<MemberRegistryParam, MemberBasic> MEMBER_REGISTRY_INFO_2_MEMBER_BASIC = memberRegistryParam -> {
        if (isNull(memberRegistryParam))
            throw new BlueException(EMPTY_PARAM);

        String account = memberRegistryParam.getAccount();
        if (isNotBlank(account)) {
            if (account.length() > BlueCommonThreshold.ACCOUNT_LEN_MAX.value)
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "account length is too long");
            if (account.length() < BlueCommonThreshold.ACCOUNT_LEN_MIN.value)
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "account length is too short");
        }

        String phone = memberRegistryParam.getPhone();
        if (isNotBlank(phone)) {
            if (phone.length() > BlueCommonThreshold.PHONE_LEN_MAX.value)
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "phone length is too long");
            if (phone.length() < BlueCommonThreshold.PHONE_LEN_MIN.value)
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "phone length is too short");
        }

        String email = memberRegistryParam.getEmail();
        if (isNotBlank(email)) {
            if (email.length() > BlueCommonThreshold.EMAIL_LEN_MAX.value)
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "email length is too long");
            if (email.length() < BlueCommonThreshold.EMAIL_LEN_MIN.value)
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "email length is too short");
        }

        String name = memberRegistryParam.getName();
        if (isBlank(name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");

        Integer gender = ofNullable(memberRegistryParam.getGender())
                .orElse(UNKNOWN.identity);
        assertGender(gender, false);

        String source = ofNullable(memberRegistryParam.getSource())
                .filter(BlueChecker::isNotBlank).orElse(APP.identity);
        assertSource(source, false);

        Long stamp = TIME_STAMP_GETTER.get();

        MemberBasic memberBasic = new MemberBasic();
        memberBasic.setAccount(account);
        memberBasic.setPhone(phone);
        memberBasic.setEmail(email);
        memberBasic.setName(name);
        memberBasic.setIcon(ofNullable(memberRegistryParam.getIcon()).orElse(EMPTY_DATA.value));
        memberBasic.setQrCode(EMPTY_DATA.value);
        memberBasic.setGender(gender);
        memberBasic.setProfile(EMPTY_DATA.value);
        memberBasic.setStatus(Status.VALID.status);
        memberBasic.setSource(source);
        memberBasic.setCreateTime(stamp);
        memberBasic.setUpdateTime(stamp);

        return memberBasic;
    };

    public static final Function<MemberBasic, MemberBasicInfo> MEMBER_BASIC_2_MEMBER_BASIC_INFO = memberBasic -> {
        if (memberBasic != null)
            return new MemberBasicInfo(memberBasic.getId(), memberBasic.getAccount(), memberBasic.getPhone(), memberBasic.getEmail(),
                    memberBasic.getName(), memberBasic.getIcon(), memberBasic.getQrCode(), memberBasic.getGender(), memberBasic.getProfile(),
                    memberBasic.getStatus(), memberBasic.getCreateTime(), memberBasic.getUpdateTime());

        throw new BlueException(EMPTY_PARAM);
    };

    public static final Function<List<MemberBasic>, List<MemberBasicInfo>> MEMBER_BASICS_2_MEMBER_BASICS_INFO = mbs ->
            mbs != null && mbs.size() > 0 ? mbs.stream()
                    .map(MEMBER_BASIC_2_MEMBER_BASIC_INFO)
                    .collect(toList()) : emptyList();

    public static final Function<Address, AddressInfo> ADDRESS_2_ADDRESS_INFO = address -> {
        if (address != null)
            return new AddressInfo(address.getId(), address.getMemberId(), address.getContact(), address.getGender(),
                    address.getPhone(), address.getEmail(), address.getCountryId(), address.getCountry(), address.getStateId(),
                    address.getState(), address.getCityId(), address.getCity(), address.getAreaId(), address.getArea(),
                    address.getDetail(), address.getReference(), address.getExtra());

        throw new BlueException(EMPTY_PARAM);
    };

    public static final Function<List<Address>, List<AddressInfo>> ADDRESSES_2_ADDRESSES_INFO = as ->
            as != null && as.size() > 0 ? as.stream()
                    .map(ADDRESS_2_ADDRESS_INFO)
                    .collect(toList()) : emptyList();


    public static final Function<Card, CardInfo> CARD_2_CARD_INFO = card -> {
        if (card != null)
            return new CardInfo(card.getId(), card.getMemberId(), card.getName(), card.getDetail(), card.getCoverId(),
                    card.getCoverLink(), card.getContentId(), card.getContentLink(), card.getExtra(), card.getCreateTime(), card.getUpdateTime());

        throw new BlueException(EMPTY_PARAM);
    };

    public static final Function<List<Card>, List<CardInfo>> CARDS_2_CARDS_INFO = cards ->
            cards != null && cards.size() > 0 ? cards.stream()
                    .map(CARD_2_CARD_INFO)
                    .collect(toList()) : emptyList();

    /**
     * card -> card detail info
     */
    public static final BiFunction<Card, String, CardDetailInfo> CARD_2_CARD_DETAIL_INFO_CONVERTER = (card, creatorName) -> {
        if (isNull(card))
            throw new BlueException(EMPTY_PARAM);

        return new CardDetailInfo(card.getId(), card.getMemberId(), card.getName(), card.getDetail(), card.getCoverId(),
                card.getCoverLink(), card.getContentId(), card.getContentLink(), card.getExtra(), card.getCreateTime(), card.getUpdateTime(),
                isNotBlank(creatorName) ? creatorName : EMPTY_DATA.value);
    };

    public static final Function<MemberDetail, MemberDetailInfo> MEMBER_DETAIL_2_MEMBER_DETAIL_INFO = memberDetail -> {
        if (memberDetail != null)
            return new MemberDetailInfo(
                    memberDetail.getId(), memberDetail.getMemberId(), memberDetail.getName(),
                    memberDetail.getGender(), memberDetail.getPhone(), memberDetail.getEmail(),
                    memberDetail.getYearOfBirth(), memberDetail.getMonthOfBirth(), memberDetail.getDayOfBirth(),
                    memberDetail.getChineseZodiac(), memberDetail.getZodiacSign(),
                    memberDetail.getHeight(), memberDetail.getWeight(), memberDetail.getCountryId(), memberDetail.getCountry(),
                    memberDetail.getStateId(), memberDetail.getState(), memberDetail.getCityId(),
                    memberDetail.getCity(), memberDetail.getAddress(), memberDetail.getProfile(),
                    memberDetail.getHobby(), memberDetail.getHomepage(), memberDetail.getExtra()
            );

        throw new BlueException(EMPTY_PARAM);
    };

    public static final Function<List<MemberDetail>, List<MemberDetailInfo>> MEMBER_DETAILS_2_MEMBER_DETAILS_INFO = mds ->
            mds != null && mds.size() > 0 ? mds.stream()
                    .map(MEMBER_DETAIL_2_MEMBER_DETAIL_INFO)
                    .collect(toList()) : emptyList();

    public static final Function<RealName, RealNameInfo> REAL_NAME_2_REAL_NAME_INFO = realName -> {
        if (realName != null)
            return new RealNameInfo(
                    realName.getId(), realName.getMemberId(), realName.getRealName(),
                    realName.getGender(), realName.getBirthday(), realName.getNationality(),
                    realName.getEthnic(), realName.getIdCardNo(), realName.getResidenceAddress(),
                    realName.getIssuingAuthority(), realName.getSinceDate(), realName.getExpireDate(),
                    realName.getExtra(), realName.getStatus()
            );

        throw new BlueException(EMPTY_PARAM);
    };

    public static final Function<List<RealName>, List<RealNameInfo>> REAL_NAMES_2_REAL_NAMES_INFO = rns ->
            rns != null && rns.size() > 0 ? rns.stream()
                    .map(REAL_NAME_2_REAL_NAME_INFO)
                    .collect(toList()) : emptyList();

}
