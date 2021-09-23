package com.blue.member.converter;

import com.blue.base.common.base.ConstantProcessor;
import com.blue.base.constant.base.Status;
import com.blue.base.constant.base.ThresholdNumericalValue;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberRegistryInfo;
import com.blue.member.repository.entity.MemberBasic;

import java.time.Instant;
import java.util.function.Function;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;

/**
 * member服务转换器
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class MemberModelConverters {

    public static final Function<MemberRegistryInfo, MemberBasic> MEMBER_REGISTRY_INFO_2_MEMBER_BASIC = memberRegistryInfo -> {
        if (memberRegistryInfo == null)
            throw new RuntimeException("memberRegistryInfo can't be null");

        String phone = memberRegistryInfo.getPhone();
        if (phone == null || "".equals(phone))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "手机号不能为空");
        if (phone.length() > ThresholdNumericalValue.PHONE_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "手机号过长");
        if (phone.length() < ThresholdNumericalValue.PHONE_LEN_MIN.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "手机号过短");

        String email = memberRegistryInfo.getEmail();
        if (email == null || "".equals(email))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "邮箱地址不能为空");
        if (email.length() > ThresholdNumericalValue.EMAIL_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "邮箱地址号过长");
        if (email.length() < ThresholdNumericalValue.EMAIL_LEN_MIN.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "邮箱地址号过短");

        String password = memberRegistryInfo.getPassword();
        if (password == null || "".equals(password))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "密码不能为空");
        if (password.length() > ThresholdNumericalValue.ACS_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "密码过长");
        if (password.length() < ThresholdNumericalValue.ACS_LEN_MIN.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "密码过短");

        String name = memberRegistryInfo.getName();
        if (name == null || "".equals(name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "昵称不能为空");

        Integer gender = memberRegistryInfo.getGender();
        if (gender == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "性别不能为空");

        ConstantProcessor.assertGenderIdentity(gender);

        String icon = memberRegistryInfo.getIcon();

        long epochSecond = Instant.now().getEpochSecond();

        MemberBasic memberBasic = new MemberBasic();
        memberBasic.setPhone(phone);
        memberBasic.setEmail(email);
        memberBasic.setPassword(password);
        memberBasic.setName(name);
        memberBasic.setIcon(icon);
        memberBasic.setGender(gender);
        memberBasic.setStatus(Status.VALID.status);
        memberBasic.setCreateTime(epochSecond);
        memberBasic.setUpdateTime(epochSecond);

        return memberBasic;
    };

    public static final Function<MemberBasic, MemberBasicInfo> MEMBER_BASIC_2_MEMBER_BASIC_INFO = memberBasic ->
            memberBasic != null ?
                    new MemberBasicInfo(memberBasic.getId(),
                            memberBasic.getPassword(), memberBasic.getPhone(),
                            memberBasic.getName(), memberBasic.getIcon(),
                            memberBasic.getGender(), memberBasic.getStatus(),
                            memberBasic.getCreateTime(), memberBasic.getUpdateTime())
                    : null;

}
