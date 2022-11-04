package com.blue.auth.service.impl;

import com.blue.auth.config.deploy.QuestionDeploy;
import com.blue.auth.model.SecurityQuestionInfo;
import com.blue.auth.model.SecurityQuestionInsertParam;
import com.blue.auth.repository.entity.SecurityQuestion;
import com.blue.auth.repository.mapper.SecurityQuestionMapper;
import com.blue.auth.service.inter.SecurityQuestionService;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import static com.blue.auth.converter.AuthModelConverters.SECURITY_QUESTION_2_SECURITY_QUESTION_INFO_CONVERTER;
import static com.blue.auth.converter.AuthModelConverters.SECURITY_QUESTION_INSERT_PARAM_2_SECURITY_QUESTION_CONVERTER;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.*;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * security question service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class SecurityQuestionServiceImpl implements SecurityQuestionService {

    private static final Logger LOGGER = getLogger(SecurityQuestionServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private SecurityQuestionMapper securityQuestionMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SecurityQuestionServiceImpl(BlueIdentityProcessor blueIdentityProcessor, SecurityQuestionMapper securityQuestionMapper,
                                       QuestionDeploy questionDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.securityQuestionMapper = securityQuestionMapper;

        this.maxQuestion = questionDeploy.getMax();
    }

    private long maxQuestion;

    private final BiConsumer<List<SecurityQuestion>, Long> QUESTIONS_VALIDATOR = (qs, mid) -> {
        if (isEmpty(qs))
            throw new BlueException(EMPTY_PARAM);
        int questionSize = qs.size();
        if (questionSize >= maxQuestion)
            throw new BlueException(DATA_ALREADY_EXIST);

        List<SecurityQuestion> existSecurityQuestions = securityQuestionMapper.selectByMemberId(mid);
        int existSize = existSecurityQuestions.size();

        if ((existSize + questionSize) > maxQuestion)
            throw new BlueException(DATA_ALREADY_EXIST);

        Set<String> questionsStrSet = existSecurityQuestions.stream().map(SecurityQuestion::getQuestion).collect(toSet());
        if (qs.stream().map(SecurityQuestion::getQuestion).anyMatch(questionsStrSet::contains))
            throw new BlueException(DATA_ALREADY_EXIST);

        qs.forEach(q -> q.setMemberId(mid));
    };

    /**
     * insert security question
     *
     * @param securityQuestionInsertParam
     * @param memberId
     */
    @Override
    public int insertSecurityQuestion(SecurityQuestionInsertParam securityQuestionInsertParam, Long memberId) {
        LOGGER.info("securityQuestionInsertParam = {}, memberId = {}", securityQuestionInsertParam, memberId);
        if (isNull(securityQuestionInsertParam))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        SecurityQuestion securityQuestion = SECURITY_QUESTION_INSERT_PARAM_2_SECURITY_QUESTION_CONVERTER.apply(securityQuestionInsertParam);

        QUESTIONS_VALIDATOR.accept(singletonList(securityQuestion), memberId);

        if (isInvalidIdentity(securityQuestion.getId()))
            securityQuestion.setId(blueIdentityProcessor.generate(SecurityQuestion.class));

        return securityQuestionMapper.insert(securityQuestion);
    }

    /**
     * insert security question batch
     *
     * @param securityQuestionInsertParams
     * @param memberId
     */
    @Override
    public int insertSecurityQuestions(List<SecurityQuestionInsertParam> securityQuestionInsertParams, Long memberId) {
        LOGGER.info("securityQuestionInsertParams = {}, memberId = {}", securityQuestionInsertParams, memberId);
        if (isEmpty(securityQuestionInsertParams))
            throw new BlueException(EMPTY_PARAM);
        if (securityQuestionInsertParams.size() > maxQuestion)
            throw new BlueException(PAYLOAD_TOO_LARGE);
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        List<SecurityQuestion> securityQuestions = securityQuestionInsertParams.stream()
                .map(SECURITY_QUESTION_INSERT_PARAM_2_SECURITY_QUESTION_CONVERTER).collect(toList());

        QUESTIONS_VALIDATOR.accept(securityQuestions, memberId);

        for (SecurityQuestion securityQuestion : securityQuestions)
            if (isInvalidIdentity(securityQuestion.getId()))
                securityQuestion.setId(blueIdentityProcessor.generate(SecurityQuestion.class));

        return securityQuestionMapper.insertBatch(securityQuestions);
    }

    /**
     * count security question mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<Long> countSecurityQuestionByMemberId(Long memberId) {
        return just(ofNullable(securityQuestionMapper.countByMemberId(memberId)).orElse(0L));
    }

    /**
     * select security question mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<SecurityQuestion>> selectSecurityQuestionByMemberId(Long memberId) {
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return just(securityQuestionMapper.selectByMemberId(memberId));
    }

    /**
     * select security question info mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<SecurityQuestionInfo>> selectSecurityQuestionInfoByMemberId(Long memberId) {
        return selectSecurityQuestionByMemberId(memberId)
                .flatMap(l -> just(l.stream().map(SECURITY_QUESTION_2_SECURITY_QUESTION_INFO_CONVERTER).collect(toList())));
    }

}
