package com.blue.auth.api.inter;

import com.blue.auth.api.model.AccessAssert;
import com.blue.auth.api.model.AccessAsserted;
import com.blue.base.model.base.Access;

import java.util.concurrent.CompletableFuture;

/**
 * rpc auth interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcAuthService {

    /**
     * authentication and authorization
     *
     * @param accessAssert
     * @return
     */
    CompletableFuture<AccessAsserted> assertAccess(AccessAssert accessAssert);

    /**
     * invalid auth by access
     *
     * @param access
     * @return
     */
    CompletableFuture<Boolean> invalidateAuthByAccess(Access access);

    /**
     * invalid auth by jwt
     *
     * @param jwt
     * @return
     */
    CompletableFuture<Boolean> invalidateAuthByJwt(String jwt);

    /**
     * invalid auth by member id
     *
     * @param memberId
     * @return
     */
    CompletableFuture<Boolean> invalidateAuthByMemberId(Long memberId);

}
