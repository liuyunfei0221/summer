package com.blue.member.converter;

import com.blue.base.constant.common.BlueNumericalValue;
import com.blue.base.constant.common.Status;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.*;
import com.blue.member.repository.entity.*;

import java.util.List;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.common.base.ConstantProcessor.assertGenderIdentity;
import static com.blue.base.constant.auth.CredentialType.PHONE_PWD;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.member.Gender.UNKNOWN;
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
        memberBasic.setGender(ofNullable(memberRegistryParam.getGender())
                .map(gender -> {
                    assertGenderIdentity(gender, false);
                    return gender;
                }).orElseGet(() -> UNKNOWN.identity));
        memberBasic.setStatus(Status.VALID.status);

        String source = memberRegistryParam.getSource();
        memberBasic.setSource(isNotBlank(source) ? source : PHONE_PWD.source);

        memberBasic.setCreateTime(stamp);
        memberBasic.setUpdateTime(stamp);

        return memberBasic;
    };

    public static final Function<MemberBasic, MemberBasicInfo> MEMBER_BASIC_2_MEMBER_BASIC_INFO = memberBasic -> {
        if (memberBasic != null)
            return new MemberBasicInfo(memberBasic.getId(), memberBasic.getPhone(), memberBasic.getEmail(),
                    memberBasic.getName(), memberBasic.getIcon(), memberBasic.getGender(), memberBasic.getSummary(),
                    memberBasic.getStatus(), memberBasic.getCreateTime(), memberBasic.getUpdateTime());

        throw new BlueException(EMPTY_PARAM);
    };

    public static final Function<MemberBasic, MemberInfo> MEMBER_BASIC_2_MEMBER_INFO = memberBasic -> {
        if (memberBasic != null)
            return new MemberInfo(memberBasic.getId(), memberBasic.getName(), memberBasic.getIcon(), memberBasic.getGender());

        throw new BlueException(EMPTY_PARAM);
    };

    public static final Function<MemberAddress, MemberAddressInfo> MEMBER_ADDRESS_2_MEMBER_ADDRESS_INFO = memberAddress -> {
        if (memberAddress != null)
            return new MemberAddressInfo(memberAddress.getId(), memberAddress.getMemberId(), memberAddress.getMemberName(), memberAddress.getGender(),
                    memberAddress.getPhone(), memberAddress.getEmail(), memberAddress.getCountryId(), memberAddress.getCountry(), memberAddress.getStateId(),
                    memberAddress.getState(), memberAddress.getCityId(), memberAddress.getCity(), memberAddress.getAreaId(), memberAddress.getArea(),
                    memberAddress.getDetail(), memberAddress.getReference(), memberAddress.getExtra());

        throw new BlueException(EMPTY_PARAM);
    };

    public static final Function<List<MemberAddress>, List<MemberAddressInfo>> MEMBER_ADDRESSES_2_MEMBER_ADDRESSES_INFO = mas ->
            mas != null && mas.size() > 0 ? mas.stream()
                    .map(MEMBER_ADDRESS_2_MEMBER_ADDRESS_INFO)
                    .collect(toList()) : emptyList();

    public static final Function<MemberBusiness, MemberBusinessInfo> MEMBER_BUSINESS_2_MEMBER_BUSINESS_INFO = memberBusiness -> {
        if (memberBusiness != null)
            return new MemberBusinessInfo(memberBusiness.getId(), memberBusiness.getMemberId(), memberBusiness.getQrCode(), memberBusiness.getProfile(),
                    memberBusiness.getExtra(), memberBusiness.getStatus(), memberBusiness.getCreateTime(), memberBusiness.getUpdateTime());

        throw new BlueException(EMPTY_PARAM);
    };

    public static final Function<MemberDetail, MemberDetailInfo> MEMBER_DETAIL_2_MEMBER_DETAIL_INFO = memberDetail -> {
        if (memberDetail != null)
            return new MemberDetailInfo(
                    memberDetail.getId(), memberDetail.getMemberId(), memberDetail.getName(),
                    memberDetail.getGender(), memberDetail.getPhone(), memberDetail.getEmail(),
                    memberDetail.getCountryId(), memberDetail.getCountry(),
                    memberDetail.getStateId(), memberDetail.getState(), memberDetail.getCityId(),
                    memberDetail.getCity(), memberDetail.getAddress(), memberDetail.getProfile(),
                    memberDetail.getHobby(), memberDetail.getHomepage(), memberDetail.getExtra()
            );

        throw new BlueException(EMPTY_PARAM);
    };

    public static final Function<MemberRealName, MemberRealNameInfo> MEMBER_REAL_NAME_2_MEMBER_REAL_NAME_INFO = memberRealName -> {
        if (memberRealName != null)
            return new MemberRealNameInfo(
                    memberRealName.getId(), memberRealName.getMemberId(), memberRealName.getRealName(),
                    memberRealName.getGender(), memberRealName.getBirthday(), memberRealName.getNationalityId(),
                    memberRealName.getEthnicId(), memberRealName.getIdCardNo(), memberRealName.getResidenceAddress(),
                    memberRealName.getIssuingAuthority(), memberRealName.getSinceDate(), memberRealName.getExpireDate(),
                    memberRealName.getExtra(), memberRealName.getStatus()
            );

        throw new BlueException(EMPTY_PARAM);
    };

}
