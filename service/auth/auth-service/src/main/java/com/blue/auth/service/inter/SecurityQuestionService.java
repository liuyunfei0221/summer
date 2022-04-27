package com.blue.auth.service.inter;

import com.blue.auth.model.SecurityQuestionInfo;
import com.blue.auth.model.SecurityQuestionInsertParam;
import com.blue.auth.repository.entity.SecurityQuestion;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * security question service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface SecurityQuestionService {

    /**
     * insert security question
     *
     * @param securityQuestionInsertParam
     * @param memberId
     */
    void insertSecurityQuestion(SecurityQuestionInsertParam securityQuestionInsertParam, Long memberId);

    /**
     * insert security question batch
     *
     * @param securityQuestionInsertParams
     * @param memberId
     */
    void insertSecurityQuestions(List<SecurityQuestionInsertParam> securityQuestionInsertParams, Long memberId);

    /**
     * count security question by member id
     *
     * @param memberId
     * @return
     */
    Long countSecurityQuestionByMemberId(Long memberId);

    /**
     * count security question mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<Long> countSecurityQuestionMonoByMemberId(Long memberId);

    /**
     * select security question by member id
     *
     * @param memberId
     * @return
     */
    List<SecurityQuestion> selectSecurityQuestionByMemberId(Long memberId);

    /**
     * select security question mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<List<SecurityQuestion>> selectSecurityQuestionMonoByMemberId(Long memberId);

    /**
     * select security question info by member id
     *
     * @param memberId
     * @return
     */
    List<SecurityQuestionInfo> selectSecurityQuestionInfoByMemberId(Long memberId);

    /**
     * select security question info mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<List<SecurityQuestionInfo>> selectSecurityQuestionInfoMonoByMemberId(Long memberId);

}
