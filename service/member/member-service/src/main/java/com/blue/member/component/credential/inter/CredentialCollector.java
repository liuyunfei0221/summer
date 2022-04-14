package com.blue.member.component.credential.inter;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.member.repository.entity.MemberBasic;

import java.util.List;

/**
 * generate credential by member basic
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc"})
public interface CredentialCollector {

    /**
     * collect credential
     *
     * @param memberBasic
     * @param access
     * @param credentials
     */
    void collect(MemberBasic memberBasic, String access, List<CredentialInfo> credentials);

}
