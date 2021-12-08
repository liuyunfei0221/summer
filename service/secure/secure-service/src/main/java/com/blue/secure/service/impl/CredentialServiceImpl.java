package com.blue.secure.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.secure.repository.entity.Credential;
import com.blue.secure.repository.mapper.CredentialMapper;
import com.blue.secure.service.inter.CredentialService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.Optional;

import static com.blue.base.common.base.Asserter.isBlank;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.EMPTY_PARAM;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * credential service impl
 *
 * @author liuyunfei
 * @date 2021/12/8
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class CredentialServiceImpl implements CredentialService {

    private static final Logger LOGGER = getLogger(CredentialServiceImpl.class);

    private final CredentialMapper credentialMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public CredentialServiceImpl(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    /**
     * get by identity and type
     *
     * @param identity
     * @param loginType
     * @return
     */
    @Override
    public Mono<Optional<Credential>> getCredentialMonoByIdentityAndLoginType(String identity, String loginType) {
        LOGGER.info("Mono<Optional<Credential>> getCredentialMonoByIdentityAndLoginType(String identity, String loginType), identity = {}, loginType = {}",
                identity, loginType);
        if (isBlank(identity) || isBlank(loginType))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message);

        return just(ofNullable(credentialMapper.selectCredentialByIdentityAndLoginType(identity, loginType)));
    }

}
