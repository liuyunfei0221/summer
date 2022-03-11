package com.blue.auth.api.inter;

import com.blue.auth.api.model.AssertAuth;
import com.blue.auth.api.model.AuthAsserted;
import com.blue.base.model.base.Access;

import java.util.concurrent.CompletableFuture;

/**
 * rpc auth interface
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcAuthService {

    /**
     * authentication and authorization
     *
     * @param assertAuth
     * @return
     */
    CompletableFuture<AuthAsserted> assertAuth(AssertAuth assertAuth);

    /**
     * invalid auth by access
     *
     * @param access
     * @return
     */
    CompletableFuture<Boolean> invalidAuthByAccess(Access access);

    /**
     * invalid auth by jwt
     *
     * @param jwt
     * @return
     */
    CompletableFuture<Boolean> invalidAuthByJwt(String jwt);

    /**
     * invalid auth by member id
     *
     * @param memberId
     * @return
     */
    CompletableFuture<Boolean> invalidAuthByMemberId(Long memberId);

}
