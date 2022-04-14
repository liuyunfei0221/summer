package com.blue.member.component.credential.impl;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.member.component.credential.inter.CredentialCollector;
import com.blue.member.repository.entity.MemberBasic;

import java.util.LinkedList;
import java.util.List;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.BlueChecker.isNotBlank;
import static com.blue.base.constant.auth.CredentialType.EMAIL_PWD;
import static com.blue.base.constant.auth.CredentialType.EMAIL_VERIFY_AUTO_REGISTER;
import static com.blue.base.constant.base.Status.VALID;

/**
 * generate credential by phone
 *
 * @author liuyunfei
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
        if (isBlank(email))
            return;

        credentials.add(new CredentialInfo(email, EMAIL_VERIFY_AUTO_REGISTER.identity, "", VALID.status, "from registry"));

        if (isNotBlank(access))
            credentials.add(new CredentialInfo(email, EMAIL_PWD.identity, access, VALID.status, "from registry"));
    }

}
