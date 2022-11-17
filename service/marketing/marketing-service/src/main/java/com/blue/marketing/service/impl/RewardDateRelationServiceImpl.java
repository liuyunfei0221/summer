package com.blue.marketing.service.impl;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.common.MonthParam;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.marketing.api.model.RewardDateRelationInfo;
import com.blue.marketing.api.model.RewardInfo;
import com.blue.marketing.constant.RewardDateRelationSortAttribute;
import com.blue.marketing.model.*;
import com.blue.marketing.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.marketing.repository.entity.Reward;
import com.blue.marketing.repository.entity.RewardDateRelation;
import com.blue.marketing.repository.mapper.RewardDateRelationMapper;
import com.blue.marketing.service.inter.RewardDateRelationService;
import com.blue.marketing.service.inter.RewardService;
import com.blue.member.api.model.MemberBasicInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.LocalDate;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.database.common.ConditionSortProcessor.process;
import static com.blue.marketing.converter.MarketingModelConverters.*;
import static java.lang.Integer.parseInt;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * sign reward today relation service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class RewardDateRelationServiceImpl implements RewardDateRelationService {

    private static final Logger LOGGER = getLogger(RewardDateRelationServiceImpl.class);

    private RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private RewardService rewardService;

    private RewardDateRelationMapper rewardDateRelationMapper;

    public RewardDateRelationServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, BlueIdentityProcessor blueIdentityProcessor,
                                         RewardService rewardService, RewardDateRelationMapper rewardDateRelationMapper) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.rewardService = rewardService;
        this.rewardDateRelationMapper = rewardDateRelationMapper;
    }

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(RewardDateRelationSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<RewardDateRelationCondition> CONDITION_PROCESSOR = c -> {
        RewardDateRelationCondition rc = isNotNull(c) ? c : new RewardDateRelationCondition();

        process(rc, SORT_ATTRIBUTE_MAPPING, RewardDateRelationSortAttribute.CREATE_TIME.column);

        return rc;
    };

    private static final Function<List<RewardDateRelation>, List<Long>> OPERATORS_GETTER = relations -> {
        Set<Long> operatorIds = new HashSet<>(relations.size());

        for (RewardDateRelation r : relations) {
            operatorIds.add(r.getCreator());
            operatorIds.add(r.getUpdater());
        }

        return new ArrayList<>(operatorIds);
    };

    private final Consumer<RewardDateRelationInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Integer year = p.getYear();
        Integer month = p.getMonth();
        Integer day = p.getDay();

        LocalDate currentDate = LocalDate.of(year, month, 1);
        if (day > currentDate.lengthOfMonth())
            throw new BlueException(INVALID_PARAM);

        if (isNotNull(rewardDateRelationMapper.selectByUnique(year, month, day)))
            throw new BlueException(DATA_ALREADY_EXIST);
    };

    private final Function<RewardDateRelationUpdateParam, RewardDateRelation> UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long id = p.getId();

        RewardDateRelation rewardDateRelation = rewardDateRelationMapper.selectByPrimaryKey(id);
        if (isNull(rewardDateRelation))
            throw new BlueException(DATA_NOT_EXIST);

        ofNullable(rewardDateRelationMapper.selectByUnique(
                p.getYear(), p.getMonth(), p.getDay())
        )
                .map(RewardDateRelation::getId)
                .ifPresent(rid -> {
                    if (!id.equals(rid))
                        throw new BlueException(DATA_ALREADY_EXIST);
                });

        return rewardDateRelation;
    };

    public static final BiConsumer<RewardDateRelationUpdateParam, RewardDateRelation> UPDATE_ITEM_WITH_ASSERT_PACKAGER = (p, t) -> {
        if (isNull(p) || isNull(t))
            throw new BlueException(BAD_REQUEST);

        if (!p.getId().equals(t.getId()))
            throw new BlueException(BAD_REQUEST);

        boolean alteration = false;

        Long rewardId = p.getRewardId();
        if (isValidIdentity(rewardId) && !rewardId.equals(t.getRewardId())) {
            t.setRewardId(rewardId);
            alteration = true;
        }

        Integer year = p.getYear();
        if (isNotNull(year) && !year.equals(t.getYear())) {
            t.setYear(year);
            alteration = true;
        }

        Integer month = p.getMonth();
        if (isNotNull(year) && !year.equals(t.getYear())) {
            t.setMonth(month);
            alteration = true;
        }

        Integer day = p.getDay();
        if (isNotNull(day) && !day.equals(t.getDay())) {
            t.setDay(day);
            alteration = true;
        }

        if (!alteration)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        t.setUpdateTime(TIME_STAMP_GETTER.get());
    };

    private final Function<List<RewardDateRelation>, Mono<List<RewardDateRelationManagerInfo>>> REWARD_DATE_REL_MANAGER_INFO_CONVERTER = relations -> {
        LOGGER.info("relations = {}", relations);
        return isNotEmpty(relations) ?
                zip(rewardService.selectRewardInfoByIds(relations.parallelStream().map(RewardDateRelation::getRewardId).collect(toList()))
                                .map(rewardInfos -> rewardInfos.parallelStream().collect(toMap(RewardInfo::getId, ri -> ri, (a, b) -> a)))
                        ,
                        rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(OPERATORS_GETTER.apply(relations))
                                .map(memberBasicInfos -> memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a)))
                ).flatMap(t2 -> {
                    Map<Long, RewardInfo> rewardInfoIdAndNameMapping = t2.getT1();
                    Map<Long, String> memberIdAndNameMapping = t2.getT2();

                    return just(relations.stream().map(rel ->
                                    rewardDateRelToRewardDateRelManagerInfo(rel, rewardInfoIdAndNameMapping, memberIdAndNameMapping))
                            .collect(toList()));
                })
                :
                just(emptyList());
    };

    /**
     * insert a new relation
     *
     * @param rewardDateRelationInsertParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public RewardDateRelationInfo insertRewardDateRelation(RewardDateRelationInsertParam rewardDateRelationInsertParam, Long operatorId) {
        LOGGER.info("rewardDateRelationInsertParam = {}, operatorId = {}",
                rewardDateRelationInsertParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        INSERT_ITEM_VALIDATOR.accept(rewardDateRelationInsertParam);
        RewardDateRelation rewardDateRelation = REWARD_DATE_REL_INSERT_PARAM_2_REWARD_DATE_REL_CONVERTER.apply(rewardDateRelationInsertParam);

        Reward reward = rewardService.getRewardOpt(rewardDateRelation.getRewardId())
                .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));

        rewardDateRelation.setId(blueIdentityProcessor.generate(Reward.class));
        rewardDateRelation.setCreator(operatorId);
        rewardDateRelation.setUpdater(operatorId);

        rewardDateRelationMapper.insert(rewardDateRelation);

        return REWARD_DATE_REL_2_REWARD_DATE_REL_INFO_CONVERTER.apply(rewardDateRelation, REWARD_2_REWARD_INFO_CONVERTER.apply(reward));
    }

    /**
     * insert relation by year and month
     *
     * @param rewardDateRelationBatchInsertParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public List<RewardDateRelationInfo> insertRewardDateRelationMonoByYearAndMonth(RewardDateRelationBatchInsertParam rewardDateRelationBatchInsertParam, Long operatorId) {
        LOGGER.info("rewardDateRelationBatchInsertParam = {}, operatorId = {}",
                rewardDateRelationBatchInsertParam, operatorId);
        if (isNull(rewardDateRelationBatchInsertParam))
            throw new BlueException(EMPTY_PARAM);
        rewardDateRelationBatchInsertParam.asserts();

        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        Integer year = rewardDateRelationBatchInsertParam.getYear();
        Integer month = rewardDateRelationBatchInsertParam.getMonth();

        Map<Integer, Long> dayRelations;
        try {
            dayRelations = rewardDateRelationBatchInsertParam.getDayRelations().entrySet().stream().collect(Collectors.toMap(entry -> parseInt(entry.getKey()), Map.Entry::getValue, (a, b) -> a));
        } catch (Exception e) {
            throw new BlueException(INVALID_PARAM);
        }

        int minDayOfMonth = ((int) MAX_DAY_OF_MONTH.value);
        int maxDayOfMonth = ((int) MIN_DAY_OF_MONTH.value);

        Set<Integer> days = dayRelations.keySet();
        for (Integer dayOfMonth : days) {
            if (isNull(dayRelations.get(dayOfMonth))) {
                dayRelations.remove(dayOfMonth);
                continue;
            }

            if (minDayOfMonth > dayOfMonth)
                minDayOfMonth = dayOfMonth;

            if (maxDayOfMonth < dayOfMonth)
                maxDayOfMonth = dayOfMonth;
        }

        int dayOfMonth = LocalDate.of(year, month, 1).lengthOfMonth();
        if (maxDayOfMonth > dayOfMonth || dayRelations.size() > dayOfMonth)
            throw new BlueException(INVALID_PARAM);

        Set<Long> rewardIdSet = new HashSet<>(dayRelations.values());

        if (this.selectRewardDateRelationByYearAndMonth(year, month).toFuture().join()
                .stream().map(RewardDateRelation::getDay).anyMatch(dayRelations::containsKey))
            throw new BlueException(DATA_ALREADY_EXIST);

        List<RewardInfo> rewardInfos = rewardService.selectRewardInfoByIds(new ArrayList<>(rewardIdSet))
                .toFuture().join();
        if (rewardIdSet.size() != rewardInfos.size())
            throw new BlueException(DATA_NOT_EXIST);

        Map<Long, RewardInfo> rewardInfoIdAndNameMapping = rewardInfos.parallelStream().collect(toMap(RewardInfo::getId, ri -> ri, (a, b) -> a));

        Long stamp = TIME_STAMP_GETTER.get();
        List<RewardDateRelation> relations = dayRelations.entrySet().stream()
                .map(entry -> {
                    RewardDateRelation relation = new RewardDateRelation();

                    relation.setId(blueIdentityProcessor.generate(RewardDateRelation.class));
                    relation.setRewardId(entry.getValue());
                    relation.setYear(year);
                    relation.setMonth(month);
                    relation.setDay(entry.getKey());
                    relation.setCreateTime(stamp);
                    relation.setUpdateTime(stamp);
                    relation.setCreator(operatorId);
                    relation.setUpdater(operatorId);

                    return relation;
                }).collect(toList());

        rewardDateRelationMapper.insertBatch(relations);

        return relations.stream().map(relation -> REWARD_DATE_REL_2_REWARD_DATE_REL_INFO_CONVERTER.apply(relation,
                rewardInfoIdAndNameMapping.get(relation.getRewardId()))).collect(toList());
    }

    /**
     * update a exist relation
     *
     * @param rewardDateRelationUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public RewardDateRelationInfo updateRewardDateRelation(RewardDateRelationUpdateParam rewardDateRelationUpdateParam, Long operatorId) {
        LOGGER.info("rewardDateRelationUpdateParam = {}, operatorId = {}",
                rewardDateRelationUpdateParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        RewardDateRelation rewardDateRelation = UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER.apply(rewardDateRelationUpdateParam);

        UPDATE_ITEM_WITH_ASSERT_PACKAGER.accept(rewardDateRelationUpdateParam, rewardDateRelation);
        rewardDateRelation.setUpdater(operatorId);

        RewardInfo rewardInfo = rewardService.getRewardOpt(rewardDateRelation.getRewardId())
                .map(REWARD_2_REWARD_INFO_CONVERTER)
                .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));

        rewardDateRelationMapper.updateByPrimaryKeySelective(rewardDateRelation);

        return REWARD_DATE_REL_2_REWARD_DATE_REL_INFO_CONVERTER.apply(rewardDateRelation, rewardInfo);
    }

    /**
     * delete relation
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public RewardDateRelationInfo deleteRewardDateRelation(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        RewardDateRelation rewardDateRelation = rewardDateRelationMapper.selectByPrimaryKey(id);
        if (isNull(rewardDateRelation))
            throw new BlueException(DATA_NOT_EXIST);

        LocalDate now = LocalDate.now();
        if (rewardDateRelation.getYear().equals(now.getYear()) && rewardDateRelation.getMonth().equals(now.getMonthValue()))
            throw new BlueException(UNSUPPORTED_OPERATE);

        rewardDateRelationMapper.deleteByPrimaryKey(id);

        return REWARD_DATE_REL_2_REWARD_DATE_REL_INFO_CONVERTER.apply(rewardDateRelation, rewardService.getRewardOpt(rewardDateRelation.getRewardId())
                .map(REWARD_2_REWARD_INFO_CONVERTER).orElse(null));
    }

    /**
     * delete relation by reward id
     *
     * @param rewardId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public Integer deleteRewardDateRelationByRewardId(Long rewardId) {
        LOGGER.info("rewardId = {}", rewardId);
        if (isInvalidIdentity(rewardId))
            throw new BlueException(INVALID_IDENTITY);

        return rewardDateRelationMapper.deleteByRewardId(rewardId);
    }

    /**
     * select relation by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<RewardDateRelation> getRewardDateRelationOpt(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(rewardDateRelationMapper.selectByPrimaryKey(id));
    }

    /**
     * get relation mono by role id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<RewardDateRelation> getRewardDateRelation(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return justOrEmpty(rewardDateRelationMapper.selectByPrimaryKey(id));
    }

    /**
     * select relation by date
     *
     * @param year
     * @param month
     * @return
     */
    @Override
    public Mono<List<RewardDateRelation>> selectRewardDateRelationByYearAndMonth(Integer year, Integer month) {
        LOGGER.info("year = {}, month = {}", year, month);
        if (isNull(year) || isNull(month) || year < 1 || month < 1)
            throw new BlueException(BAD_REQUEST);

        return just(rewardDateRelationMapper.selectByYearAndMonth(year, month));
    }

    /**
     * select relation mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<RewardDateRelation>> selectRewardDateRelationByIds(List<Long> ids) {
        LOGGER.info("ids = {}", ids);
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return just(allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(rewardDateRelationMapper::selectByIds)
                .flatMap(List::stream)
                .collect(toList()));
    }

    /**
     * select relation by page and condition
     *
     * @param limit
     * @param rows
     * @param rewardDateRelationCondition
     * @return
     */
    @Override
    public Mono<List<RewardDateRelation>> selectRewardDateRelationByLimitAndCondition(Long limit, Long rows, RewardDateRelationCondition rewardDateRelationCondition) {
        LOGGER.info("limit = {}, rows = {}, rewardDateRelationCondition = {}", limit, rows, rewardDateRelationCondition);
        return just(rewardDateRelationMapper.selectByLimitAndCondition(limit, rows, rewardDateRelationCondition));
    }

    /**
     * count relation by condition
     *
     * @param rewardDateRelationCondition
     * @return
     */
    @Override
    public Mono<Long> countRewardDateRelationByCondition(RewardDateRelationCondition rewardDateRelationCondition) {
        LOGGER.info("rewardDateRelationCondition = {}", rewardDateRelationCondition);
        return just(ofNullable(rewardDateRelationMapper.countByCondition(rewardDateRelationCondition)).orElse(0L));
    }

    /**
     * select relation info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<RewardDateRelationManagerInfo>> selectRewardManagerInfoPageByPageAndCondition(PageModelRequest<RewardDateRelationCondition> pageModelRequest) {
        LOGGER.info("pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        RewardDateRelationCondition rewardDateRelationCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectRewardDateRelationByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), rewardDateRelationCondition), countRewardDateRelationByCondition(rewardDateRelationCondition))
                .flatMap(tuple2 ->
                        REWARD_DATE_REL_MANAGER_INFO_CONVERTER.apply(tuple2.getT1())
                                .flatMap(rewardDateRelationManagerInfos ->
                                        just(new PageModelResponse<>(rewardDateRelationManagerInfos, tuple2.getT2()))));
    }

    /**
     * select relation manager info by year and month
     *
     * @param monthParam
     * @return
     */
    @Override
    public Mono<List<RewardDateRelationManagerInfo>> selectRewardDateRelationByYearAndMonth(MonthParam monthParam) {
        LOGGER.info("monthParam = {}", monthParam);
        if (isNull(monthParam))
            throw new BlueException(EMPTY_PARAM);
        monthParam.asserts();

        return this.selectRewardDateRelationByYearAndMonth(monthParam.getYear(), monthParam.getMonth())
                .flatMap(REWARD_DATE_REL_MANAGER_INFO_CONVERTER);
    }

}
