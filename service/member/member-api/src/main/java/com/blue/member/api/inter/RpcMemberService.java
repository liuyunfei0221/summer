package com.blue.member.api.inter;

import com.blue.member.api.model.MemberBasicInfo;

import java.util.concurrent.CompletableFuture;

/**
 * rpc member interface
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface RpcMemberService {

    /**
     * query member basic by phone
     *
     * @param phone
     * @return
     */
    CompletableFuture<MemberBasicInfo> getMemberBasicByPhone(String phone);

    /**
     * query member basic by email
     *
     * @param email
     * @return
     */
    CompletableFuture<MemberBasicInfo> getMemberBasicByEmail(String email);

}
