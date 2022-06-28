package com.blue.member.converter;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.constant.common.BlueNumericalValue;
import com.blue.base.constant.common.Status;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.*;
import com.blue.member.repository.entity.*;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.common.base.ConstantProcessor.assertGenderIdentity;
import static com.blue.base.common.base.ConstantProcessor.assertSource;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.base.constant.member.Gender.UNKNOWN;
import static com.blue.base.constant.member.SourceType.APP;
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

        String phone = memberRegistryParam.getPhone();
        if (isNotBlank(phone)) {
            if (phone.length() > BlueNumericalValue.PHONE_LEN_MAX.value)
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "phone length is too long");
            if (phone.length() < BlueNumericalValue.PHONE_LEN_MIN.value)
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "phone length is too short");
        }

        String email = memberRegistryParam.getEmail();
        if (isNotBlank(email)) {
            if (email.length() > BlueNumericalValue.EMAIL_LEN_MAX.value)
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "email length is too long");
            if (email.length() < BlueNumericalValue.EMAIL_LEN_MIN.value)
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "email length is too short");
        }

        String name = memberRegistryParam.getName();
        if (isBlank(name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");

        Long stamp = TIME_STAMP_GETTER.get();

        MemberBasic memberBasic = new MemberBasic();
        memberBasic.setPhone(phone);
        memberBasic.setEmail(email);

        memberBasic.setName(name);
        memberBasic.setIcon(memberRegistryParam.getIcon());
        memberBasic.setQrCode(EMPTY_DATA.value);
        memberBasic.setGender(ofNullable(memberRegistryParam.getGender())
                .map(gender -> {
                    assertGenderIdentity(gender, false);
                    return gender;
                }).orElseGet(() -> UNKNOWN.identity));
        memberBasic.setStatus(Status.VALID.status);

        String source = ofNullable(memberRegistryParam.getSource())
                .filter(BlueChecker::isNotBlank).orElse(APP.identity);
        assertSource(source, false);
        memberBasic.setSource(source);

        memberBasic.setCreateTime(stamp);
        memberBasic.setUpdateTime(stamp);

        return memberBasic;
    };

    public static final Function<MemberBasic, MemberBasicInfo> MEMBER_BASIC_2_MEMBER_BASIC_INFO = memberBasic -> {
        if (memberBasic != null)
            return new MemberBasicInfo(memberBasic.getId(), memberBasic.getPhone(), memberBasic.getEmail(),
                    memberBasic.getName(), memberBasic.getIcon(), memberBasic.getQrCode(), memberBasic.getGender(), memberBasic.getProfile(),
                    memberBasic.getStatus(), memberBasic.getCreateTime(), memberBasic.getUpdateTime());

        throw new BlueException(EMPTY_PARAM);
    };

    public static final Function<Address, AddressInfo> ADDRESS_2_ADDRESS_INFO = address -> {
        if (address != null)
            return new AddressInfo(address.getId(), address.getMemberId(), address.getContact(), address.getGender(),
                    address.getPhone(), address.getEmail(), address.getCountryId(), address.getCountry(), address.getStateId(),
                    address.getState(), address.getCityId(), address.getCity(), address.getAreaId(), address.getArea(),
                    address.getDetail(), address.getReference(), address.getExtra());

        throw new BlueException(EMPTY_PARAM);
    };

    public static final Function<List<Address>, List<AddressInfo>> ADDRESSES_2_ADDRESSES_INFO = mas ->
            mas != null && mas.size() > 0 ? mas.stream()
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

    public static final Function<RealName, RealNameInfo> REAL_NAME_2_REAL_NAME_INFO = realName -> {
        if (realName != null)
            return new RealNameInfo(
                    realName.getId(), realName.getMemberId(), realName.getRealName(),
                    realName.getGender(), realName.getBirthday(), realName.getNationalityId(),
                    realName.getEthnic(), realName.getIdCardNo(), realName.getResidenceAddress(),
                    realName.getIssuingAuthority(), realName.getSinceDate(), realName.getExpireDate(),
                    realName.getExtra(), realName.getStatus()
            );

        throw new BlueException(EMPTY_PARAM);
    };

}
