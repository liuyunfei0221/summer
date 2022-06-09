package com.blue.member.component.credential.impl;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.member.component.credential.inter.CredentialCollector;
import com.blue.member.repository.entity.MemberBasic;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.auth.CredentialType.*;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.base.constant.common.Status.INVALID;
import static com.blue.base.constant.common.Status.VALID;
import static java.util.stream.Collectors.toSet;

/**
 * generate credential by phone
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces", "JavadocDeclaration", "DuplicatedCode", "JavaDoc"})
public final class PhoneCredentialCollector implements CredentialCollector {

    private static final Set<String> TAR_TYPES = Stream.of(
            PHONE_VERIFY_AUTO_REGISTER, LOCAL_PHONE_AUTO_REGISTER, WECHAT_AUTO_REGISTER, MINI_PRO_AUTO_REGISTER, PHONE_PWD
    ).map(t -> t.identity).collect(toSet());

    private static final BiFunction<String, String, Integer> STATUS_GETTER = (type, access) ->
            !PHONE_PWD.identity.equals(type) || isNotBlank(access) ? VALID.status : INVALID.status;

    /**
     * collect credential
     *
     * @param memberBasic
     * @param access
     * @param credentials
     */
    @Override
    public void collect(MemberBasic memberBasic, String access, List<CredentialInfo> credentials) {
        if (isNull(credentials))
            credentials = new LinkedList<>();

        if (isNull(memberBasic))
            return;

        String phone = memberBasic.getPhone();
        if (isBlank(phone))
            return;

        String tarAccess = isNotNull(access) ? access : EMPTY_DATA.value;
        TAR_TYPES.stream()
                .map(type -> new CredentialInfo(phone, type, tarAccess, STATUS_GETTER.apply(type, tarAccess), "from registry"))
                .forEach(credentials::add);
    }

    /**
     * package credential attribute to member basic
     *
     * @param credentialTypes
     * @param credential
     * @param memberBasic
     */
    @Override
    public void packageCredentialAttr(List<String> credentialTypes, String credential, MemberBasic memberBasic) {
        if (isEmpty(credentialTypes) || isBlank(credential) || isNull(memberBasic))
            return;

        for (String type : credentialTypes)
            if (TAR_TYPES.contains(type)) {
                memberBasic.setPhone(credential);
                return;
            }
    }

}
