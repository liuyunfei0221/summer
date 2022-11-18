package com.blue.risk.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.common.SortType;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.common.SortElement;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.redisson.component.SynchronizedProcessor;
import com.blue.risk.api.model.RiskStrategyInfo;
import com.blue.risk.component.risk.RiskProcessor;
import com.blue.risk.constant.RiskStrategyColumnName;
import com.blue.risk.constant.RiskStrategySortAttribute;
import com.blue.risk.event.producer.UpdateRiskStrategyProducer;
import com.blue.risk.model.RiskStrategyCondition;
import com.blue.risk.model.RiskStrategyInsertParam;
import com.blue.risk.model.RiskStrategyManagerInfo;
import com.blue.risk.model.RiskStrategyUpdateParam;
import com.blue.risk.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.risk.repository.entity.RiskStrategy;
import com.blue.risk.repository.template.RiskStrategyRepository;
import com.blue.risk.service.inter.RiskStrategyService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.assertRiskType;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SyncKeyPrefix.RISK_STRATEGY_UPDATE_PRE;
import static com.blue.mongo.common.MongoSortProcessor.process;
import static com.blue.mongo.constant.LikeElement.PREFIX;
import static com.blue.mongo.constant.LikeElement.SUFFIX;
import static com.blue.risk.converter.RiskModelConverters.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
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

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private RiskStrategyRepository riskStrategyRepository;

    public RiskStrategyServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, BlueIdentityProcessor blueIdentityProcessor, SynchronizedProcessor synchronizedProcessor,
                                   RiskProcessor riskProcessor, UpdateRiskStrategyProducer updateRiskStrategyProducer, ReactiveMongoTemplate reactiveMongoTemplate, RiskStrategyRepository riskStrategyRepository) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.synchronizedProcessor = synchronizedProcessor;
        this.riskProcessor = riskProcessor;
        this.updateRiskStrategyProducer = updateRiskStrategyProducer;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.riskStrategyRepository = riskStrategyRepository;
    }

    private final Consumer<RiskStrategyInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        RiskStrategy probe = new RiskStrategy();
        probe.setType(p.getType());

        if (ofNullable(riskStrategyRepository.count(Example.of(probe)).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(DATA_ALREADY_EXIST);
    };

    private final Function<RiskStrategyUpdateParam, RiskStrategy> UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long id = p.getId();

        RiskStrategy riskStrategy = riskStrategyRepository.findById(id).toFuture().join();
        if (isNull(riskStrategy))
            throw new BlueException(DATA_NOT_EXIST);

        RiskStrategy probe = new RiskStrategy();
        probe.setType(p.getType());

        List<RiskStrategy> riskStrategies = ofNullable(riskStrategyRepository.findAll(Example.of(probe)).collectList()
                .toFuture().join())
                .orElseGet(Collections::emptyList);

        if (riskStrategies.stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(DATA_ALREADY_EXIST);

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
        assertRiskType(type, true);
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
        if (enable != null && enable != t.getEnable()) {
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

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(RiskStrategySortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Function<RiskStrategyCondition, Sort> SORTER_CONVERTER = c -> {
        String sortAttribute = ofNullable(c).map(RiskStrategyCondition::getSortAttribute)
                .map(SORT_ATTRIBUTE_MAPPING::get)
                .filter(BlueChecker::isNotBlank)
                .orElse(RiskStrategySortAttribute.CREATE_TIME.column);

        String sortType = ofNullable(c).map(RiskStrategyCondition::getSortType)
                .filter(BlueChecker::isNotBlank)
                .orElse(SortType.DESC.identity);

        return process(
                sortAttribute.equals(RiskStrategySortAttribute.ID.column) ?
                        singletonList(new SortElement(sortAttribute, sortType))
                        :
                        Stream.of(sortAttribute, RiskStrategySortAttribute.ID.column)
                                .map(attr -> new SortElement(attr, sortType)).collect(toList())
        );
    };

    private static final Function<RiskStrategyCondition, Query> CONDITION_PROCESSOR = c -> {
        Query query = new Query();

        if (isNull(c)) {
            query.with(SORTER_CONVERTER.apply(new RiskStrategyCondition()));
            return query;
        }

        RiskStrategy probe = new RiskStrategy();

        ofNullable(c.getId()).ifPresent(probe::setId);
        ofNullable(c.getType()).ifPresent(probe::setType);
        ofNullable(c.getEnable()).ifPresent(probe::setEnable);
        ofNullable(c.getCreator()).ifPresent(probe::setCreator);
        ofNullable(c.getUpdater()).ifPresent(probe::setUpdater);

        query.addCriteria(byExample(probe));

        ofNullable(c.getNameLike()).ifPresent(nameLike ->
                query.addCriteria(where(RiskStrategyColumnName.NAME.name).regex(compile(PREFIX.element + nameLike + SUFFIX.element, CASE_INSENSITIVE))));

        ofNullable(c.getCreateTimeBegin()).ifPresent(createTimeBegin ->
                query.addCriteria(where(RiskStrategyColumnName.CREATE_TIME.name).gte(createTimeBegin)));
        ofNullable(c.getCreateTimeEnd()).ifPresent(createTimeEnd ->
                query.addCriteria(where(RiskStrategyColumnName.CREATE_TIME.name).lte(createTimeEnd)));
        ofNullable(c.getUpdateTimeBegin()).ifPresent(updateTimeBegin ->
                query.addCriteria(where(RiskStrategyColumnName.UPDATE_TIME.name).gte(updateTimeBegin)));
        ofNullable(c.getUpdateTimeEnd()).ifPresent(updateTimeEnd ->
                query.addCriteria(where(RiskStrategyColumnName.UPDATE_TIME.name).lte(updateTimeEnd)));

        query.with(SORTER_CONVERTER.apply(c));

        return query;
    };

    private static final Function<List<RiskStrategy>, List<Long>> OPERATORS_GETTER = ns -> {
        Set<Long> oIds = new HashSet<>(ns.size());

        for (RiskStrategy r : ns) {
            oIds.add(r.getCreator());
            oIds.add(r.getUpdater());
        }

        return new ArrayList<>(oIds);
    };

    /**
     * insert risk strategy
     *
     * @param riskStrategyInsertParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<RiskStrategyInfo> insertRiskStrategy(RiskStrategyInsertParam riskStrategyInsertParam, Long operatorId) {
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

            return riskStrategyRepository.insert(riskStrategy)
                    .then(just(riskStrategyInfo))
                    .doOnSuccess(updateRiskStrategyProducer::send);
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
    public Mono<RiskStrategyInfo> updateRiskStrategy(RiskStrategyUpdateParam riskStrategyUpdateParam, Long operatorId) {
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

            return riskStrategyRepository.save(riskStrategy)
                    .then(just(riskStrategyInfo))
                    .doOnSuccess(updateRiskStrategyProducer::send);
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
    public Mono<RiskStrategyInfo> deleteRiskStrategy(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return riskStrategyRepository.findById(id)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(riskStrategy ->
                        synchronizedProcessor.handleSupWithSync(RISK_STRATEGY_UPDATE_SYNC_KEY_GEN.apply(riskStrategy.getType()), () ->
                                riskStrategyRepository.delete(riskStrategy)
                                        .then(just(RISK_STRATEGY_2_RISK_STRATEGY_INFO_CONVERTER.apply(riskStrategy)))
                                        .map(rs -> {
                                            rs.setEnable(false);
                                            return rs;
                                        }).doOnSuccess(updateRiskStrategyProducer::send)));
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

        return riskStrategyRepository.findById(id);
    }

    /**
     * get risk strategy by type
     *
     * @param type
     * @return
     */
    @Override
    public Mono<RiskStrategy> getRiskStrategyByType(Integer type) {
        LOGGER.info("type = {}", type);
        assertRiskType(type, false);

        RiskStrategy probe = new RiskStrategy();
        probe.setType(type);

        return riskStrategyRepository.findOne(Example.of(probe))
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))));
    }

    /**
     * select all risk strategies
     *
     * @return
     */
    @Override
    public Mono<List<RiskStrategy>> selectRiskStrategy() {
        return riskStrategyRepository.findAll().collectList();
    }

    /**
     * select risk strategy by page and condition
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    @Override
    public Mono<List<RiskStrategy>> selectRiskStrategyByLimitAndCondition(Long limit, Long rows, Query query) {
        LOGGER.info("limit = {}, rows = {}, query = {}", limit, rows, query);
        if (isInvalidLimit(limit) || isInvalidRows(rows))
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, RiskStrategy.class).collectList();
    }

    /**
     * count risk strategy by condition
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countRiskStrategyByCondition(Query query) {
        LOGGER.info("query = {}", query);
        return reactiveMongoTemplate.count(query, RiskStrategy.class);
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

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectRiskStrategyByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), query),
                countRiskStrategyByCondition(query))
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
