package com.blue.lake.service.inter;

import com.blue.basic.model.common.Access;
import com.blue.basic.model.common.Session;
import reactor.core.publisher.Mono;

/**
 * auth service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface AuthService {

    /**
     * jwt -> access
     *
     * @param authentication
     * @return
     */
    Mono<Access> parseAccess(String authentication);

    /**
     * jwt -> session
     *
     * @param authentication
     * @return
     */
    Mono<Session> parseSession(String authentication);

}
