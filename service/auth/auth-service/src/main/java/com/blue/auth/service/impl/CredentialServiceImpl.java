package com.blue.auth.service.impl;

import com.blue.auth.repository.entity.Credential;
import com.blue.auth.repository.mapper.CredentialMapper;
import com.blue.auth.service.inter.CredentialService;
import com.blue.base.constant.base.BlueNumericalValue;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.*;
import java.util.function.Consumer;

import static com.blue.auth.common.AccessEncoder.encryptAccess;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.ConstantProcessor.assertCredentialType;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Status.VALID;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
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

        for (Credential credential : existCredentials)
            if (memberIdAndTypes.get(credential.getMemberId()).contains(credential.getType()))
                throw new BlueException(DATA_ALREADY_EXIST);

        Map<String, Set<String>> credentialAndTypes = credentials.stream().collect(groupingBy(Credential::getCredential))
                .entrySet().stream().collect(toMap(Map.Entry::getKey, e -> e.getValue().stream().map(Credential::getType).collect(toSet())));

        existCredentials = credentialMapper.selectByCredentials(new ArrayList<>(credentialAndTypes.keySet()));

        for (Credential credential : existCredentials)
            if (credentialAndTypes.get(credential.getCredential()).contains(credential.getType()))
                throw new BlueException(DATA_ALREADY_EXIST);
    };

    /**
     * get by credential and type
     *
     * @param credential
     * @param credentialType
     * @return
     */
    @Override
    public Optional<Credential> getCredentialByCredentialAndType(String credential, String credentialType) {
        LOGGER.info("Mono<Optional<Credential>> getCredentialByCredentialAndType(String credential, String credentialType), credential = {}, credentialType = {}", credential, credentialType);
        if (isBlank(credential))
            throw new BlueException(EMPTY_PARAM);

        assertCredentialType(credentialType, false);

        return ofNullable(credentialMapper.getByCredentialAndType(credential, credentialType));
    }

    /**
     * get mono by credential and type
     *
     * @param credential
     * @param credentialType
     * @return
     */
    @Override
    public Mono<Optional<Credential>> getCredentialMonoByCredentialAndType(String credential, String credentialType) {
        return just(getCredentialByCredentialAndType(credential, credentialType));
    }

    /**
     * select by credential and types
     *
     * @param credential
     * @param credentialTypes
     * @return
     */
    @Override
    public List<Credential> selectCredentialByCredentialAndTypes(String credential, List<String> credentialTypes) {
        return isNotBlank(credential) && isNotEmpty(credentialTypes) ?
                credentialMapper.selectByCredentialAndTypes(credential, credentialTypes)
                :
                emptyList();
    }

    /**
     * select by credential and types
     *
     * @param credential
     * @param credentialTypes
     * @return
     */
    @Override
    public Mono<List<Credential>> selectCredentialMonoByCredentialAndTypes(String credential, List<String> credentialTypes) {
        return just(this.selectCredentialByCredentialAndTypes(credential, credentialTypes));
    }

    /**
     * get by member id and type
     *
     * @param memberId
     * @param credentialType
     * @return
     */
    @Override
    public Optional<Credential> getCredentialByMemberIdAndType(Long memberId, String credentialType) {
        LOGGER.info("Optional<Credential> getCredentialByMemberIdAndType(Long memberId, String credentialType), memberId = {}, credentialType = {}", memberId, credentialType);
        if (isInvalidIdentity(memberId))
            throw new BlueException(EMPTY_PARAM);

        assertCredentialType(credentialType, false);

        return ofNullable(credentialMapper.getByMemberIdAndType(memberId, credentialType));
    }

    /**
     * get mono by member id and type
     *
     * @param memberId
     * @param credentialType
     * @return
     */
    @Override
    public Mono<Optional<Credential>> getCredentialMonoByMemberIdAndType(Long memberId, String credentialType) {
        return just(getCredentialByMemberIdAndType(memberId, credentialType));
    }

    /**
     * select by member id and types
     *
     * @param memberId
     * @param credentialTypes
     * @return
     */
    @Override
    public List<Credential> selectCredentialByMemberIdAndTypes(Long memberId, List<String> credentialTypes) {
        return isValidIdentity(memberId) && isNotEmpty(credentialTypes) ?
                credentialMapper.selectByMemberIdAndTypes(memberId, credentialTypes)
                :
                emptyList();
    }

    /**
     * select mono by member id and types
     *
     * @param memberId
     * @param credentialTypes
     * @return
     */
    @Override
    public Mono<List<Credential>> selectCredentialMonoByMemberIdAndTypes(Long memberId, List<String> credentialTypes) {
        return just(this.selectCredentialByMemberIdAndTypes(memberId, credentialTypes));
    }

    /**
     * insert credential batch
     *
     * @param credentials
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
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
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public void insertCredential(Credential credential) {
        LOGGER.info("void insertCredential(Credential credential, Long operatorId), credential = {}", credential);

        Long memberId;
        if (isNull(credential) || isInvalidIdentity(memberId = credential.getMemberId()))
            throw new BlueException(EMPTY_PARAM);

        String type = credential.getType();
        assertCredentialType(type, false);

        Optional<Credential> existOptional = this.getCredentialByMemberIdAndType(memberId, type);
        if (existOptional.isPresent())
            throw new BlueException(DATA_ALREADY_EXIST);

        credential.setId(blueIdentityProcessor.generate(Credential.class));

        credentialMapper.insert(credential);
    }

    /**
     * insert credential
     *
     * @param credential
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public void updateCredentialByIds(String credential, List<Long> ids) {
        LOGGER.info("void updateCredentialByIds(String credential, List<Long> ids), credential = {}, ids = {}", credential, ids);

        if (isBlank(credential) || isEmpty(ids))
            throw new BlueException(EMPTY_PARAM);

        if (isNotEmpty(credentialMapper.selectByCredentials(singletonList(credential))))
            throw new BlueException(DATA_ALREADY_EXIST);

        credentialMapper.updateCredentialByIds(credential, ids);
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
        if (isNull(credential))
            throw new BlueException(EMPTY_PARAM);

        if (isInvalidIdentity(credential.getId()))
            throw new BlueException(INVALID_IDENTITY);

        credentialMapper.updateByPrimaryKeySelective(credential);
    }

    /**
     * delete credential
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

    /**
     * update access
     *
     * @param memberId
     * @param credentialTypes
     * @param access
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public Boolean updateAccess(Long memberId, List<String> credentialTypes, String access) {
        LOGGER.info("Boolean updateAccess(Long memberId, VerifyType verifyType, String access), memberId = {}, credentialTypes = {}, access = {}", memberId, credentialTypes, ":)");

        if (isBlank(access))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access can't be blank");
        if (access.length() > BlueNumericalValue.ACS_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access length is too long");
        if (access.length() < BlueNumericalValue.ACS_LEN_MIN.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access length is too short");
        if (isEmpty(credentialTypes))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "credentialTypes is empty");

        int updates = credentialMapper.updateAccessByMemberAndTypes(memberId, credentialTypes, encryptAccess(access), VALID.status);

        LOGGER.info("updates = {}", updates);

        return true;
    }

}
