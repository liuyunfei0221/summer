package com.blue.secure.api.inter;

import com.blue.base.model.base.Access;
import com.blue.secure.api.model.AssertAuth;
import com.blue.secure.api.model.AuthAsserted;
import com.blue.secure.api.model.Authority;

import java.util.concurrent.CompletableFuture;

/**
 * rpc secure interface
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcSecureService {

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
     * query authority by access
     *
     * @param access
     * @return
     */
    CompletableFuture<Authority> getAuthorityByAccess(Access access);

    /**
     * query authority by member id
     *
     * @param memberId
     * @return
     */
    CompletableFuture<Authority> getAuthorityByMemberId(Long memberId);

}
