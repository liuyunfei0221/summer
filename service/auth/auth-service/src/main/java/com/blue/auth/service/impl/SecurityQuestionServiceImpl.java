package com.blue.auth.service.impl;

import com.blue.auth.config.deploy.QuestionDeploy;
import com.blue.auth.model.SecurityQuestionInfo;
import com.blue.auth.model.SecurityQuestionInsertParam;
import com.blue.auth.repository.entity.SecurityQuestion;
import com.blue.auth.repository.mapper.SecurityQuestionMapper;
import com.blue.auth.service.inter.SecurityQuestionService;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.blue.auth.converter.AuthModelConverters.SECURITY_QUESTION_2_SECURITY_QUESTION_INFO_CONVERTER;
import static com.blue.auth.converter.AuthModelConverters.SECURITY_QUESTION_INSERT_PARAM_2_SECURITY_QUESTION_CONVERTER;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.SyncKeyPrefix.QUESTION_INSERT_PRE;
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

    private final RedissonClient redissonClient;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SecurityQuestionServiceImpl(BlueIdentityProcessor blueIdentityProcessor, SecurityQuestionMapper securityQuestionMapper, RedissonClient redissonClient, QuestionDeploy questionDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.securityQuestionMapper = securityQuestionMapper;
        this.redissonClient = redissonClient;

        this.maxQuestion = questionDeploy.getMax();
    }

    private long maxQuestion;

    /**
     * security question validator
     */
    private final BiConsumer<List<SecurityQuestion>, Long> QUESTIONS_VALIDATOR = (questions, memberId) -> {
        if (isEmpty(questions))
            throw new BlueException(EMPTY_PARAM);
        int questionSize = questions.size();
        if (questionSize >= maxQuestion)
            throw new BlueException(DATA_ALREADY_EXIST);

        List<SecurityQuestion> existSecurityQuestions = securityQuestionMapper.selectByMemberId(memberId);
        int existSize = existSecurityQuestions.size();

        if ((existSize + questionSize) > maxQuestion)
            throw new BlueException(DATA_ALREADY_EXIST);

        Set<String> questionsStrSet = existSecurityQuestions.stream().map(SecurityQuestion::getQuestion).collect(toSet());
        if (questions.stream().map(SecurityQuestion::getQuestion).anyMatch(questionsStrSet::contains))
            throw new BlueException(DATA_ALREADY_EXIST);

        questions.forEach(q -> q.setMemberId(memberId));
    };

    /**
     * question insert sync key
     */
    private static final Function<Long, String> QUESTION_INSERT_SYNC_KEY_GEN = memberId -> QUESTION_INSERT_PRE.prefix + memberId;

    /**
     * insert security question
     *
     * @param securityQuestionInsertParam
     * @param memberId
     */
    @Override
    public void insertSecurityQuestion(SecurityQuestionInsertParam securityQuestionInsertParam, Long memberId) {
        LOGGER.info("void insertSecurityQuestion(SecurityQuestionInsertParam securityQuestionInsertParam, Long memberId), securityQuestionInsertParam = {}, memberId = {}", securityQuestionInsertParam, memberId);
        if (isNull(securityQuestionInsertParam))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        SecurityQuestion securityQuestion = SECURITY_QUESTION_INSERT_PARAM_2_SECURITY_QUESTION_CONVERTER.apply(securityQuestionInsertParam);

        RLock lock = redissonClient.getLock(QUESTION_INSERT_SYNC_KEY_GEN.apply(memberId));
        boolean tryLock = true;
        try {
            tryLock = lock.tryLock();
            if (tryLock) {
                QUESTIONS_VALIDATOR.accept(singletonList(securityQuestion), memberId);

                if (isInvalidIdentity(securityQuestion.getId()))
                    securityQuestion.setId(blueIdentityProcessor.generate(SecurityQuestion.class));

                securityQuestionMapper.insert(securityQuestion);
            } else {
                throw new BlueException(DATA_ALREADY_EXIST);
            }
        } catch (Exception e) {
            LOGGER.error("insertSecurityQuestion(SecurityQuestionInsertParam securityQuestionInsertParam, Long memberId) failed, e = {}", e);
            throw e;
        } finally {
            if (tryLock)
                try {
                    lock.unlock();
                } catch (Exception e) {
                    LOGGER.warn("securityQuestionMapper.insert, lock.unlock() failed, e = {}", e);
                }
        }
    }

    /**
     * insert security question batch
     *
     * @param securityQuestionInsertParams
     * @param memberId
     */
    @Override
    public void insertSecurityQuestions(List<SecurityQuestionInsertParam> securityQuestionInsertParams, Long memberId) {
        LOGGER.info("void insertSecurityQuestions(List<SecurityQuestionInsertParam> securityQuestionInsertParams, Long memberId), securityQuestionInsertParams = {}, memberId = {}", securityQuestionInsertParams, memberId);
        if (isEmpty(securityQuestionInsertParams))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        List<SecurityQuestion> securityQuestions = securityQuestionInsertParams.stream().map(SECURITY_QUESTION_INSERT_PARAM_2_SECURITY_QUESTION_CONVERTER).collect(toList());

        RLock lock = redissonClient.getLock(QUESTION_INSERT_SYNC_KEY_GEN.apply(memberId));
        boolean tryLock = true;
        try {
            tryLock = lock.tryLock();
            if (tryLock) {
                QUESTIONS_VALIDATOR.accept(securityQuestions, memberId);

                for (SecurityQuestion securityQuestion : securityQuestions)
                    if (isInvalidIdentity(securityQuestion.getId()))
                        securityQuestion.setId(blueIdentityProcessor.generate(SecurityQuestion.class));

                securityQuestionMapper.insertBatch(securityQuestions);
            } else {
                throw new BlueException(DATA_ALREADY_EXIST);
            }
        } catch (Exception e) {
            LOGGER.error("insertSecurityQuestions(List<SecurityQuestionInsertParam> securityQuestionInsertParams, Long memberId) failed, e = {}", e);
            throw e;
        } finally {
            if (tryLock)
                try {
                    lock.unlock();
                } catch (Exception e) {
                    LOGGER.warn("securityQuestionMapper.insert, lock.unlock() failed, e = {}", e);
                }
        }
    }

    /**
     * count security question by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Long countSecurityQuestionByMemberId(Long memberId) {
        LOGGER.info("Long countSecurityQuestionByMemberId(Long memberId), memberId = {}", memberId);
        return ofNullable(securityQuestionMapper.countByMemberId(memberId)).orElse(0L);
    }

    /**
     * count security question mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<Long> countSecurityQuestionMonoByMemberId(Long memberId) {
        return just(countSecurityQuestionByMemberId(memberId));
    }

    /**
     * select security question by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public List<SecurityQuestion> selectSecurityQuestionByMemberId(Long memberId) {
        LOGGER.info("List<SecurityQuestion> selectSecurityQuestionByMemberId(Long memberId), memberId = {}", memberId);
        return securityQuestionMapper.selectByMemberId(memberId);
    }

    /**
     * select security question mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<SecurityQuestion>> selectSecurityQuestionMonoByMemberId(Long memberId) {
        return just(selectSecurityQuestionByMemberId(memberId));
    }

    /**
     * select security question info by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public List<SecurityQuestionInfo> selectSecurityQuestionInfoByMemberId(Long memberId) {
        return selectSecurityQuestionByMemberId(memberId).stream().map(SECURITY_QUESTION_2_SECURITY_QUESTION_INFO_CONVERTER).collect(toList());
    }

    /**
     * select security question info mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<SecurityQuestionInfo>> selectSecurityQuestionInfoMonoByMemberId(Long memberId) {
        return selectSecurityQuestionMonoByMemberId(memberId)
                .flatMap(l -> just(l.stream().map(SECURITY_QUESTION_2_SECURITY_QUESTION_INFO_CONVERTER).collect(toList())));
    }

}
