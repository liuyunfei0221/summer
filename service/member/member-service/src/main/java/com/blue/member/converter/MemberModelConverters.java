package com.blue.member.converter;

import com.blue.base.constant.base.BlueNumericalValue;
import com.blue.base.constant.base.Status;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberInfo;
import com.blue.member.api.model.MemberRegistryParam;
import com.blue.member.repository.entity.MemberBasic;

import java.time.Instant;
import java.util.function.Function;

import static com.blue.base.common.base.ConstantProcessor.assertGenderIdentity;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.member.Gender.UNKNOWN;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * model converters in member project
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class MemberModelConverters {

    public static final Function<MemberRegistryParam, MemberBasic> MEMBER_REGISTRY_INFO_2_MEMBER_BASIC = memberRegistryParam -> {
        if (memberRegistryParam == null)
            throw new BlueException(EMPTY_PARAM);

        String phone = memberRegistryParam.getPhone();
        if (isBlank(phone))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "phone can't be blank");
        if (phone.length() > BlueNumericalValue.PHONE_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "phone length is too long");
        if (phone.length() < BlueNumericalValue.PHONE_LEN_MIN.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "phone length is too short");

        String access = memberRegistryParam.getAccess();
        if (isBlank(access))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access can't be blank");
        if (access.length() > BlueNumericalValue.ACS_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access length is too long");
        if (access.length() < BlueNumericalValue.ACS_LEN_MIN.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access length is too short");

        String name = memberRegistryParam.getName();
        if (isBlank(name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");

        long epochSecond = Instant.now().getEpochSecond();

        MemberBasic memberBasic = new MemberBasic();
        memberBasic.setPhone(phone);
        memberBasic.setEmail(memberRegistryParam.getEmail());
        memberBasic.setName(name);
        memberBasic.setIcon(memberRegistryParam.getIcon());
        memberBasic.setGender(ofNullable(memberRegistryParam.getGender())
                .map(gender -> {
                    assertGenderIdentity(gender, false);
                    return gender;
                }).orElseGet(() -> UNKNOWN.identity));
        memberBasic.setStatus(Status.VALID.status);
        memberBasic.setCreateTime(epochSecond);
        memberBasic.setUpdateTime(epochSecond);

        return memberBasic;
    };

    public static final Function<MemberBasic, MemberBasicInfo> MEMBER_BASIC_2_MEMBER_BASIC_INFO = memberBasic ->
            memberBasic != null ?
                    new MemberBasicInfo(memberBasic.getId(),
                            memberBasic.getPhone(), memberBasic.getEmail(),
                            memberBasic.getName(), memberBasic.getIcon(),
                            memberBasic.getGender(), memberBasic.getStatus(),
                            memberBasic.getCreateTime(), memberBasic.getUpdateTime())
                    : null;

    public static final Function<MemberBasic, MemberInfo> MEMBER_BASIC_2_MEMBER_INFO = memberBasic ->
            memberBasic != null ?
                    new MemberInfo(memberBasic.getId(), memberBasic.getName(), memberBasic.getIcon(), memberBasic.getGender())
                    : null;


}
