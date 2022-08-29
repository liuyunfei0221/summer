package com.blue.auth.service.impl;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.repository.entity.Credential;
import com.blue.auth.repository.mapper.CredentialMapper;
import com.blue.auth.service.inter.CredentialService;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.redisson.component.SynchronizedProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static com.blue.auth.common.AccessEncoder.encryptAccess;
import static com.blue.auth.converter.AuthModelConverters.CREDENTIAL_2_CREDENTIAL_INFO_CONVERTER;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.assertCredentialType;
import static com.blue.basic.constant.common.BlueCommonThreshold.ACS_LEN_MAX;
import static com.blue.basic.constant.common.BlueCommonThreshold.ACS_LEN_MIN;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Status.VALID;
import static com.blue.basic.constant.common.SyncKeyPrefix.CREDENTIAL_UPDATE_PRE;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.*;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.justOrEmpty;
import static reactor.util.Loggers.getLogger;

/**
 * credential service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class CredentialServiceImpl implements CredentialService {

    private static final Logger LOGGER = getLogger(CredentialServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final SynchronizedProcessor synchronizedProcessor;

    private CredentialMapper credentialMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")

    public CredentialServiceImpl(BlueIdentityProcessor blueIdentityProcessor, SynchronizedProcessor synchronizedProcessor, CredentialMapper credentialMapper) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.synchronizedProcessor = synchronizedProcessor;
        this.credentialMapper = credentialMapper;
    }

    private static final UnaryOperator<String> CREDENTIAL_UPDATE_SYNC_KEY_GEN = credential -> {
        if (isNotBlank(credential))
            return CREDENTIAL_UPDATE_PRE.prefix + credential;

        throw new BlueException(BAD_REQUEST);
    };

    private final Consumer<List<Credential>> CREDENTIALS_ASSERTER = cs -> {
        if (isEmpty(cs))
            throw new BlueException(EMPTY_PARAM);

        if (isNotEmpty(credentialMapper.selectByCredentials(
                cs.stream().map(Credential::getCredential).collect(toList()))))
            throw new BlueException(DATA_ALREADY_EXIST);
    };

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

        String type = credential.getType();
        assertCredentialType(type, false);

        credential.setId(blueIdentityProcessor.generate(Credential.class));

        synchronizedProcessor.handleTaskWithLock(CREDENTIAL_UPDATE_SYNC_KEY_GEN.apply(credential.getCredential()),
                () -> {
                    CREDENTIALS_ASSERTER.accept(singletonList(credential));
                    credentialMapper.insert(credential);
                });

        LOGGER.info("insert credential = {}", credential);
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

        credentials.parallelStream()
                .forEach(c -> c.setId(blueIdentityProcessor.generate(Credential.class)));

        Map<String, List<Credential>> credentialAndCredentialsMapping = credentials.stream()
                .collect(groupingBy(Credential::getCredential));

        String c;
        for (Map.Entry<String, List<Credential>> entry : credentialAndCredentialsMapping.entrySet()) {
            c = entry.getKey();
            List<Credential> cs = entry.getValue();

            CREDENTIALS_ASSERTER.accept(cs);

            synchronizedProcessor.handleTaskWithLock(CREDENTIAL_UPDATE_SYNC_KEY_GEN.apply(c),
                    () -> credentialMapper.insertBatch(cs));
        }

        LOGGER.info("insert batch credentials = {}", credentials);
    }

    /**
     * insert credential
     *
     * @param credential
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public void updateCredentialByIds(String credential, List<Long> ids) {
        LOGGER.info("void updateCredentialByIds(String credential, List<Long> ids), credential = {}, ids = {}", credential, ids);
        if (isBlank(credential) || isEmpty(ids))
            throw new BlueException(EMPTY_PARAM);
        if (isNotEmpty(credentialMapper.selectByCredentials(singletonList(credential))))
            throw new BlueException(DATA_ALREADY_EXIST);

        synchronizedProcessor.handleTaskWithLock(CREDENTIAL_UPDATE_SYNC_KEY_GEN.apply(credential),
                () -> {
                    if (isNotEmpty(credentialMapper.selectByCredentials(
                            singletonList(credential))))
                        throw new BlueException(DATA_ALREADY_EXIST);
                    credentialMapper.updateCredentialByIds(credential, TIME_STAMP_GETTER.get(), ids);
                });

        LOGGER.info("update batch credential = {}", credential);
    }

    /**
     * delete credential
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public void deleteCredential(Long id) {
        LOGGER.info("void deleteCredentialById(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        Credential credential = credentialMapper.selectByPrimaryKey(id);
        if (isNull(credential))
            return;

        synchronizedProcessor.handleTaskWithLock(CREDENTIAL_UPDATE_SYNC_KEY_GEN.apply(credential.getCredential()),
                () -> credentialMapper.deleteByPrimaryKey(id));
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
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public boolean updateAccess(Long memberId, List<String> credentialTypes, String access) {
        LOGGER.info("Boolean updateAccess(Long memberId, VerifyType verifyType, String access), memberId = {}, credentialTypes = {}, access = {}", memberId, credentialTypes, ":)");

        if (isBlank(access))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access can't be blank");
        if (access.length() > ACS_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access length is too long");
        if (access.length() < ACS_LEN_MIN.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access length is too short");
        if (isEmpty(credentialTypes))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "credentialTypes is empty");

        int updates = credentialMapper.updateAccessByMemberAndTypes(encryptAccess(access), VALID.status, TIME_STAMP_GETTER.get(), memberId, credentialTypes);

        LOGGER.info("updates = {}", updates);

        return updates > 0;
    }

    /**
     * get by credential and type
     *
     * @param credential
     * @param credentialType
     * @return
     */
    @Override
    public Optional<Credential> getCredentialByCredentialAndType(String credential, String credentialType) {
        LOGGER.info("Optional<Credential> getCredentialByCredentialAndType(String credential, String credentialType), credential = {}, credentialType = {}", credential, credentialType);
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
    public Mono<Credential> getCredentialMonoByCredentialAndType(String credential, String credentialType) {
        return justOrEmpty(credentialMapper.getByCredentialAndType(credential, credentialType));
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
     * select info mono by credential and types
     *
     * @param credential
     * @param credentialTypes
     * @return
     */
    @Override
    public Mono<List<CredentialInfo>> selectCredentialInfoMonoByCredentialAndTypes(String credential, List<String> credentialTypes) {
        return this.selectCredentialMonoByCredentialAndTypes(credential, credentialTypes)
                .map(cis -> cis.stream().map(CREDENTIAL_2_CREDENTIAL_INFO_CONVERTER).collect(toList()));
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
    public Mono<Credential> getCredentialMonoByMemberIdAndType(Long memberId, String credentialType) {
        return justOrEmpty(credentialMapper.getByMemberIdAndType(memberId, credentialType));
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
     * select info mono by member id and types
     *
     * @param memberId
     * @param credentialTypes
     * @return
     */
    @Override
    public Mono<List<CredentialInfo>> selectCredentialInfoMonoByMemberIdAndTypes(Long memberId, List<String> credentialTypes) {
        return this.selectCredentialMonoByMemberIdAndTypes(memberId, credentialTypes)
                .map(cs -> cs.stream().map(CREDENTIAL_2_CREDENTIAL_INFO_CONVERTER).collect(toList()));
    }

}
