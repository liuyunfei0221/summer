package com.blue.verify.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.common.SortType;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.common.SortElement;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.redisson.component.SynchronizedProcessor;
import com.blue.verify.api.model.VerifyTemplateInfo;
import com.blue.verify.config.deploy.VerifyTemplateDeploy;
import com.blue.verify.constant.VerifyTemplateSortAttribute;
import com.blue.verify.model.VerifyTemplateCondition;
import com.blue.verify.model.VerifyTemplateInsertParam;
import com.blue.verify.model.VerifyTemplateManagerInfo;
import com.blue.verify.model.VerifyTemplateUpdateParam;
import com.blue.verify.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.verify.repository.entity.VerifyTemplate;
import com.blue.verify.repository.template.VerifyTemplateRepository;
import com.blue.verify.service.inter.VerifyTemplateService;
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
import java.util.function.*;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.assertVerifyBusinessType;
import static com.blue.basic.common.base.ConstantProcessor.assertVerifyType;
import static com.blue.basic.constant.common.CacheKeyPrefix.VERIFY_TEMPLATE_PRE;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Symbol.PAR_CONCATENATION;
import static com.blue.basic.constant.common.SyncKey.VERIFY_TEMPLATE_UPDATE_SYNC;
import static com.blue.mongo.common.MongoSortProcessor.process;
import static com.blue.mongo.constant.LikeElement.PREFIX;
import static com.blue.mongo.constant.LikeElement.SUFFIX;
import static com.blue.verify.constant.VerifyTemplateColumnName.*;
import static com.blue.verify.converter.VerifyModelConverters.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static reactor.core.publisher.Mono.*;

/**
 * verify template service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
@Service
public class VerifyTemplateServiceImpl implements VerifyTemplateService {

    private static final Logger LOGGER = Loggers.getLogger(VerifyTemplateServiceImpl.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final SynchronizedProcessor synchronizedProcessor;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private Scheduler scheduler;

    private VerifyTemplateRepository verifyTemplateRepository;

    public VerifyTemplateServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, ReactiveStringRedisTemplate reactiveStringRedisTemplate,
                                     BlueIdentityProcessor blueIdentityProcessor, SynchronizedProcessor synchronizedProcessor, ReactiveMongoTemplate reactiveMongoTemplate,
                                     Scheduler scheduler, VerifyTemplateRepository verifyTemplateRepository, VerifyTemplateDeploy verifyTemplateDeploy) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.synchronizedProcessor = synchronizedProcessor;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.scheduler = scheduler;
        this.verifyTemplateRepository = verifyTemplateRepository;

        Long cacheExpiresSecond = verifyTemplateDeploy.getCacheExpiresSecond();
        if (isNull(cacheExpiresSecond) || cacheExpiresSecond < 1L)
            throw new RuntimeException("cacheExpiresSecond can't be null or less than 1");

        this.expireDuration = Duration.of(cacheExpiresSecond, ChronoUnit.SECONDS);
    }

    private Duration expireDuration;

    private static final BinaryOperator<String> TEMPLATE_CACHE_KEY_GENERATOR = (type, businessType) -> VERIFY_TEMPLATE_PRE.prefix + type + PAR_CONCATENATION + businessType;

    private final BiConsumer<String, String> REDIS_CACHE_DELETER = (type, businessType) ->
            reactiveStringRedisTemplate.delete(TEMPLATE_CACHE_KEY_GENERATOR.apply(type, businessType))
                    .subscribe(size -> LOGGER.info("REDIS_CACHE_DELETER, type = {}, businessType = {}, size = {}", type, size));

    private final BiFunction<String, String, Mono<VerifyTemplateInfo>> TEMPLATE_DB_GETTER = (type, businessType) -> {
        assertVerifyType(type, false);
        assertVerifyBusinessType(businessType, false);

        VerifyTemplate probe = new VerifyTemplate();
        probe.setType(type);
        probe.setBusinessType(businessType);

        return verifyTemplateRepository.findOne(Example.of(probe)).publishOn(scheduler)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .map(VERIFY_TEMPLATE_2_VERIFY_TEMPLATE_INFO_CONVERTER);
    };

    private final BiFunction<String, String, Mono<VerifyTemplateInfo>> TEMPLATE_WITH_REDIS_CACHE_GETTER = (type, businessType) -> {
        assertVerifyType(type, false);
        assertVerifyBusinessType(businessType, false);

        String key = TEMPLATE_CACHE_KEY_GENERATOR.apply(type, businessType);

        return reactiveStringRedisTemplate.opsForValue().get(key)
                .map(s -> GSON.fromJson(s, VerifyTemplateInfo.class))
                .switchIfEmpty(defer(() ->
                        TEMPLATE_DB_GETTER.apply(type, businessType)
                                .flatMap(verifyTemplateInfo ->
                                        reactiveStringRedisTemplate.opsForValue().set(key, GSON.toJson(verifyTemplateInfo), expireDuration)
                                                .flatMap(success -> {
                                                    LOGGER.info("reactiveStringRedisTemplate.opsForValue().set(key, GSON.toJson(verifyTemplateInfo), expireDuration), success = {}", success);
                                                    return just(verifyTemplateInfo);
                                                }))
                ));
    };

    private final Consumer<VerifyTemplateInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        VerifyTemplate probe = new VerifyTemplate();
        probe.setType(p.getType());
        probe.setBusinessType(p.getBusinessType());

        if (ofNullable(verifyTemplateRepository.count(Example.of(probe)).publishOn(scheduler).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(DATA_ALREADY_EXIST);
    };

    private final Function<VerifyTemplateUpdateParam, VerifyTemplate> UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long id = p.getId();

        VerifyTemplate probe = new VerifyTemplate();
        probe.setType(p.getType());
        probe.setBusinessType(p.getBusinessType());

        List<VerifyTemplate> templates = ofNullable(verifyTemplateRepository.findAll(Example.of(probe)).collectList()
                .publishOn(scheduler).toFuture().join())
                .orElseGet(Collections::emptyList);

        if (templates.stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(DATA_ALREADY_EXIST);

        VerifyTemplate verifyTemplate = verifyTemplateRepository.findById(id).publishOn(scheduler).toFuture().join();
        if (isNull(verifyTemplate))
            throw new BlueException(DATA_NOT_EXIST);

        return verifyTemplate;
    };

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(VerifyTemplateSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Function<VerifyTemplateCondition, Sort> SORTER_CONVERTER = c -> {
        String sortAttribute = ofNullable(c).map(VerifyTemplateCondition::getSortAttribute)
                .map(SORT_ATTRIBUTE_MAPPING::get)
                .filter(BlueChecker::isNotBlank)
                .orElse(VerifyTemplateSortAttribute.CREATE_TIME.column);

        String sortType = ofNullable(c).map(VerifyTemplateCondition::getSortType)
                .filter(BlueChecker::isNotBlank)
                .orElse(SortType.DESC.identity);

        return sortAttribute.equals(VerifyTemplateSortAttribute.ID.column) ?
                process(singletonList(new SortElement(sortAttribute, sortType)))
                :
                process(Stream.of(sortAttribute, VerifyTemplateSortAttribute.ID.column)
                        .map(attr -> new SortElement(attr, sortType)).collect(toList()));
    };

    private static final Function<VerifyTemplateCondition, Query> CONDITION_PROCESSOR = c -> {
        Query query = new Query();

        if (c == null) {
            query.with(SORTER_CONVERTER.apply(new VerifyTemplateCondition()));
            return query;
        }

        VerifyTemplate probe = new VerifyTemplate();

        ofNullable(c.getId()).ifPresent(probe::setId);
        ofNullable(c.getType()).ifPresent(type -> {
            assertVerifyType(type, false);
            probe.setType(type);
        });
        ofNullable(c.getBusinessType()).ifPresent(businessType -> {
            assertVerifyBusinessType(businessType, false);
            probe.setBusinessType(businessType);
        });
        ofNullable(c.getCreator()).ifPresent(probe::setCreator);
        ofNullable(c.getUpdater()).ifPresent(probe::setUpdater);

        query.addCriteria(byExample(probe));

        ofNullable(c.getNameLike()).ifPresent(nameLike ->
                query.addCriteria(where(NAME.name).regex(compile(PREFIX.element + nameLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getDescriptionLike()).ifPresent(descriptionLike ->
                query.addCriteria(where(DESCRIPTION.name).regex(compile(PREFIX.element + descriptionLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getTitleLike()).ifPresent(titleLike ->
                query.addCriteria(where(TITLE.name).regex(compile(PREFIX.element + titleLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getContentLike()).ifPresent(contentLike ->
                query.addCriteria(where(CONTENT.name).regex(compile(PREFIX.element + contentLike + SUFFIX.element, CASE_INSENSITIVE))));

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

    private static final Function<List<VerifyTemplate>, List<Long>> OPERATORS_GETTER = ts -> {
        Set<Long> operatorIds = new HashSet<>(ts.size());

        for (VerifyTemplate t : ts) {
            operatorIds.add(t.getCreator());
            operatorIds.add(t.getUpdater());
        }

        return new ArrayList<>(operatorIds);
    };

    private static boolean validateAndPackageConfigForUpdate(VerifyTemplateUpdateParam param, VerifyTemplate verifyTemplate, Long operatorId) {
        if (isNull(param) || isNull(verifyTemplate))
            throw new BlueException(BAD_REQUEST);
        if (!param.getId().equals(verifyTemplate.getId()))
            throw new BlueException(BAD_REQUEST);

        boolean alteration = false;

        String name = param.getName();
        if (isNotBlank(name) && !name.equals(verifyTemplate.getName())) {
            verifyTemplate.setName(name);
            alteration = true;
        }

        String description = param.getDescription();
        if (isNotBlank(description) && !description.equals(verifyTemplate.getDescription())) {
            verifyTemplate.setDescription(description);
            alteration = true;
        }

        String type = param.getType();
        if (isNotNull(type) && !type.equals(verifyTemplate.getType())) {
            assertVerifyType(type, false);
            verifyTemplate.setType(type);
            alteration = true;
        }

        String businessType = param.getBusinessType();
        if (isNotNull(businessType) && !businessType.equals(verifyTemplate.getBusinessType())) {
            assertVerifyBusinessType(businessType, false);
            verifyTemplate.setBusinessType(businessType);
            alteration = true;
        }

        String title = param.getTitle();
        if (isNotBlank(title) && !title.equals(verifyTemplate.getTitle())) {
            verifyTemplate.setTitle(title);
            alteration = true;
        }

        String content = param.getContent();
        if (isNotBlank(content) && !content.equals(verifyTemplate.getContent())) {
            verifyTemplate.setContent(content);
            alteration = true;
        }

        if (alteration) {
            verifyTemplate.setUpdateTime(TIME_STAMP_GETTER.get());
            verifyTemplate.setUpdater(operatorId);
        }

        return alteration;
    }

    /**
     * insert verify template
     *
     * @param verifyTemplateInsertParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<VerifyTemplateInfo> insertVerifyTemplate(VerifyTemplateInsertParam verifyTemplateInsertParam, Long operatorId) {
        LOGGER.info("Mono<VerifyTemplateInfo> insertVerifyTemplate(VerifyTemplateInsertParam verifyTemplateInsertParam, Long operatorId), verifyTemplateInsertParam = {}, operatorId = {}",
                verifyTemplateInsertParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(EMPTY_PARAM);

        return synchronizedProcessor.handleSupWithLock(VERIFY_TEMPLATE_UPDATE_SYNC.key, () -> {
            INSERT_ITEM_VALIDATOR.accept(verifyTemplateInsertParam);
            VerifyTemplate verifyTemplate = VERIFY_TEMPLATE_INSERT_PARAM_2_VERIFY_TEMPLATE_CONVERTER.apply(verifyTemplateInsertParam);

            verifyTemplate.setId(blueIdentityProcessor.generate(VerifyTemplate.class));
            verifyTemplate.setCreator(operatorId);
            verifyTemplate.setUpdater(operatorId);

            return verifyTemplateRepository.insert(verifyTemplate)
                    .publishOn(scheduler)
                    .map(VERIFY_TEMPLATE_2_VERIFY_TEMPLATE_INFO_CONVERTER);
        });
    }

    /**
     * update verify template
     *
     * @param verifyTemplateUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<VerifyTemplateInfo> updateVerifyTemplate(VerifyTemplateUpdateParam verifyTemplateUpdateParam, Long operatorId) {
        LOGGER.info("Mono<VerifyTemplateInfo> updateVerifyTemplate(VerifyTemplateUpdateParam verifyTemplateUpdateParam, Long operatorId), verifyTemplateUpdateParam = {}, operatorId = {]",
                verifyTemplateUpdateParam, operatorId);

        return synchronizedProcessor.handleSupWithLock(VERIFY_TEMPLATE_UPDATE_SYNC.key, () -> {
            VerifyTemplate verifyTemplate = UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER.apply(verifyTemplateUpdateParam);
            String originalType = verifyTemplate.getType();
            String originalBusinessType = verifyTemplate.getBusinessType();

            if (!validateAndPackageConfigForUpdate(verifyTemplateUpdateParam, verifyTemplate, operatorId))
                throw new BlueException(DATA_HAS_NOT_CHANGED);

            verifyTemplate.setUpdater(operatorId);
            verifyTemplate.setUpdateTime(TIME_STAMP_GETTER.get());

            return verifyTemplateRepository.save(verifyTemplate)
                    .publishOn(scheduler)
                    .doOnSuccess(template -> {
                        String tarType = template.getType();
                        String tarBusinessType = template.getBusinessType();
                        REDIS_CACHE_DELETER.accept(tarType, tarBusinessType);

                        if (!originalType.equals(tarType) || !originalBusinessType.equals(tarBusinessType))
                            REDIS_CACHE_DELETER.accept(originalType, originalBusinessType);
                    })
                    .map(VERIFY_TEMPLATE_2_VERIFY_TEMPLATE_INFO_CONVERTER);
        });
    }

    /**
     * delete verify template
     *
     * @param id
     * @return
     */
    @Override
    public Mono<VerifyTemplateInfo> deleteVerifyTemplate(Long id) {
        LOGGER.info("Mono<VerifyTemplateInfo> deleteVerifyTemplate(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return synchronizedProcessor.handleSupWithLock(VERIFY_TEMPLATE_UPDATE_SYNC.key, () ->
                verifyTemplateRepository.findById(id)
                        .publishOn(scheduler)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                        .flatMap(template -> verifyTemplateRepository.delete(template).then(just(template)))
                        .doOnSuccess(template -> REDIS_CACHE_DELETER.accept(template.getType(), template.getBusinessType()))
                        .map(VERIFY_TEMPLATE_2_VERIFY_TEMPLATE_INFO_CONVERTER)
        );
    }

    /**
     * get verify template mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<VerifyTemplate> getVerifyTemplateMono(Long id) {
        LOGGER.info("Mono<VerifyTemplate> getVerifyTemplateMono(Long id), id = {}", id);

        return verifyTemplateRepository.findById(id).publishOn(scheduler);
    }

    /**
     * get verify template mono by type and business type
     *
     * @param type
     * @return
     */
    @Override
    public Mono<VerifyTemplateInfo> getVerifyTemplateInfoMonoByType(String type, String businessType) {
        LOGGER.info("Mono<VerifyTemplateInfo> getVerifyTemplateInfoMonoByType(String type, String businessType), type = {}, businessType = {}", type, businessType);
        if (isNull(type))
            throw new BlueException(EMPTY_PARAM);

        return TEMPLATE_WITH_REDIS_CACHE_GETTER.apply(type, businessType);
    }

    /**
     * select verify template by limit and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    @Override
    public Mono<List<VerifyTemplate>> selectVerifyTemplateMonoByLimitAndCondition(Long limit, Long rows, Query query) {
        LOGGER.info("Mono<List<VerifyTemplate>> selectVerifyTemplateMonoByLimitAndCondition(Long limit, Long rows, Query query)," +
                " limit = {}, rows = {}, query = {}", limit, rows, query);

        if (isInvalidLimit(limit) || isInvalidRows(rows))
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, VerifyTemplate.class).publishOn(scheduler).collectList();
    }

    /**
     * count verify template by query
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countVerifyTemplateMonoByCondition(Query query) {
        LOGGER.info("Mono<Long> countVerifyTemplateMonoByCondition(Query query), query = {}", query);

        return reactiveMongoTemplate.count(query, VerifyTemplate.class).publishOn(scheduler);
    }

    /**
     * select verify template manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<VerifyTemplateManagerInfo>> selectVerifyTemplateManagerInfoPageMonoByPageAndCondition(PageModelRequest<VerifyTemplateCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<VerifyTemplateManagerInfo>> selectVerifyTemplateManagerInfoPageMonoByPageAndCondition(PageModelRequest<VerifyTemplateCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);

        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectVerifyTemplateMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), query), countVerifyTemplateMonoByCondition(query))
                .flatMap(tuple2 -> {
                    List<VerifyTemplate> templates = tuple2.getT1();
                    return isNotEmpty(templates) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(OPERATORS_GETTER.apply(templates))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndMemberNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));

                                        return just(templates.stream().map(t ->
                                                VERIFY_TEMPLATE_2_VERIFY_TEMPLATE_MANAGER_INFO_CONVERTER.apply(t, idAndMemberNameMapping)).collect(toList()));
                                    }).flatMap(configManagerInfos ->
                                            just(new PageModelResponse<>(configManagerInfos, tuple2.getT2())))
                            :
                            just(new PageModelResponse<>(emptyList(), tuple2.getT2()));
                });
    }

}
