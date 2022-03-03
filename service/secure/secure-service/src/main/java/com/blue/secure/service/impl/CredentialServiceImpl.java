package com.blue.secure.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.secure.repository.entity.Credential;
import com.blue.secure.repository.mapper.CredentialMapper;
import com.blue.secure.service.inter.CredentialService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.*;
import java.util.function.Consumer;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.ConstantProcessor.assertLoginType;
import static com.blue.base.constant.base.ResponseElement.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.*;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.just;
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

    private CredentialMapper credentialMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public CredentialServiceImpl(BlueIdentityProcessor blueIdentityProcessor, CredentialMapper credentialMapper) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.credentialMapper = credentialMapper;
    }

    private final Consumer<List<Credential>> CREDENTIALS_ASSERTER = credentials -> {
        if (isEmpty(credentials))
            throw new BlueException(EMPTY_PARAM);

        Map<Long, Set<String>> memberIdAndTypes = credentials.stream().collect(groupingBy(Credential::getMemberId))
                .entrySet().stream().collect(toMap(Map.Entry::getKey, e -> e.getValue().stream().map(Credential::getType).collect(toSet())));

        List<Credential> existCredentials = credentialMapper.selectByMemberIds(new ArrayList<>(memberIdAndTypes.keySet()));

        for (Credential credential : existCredentials) {
            if (memberIdAndTypes.get(credential.getMemberId()).contains(credential.getType()))
                throw new BlueException(DATA_ALREADY_EXIST);
        }

        Map<String, Set<String>> credentialAndTypes = credentials.stream().collect(groupingBy(Credential::getCredential))
                .entrySet().stream().collect(toMap(Map.Entry::getKey, e -> e.getValue().stream().map(Credential::getType).collect(toSet())));

        existCredentials = credentialMapper.selectByCredentials(new ArrayList<>(credentialAndTypes.keySet()));

        for (Credential credential : existCredentials) {
            if (credentialAndTypes.get(credential.getCredential()).contains(credential.getType()))
                throw new BlueException(DATA_ALREADY_EXIST);
        }
    };

    /**
     * get by credential and type
     *
     * @param credential
     * @param type
     * @return
     */
    @Override
    public Mono<Optional<Credential>> getCredentialByCredentialAndType(String credential, String type) {
        LOGGER.info("Mono<Optional<Credential>> getCredentialByCredentialAndType(String credential, String type), credential = {}, type = {}", credential, type);

        if (isBlank(credential))
            throw new BlueException(EMPTY_PARAM);

        assertLoginType(type, false);

        return just(ofNullable(credentialMapper.getByCredentialAndType(credential, type)));
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
     * insert credential batch
     *
     * @param credentials
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public void insertCredentials(List<Credential> credentials) {
        LOGGER.info("void insertCredentials(List<Credential> credentials), credential = {}", credentials);

        if (isEmpty(credentials))
            return;

        CREDENTIALS_ASSERTER.accept(credentials);

        credentials.parallelStream()
                .forEach(c -> c.setId(blueIdentityProcessor.generate(Credential.class)));

        credentialMapper.insertBatch(credentials);
    }

    /**
     * insert a new role
     *
     * @param credential
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
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
        LOGGER.info("void updateCredential(Credential credential), credential = {}", credential);

        if (credential == null)
            throw new BlueException(EMPTY_PARAM);

        if (isInvalidIdentity(credential.getId()))
            throw new BlueException(INVALID_IDENTITY);

        credentialMapper.updateByPrimaryKeySelective(credential);
    }

    /**
     * delete role
     *
     * @param id
     * @return
     */
    @Override
    public void deleteCredentialById(Long id) {
        LOGGER.info("void deleteCredentialById(Long id), id = {}", id);

        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        credentialMapper.deleteByPrimaryKey(id);
    }

}
