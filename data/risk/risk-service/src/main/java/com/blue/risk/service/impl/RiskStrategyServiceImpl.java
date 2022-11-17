package com.blue.risk.service.impl;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.redisson.component.SynchronizedProcessor;
import com.blue.risk.api.model.RiskStrategyInfo;
import com.blue.risk.component.risk.RiskProcessor;
import com.blue.risk.constant.RiskStrategySortAttribute;
import com.blue.risk.event.producer.UpdateRiskStrategyProducer;
import com.blue.risk.model.RiskStrategyCondition;
import com.blue.risk.model.RiskStrategyInsertParam;
import com.blue.risk.model.RiskStrategyManagerInfo;
import com.blue.risk.model.RiskStrategyUpdateParam;
import com.blue.risk.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.risk.repository.entity.RiskStrategy;
import com.blue.risk.repository.mapper.RiskStrategyMapper;
import com.blue.risk.service.inter.RiskStrategyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.*;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Symbol.PERCENT;
import static com.blue.basic.constant.common.SyncKeyPrefix.RISK_STRATEGY_UPDATE_PRE;
import static com.blue.database.common.ConditionSortProcessor.process;
import static com.blue.risk.converter.RiskModelConverters.*;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * risk strategy service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection", "DuplicatedCode"})
@Service
public class RiskStrategyServiceImpl implements RiskStrategyService {

    private static final Logger LOGGER = getLogger(RiskStrategyServiceImpl.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final SynchronizedProcessor synchronizedProcessor;

    private final RiskProcessor riskProcessor;

    private final UpdateRiskStrategyProducer updateRiskStrategyProducer;

    private RiskStrategyMapper riskStrategyMapper;

    public RiskStrategyServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, BlueIdentityProcessor blueIdentityProcessor, SynchronizedProcessor synchronizedProcessor,
                                   RiskProcessor riskProcessor, UpdateRiskStrategyProducer updateRiskStrategyProducer, RiskStrategyMapper riskStrategyMapper) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.synchronizedProcessor = synchronizedProcessor;
        this.riskProcessor = riskProcessor;
        this.updateRiskStrategyProducer = updateRiskStrategyProducer;
        this.riskStrategyMapper = riskStrategyMapper;
    }

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(RiskStrategySortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<RiskStrategyCondition> CONDITION_PROCESSOR = c -> {
        RiskStrategyCondition rsc = isNotNull(c) ? c : new RiskStrategyCondition();

        process(rsc, SORT_ATTRIBUTE_MAPPING, RiskStrategySortAttribute.CREATE_TIME.column);

        ofNullable(rsc.getNameLike())
                .filter(StringUtils::hasText).ifPresent(nameLike -> rsc.setNameLike(PERCENT.identity + nameLike + PERCENT.identity));

        return rsc;
    };

    private static final Function<List<RiskStrategy>, List<Long>> OPERATORS_GETTER = ns -> {
        Set<Long> oIds = new HashSet<>(ns.size());

        for (RiskStrategy r : ns) {
            oIds.add(r.getCreator());
            oIds.add(r.getUpdater());
        }

        return new ArrayList<>(oIds);
    };

    private final Consumer<RiskStrategyInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        if (isNotNull(riskStrategyMapper.selectByType(p.getType())))
            throw new BlueException(NOTICE_TYPE_ALREADY_EXIST, new String[]{String.valueOf(p.getType())});
    };

    private final Function<RiskStrategyUpdateParam, RiskStrategy> UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long id = p.getId();

        ofNullable(p.getType())
                .map(t -> {
                    assertNoticeType(t, false);

                    return riskStrategyMapper.selectByType(t);
                })
                .map(RiskStrategy::getId)
                .ifPresent(eid -> {
                    if (!id.equals(eid))
                        throw new BlueException(NOTICE_TYPE_ALREADY_EXIST, new String[]{getNoticeTypeByIdentity(p.getType()).disc});
                });

        RiskStrategy riskStrategy = riskStrategyMapper.selectByPrimaryKey(id);
        if (isNull(riskStrategy))
            throw new BlueException(DATA_NOT_EXIST);

        return riskStrategy;
    };

    public static final BiConsumer<RiskStrategyUpdateParam, RiskStrategy> UPDATE_ITEM_WITH_ASSERT_PACKAGER = (p, t) -> {
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

        String description = p.getDescription();
        if (isNotBlank(description) && !description.equals(t.getDescription())) {
            t.setDescription(description);
            alteration = true;
        }

        Integer type = p.getType();
        assertBulletinType(type, true);
        if (type != null && !type.equals(t.getType())) {
            t.setType(type);
            alteration = true;
        }

        Map<String, String> attributes = p.getAttributes();
        if (isNotEmpty(attributes)) {
            Map<String, String> originalAttributesMap;
            try {
                originalAttributesMap = ATTR_JSON_2_ATTR_MAP_CONVERTER.apply(t.getAttributes());
            } catch (Exception e) {
                LOGGER.error("convert to originalAttributesMap failed, attr = {}, e = {}", t.getAttributes(), e);
                throw new BlueException(INTERNAL_SERVER_ERROR);
            }

            if (attributes.keySet().size() != originalAttributesMap.keySet().size()) {
                t.setAttributes(ATTR_MAP_2_ATTR_JSON_CONVERTER.apply(attributes));
                alteration = true;
            }

            Set<Map.Entry<String, String>> attrEntries = attributes.entrySet();
            for (Map.Entry<String, String> entry : attrEntries)
                if (!entry.getValue().equals(originalAttributesMap.get(entry.getKey()))) {
                    t.setAttributes(ATTR_MAP_2_ATTR_JSON_CONVERTER.apply(attributes));
                    alteration = true;
                    break;
                }
        }

        Boolean enable = p.getEnable();
        if (enable != null && enable == t.getEnable()) {
            t.setEnable(enable);
            alteration = true;
        }

        if (!alteration)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        t.setUpdateTime(TIME_STAMP_GETTER.get());
    };

    private static final Function<Integer, String> RISK_STRATEGY_UPDATE_SYNC_KEY_GEN = type -> {
        assertRiskType(type, false);

        return RISK_STRATEGY_UPDATE_PRE.prefix + type;
    };

    /**
     * insert risk strategy
     *
     * @param riskStrategyInsertParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public RiskStrategyInfo insertRiskStrategy(RiskStrategyInsertParam riskStrategyInsertParam, Long operatorId) {
        LOGGER.info("riskStrategyInsertParam = {}, operatorId = {}", riskStrategyInsertParam, operatorId);
        if (isNull(riskStrategyInsertParam))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);
        riskStrategyInsertParam.asserts();

        return synchronizedProcessor.handleSupWithSync(RISK_STRATEGY_UPDATE_SYNC_KEY_GEN.apply(riskStrategyInsertParam.getType()), () -> {
            INSERT_ITEM_VALIDATOR.accept(riskStrategyInsertParam);
            RiskStrategy riskStrategy = RISK_STRATEGY_INSERT_PARAM_2_RISK_STRATEGY_CONVERTER.apply(riskStrategyInsertParam);
            riskStrategy.setId(blueIdentityProcessor.generate(RiskStrategy.class));
            riskStrategy.setCreator(operatorId);
            riskStrategy.setUpdater(operatorId);

            RiskStrategyInfo riskStrategyInfo = RISK_STRATEGY_2_RISK_STRATEGY_INFO_CONVERTER.apply(riskStrategy);
            riskProcessor.assertStrategy(riskStrategyInfo);
            riskStrategyMapper.insert(riskStrategy);
            updateRiskStrategyProducer.send(riskStrategyInfo);

            return riskStrategyInfo;
        });
    }

    /**
     * update a exist risk strategy
     *
     * @param riskStrategyUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public RiskStrategyInfo updateRiskStrategy(RiskStrategyUpdateParam riskStrategyUpdateParam, Long operatorId) {
        LOGGER.info("riskStrategyUpdateParam = {}, operatorId = {}", riskStrategyUpdateParam, operatorId);
        if (isNull(riskStrategyUpdateParam))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);
        riskStrategyUpdateParam.asserts();

        return synchronizedProcessor.handleSupWithSync(RISK_STRATEGY_UPDATE_SYNC_KEY_GEN.apply(riskStrategyUpdateParam.getType()), () -> {
            RiskStrategy riskStrategy = UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER.apply(riskStrategyUpdateParam);
            UPDATE_ITEM_WITH_ASSERT_PACKAGER.accept(riskStrategyUpdateParam, riskStrategy);
            riskStrategy.setUpdater(operatorId);

            RiskStrategyInfo riskStrategyInfo = RISK_STRATEGY_2_RISK_STRATEGY_INFO_CONVERTER.apply(riskStrategy);
            riskProcessor.assertStrategy(riskStrategyInfo);
            riskStrategyMapper.updateByPrimaryKey(riskStrategy);
            updateRiskStrategyProducer.send(riskStrategyInfo);

            return riskStrategyInfo;
        });
    }

    /**
     * delete risk strategy
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public RiskStrategyInfo deleteRiskStrategy(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        RiskStrategy riskStrategy = riskStrategyMapper.selectByPrimaryKey(id);
        if (isNull(riskStrategy))
            throw new BlueException(DATA_NOT_EXIST);

        return synchronizedProcessor.handleSupWithSync(RISK_STRATEGY_UPDATE_SYNC_KEY_GEN.apply(riskStrategy.getType()), () -> {
            if (riskProcessor.selectActiveTypes().contains(riskStrategy.getType()))
                throw new BlueException(UNSUPPORTED_OPERATE);

            RiskStrategyInfo riskStrategyInfo = RISK_STRATEGY_2_RISK_STRATEGY_INFO_CONVERTER.apply(riskStrategy);
            riskStrategyInfo.setEnable(false);
            riskStrategyMapper.deleteByPrimaryKey(id);
            updateRiskStrategyProducer.send(riskStrategyInfo);

            return riskStrategyInfo;
        });

    }

    /**
     * get risk strategy mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<RiskStrategy> getRiskStrategy(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return justOrEmpty(riskStrategyMapper.selectByPrimaryKey(id));
    }

    /**
     * get risk strategy by type
     *
     * @param riskType
     * @return
     */
    @Override
    public Mono<RiskStrategy> getRiskStrategyByType(Integer riskType) {
        LOGGER.info("riskType = {}", riskType);
        assertRiskType(riskType, false);

        return justOrEmpty(riskStrategyMapper.selectByType(riskType));
    }

    /**
     * select all risk strategies
     *
     * @return
     */
    @Override
    public Mono<List<RiskStrategy>> selectRiskStrategy() {
        return justOrEmpty(riskStrategyMapper.select()).switchIfEmpty(defer(() -> just(emptyList())));
    }

    /**
     * select risk strategy by page and condition
     *
     * @param limit
     * @param rows
     * @param riskStrategyCondition
     * @return
     */
    @Override
    public Mono<List<RiskStrategy>> selectRiskStrategyByLimitAndCondition(Long limit, Long rows, RiskStrategyCondition riskStrategyCondition) {
        LOGGER.info("limit = {}, rows = {}, riskStrategyCondition = {}", limit, rows, riskStrategyCondition);
        return just(riskStrategyMapper.selectByLimitAndCondition(limit, rows, riskStrategyCondition));
    }

    /**
     * count risk strategy by condition
     *
     * @param riskStrategyCondition
     * @return
     */
    @Override
    public Mono<Long> countRiskStrategyByCondition(RiskStrategyCondition riskStrategyCondition) {
        LOGGER.info("riskStrategyCondition = {}", riskStrategyCondition);
        return just(ofNullable(riskStrategyMapper.countByCondition(riskStrategyCondition)).orElse(0L));
    }

    /**
     * select risk strategy manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<RiskStrategyManagerInfo>> selectRiskStrategyManagerInfoPageByPageAndCondition(PageModelRequest<RiskStrategyCondition> pageModelRequest) {
        LOGGER.info("pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        RiskStrategyCondition riskStrategyCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectRiskStrategyByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), riskStrategyCondition),
                countRiskStrategyByCondition(riskStrategyCondition))
                .flatMap(tuple2 -> {
                    List<RiskStrategy> riskStrategies = tuple2.getT1();
                    Long count = tuple2.getT2();
                    return isNotEmpty(riskStrategies) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(OPERATORS_GETTER.apply(riskStrategies))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndMemberNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(riskStrategies.stream().map(rs ->
                                                RISK_STRATEGY_2_RISK_STRATEGY_MANAGER_INFOS_CONVERTER.apply(rs, idAndMemberNameMapping)).collect(toList()));
                                    }).flatMap(riskStrategyManagerInfos ->
                                            just(new PageModelResponse<>(riskStrategyManagerInfos, count)))
                            :
                            just(new PageModelResponse<>(emptyList(), count));
                });
    }

}
