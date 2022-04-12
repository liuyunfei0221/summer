package com.blue.member.component.credential.impl;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.member.component.credential.inter.CredentialCollector;
import com.blue.member.repository.entity.MemberBasic;

import java.util.LinkedList;
import java.util.List;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.constant.auth.CredentialType.*;

/**
 * generate credential by phone
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class PhoneCredentialCollector implements CredentialCollector {

    @Override
    public void collect(MemberBasic memberBasic, String access, List<CredentialInfo> credentials) {

        if (credentials == null)
            credentials = new LinkedList<>();

        if (memberBasic == null)
            return;

        String phone = memberBasic.getPhone();

        if (isBlank(phone) || isBlank(access))
            return;

        credentials.add(new CredentialInfo(phone, PHONE_VERIFY_AUTO_REGISTER.identity, "", "from registry"));
        credentials.add(new CredentialInfo(phone, PHONE_PWD.identity, access, "from registry"));
        credentials.add(new CredentialInfo(phone, LOCAL_PHONE_AUTO_REGISTER.identity, "", "from registry"));
        credentials.add(new CredentialInfo(phone, WECHAT_AUTO_REGISTER.identity, "", "from registry"));
        credentials.add(new CredentialInfo(phone, MINI_PRO_AUTO_REGISTER.identity, "", "from registry"));
    }

}
