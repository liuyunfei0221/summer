package com.blue.auth.service.inter;

import com.blue.auth.model.CredentialHistoryInfo;
import com.blue.auth.repository.entity.Credential;
import com.blue.auth.repository.entity.CredentialHistory;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * credential history service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface CredentialHistoryService {

    /**
     * insert credential history
     *
     * @param credentialHistory
     * @return
     */
    Mono<CredentialHistory> insertCredentialHistory(CredentialHistory credentialHistory);

    /**
     * insert credential histories
     *
     * @param credentialHistories
     * @return
     */
    Mono<List<CredentialHistory>> insertCredentialHistories(List<CredentialHistory> credentialHistories);

    /**
     * insert credential history by credential and member id
     *
     * @param credential
     * @param memberId
     */
    void insertCredentialHistoryByCredentialAndMemberIdAsync(String credential, Long memberId);

    /**
     * insert credential histories by credentials and member id
     *
     * @param credentials
     * @param memberId
     */
    void insertCredentialHistoriesByCredentialsAndMemberIdAsync(List<Credential> credentials, Long memberId);

    /**
     * select credential histories mono by member id and limit
     *
     * @param memberId
     * @param limit
     * @return
     */
    Mono<List<CredentialHistory>> selectCredentialHistoryMonoByMemberIdAndLimit(Long memberId, Integer limit);

    /**
     * select credential history info mono by member id with limit
     *
     * @param memberId
     * @return
     */
    Mono<List<CredentialHistoryInfo>> selectCredentialHistoryInfoMonoByMemberIdWithLimit(Long memberId);

}
