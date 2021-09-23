package com.blue.member.api.inter;

import com.blue.member.api.model.MemberBasicInfo;

import java.util.concurrent.CompletableFuture;

/**
 * 成员相关RPC接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface RpcMemberService {

    /**
     * 根据手机号获取成员关键信息
     *
     * @param phone
     * @return
     */
    CompletableFuture<MemberBasicInfo> getMemberBasicByPhone(String phone);

    /**
     * 根据邮箱地址获取成员关键信息
     *
     * @param email
     * @return
     */
    CompletableFuture<MemberBasicInfo> getMemberBasicByEmail(String email);

}
