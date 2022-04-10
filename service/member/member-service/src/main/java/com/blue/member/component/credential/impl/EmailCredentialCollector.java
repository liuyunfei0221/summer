package com.blue.member.component.credential.impl;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.member.component.credential.inter.CredentialCollector;
import com.blue.member.repository.entity.MemberBasic;

import java.util.LinkedList;
import java.util.List;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.constant.auth.LoginType.EMAIL_PWD;

/**
 * generate credential by phone
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class EmailCredentialCollector implements CredentialCollector {

    @Override
    public void collect(MemberBasic memberBasic, String access, List<CredentialInfo> credentials) {

        if (credentials == null)
            credentials = new LinkedList<>();

        if (memberBasic == null)
            return;

        String email = memberBasic.getEmail();

        if (isBlank(email) || isBlank(access))
            return;

        credentials.add(new CredentialInfo(email, EMAIL_PWD.identity, access, "from registry"));
    }

}