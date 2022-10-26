package com.blue.media.service.impl;

import com.blue.auth.api.model.RoleInfo;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.common.SortType;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.common.SortElement;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.media.api.model.QrCodeConfigInfo;
import com.blue.media.config.deploy.QrCodeDeploy;
import com.blue.media.constant.QrCodeConfigSortAttribute;
import com.blue.media.model.QrCodeCondition;
import com.blue.media.model.QrCodeConfigInsertParam;
import com.blue.media.model.QrCodeConfigManagerInfo;
import com.blue.media.model.QrCodeConfigUpdateParam;
import com.blue.media.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.media.remote.consumer.RpcRoleServiceConsumer;
import com.blue.media.repository.entity.QrCodeConfig;
import com.blue.media.repository.template.QrCodeConfigRepository;
import com.blue.media.service.inter.QrCodeConfigService;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.redisson.component.SynchronizedProcessor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.assertQrCodeType;
import static com.blue.basic.constant.common.CacheKeyPrefix.QR_CONF_PRE;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Symbol.TEXT_PLACEHOLDER;
import static com.blue.basic.constant.common.SyncKey.QR_CODE_CONFIG_UPDATE_SYNC;
import static com.blue.media.constant.QrCodeConfigColumnName.*;
import static com.blue.media.converter.MediaModelConverters.*;
import static com.blue.mongo.common.MongoSortProcessor.process;
import static com.blue.mongo.constant.LikeElement.PREFIX;
import static com.blue.mongo.constant.LikeElement.SUFFIX;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static reactor.core.publisher.Mono.*;

/**
 * qr code config service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class QrCodeConfigServiceImpl implements QrCodeConfigService {

    private static final Logger LOGGER = Loggers.getLogger(QrCodeConfigServiceImpl.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final RpcRoleServiceConsumer rpcRoleServiceConsumer;

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final SynchronizedProcessor synchronizedProcessor;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private Scheduler scheduler;

    private QrCodeConfigRepository qrCodeConfigRepository;

    public QrCodeConfigServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, RpcRoleServiceConsumer rpcRoleServiceConsumer,
                                   ReactiveStringRedisTemplate reactiveStringRedisTemplate, BlueIdentityProcessor blueIdentityProcessor, SynchronizedProcessor synchronizedProcessor,
                                   ReactiveMongoTemplate reactiveMongoTemplate, Scheduler scheduler, QrCodeConfigRepository qrCodeConfigRepository, QrCodeDeploy qrCodeDeploy) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.rpcRoleServiceConsumer = rpcRoleServiceConsumer;
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.synchronizedProcessor = synchronizedProcessor;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.scheduler = scheduler;
        this.qrCodeConfigRepository = qrCodeConfigRepository;

        Long cacheExpiresSecond = qrCodeDeploy.getCacheExpiresSecond();
        if (isNull(cacheExpiresSecond) || cacheExpiresSecond < 1L)
            throw new RuntimeException("cacheExpiresSecond can't be null or less than 1");

        this.expireDuration = Duration.of(cacheExpiresSecond, ChronoUnit.SECONDS);
    }

    private Duration expireDuration;

    private static final Function<Integer, String> INFO_CACHE_KEY_GENERATOR = type -> QR_CONF_PRE.prefix + type;

    private final Consumer<Integer> REDIS_CACHE_DELETER = type ->
            reactiveStringRedisTemplate.delete(INFO_CACHE_KEY_GENERATOR.apply(type))
                    .subscribe(size -> LOGGER.info("REDIS_CACHE_DELETER, type = {}, size = {}", type, size));

    private final Function<Integer, Mono<QrCodeConfigInfo>> INFO_DB_GETTER = type -> {
        assertQrCodeType(type, false);

        QrCodeConfig probe = new QrCodeConfig();
        probe.setType(type);

        return qrCodeConfigRepository.findOne(Example.of(probe)).publishOn(scheduler)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .map(QR_CODE_CONFIG_2_QR_CODE_CONFIG_INFO_CONVERTER);
    };

    private final Function<Integer, Mono<QrCodeConfigInfo>> INFO_WITH_REDIS_CACHE_GETTER = type -> {
        assertQrCodeType(type, false);

        String key = INFO_CACHE_KEY_GENERATOR.apply(type);

        return reactiveStringRedisTemplate.opsForValue().get(key)
                .map(s -> GSON.fromJson(s, QrCodeConfigInfo.class))
                .switchIfEmpty(defer(() ->
                        INFO_DB_GETTER.apply(type)
                                .flatMap(qrCodeConfigInfo ->
                                        reactiveStringRedisTemplate.opsForValue().set(key, GSON.toJson(qrCodeConfigInfo), expireDuration)
                                                .flatMap(success -> {
                                                    LOGGER.info("reactiveStringRedisTemplate.opsForValue().set(key, GSON.toJson(qrCodeConfigInfo), expireDuration), success = {}", success);
                                                    return just(qrCodeConfigInfo);
                                                }))
                ));
    };

    private final Consumer<QrCodeConfigInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        QrCodeConfig probe = new QrCodeConfig();
        probe.setType(p.getType());

        if (ofNullable(qrCodeConfigRepository.count(Example.of(probe)).publishOn(scheduler).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(DATA_ALREADY_EXIST);
    };

    private final Function<QrCodeConfigUpdateParam, QrCodeConfig> UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long id = p.getId();

        QrCodeConfig probe = new QrCodeConfig();
        probe.setType(p.getType());

        List<QrCodeConfig> configs = ofNullable(qrCodeConfigRepository.findAll(Example.of(probe)).collectList()
                .publishOn(scheduler).toFuture().join())
                .orElseGet(Collections::emptyList);

        if (configs.stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(DATA_ALREADY_EXIST);

        QrCodeConfig qrCodeConfig = qrCodeConfigRepository.findById(id).publishOn(scheduler).toFuture().join();
        if (isNull(qrCodeConfig))
            throw new BlueException(DATA_NOT_EXIST);

        return qrCodeConfig;
    };

    public static final BiConsumer<QrCodeConfigUpdateParam, QrCodeConfig> UPDATE_ITEM_WITH_ASSERT_PACKAGER = (p, t) -> {
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
        if (isNotNull(type) && !type.equals(t.getType())) {
            t.setType(type);
            alteration = true;
        }

        String domain = p.getDomain();
        if (isNotBlank(domain) && !domain.equals(t.getDomain())) {
            t.setDomain(domain);
            alteration = true;
        }

        String pathToBeFilled = p.getPathToBeFilled();
        if (isNotBlank(pathToBeFilled) && !pathToBeFilled.equals(t.getPathToBeFilled())) {
            t.setPathToBeFilled(pathToBeFilled);
            t.setPlaceholderCount(countMatches(pathToBeFilled, TEXT_PLACEHOLDER.identity));
            alteration = true;
        }

        List<Long> allowedRoles = p.getAllowedRoles();
        if (isNotEmpty(allowedRoles) && isEquals(allowedRoles, t.getAllowedRoles())) {
            t.setAllowedRoles(allowedRoles);
            alteration = true;
        }

        if (!alteration)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        t.setUpdateTime(TIME_STAMP_GETTER.get());
    };

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(QrCodeConfigSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Function<QrCodeCondition, Sort> SORTER_CONVERTER = c -> {
        String sortAttribute = ofNullable(c).map(QrCodeCondition::getSortAttribute)
                .map(SORT_ATTRIBUTE_MAPPING::get)
                .filter(BlueChecker::isNotBlank)
                .orElse(QrCodeConfigSortAttribute.CREATE_TIME.column);

        String sortType = ofNullable(c).map(QrCodeCondition::getSortType)
                .filter(BlueChecker::isNotBlank)
                .orElse(SortType.DESC.identity);

        return sortAttribute.equals(QrCodeConfigSortAttribute.ID.column) ?
                process(singletonList(new SortElement(sortAttribute, sortType)))
                :
                process(Stream.of(sortAttribute, QrCodeConfigSortAttribute.ID.column)
                        .map(attr -> new SortElement(attr, sortType)).collect(toList()));
    };

    private static final Function<QrCodeCondition, Query> CONDITION_PROCESSOR = c -> {
        Query query = new Query();

        if (c == null) {
            query.with(SORTER_CONVERTER.apply(new QrCodeCondition()));
            return query;
        }

        QrCodeConfig probe = new QrCodeConfig();

        ofNullable(c.getId()).ifPresent(probe::setId);
        ofNullable(c.getType()).ifPresent(probe::setType);
        ofNullable(c.getPlaceholderCount()).ifPresent(probe::setPlaceholderCount);
        ofNullable(c.getCreator()).ifPresent(probe::setCreator);
        ofNullable(c.getUpdater()).ifPresent(probe::setUpdater);

        query.addCriteria(byExample(probe));

        ofNullable(c.getNameLike()).ifPresent(nameLike ->
                query.addCriteria(where(NAME.name).regex(compile(PREFIX.element + nameLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getDescriptionLike()).ifPresent(descriptionLike ->
                query.addCriteria(where(DESCRIPTION.name).regex(compile(PREFIX.element + descriptionLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getDomainLike()).ifPresent(domainLike ->
                query.addCriteria(where(DOMAIN.name).regex(compile(PREFIX.element + domainLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getPathToBeFilledLike()).ifPresent(pathToBeFilledLike ->
                query.addCriteria(where(PATH_TO_BE_FILLED.name).regex(compile(PREFIX.element + pathToBeFilledLike + SUFFIX.element, CASE_INSENSITIVE))));

        ofNullable(c.getCreateTimeBegin()).ifPresent(createTimeBegin ->
                query.addCriteria(where(CREATE_TIME.name).gte(createTimeBegin)));
        ofNullable(c.getCreateTimeEnd()).ifPresent(createTimeEnd ->
                query.addCriteria(where(CREATE_TIME.name).lte(createTimeEnd)));
        ofNullable(c.getUpdateTimeBegin()).ifPresent(updateTimeBegin ->
                query.addCriteria(where(UPDATE_TIME.name).gte(updateTimeBegin)));
        ofNullable(c.getUpdateTimeEnd()).ifPresent(updateTimeEnd ->
                query.addCriteria(where(UPDATE_TIME.name).lte(updateTimeEnd)));

        query.with(SORTER_CONVERTER.apply(c));

        return query;
    };

    private static final Function<List<QrCodeConfig>, List<Long>> OPERATORS_GETTER = cs -> {
        Set<Long> operatorIds = new HashSet<>(cs.size());

        for (QrCodeConfig c : cs) {
            operatorIds.add(c.getCreator());
            operatorIds.add(c.getUpdater());
        }

        return new ArrayList<>(operatorIds);
    };

    /**
     * insert qr code config
     *
     * @param qrCodeConfigInsertParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<QrCodeConfigInfo> insertQrCodeConfig(QrCodeConfigInsertParam qrCodeConfigInsertParam, Long operatorId) {
        LOGGER.info("Mono<QrCodeConfigInfo> insertQrCodeConfig(QrCodeConfigInsertParam qrCodeConfigInsertParam, Long operatorId), qrCodeConfigInsertParam = {}, operatorId = {}",
                qrCodeConfigInsertParam, operatorId);
        if (isNull(qrCodeConfigInsertParam))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        return synchronizedProcessor.handleSupWithSync(QR_CODE_CONFIG_UPDATE_SYNC.key, () -> {
            INSERT_ITEM_VALIDATOR.accept(qrCodeConfigInsertParam);
            QrCodeConfig qrCodeConfig = CONFIG_INSERT_PARAM_2_CONFIG_CONVERTER.apply(qrCodeConfigInsertParam);

            qrCodeConfig.setId(blueIdentityProcessor.generate(QrCodeConfig.class));
            qrCodeConfig.setCreator(operatorId);
            qrCodeConfig.setUpdater(operatorId);

            return qrCodeConfigRepository.insert(qrCodeConfig)
                    .publishOn(scheduler)
                    .map(QR_CODE_CONFIG_2_QR_CODE_CONFIG_INFO_CONVERTER);
        });
    }

    /**
     * update qr code config
     *
     * @param qrCodeConfigUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<QrCodeConfigInfo> updateQrCodeConfig(QrCodeConfigUpdateParam qrCodeConfigUpdateParam, Long operatorId) {
        LOGGER.info("Mono<QrCodeConfigInfo> updateQrCodeConfig(QrCodeConfigUpdateParam qrCodeConfigUpdateParam, Long operatorId), qrCodeConfigUpdateParam = {}, operatorId = {]",
                qrCodeConfigUpdateParam, operatorId);
        if (isNull(qrCodeConfigUpdateParam))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        return synchronizedProcessor.handleSupWithSync(QR_CODE_CONFIG_UPDATE_SYNC.key, () -> {
            QrCodeConfig qrCodeConfig = UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER.apply(qrCodeConfigUpdateParam);
            Integer originalType = qrCodeConfig.getType();

            UPDATE_ITEM_WITH_ASSERT_PACKAGER.accept(qrCodeConfigUpdateParam, qrCodeConfig);
            qrCodeConfig.setUpdater(operatorId);

            return qrCodeConfigRepository.save(qrCodeConfig)
                    .publishOn(scheduler)
                    .doOnSuccess(config -> {
                        Integer tarType = config.getType();
                        REDIS_CACHE_DELETER.accept(tarType);

                        if (!originalType.equals(tarType))
                            REDIS_CACHE_DELETER.accept(originalType);
                    })
                    .map(QR_CODE_CONFIG_2_QR_CODE_CONFIG_INFO_CONVERTER);
        });
    }

    /**
     * delete config
     *
     * @param id
     * @return
     */
    @Override
    public Mono<QrCodeConfigInfo> deleteQrCodeConfig(Long id) {
        LOGGER.info("Mono<QrCodeConfigInfo> deleteQrCodeConfig(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return synchronizedProcessor.handleSupWithSync(QR_CODE_CONFIG_UPDATE_SYNC.key, () ->
                qrCodeConfigRepository.findById(id)
                        .publishOn(scheduler)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                        .flatMap(config -> qrCodeConfigRepository.delete(config).then(just(config)))
                        .doOnSuccess(config -> REDIS_CACHE_DELETER.accept(config.getType()))
                        .map(QR_CODE_CONFIG_2_QR_CODE_CONFIG_INFO_CONVERTER)
        );
    }

    /**
     * get config mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<QrCodeConfig> getQrCodeConfigMono(Long id) {
        LOGGER.info("Mono<QrCodeConfig> getQrCodeConfigMono(Long id), id = {}", id);

        return qrCodeConfigRepository.findById(id).publishOn(scheduler);
    }

    /**
     * get config mono by type
     *
     * @param type
     * @return
     */
    @Override
    public Mono<QrCodeConfigInfo> getQrCodeConfigInfoMonoByType(Integer type) {
        LOGGER.info("Mono<QrCodeConfig> getQrCodeConfigMonoByType(Integer type), type = {}", type);

        return INFO_WITH_REDIS_CACHE_GETTER.apply(type);
    }

    /**
     * select config by page and condition
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    @Override
    public Mono<List<QrCodeConfig>> selectQrCodeConfigMonoByLimitAndCondition(Long limit, Long rows, Query query) {
        LOGGER.info("Mono<List<QrCodeConfig>> selectQrCodeConfigMonoByLimitAndCondition(Long limit, Long rows, Query query)," +
                " limit = {}, rows = {}, query = {}", limit, rows, query);

        if (isInvalidLimit(limit) || isInvalidRows(rows))
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, QrCodeConfig.class).publishOn(scheduler).collectList();
    }

    /**
     * count config by condition
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countQrCodeConfigMonoByCondition(Query query) {
        LOGGER.info("Mono<Long> countQrCodeConfigMonoByCondition(Query query), query = {}", query);

        return reactiveMongoTemplate.count(query, QrCodeConfig.class).publishOn(scheduler);
    }

    /**
     * select config manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<QrCodeConfigManagerInfo>> selectQrCodeConfigManagerInfoPageMonoByPageAndCondition(PageModelRequest<QrCodeCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<QrCodeConfigManagerInfo>> selectQrCodeConfigManagerInfoPageMonoByPageAndCondition(PageModelRequest<QrCodeCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);

        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectQrCodeConfigMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), query), countQrCodeConfigMonoByCondition(query))
                .flatMap(tuple2 -> {
                    List<QrCodeConfig> configs = tuple2.getT1();
                    Long count = tuple2.getT2();
                    return isNotEmpty(configs) ?
                            zip(rpcRoleServiceConsumer.selectRoleInfo(), rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(OPERATORS_GETTER.apply(configs)))
                                    .flatMap(t2 -> {
                                        Map<Long, RoleInfo> idAndRoleInfoMapping = t2.getT1().parallelStream().collect(toMap(RoleInfo::getId, ri -> ri, (a, b) -> a));
                                        Map<Long, String> idAndMemberNameMapping = t2.getT2().parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));

                                        return just(configs.stream().map(c ->
                                                qrCodeConfigToQrCodeConfigManagerInfo(c, idAndRoleInfoMapping, idAndMemberNameMapping)).collect(toList()));
                                    }).flatMap(configManagerInfos ->
                                            just(new PageModelResponse<>(configManagerInfos, count)))
                            :
                            just(new PageModelResponse<>(emptyList(), count));
                });
    }

}