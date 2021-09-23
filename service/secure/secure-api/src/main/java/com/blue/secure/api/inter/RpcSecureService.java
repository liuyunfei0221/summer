package com.blue.secure.api.inter;

import com.blue.base.model.base.Access;
import com.blue.secure.api.model.AssertAuth;
import com.blue.secure.api.model.AuthAsserted;
import com.blue.secure.api.model.Authority;

import java.util.concurrent.CompletableFuture;

/**
 * 认证鉴权RPC接口
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcSecureService {

    /**
     * 认证鉴权
     *
     * @param assertAuth
     * @return
     */
    CompletableFuture<AuthAsserted> assertAuth(AssertAuth assertAuth);

    /**
     * 根据accessInfo清除认证信息
     *
     * @param access
     * @return
     */
    CompletableFuture<Boolean> invalidAuthByAccess(Access access);

    /**
     * 根据jwt清除认证信息
     *
     * @param jwt
     * @return
     */
    CompletableFuture<Boolean> invalidAuthByJwt(String jwt);

    /**
     * 根据access查询成员的角色及权限信息
     *
     * @param access
     * @return
     */
    CompletableFuture<Authority> getAuthorityByAccess(Access access);

    /**
     * 根据memberId查询成员的角色及权限信息
     *
     * @param memberId
     * @return
     */
    CompletableFuture<Authority> getAuthorityByMemberId(Long memberId);

}
