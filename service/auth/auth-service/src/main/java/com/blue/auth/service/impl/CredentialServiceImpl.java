package com.blue.auth.service.impl;

import com.blue.auth.repository.entity.Credential;
import com.blue.auth.repository.mapper.CredentialMapper;
import com.blue.auth.service.inter.CredentialService;
import com.blue.base.constant.base.BlueNumericalValue;
import com.blue.base.constant.verify.VerifyType;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.auth.common.AccessEncoder.encryptAccess;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.ConstantProcessor.assertLoginType;
import static com.blue.base.constant.auth.LoginType.EMAIL_PWD;
import static com.blue.base.constant.auth.LoginType.PHONE_PWD;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.verify.VerifyType.MAIL;
import static com.blue.base.constant.verify.VerifyType.SMS;
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


    private static final Map<VerifyType, List<String>> V_TYPE_AND_L_TYPE_ACCESS_MAPPING = new HashMap<>(VerifyType.values().length);

    static {
        V_TYPE_AND_L_TYPE_ACCESS_MAPPING.put(SMS, Stream.of(PHONE_PWD.identity, EMAIL_PWD.identity).collect(toList()));
        V_TYPE_AND_L_TYPE_ACCESS_MAPPING.put(MAIL, Stream.of(PHONE_PWD.identity, EMAIL_PWD.identity).collect(toList()));
    }

    private static final Function<VerifyType, List<String>> LOGIN_TYPES_GETTER = verifyType -> {
        if (verifyType == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "verifyType can't be null");

        List<String> loginTypes = V_TYPE_AND_L_TYPE_ACCESS_MAPPING.get(verifyType);
        if (isEmpty(loginTypes))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "verifyType unsupported");

        return loginTypes;
    };

    /**
     * get by credential and type
     *
     * @param credential
     * @param type
     * @return
     */
    @Override
    public Optional<Credential> getCredentialByCredentialAndType(String credential, String type) {
        LOGGER.info("Mono<Optional<Credential>> getCredentialByCredentialAndType(String credential, String type), credential = {}, type = {}", credential, type);
        if (isBlank(credential))
            throw new BlueException(EMPTY_PARAM);

        assertLoginType(type, false);

        return ofNullable(credentialMapper.getByCredentialAndType(credential, type));
    }

    /**
     * get mono by credential and type
     *
     * @param credential
     * @param type
     * @return
     */
    @Override
    public Mono<Optional<Credential>> getCredentialMonoByCredentialAndType(String credential, String type) {
        return just(getCredentialByCredentialAndType(credential, type));
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
     * get mono by member id and type
     *
     * @param memberId
     * @param type
     * @return
     */
    @Override
    public Mono<Optional<Credential>> getCredentialMonoByMemberIdAndType(Long memberId, String type) {
        return just(getCredentialByMemberIdAndType(memberId, type));
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

    /**
     * update access
     *
     * @param memberId
     * @param verifyType
     * @param access
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public Boolean updateAccess(Long memberId, VerifyType verifyType, String access) {
        LOGGER.info("Boolean updateAccess(Long memberId, VerifyType verifyType, String access), memberId = {}, verifyType = {}, access = {}", memberId, verifyType, ":)");

        if (isBlank(access))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access can't be blank");
        if (access.length() > BlueNumericalValue.ACS_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access length is too long");
        if (access.length() < BlueNumericalValue.ACS_LEN_MIN.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access length is too short");

        List<String> loginTypes = LOGIN_TYPES_GETTER.apply(verifyType);

        int updates = credentialMapper.updateAccess(memberId, loginTypes, encryptAccess(access));
        LOGGER.info("updates = {}", updates);

        return true;
    }

}
