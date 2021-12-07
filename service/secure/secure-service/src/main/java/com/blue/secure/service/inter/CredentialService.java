package com.blue.secure.service.inter;

import com.blue.secure.repository.entity.Credential;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * credential service
 *
 * @author liuyunfei
 * @date 2021/12/8
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public interface CredentialService {

    /**
     * get by identity and type
     *
     * @param identity
     * @param loginType
     * @return
     */
    Mono<Optional<Credential>> getCredentialMonoByIdentityAndLoginType(String identity, String loginType);

}
