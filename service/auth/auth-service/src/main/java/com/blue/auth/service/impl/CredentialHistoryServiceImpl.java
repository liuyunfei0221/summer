package com.blue.auth.service.impl;

import com.blue.auth.config.deploy.CredentialHistoryDeploy;
import com.blue.auth.model.CredentialHistoryInfo;
import com.blue.auth.repository.entity.Credential;
import com.blue.auth.repository.entity.CredentialHistory;
import com.blue.auth.repository.template.CredentialHistoryRepository;
import com.blue.auth.service.inter.CredentialHistoryService;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.blue.auth.constant.CredentialHistorySortAttribute.CREATE_TIME;
import static com.blue.auth.converter.AuthModelConverters.CREDENTIAL_HISTORY_2_CREDENTIAL_HISTORY_INFO_CONVERTER;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.component.encoder.api.common.StringColumnEncoder.decryptString;
import static com.blue.basic.component.encoder.api.common.StringColumnEncoder.encryptString;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * credential history service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class CredentialHistoryServiceImpl implements CredentialHistoryService {

    private static final Logger LOGGER = getLogger(CredentialHistoryServiceImpl.class);

    private final CredentialHistoryRepository credentialHistoryRepository;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final ExecutorService executorService;

    public CredentialHistoryServiceImpl(CredentialHistoryRepository credentialHistoryRepository, BlueIdentityProcessor blueIdentityProcessor,
                                        ExecutorService executorService, CredentialHistoryDeploy credentialHistoryDeploy) {
        this.credentialHistoryRepository = credentialHistoryRepository;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.executorService = executorService;

        this.HISTORY_SELECT_LIMIT = credentialHistoryDeploy.getLimit();
    }

    private final int HISTORY_SELECT_LIMIT;

    /**
     * insert credential history
     *
     * @param credentialHistory
     * @return
     */
    @Override
    public Mono<CredentialHistory> insertCredentialHistory(CredentialHistory credentialHistory) {
        LOGGER.info("Mono<CredentialHistory> insertCredentialHistory(CredentialHistory credentialHistory), credentialHistory = {}", credentialHistory);
        if (isNull(credentialHistory))
            throw new BlueException(EMPTY_PARAM);

        if (isInvalidIdentity(credentialHistory.getId()))
            credentialHistory.setId(blueIdentityProcessor.generate(CredentialHistory.class));

        credentialHistory.setCredential(encryptString(credentialHistory.getCredential()));
        return credentialHistoryRepository.insert(credentialHistory);
    }

    /**
     * insert credential histories
     *
     * @param credentialHistories
     * @return
     */
    @Override
    public Mono<List<CredentialHistory>> insertCredentialHistories(List<CredentialHistory> credentialHistories) {
        LOGGER.info("Mono<List<CredentialHistory>> insertCredentialHistories(List<CredentialHistory> credentialHistories), credentialHistories = {}", credentialHistories);
        if (isEmpty(credentialHistories))
            throw new BlueException(EMPTY_PARAM);

        return credentialHistoryRepository.insert(
                credentialHistories.stream().peek(credentialHistory -> {
                    credentialHistory.setCredential(encryptString(credentialHistory.getCredential()));
                    if (isInvalidIdentity(credentialHistory.getId()))
                        credentialHistory.setId(blueIdentityProcessor.generate(CredentialHistory.class));
                }).collect(toList())).collectList();
    }

    /**
     * insert credential history by credential and member id
     *
     * @param credential
     * @param memberId
     */
    @Override
    public void insertCredentialHistoryByCredentialAndMemberIdAsync(String credential, Long memberId) {
        LOGGER.info("void insertCredentialHistoryByCredentialAndMemberIdAsync(String credential, Long memberId), credential = {}, memberId = {}", credential, memberId);
        if (isBlank(credential) || isInvalidIdentity(memberId))
            throw new BlueException(EMPTY_PARAM);

        executorService.submit(() -> {
            try {
                CredentialHistory credentialHistory = this.insertCredentialHistory(
                                new CredentialHistory(blueIdentityProcessor.generate(CredentialHistory.class), memberId, credential, TIME_STAMP_GETTER.get()))
                        .toFuture().join();
                LOGGER.info("executorService.submit(), insertCredentialHistory success, credentialHistory = {}", credentialHistory);
            } catch (Exception e) {
                LOGGER.error("executorService.submit(), insertCredentialHistory failed, credential = {}, memberId = {}", credential, memberId);
            }
        });
    }

    /**
     * insert credential histories by credentials and member id
     *
     * @param credentials
     * @param memberId
     */
    @Override
    public void insertCredentialHistoriesByCredentialsAndMemberIdAsync(List<Credential> credentials, Long memberId) {
        LOGGER.info("void insertCredentialHistoriesByCredentialsAndMemberIdAsync(List<String> credentials, Long memberId), credentials = {}, memberId = {}", credentials, memberId);
        if (isEmpty(credentials) || isInvalidIdentity(memberId))
            throw new BlueException(EMPTY_PARAM);

        executorService.submit(() -> {
            try {
                List<CredentialHistory> credentialHistories = this.insertCredentialHistories(credentials.stream().map(Credential::getCredential).distinct().filter(BlueChecker::isNotBlank)
                                .map(cre -> new CredentialHistory(blueIdentityProcessor.generate(CredentialHistory.class), memberId, cre, TIME_STAMP_GETTER.get())).collect(toList()))
                        .toFuture().join();
                LOGGER.info("executorService.submit(), insertCredentialHistories success, credentialHistories = {}", credentialHistories);
            } catch (Exception e) {
                LOGGER.error("executorService.submit(), insertCredentialHistories failed, credentials = {}, memberId = {}", credentials, memberId);
            }
        });
    }

    /**
     * select credential histories mono by member id and limit
     *
     * @param memberId
     * @param limit
     * @return
     */
    @Override
    public Mono<List<CredentialHistory>> selectCredentialHistoryMonoByMemberIdAndLimit(Long memberId, Integer limit) {
        LOGGER.info("Mono<List<CredentialHistory>> selectCredentialHistoryMonoByMemberId(Long memberId), memberId = {}, limit = {}", memberId, limit);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        int tarLimit = isNotNull(limit) && limit > 0 ? limit : HISTORY_SELECT_LIMIT;

        CredentialHistory probe = new CredentialHistory();
        probe.setMemberId(memberId);

        return credentialHistoryRepository.findAll(Example.of(probe), Sort.by(Sort.Order.desc(CREATE_TIME.attribute))).take(tarLimit)
                .flatMap(credentialHistory -> {
                    credentialHistory.setCredential(decryptString(credentialHistory.getCredential()));
                    return just(credentialHistory);
                })
                .collectList();
    }

    /**
     * select credential history info mono by member id with limit
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<CredentialHistoryInfo>> selectCredentialHistoryInfoMonoByMemberIdWithLimit(Long memberId) {
        LOGGER.info("Mono<List<CredentialHistoryInfo>> selectCredentialHistoryInfoMonoByMemberIdWithLimit(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        CredentialHistory probe = new CredentialHistory();
        probe.setMemberId(memberId);

        return credentialHistoryRepository.findAll(Example.of(probe), Sort.by(Sort.Order.desc(CREATE_TIME.attribute)))
                .take(HISTORY_SELECT_LIMIT)
                .flatMap(credentialHistory -> {
                    credentialHistory.setCredential(decryptString(credentialHistory.getCredential()));
                    return just(CREDENTIAL_HISTORY_2_CREDENTIAL_HISTORY_INFO_CONVERTER.apply(credentialHistory));
                }).collectList();
    }

}
