package com.blue.media.service.impl;

import com.blue.auth.api.model.RoleInfo;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.media.api.model.QrCodeConfigInfo;
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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Status.VALID;
import static com.blue.media.constant.QrCodeConfigColumnName.*;
import static com.blue.media.constant.QrCodeConfigSortAttribute.ID;
import static com.blue.media.converter.MediaModelConverters.QR_CODE_CONFIG_2_QR_CODE_CONFIG_INFO_CONVERTER;
import static com.blue.media.converter.MediaModelConverters.qrCodeConfigToQrCodeConfigManagerInfo;
import static com.blue.mongo.common.MongoSortProcessor.process;
import static com.blue.mongo.constant.LikeElement.PREFIX;
import static com.blue.mongo.constant.LikeElement.SUFFIX;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static reactor.core.publisher.Mono.*;

/**
 * qr code config service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
@Service
public class QrCodeConfigServiceImpl implements QrCodeConfigService {

    private static final Logger LOGGER = Loggers.getLogger(QrCodeConfigServiceImpl.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final RpcRoleServiceConsumer rpcRoleServiceConsumer;

    private BlueIdentityProcessor blueIdentityProcessor;

    private QrCodeConfigRepository qrCodeConfigRepository;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private Scheduler scheduler;

    public QrCodeConfigServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, RpcRoleServiceConsumer rpcRoleServiceConsumer, BlueIdentityProcessor blueIdentityProcessor,
                                   QrCodeConfigRepository qrCodeConfigRepository, ReactiveMongoTemplate reactiveMongoTemplate, Scheduler scheduler) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.rpcRoleServiceConsumer = rpcRoleServiceConsumer;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.qrCodeConfigRepository = qrCodeConfigRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.scheduler = scheduler;
    }

    private final Consumer<QrCodeConfigInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        QrCodeConfig probe = new QrCodeConfig();

        probe.setType(p.getType());
        probe.setName(p.getName());

        if (ofNullable(qrCodeConfigRepository.count(Example.of(probe)).publishOn(scheduler).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(DATA_ALREADY_EXIST);
    };

    public final BiFunction<QrCodeConfigInsertParam, Long, QrCodeConfig> CONFIG_INSERT_PARAM_2_CONFIG_CONVERTER = (p, oid) -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(oid))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        QrCodeConfig qrCodeConfig = new QrCodeConfig();

        qrCodeConfig.setId(blueIdentityProcessor.generate(QrCodeConfig.class));
        qrCodeConfig.setName(p.getName());
        qrCodeConfig.setDescription(p.getDescription());
        qrCodeConfig.setType(p.getType());
        qrCodeConfig.setGenHandlerType(p.getGenHandlerType());
        qrCodeConfig.setDomain(p.getDomain());
        qrCodeConfig.setPathToBeFilled(p.getPathToBeFilled());
        qrCodeConfig.setPlaceholderCount(p.getPlaceholderCount());
        qrCodeConfig.setAllowedRoles(p.getAllowedRoles());

        qrCodeConfig.setStatus(VALID.status);
        Long stamp = TIME_STAMP_GETTER.get();
        qrCodeConfig.setCreateTime(stamp);
        qrCodeConfig.setUpdateTime(stamp);
        qrCodeConfig.setCreator(oid);
        qrCodeConfig.setUpdater(oid);

        return qrCodeConfig;
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

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(QrCodeConfigSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Function<QrCodeCondition, Sort> SORTER_CONVERTER = c ->
            process(c, SORT_ATTRIBUTE_MAPPING, ID.column);

    private static final Function<QrCodeCondition, Query> CONDITION_PROCESSOR = c -> {
        Query query = new Query();

        if (c == null) {
            query.with(SORTER_CONVERTER.apply(new QrCodeCondition()));
            return query;
        }

        QrCodeConfig probe = new QrCodeConfig();

        ofNullable(c.getId()).ifPresent(probe::setId);
        ofNullable(c.getType()).ifPresent(probe::setType);
        ofNullable(c.getGenHandlerType()).ifPresent(probe::setGenHandlerType);
        ofNullable(c.getPlaceholderCount()).ifPresent(probe::setPlaceholderCount);
        ofNullable(c.getStatus()).ifPresent(probe::setStatus);
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

    private static boolean validateAndPackageConfigForUpdate(QrCodeConfigUpdateParam param, QrCodeConfig qrCodeConfig, Long operatorId) {
        if (isNull(param) || isNull(qrCodeConfig))
            throw new BlueException(BAD_REQUEST);
        if (!param.getId().equals(qrCodeConfig.getId()))
            throw new BlueException(BAD_REQUEST);

        boolean alteration = false;

        String name = param.getName();
        if (isNotBlank(name) && !name.equals(qrCodeConfig.getName())) {
            qrCodeConfig.setName(name);
            alteration = true;
        }

        String description = param.getDescription();
        if (isNotBlank(description) && !description.equals(qrCodeConfig.getDescription())) {
            qrCodeConfig.setDescription(description);
            alteration = true;
        }

        Integer type = param.getType();
        if (isNotNull(type) && !type.equals(qrCodeConfig.getType())) {
            qrCodeConfig.setType(type);
            alteration = true;
        }

        Integer genHandlerType = param.getGenHandlerType();
        if (isNotNull(genHandlerType) && !genHandlerType.equals(qrCodeConfig.getGenHandlerType())) {
            qrCodeConfig.setGenHandlerType(genHandlerType);
            alteration = true;
        }

        String domain = param.getDomain();
        if (isNotBlank(domain) && !domain.equals(qrCodeConfig.getDomain())) {
            qrCodeConfig.setDomain(domain);
            alteration = true;
        }

        String pathToBeFilled = param.getPathToBeFilled();
        if (isNotBlank(pathToBeFilled) && !pathToBeFilled.equals(qrCodeConfig.getPathToBeFilled())) {
            qrCodeConfig.setPathToBeFilled(pathToBeFilled);
            alteration = true;
        }

        Integer placeholderCount = param.getPlaceholderCount();
        if (isNotNull(placeholderCount) && !placeholderCount.equals(qrCodeConfig.getPlaceholderCount())) {
            qrCodeConfig.setPlaceholderCount(placeholderCount);
            alteration = true;
        }

        List<Long> allowedRoles = param.getAllowedRoles();
        if (isNotEmpty(allowedRoles) && isEquals(allowedRoles, qrCodeConfig.getAllowedRoles())) {
            qrCodeConfig.setAllowedRoles(allowedRoles);
            alteration = true;
        }

        if (alteration) {
            qrCodeConfig.setUpdateTime(TIME_STAMP_GETTER.get());
            qrCodeConfig.setUpdater(operatorId);
        }

        return alteration;
    }

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

        INSERT_ITEM_VALIDATOR.accept(qrCodeConfigInsertParam);
        QrCodeConfig qrCodeConfig = CONFIG_INSERT_PARAM_2_CONFIG_CONVERTER.apply(qrCodeConfigInsertParam, operatorId);

        return qrCodeConfigRepository.insert(qrCodeConfig)
                .publishOn(scheduler)
                .map(QR_CODE_CONFIG_2_QR_CODE_CONFIG_INFO_CONVERTER);
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

        QrCodeConfig qrCodeConfig = UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER.apply(qrCodeConfigUpdateParam);
        if (!validateAndPackageConfigForUpdate(qrCodeConfigUpdateParam, qrCodeConfig, operatorId))
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        qrCodeConfig.setUpdater(operatorId);
        qrCodeConfig.setUpdateTime(TIME_STAMP_GETTER.get());

        return qrCodeConfigRepository.save(qrCodeConfig)
                .publishOn(scheduler)
                .map(QR_CODE_CONFIG_2_QR_CODE_CONFIG_INFO_CONVERTER);
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

        return qrCodeConfigRepository.findById(id)
                .publishOn(scheduler)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(config -> qrCodeConfigRepository.delete(config).then(just(config)))
                .map(QR_CODE_CONFIG_2_QR_CODE_CONFIG_INFO_CONVERTER);
    }

    /**
     * get config by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<QrCodeConfig> getQrCodeConfig(Long id) {
        LOGGER.info("Optional<QrCodeConfig> getQrCodeConfig(Long id), id = {}", id);

        return ofNullable(qrCodeConfigRepository.findById(id).publishOn(scheduler).toFuture().join());
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
     * select all config
     *
     * @return
     */
    @Override
    public Mono<List<QrCodeConfig>> selectQrCodeConfig() {
        LOGGER.info("Mono<List<QrCodeConfig>> selectQrCodeConfig()");

        return qrCodeConfigRepository.findAll().publishOn(scheduler).collectList();
    }

    /**
     * get config by type
     *
     * @param type
     * @return
     */
    @Override
    public Optional<QrCodeConfigInfo> getQrCodeConfigInfoByType(Integer type) {
        LOGGER.info("Optional<QrCodeConfigInfo> getQrCodeConfigInfoByType(Integer type), type = {}", type);

        QrCodeConfig probe = new QrCodeConfig();
        probe.setType(ofNullable(type).orElseThrow(() -> new BlueException(BAD_REQUEST)));

        return ofNullable(qrCodeConfigRepository.findOne(Example.of(probe)).publishOn(scheduler).toFuture().join()).map(QR_CODE_CONFIG_2_QR_CODE_CONFIG_INFO_CONVERTER);
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

        QrCodeConfig probe = new QrCodeConfig();
        probe.setType(ofNullable(type).orElseThrow(() -> new BlueException(BAD_REQUEST)));

        return qrCodeConfigRepository.findOne(Example.of(probe))
                .publishOn(scheduler)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .map(QR_CODE_CONFIG_2_QR_CODE_CONFIG_INFO_CONVERTER);
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
                    return isNotEmpty(configs) ?
                            zip(rpcRoleServiceConsumer.selectRoleInfo(), rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(OPERATORS_GETTER.apply(configs)))
                                    .flatMap(t2 -> {
                                        Map<Long, RoleInfo> idAndRoleInfoMapping = t2.getT1().parallelStream().collect(toMap(RoleInfo::getId, ri -> ri, (a, b) -> a));
                                        Map<Long, String> idAndMemberNameMapping = t2.getT2().parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));

                                        return just(configs.stream().map(c ->
                                                qrCodeConfigToQrCodeConfigManagerInfo(c, idAndRoleInfoMapping, idAndMemberNameMapping)).collect(toList()));
                                    }).flatMap(configManagerInfos ->
                                            just(new PageModelResponse<>(configManagerInfos, tuple2.getT2())))
                            :
                            just(new PageModelResponse<>(emptyList(), tuple2.getT2()));
                });
    }

}