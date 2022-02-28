package com.blue.secure.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.secure.repository.entity.Credential;
import com.blue.secure.repository.mapper.CredentialMapper;
import com.blue.secure.service.inter.CredentialService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.Logger;

import java.util.Optional;

import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.common.base.ConstantProcessor.assertLoginType;
import static com.blue.base.constant.base.ResponseElement.DATA_ALREADY_EXIST;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static java.util.Optional.ofNullable;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.util.Loggers.getLogger;

/**
 * credential service
 *
 * @author liuyunfei
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class CredentialServiceImpl implements CredentialService {

    private static final Logger LOGGER = getLogger(CredentialServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final CredentialMapper credentialMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public CredentialServiceImpl(BlueIdentityProcessor blueIdentityProcessor, CredentialMapper credentialMapper) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.credentialMapper = credentialMapper;
    }

    /**
     * get by member id and type
     *
     * @param memberId
     * @param type
     * @return
     */
    @Override
    public Optional<Credential> getCredentialByMemberIdAndType(Long memberId, String type) {
        LOGGER.info("Optional<Credential> getCredentialByMemberIdAndType(Long memberId, String type), memberId = {}, type = {}", memberId, type);

        if (isInvalidIdentity(memberId))
            throw new BlueException(EMPTY_PARAM);

        assertLoginType(type, false);

        return ofNullable(credentialMapper.getByMemberIdAndType(memberId, type));
    }

    /**
     * insert a new role
     *
     * @param credential
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public void insertCredential(Credential credential) {
        LOGGER.info("void insertCredential(Credential credential, Long operatorId), credential = {}", credential);

        Long memberId;
        if (isNull(credential) || isInvalidIdentity(memberId = credential.getMemberId()))
            throw new BlueException(EMPTY_PARAM);

        String type = credential.getType();
        assertLoginType(type, false);

        Optional<Credential> existOptional = this.getCredentialByMemberIdAndType(memberId, type);
        if (existOptional.isPresent())
            throw new BlueException(DATA_ALREADY_EXIST);

        credential.setId(blueIdentityProcessor.generate(Credential.class));

        credentialMapper.insert(credential);
    }

    /**
     * update a exist role
     *
     * @param credential
     * @return
     */
    @Override
    public void updateCredential(Credential credential) {

    }

    /**
     * delete role
     *
     * @param id
     * @return
     */
    @Override
    public void deleteCredentialById(Long id) {

    }

}
