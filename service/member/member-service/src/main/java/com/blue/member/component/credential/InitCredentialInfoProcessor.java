package com.blue.member.component.credential;

import com.blue.base.model.exps.BlueException;
import com.blue.member.repository.entity.MemberBasic;
import com.blue.secure.api.model.CredentialInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.secure.LoginType.*;

/**
 * expand login type
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
@Component
public final class InitCredentialInfoProcessor {

    public List<CredentialInfo> generateCredentialInfos(MemberBasic memberBasic, String access) {
        if (memberBasic == null)
            throw new BlueException(EMPTY_PARAM);

        List<CredentialInfo> credentials = new ArrayList<>();

        String phone = memberBasic.getPhone();
        String email = memberBasic.getEmail();

        credentials.add(new CredentialInfo(phone, PHONE_PWD.identity, access, "from registry"));
        credentials.add(new CredentialInfo(email, EMAIL_PWD.identity, access, "from registry"));
        credentials.add(new CredentialInfo(phone, WECHAT_AUTO_REGISTER.identity, "", "from registry"));
        credentials.add(new CredentialInfo(phone, MINI_PRO_AUTO_REGISTER.identity, "", "from registry"));

        return credentials;
    }

}
