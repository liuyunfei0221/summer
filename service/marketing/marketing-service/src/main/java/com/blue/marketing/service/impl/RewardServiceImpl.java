package com.blue.marketing.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.marketing.api.model.RewardInfo;
import com.blue.marketing.api.model.RewardManagerInfo;
import com.blue.marketing.constant.RewardSortAttribute;
import com.blue.marketing.model.RewardCondition;
import com.blue.marketing.model.RewardInsertParam;
import com.blue.marketing.model.RewardUpdateParam;
import com.blue.marketing.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.marketing.repository.entity.Reward;
import com.blue.marketing.repository.mapper.RewardMapper;
import com.blue.marketing.service.inter.RewardService;
import com.blue.member.api.model.MemberBasicInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.ConditionSortProcessor.process;
import static com.blue.basic.common.base.ConstantProcessor.assertResourceType;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_SELECT;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_SERVICE_SELECT;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Symbol.DATABASE_WILDCARD;
import static com.blue.marketing.converter.MarketingModelConverters.*;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * reward service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class RewardServiceImpl implements RewardService {

    private static final Logger LOGGER = getLogger(RewardServiceImpl.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private RewardMapper rewardMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RewardServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, BlueIdentityProcessor blueIdentityProcessor, RewardMapper rewardMapper) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.rewardMapper = rewardMapper;
    }


    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(RewardSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<RewardCondition> CONDITION_PROCESSOR = condition -> {
        if (isNull(condition))
            return new RewardCondition();

        process(condition, SORT_ATTRIBUTE_MAPPING, RewardSortAttribute.ID.column);
        ofNullable(condition.getNameLike())
                .filter(StringUtils::hasText).ifPresent(nameLike -> condition.setNameLike(DATABASE_WILDCARD.identity + nameLike + DATABASE_WILDCARD.identity));

        return condition;
    };

    private static final Function<List<Reward>, List<Long>> OPERATORS_GETTER = rewards -> {
        Set<Long> operatorIds = new HashSet<>(rewards.size());

        for (Reward r : rewards) {
            operatorIds.add(r.getCreator());
            operatorIds.add(r.getUpdater());
        }

        return new ArrayList<>(operatorIds);
    };

    /**
     * is a reward exist?
     */
    private final Consumer<RewardInsertParam> INSERT_REWARD_VALIDATOR = rip -> {
        if (isNull(rip))
            throw new BlueException(EMPTY_PARAM);
        rip.asserts();

        if (isNotNull(rewardMapper.selectByUnique(rip.getName(), rip.getType())))
            throw new BlueException(REWARD_NAME_ALREADY_EXIST);
    };

    /**
     * is a reward exist?
     */
    private final Function<RewardUpdateParam, Reward> UPDATE_REWARD_VALIDATOR_AND_ORIGIN_RETURNER = rup -> {
        if (isNull(rup))
            throw new BlueException(EMPTY_PARAM);
        rup.asserts();

        Long id = rup.getId();

        Reward reward = rewardMapper.selectByPrimaryKey(id);
        if (isNull(reward))
            throw new BlueException(DATA_NOT_EXIST);

        ofNullable(rewardMapper.selectByUnique(
                ofNullable(rup.getName()).filter(BlueChecker::isNotBlank).orElseGet(reward::getName), rup.getType())
        )
                .map(Reward::getId)
                .ifPresent(rid -> {
                    if (!id.equals(rid))
                        throw new BlueException(REWARD_NAME_ALREADY_EXIST);
                });

        return reward;
    };

    /**
     * for reward
     */
    public static final BiFunction<RewardUpdateParam, Reward, Boolean> UPDATE_REWARD_VALIDATOR = (p, t) -> {
        if (isNull(p) || isNull(t))
            throw new BlueException(BAD_REQUEST);

        if (!p.getId().equals(t.getId()))
            throw new BlueException(BAD_REQUEST);

        boolean alteration = false;

        String name = p.getName();
        if (isNotBlank(name) && !name.equals(t.getName())) {
            t.setName(name);
            alteration = true;
        }

        String detail = p.getDetail();
        if (isNotBlank(detail) && !detail.equals(t.getDetail())) {
            t.setDetail(detail);
            alteration = true;
        }

        String link = p.getLink();
        if (isNotBlank(link) && !link.equals(t.getLink())) {
            t.setLink(link);
            alteration = true;
        }

        Integer type = p.getType();
        assertResourceType(type, true);
        if (type != null && !type.equals(t.getType())) {
            t.setType(type);
            alteration = true;
        }

        String data = p.getData();
        if (isNotBlank(data) && !data.equals(t.getData())) {
            t.setData(data);
            alteration = true;
        }

        return alteration;
    };

    /**
     * insert a new reward
     *
     * @param rewardInsertParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public RewardInfo insertReward(RewardInsertParam rewardInsertParam, Long operatorId) {
        LOGGER.info("RewardInfo insertReward(RewardInsertParam rewardInsertParam, Long operatorId), rewardInsertParam = {}, operatorId = {}",
                rewardInsertParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        INSERT_REWARD_VALIDATOR.accept(rewardInsertParam);
        Reward reward = REWARD_INSERT_PARAM_2_REWARD_CONVERTER.apply(rewardInsertParam);

        reward.setId(blueIdentityProcessor.generate(Reward.class));
        reward.setCreator(operatorId);
        reward.setUpdater(operatorId);

        rewardMapper.insert(reward);

        return REWARD_2_REWARD_INFO_CONVERTER.apply(reward);
    }

    /**
     * update a exist reward
     *
     * @param rewardUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public RewardInfo updateReward(RewardUpdateParam rewardUpdateParam, Long operatorId) {
        LOGGER.info("RewardInfo updateReward(RewardUpdateParam rewardUpdateParam, Long operatorId), rewardUpdateParam = {}, operatorId = {}",
                rewardUpdateParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        Reward reward = UPDATE_REWARD_VALIDATOR_AND_ORIGIN_RETURNER.apply(rewardUpdateParam);
        if (!UPDATE_REWARD_VALIDATOR.apply(rewardUpdateParam, reward))
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        rewardMapper.updateByPrimaryKeySelective(reward);

        return REWARD_2_REWARD_INFO_CONVERTER.apply(reward);
    }

    /**
     * delete reward
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public RewardInfo deleteReward(Long id) {
        LOGGER.info("RewardInfo deleteReward(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        Reward reward = rewardMapper.selectByPrimaryKey(id);
        if (isNull(reward))
            throw new BlueException(DATA_NOT_EXIST);

        rewardMapper.deleteByPrimaryKey(id);

        return REWARD_2_REWARD_INFO_CONVERTER.apply(reward);
    }

    /**
     * select reward by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Reward> getReward(Long id) {
        LOGGER.info("Optional<Reward> getRewardByPrimaryKey(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(rewardMapper.selectByPrimaryKey(id));
    }

    /**
     * get reward mono by role id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Reward> getRewardMono(Long id) {
        LOGGER.info("Mono<Reward> getRewardMono(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return justOrEmpty(rewardMapper.selectByPrimaryKey(id));
    }

    /**
     * select all rewards
     *
     * @return
     */
    @Override
    public Mono<List<Reward>> selectReward() {
        LOGGER.info("Mono<List<Reward>> selectReward()");
        return just(rewardMapper.select());
    }

    /**
     * select rewards by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<Reward> selectRewardByIds(List<Long> ids) {
        LOGGER.info("List<Reward> selectRewardByIds(List<Long> ids), ids = {}", ids);
        if (isEmpty(ids))
            return emptyList();
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(rewardMapper::selectByIds)
                .flatMap(List::stream)
                .collect(toList());
    }

    /**
     * select rewards mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<Reward>> selectRewardMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<Resource>> selectResourceMonoByIds(List<Long> ids), ids = {}", ids);

        return just(this.selectRewardByIds(ids));
    }

    /**
     * select reward by page and condition
     *
     * @param limit
     * @param rows
     * @param rewardCondition
     * @return
     */
    @Override
    public Mono<List<Reward>> selectRewardMonoByLimitAndCondition(Long limit, Long rows, RewardCondition rewardCondition) {
        LOGGER.info("Mono<List<Reward>> selectRewardMonoByLimitAndCondition(Long limit, Long rows, RewardCondition rewardCondition), " +
                "limit = {}, rows = {}, rewardCondition = {}", limit, rows, rewardCondition);
        return just(rewardMapper.selectByLimitAndCondition(limit, rows, rewardCondition));
    }

    /**
     * count reward by condition
     *
     * @param rewardCondition
     * @return
     */
    @Override
    public Mono<Long> countRewardMonoByCondition(RewardCondition rewardCondition) {
        LOGGER.info("Mono<Long> countRewardMonoByCondition(RewardCondition rewardCondition), rewardCondition = {}", rewardCondition);
        return just(ofNullable(rewardMapper.countByCondition(rewardCondition)).orElse(0L));
    }

    /**
     * select reward info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<RewardManagerInfo>> selectRewardManagerInfoPageMonoByPageAndCondition(PageModelRequest<RewardCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<RewardManagerInfo>> selectRewardManagerInfoPageMonoByPageAndCondition(PageModelRequest<RewardCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        RewardCondition rewardCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(selectRewardMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), rewardCondition), countRewardMonoByCondition(rewardCondition))
                .flatMap(tuple2 -> {
                    List<Reward> rewards = tuple2.getT1();
                    return isNotEmpty(rewards) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(OPERATORS_GETTER.apply(rewards))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndMemberNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(rewards.stream().map(r ->
                                                rewardToRewardManagerInfo(r, idAndMemberNameMapping)).collect(toList()));
                                    }).flatMap(rewardManagerInfos ->
                                            just(new PageModelResponse<>(rewardManagerInfos, tuple2.getT2())))
                            :
                            just(new PageModelResponse<>(emptyList(), tuple2.getT2()));
                });
    }

}
