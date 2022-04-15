package com.blue.member.component.credential.impl;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.member.component.credential.inter.CredentialCollector;
import com.blue.member.repository.entity.MemberBasic;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.auth.CredentialType.EMAIL_PWD;
import static com.blue.base.constant.auth.CredentialType.EMAIL_VERIFY_AUTO_REGISTER;
import static com.blue.base.constant.base.Status.INVALID;
import static com.blue.base.constant.base.Status.VALID;
import static java.util.stream.Collectors.toSet;

/**
 * generate credential by phone
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces", "JavadocDeclaration", "DuplicatedCode"})
public final class EmailCredentialCollector implements CredentialCollector {

    private static final Set<String> TAR_TYPES = Stream.of(
            EMAIL_VERIFY_AUTO_REGISTER, EMAIL_PWD
    ).map(t -> t.identity).collect(toSet());

    private static final BiPredicate<String, String> STATUS_GETTER = (type, access) ->
            !EMAIL_PWD.identity.equals(type) || isNotBlank(access);

    /**
     * collect credential
     *
     * @param memberBasic
     * @param access
     * @param credentials
     */
    @Override
    public void collect(MemberBasic memberBasic, String access, List<CredentialInfo> credentials) {
        if (credentials == null)
            credentials = new LinkedList<>();

        if (memberBasic == null)
            return;

        String email = memberBasic.getEmail();
        if (isBlank(email))
            return;

        String tarAccess = access != null ? access : "";
        TAR_TYPES.stream()
                .map(type -> new CredentialInfo(email, type, tarAccess, STATUS_GETTER.test(type, tarAccess) ? VALID.status : INVALID.status, "from registry"))
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
        if (isEmpty(credentialTypes) || isBlank(credential) || memberBasic == null)
            return;

        for (String type : credentialTypes)
            if (TAR_TYPES.contains(type)) {
                memberBasic.setEmail(credential);
                return;
            }
    }

}
