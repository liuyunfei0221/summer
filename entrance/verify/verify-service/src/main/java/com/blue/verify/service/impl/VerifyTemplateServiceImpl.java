package com.blue.verify.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.common.SortType;
import com.blue.basic.constant.verify.VerifyBusinessType;
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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
import static com.blue.basic.common.message.InternationalProcessor.defaultLanguageIdentity;
import static com.blue.basic.common.message.InternationalProcessor.parseLanguageIdentity;
import static com.blue.basic.constant.common.CacheKeyPrefix.VERIFY_TEMPLATE_PRE;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.basic.constant.common.Symbol.HYPHEN;
import static com.blue.basic.constant.common.Symbol.PAR_CONCATENATION;
import static com.blue.basic.constant.common.SyncKey.VERIFY_TEMPLATE_UPDATE_SYNC;
import static com.blue.basic.constant.common.SyncKeyPrefix.VERIFY_TEMPLATES_UPDATE_SYNC_PRE;
import static com.blue.mongo.common.MongoSortProcessor.process;
import static com.blue.mongo.constant.LikeElement.PREFIX;
import static com.blue.mongo.constant.LikeElement.SUFFIX;
import static com.blue.verify.constant.VerifyTemplateColumnName.*;
import static com.blue.verify.converter.VerifyModelConverters.*;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static reactor.core.publisher.Mono.*;

/**
 * verify template service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class VerifyTemplateServiceImpl implements VerifyTemplateService {

    private static final Logger LOGGER = getLogger(VerifyTemplateServiceImpl.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final SynchronizedProcessor synchronizedProcessor;

    private RedissonClient redissonClient;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private VerifyTemplateRepository verifyTemplateRepository;

    public VerifyTemplateServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, ReactiveStringRedisTemplate reactiveStringRedisTemplate,
                                     BlueIdentityProcessor blueIdentityProcessor, SynchronizedProcessor synchronizedProcessor, RedissonClient redissonClient, ReactiveMongoTemplate reactiveMongoTemplate,
                                     VerifyTemplateRepository verifyTemplateRepository, VerifyTemplateDeploy verifyTemplateDeploy) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.synchronizedProcessor = synchronizedProcessor;
        this.redissonClient = redissonClient;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.verifyTemplateRepository = verifyTemplateRepository;

        Long cacheExpiresSecond = verifyTemplateDeploy.getCacheExpiresSecond();
        if (isNull(cacheExpiresSecond) || cacheExpiresSecond < 1L)
            throw new RuntimeException("cacheExpiresSecond can't be null or less than 1");

        this.expireDuration = Duration.of(cacheExpiresSecond, ChronoUnit.SECONDS);

        this.defaultMaxWaitingMillis = synchronizedProcessor.getDefaultMaxWaitingMillis();
    }

    private static final Map<String, Set<String>> BT_ALLOWED_VTS = Stream.of(VerifyBusinessType.values())
            .collect(toMap(bt -> bt.identity, bt -> bt.allowedVerifyTypes.stream().map(vt -> vt.identity).collect(toSet())));

    private static final BiConsumer<String, String> ALLOWED_ASSERTER = (businessType, verifyType) -> {
        if (isBlank(businessType) || isBlank(verifyType))
            throw new BlueException(INVALID_PARAM);

        Set<String> verifyTypes = BT_ALLOWED_VTS.get(businessType);
        if (isEmpty(verifyTypes) || !verifyTypes.contains(verifyType))
            throw new BlueException(UNSUPPORTED_OPERATE);
    };

    private Duration expireDuration;

    private long defaultMaxWaitingMillis;

    private static final BinaryOperator<String> TEMPLATE_CACHE_KEY_GENERATOR = (type, businessType) -> VERIFY_TEMPLATE_PRE.prefix + type + PAR_CONCATENATION.identity + businessType;

    private static final BinaryOperator<String> VERIFY_TEMPLATES_SYNC_KEY_GENERATOR = (type, businessType) -> VERIFY_TEMPLATES_UPDATE_SYNC_PRE.prefix + type + PAR_CONCATENATION.identity + businessType;

    private final BiConsumer<String, String> REDIS_CACHE_DELETER = (type, businessType) ->
            reactiveStringRedisTemplate.delete(TEMPLATE_CACHE_KEY_GENERATOR.apply(type, businessType))
                    .subscribe(size -> LOGGER.info("type = {}, businessType = {}, size = {}", type, businessType, size));

    private final BiFunction<String, String, Mono<Map<String, VerifyTemplateInfo>>> TEMPLATE_DB_GETTER = (type, businessType) -> {
        assertVerifyType(type, false);
        assertVerifyBusinessType(businessType, false);

        VerifyTemplate probe = new VerifyTemplate();
        probe.setType(type);
        probe.setBusinessType(businessType);

        return verifyTemplateRepository.findAll(Example.of(probe))
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .map(VERIFY_TEMPLATE_2_VERIFY_TEMPLATE_INFO_CONVERTER).collectList()
                .map(verifyTemplateInfos ->
                        verifyTemplateInfos.stream().collect(toMap(VerifyTemplateInfo::getLanguage, vt -> vt, (a, b) -> a)));
    };

    private final BiFunction<String, String, Mono<Map<String, VerifyTemplateInfo>>> TEMPLATE_REDIS_GETTER = (type, businessType) ->
            reactiveStringRedisTemplate.opsForHash().entries(TEMPLATE_CACHE_KEY_GENERATOR.apply(type, businessType)).collectList()
                    .map(entries -> entries.stream().collect(toMap(e -> String.valueOf(e.getKey()), e -> GSON.fromJson(String.valueOf(e.getValue()), VerifyTemplateInfo.class), (a, b) -> a)));

    private final BiFunction<String, Map<String, VerifyTemplateInfo>, Mono<Map<String, VerifyTemplateInfo>>> TEMPLATE_REDIS_WITH_RETURN_SETTER = (typeIdentity, verifyTemplateInfo) ->
            isNotBlank(typeIdentity) && isNotEmpty(verifyTemplateInfo) ?
                    reactiveStringRedisTemplate.opsForHash().putAll(typeIdentity,
                                    verifyTemplateInfo.entrySet().stream().filter(e -> isNotBlank(e.getKey()) && isNotNull(e.getValue())).collect(toMap(Map.Entry::getKey, e -> GSON.toJson(e.getValue()), (a, b) -> a)))
                            .then(reactiveStringRedisTemplate.expire(typeIdentity, expireDuration))
                            .then(just(verifyTemplateInfo))
                    :
                    error(() -> new BlueException(INVALID_PARAM));

    private final BiFunction<String, String, Mono<Map<String, VerifyTemplateInfo>>> TEMPLATE_WITH_REDIS_CACHE_GETTER = (type, businessType) -> {
        assertVerifyType(type, false);
        assertVerifyBusinessType(businessType, false);

        return TEMPLATE_REDIS_GETTER.apply(type, businessType)
                .filter(BlueChecker::isNotEmpty)
                .switchIfEmpty(defer(() -> {
                    RLock lock = null;
                    boolean tryLock = false;
                    try {
                        lock = redissonClient.getLock(VERIFY_TEMPLATES_SYNC_KEY_GENERATOR.apply(type, businessType));

                        tryLock = lock.tryLock();
                        if (tryLock)
                            return TEMPLATE_DB_GETTER.apply(type, businessType)
                                    .filter(BlueChecker::isNotEmpty)
                                    .switchIfEmpty(defer(() -> error(() -> new BlueException(INVALID_PARAM))))
                                    .flatMap(verifyTemplateInfo ->
                                            TEMPLATE_REDIS_WITH_RETURN_SETTER.apply(TEMPLATE_CACHE_KEY_GENERATOR.apply(type, businessType), verifyTemplateInfo));

                        long start = currentTimeMillis();
                        while (!(tryLock = lock.tryLock()) && currentTimeMillis() - start <= defaultMaxWaitingMillis)
                            onSpinWait();

                        return tryLock ?
                                TEMPLATE_REDIS_GETTER.apply(type, businessType).switchIfEmpty(defer(() -> TEMPLATE_DB_GETTER.apply(type, businessType)))
                                :
                                TEMPLATE_DB_GETTER.apply(type, businessType);
                    } catch (Exception e) {
                        LOGGER.error("handle failed, type = {}, businessType = {}, e = {}", type, businessType, e);
                        throw new BlueException(INTERNAL_SERVER_ERROR);
                    } finally {
                        if (isNotNull(lock) && tryLock)
                            try {
                                lock.unlock();
                            } catch (Exception e) {
                                LOGGER.warn("lock.unlock() failed, e = {}", e.getMessage());
                            }
                    }
                }));
    };

    private static final BiFunction<Map<String, VerifyTemplateInfo>, List<String>, VerifyTemplateInfo> TEMPLATE_GETTER = (templateMap, languages) -> {
        if (isEmpty(templateMap))
            throw new BlueException(INVALID_PARAM);

        if (isNotEmpty(languages)) {
            VerifyTemplateInfo verifyTemplateInfo;
            for (String language : languages)
                if (isNotNull(verifyTemplateInfo = templateMap.get(lowerCase(language))))
                    return verifyTemplateInfo;
        }

        return ofNullable(templateMap.get(defaultLanguageIdentity()))
                .orElseGet(() -> templateMap.values().stream().min(comparing(VerifyTemplateInfo::getPriority))
                        .orElseThrow(() -> new BlueException(INTERNAL_SERVER_ERROR)));
    };

    private final Consumer<VerifyTemplateInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        VerifyTemplate probe = new VerifyTemplate();
        probe.setType(p.getType());
        probe.setBusinessType(p.getBusinessType());
        probe.setLanguage(lowerCase(replace(p.getLanguage(), PAR_CONCATENATION.identity, HYPHEN.identity)));

        if (ofNullable(verifyTemplateRepository.count(Example.of(probe)).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(DATA_ALREADY_EXIST);
    };

    private final Function<VerifyTemplateUpdateParam, VerifyTemplate> UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long id = p.getId();

        VerifyTemplate verifyTemplate = verifyTemplateRepository.findById(id).toFuture().join();
        if (isNull(verifyTemplate))
            throw new BlueException(DATA_NOT_EXIST);

        VerifyTemplate probe = new VerifyTemplate();
        probe.setType(p.getType());
        probe.setBusinessType(p.getBusinessType());
        probe.setLanguage(p.getLanguage());

        List<VerifyTemplate> templates = ofNullable(verifyTemplateRepository.findAll(Example.of(probe)).collectList()
                .toFuture().join()).orElseGet(Collections::emptyList);

        if (templates.stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(DATA_ALREADY_EXIST);

        return verifyTemplate;
    };

    public static final BiConsumer<VerifyTemplateUpdateParam, VerifyTemplate> UPDATE_ITEM_WITH_ASSERT_PACKAGER = (p, t) -> {
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

        String type = p.getType();
        if (isNotNull(type) && !type.equals(t.getType())) {
            assertVerifyType(type, false);
            t.setType(type);
            alteration = true;
        }

        String businessType = p.getBusinessType();
        if (isNotNull(businessType) && !businessType.equals(t.getBusinessType())) {
            assertVerifyBusinessType(businessType, false);
            t.setBusinessType(businessType);
            alteration = true;
        }

        String language = ofNullable(p.getLanguage())
                .map(l -> lowerCase(parseLanguageIdentity(l)))
                .orElse(EMPTY_VALUE.value);
        if (isNotBlank(language) && !language.equals(t.getLanguage())) {
            t.setLanguage(language);
            alteration = true;
        }

        Integer priority = p.getPriority();
        if (isNotNull(priority) && !priority.equals(t.getPriority())) {
            t.setPriority(priority);
            alteration = true;
        }

        String title = p.getTitle();
        if (isNotBlank(title) && !title.equals(t.getTitle())) {
            t.setTitle(title);
            alteration = true;
        }

        String content = p.getContent();
        if (isNotBlank(content) && !content.equals(t.getContent())) {
            t.setContent(content);
            alteration = true;
        }

        if (!alteration)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        t.setUpdateTime(TIME_STAMP_GETTER.get());
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

        return process(
                sortAttribute.equals(VerifyTemplateSortAttribute.ID.column) ?
                        singletonList(new SortElement(sortAttribute, sortType))
                        :
                        Stream.of(sortAttribute, VerifyTemplateSortAttribute.ID.column)
                                .map(attr -> new SortElement(attr, sortType)).collect(toList())
        );
    };

    private static final Function<VerifyTemplateCondition, Query> CONDITION_PROCESSOR = c -> {
        Query query = new Query();

        if (isNull(c)) {
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
        ofNullable(c.getLanguage()).filter(BlueChecker::isNotBlank).ifPresent(probe::setLanguage);

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

    /**
     * insert verify template
     *
     * @param verifyTemplateInsertParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<VerifyTemplateInfo> insertVerifyTemplate(VerifyTemplateInsertParam verifyTemplateInsertParam, Long operatorId) {
        LOGGER.info("verifyTemplateInsertParam = {}, operatorId = {}", verifyTemplateInsertParam, operatorId);
        if (isNull(verifyTemplateInsertParam))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        return synchronizedProcessor.handleSupWithSync(VERIFY_TEMPLATE_UPDATE_SYNC.key, () -> {
            INSERT_ITEM_VALIDATOR.accept(verifyTemplateInsertParam);
            VerifyTemplate verifyTemplate = VERIFY_TEMPLATE_INSERT_PARAM_2_VERIFY_TEMPLATE_CONVERTER.apply(verifyTemplateInsertParam);

            verifyTemplate.setId(blueIdentityProcessor.generate(VerifyTemplate.class));
            verifyTemplate.setCreator(operatorId);
            verifyTemplate.setUpdater(operatorId);

            return verifyTemplateRepository.insert(verifyTemplate)
                    .doOnSuccess(template -> REDIS_CACHE_DELETER.accept(template.getType(), template.getBusinessType()))
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
        LOGGER.info("verifyTemplateUpdateParam = {}, operatorId = {}", verifyTemplateUpdateParam, operatorId);
        if (isNull(verifyTemplateUpdateParam))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        return synchronizedProcessor.handleSupWithSync(VERIFY_TEMPLATE_UPDATE_SYNC.key, () -> {
            VerifyTemplate verifyTemplate = UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER.apply(verifyTemplateUpdateParam);
            String originalType = verifyTemplate.getType();
            String originalBusinessType = verifyTemplate.getBusinessType();

            UPDATE_ITEM_WITH_ASSERT_PACKAGER.accept(verifyTemplateUpdateParam, verifyTemplate);
            verifyTemplate.setUpdater(operatorId);

            return verifyTemplateRepository.save(verifyTemplate)
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
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return synchronizedProcessor.handleSupWithSync(VERIFY_TEMPLATE_UPDATE_SYNC.key, () ->
                verifyTemplateRepository.findById(id)
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
    public Mono<VerifyTemplate> getVerifyTemplate(Long id) {
        LOGGER.info("id = {}", id);

        return verifyTemplateRepository.findById(id);
    }

    /**
     * get verify template mono by type and business type and languages
     *
     * @param type
     * @param businessType
     * @param languages
     * @return
     */
    @Override
    public Mono<VerifyTemplateInfo> getVerifyTemplateInfoByTypesAndLanguages(String type, String businessType, List<String> languages) {
        LOGGER.info("type = {}, businessType = {}, languages = {}", type, businessType, languages);
        assertVerifyType(type, false);
        assertVerifyBusinessType(businessType, false);

        ALLOWED_ASSERTER.accept(businessType, type);

        return TEMPLATE_WITH_REDIS_CACHE_GETTER.apply(type, businessType)
                .map(templateMap -> TEMPLATE_GETTER.apply(templateMap, languages))
                .switchIfEmpty(defer(() -> error(() -> new BlueException(INTERNAL_SERVER_ERROR))));
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
    public Mono<List<VerifyTemplate>> selectVerifyTemplateByLimitAndCondition(Long limit, Long rows, Query query) {
        LOGGER.info("limit = {}, rows = {}, query = {}", limit, rows, query);
        if (isInvalidLimit(limit) || isInvalidRows(rows))
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, VerifyTemplate.class).collectList();
    }

    /**
     * count verify template by query
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countVerifyTemplateByCondition(Query query) {
        LOGGER.info("query = {}", query);
        if (isNull(query))
            return error(() -> new BlueException(EMPTY_PARAM));

        return reactiveMongoTemplate.count(query, VerifyTemplate.class);
    }

    /**
     * select verify template manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<VerifyTemplateManagerInfo>> selectVerifyTemplateManagerInfoPageByPageAndCondition(PageModelRequest<VerifyTemplateCondition> pageModelRequest) {
        LOGGER.info("pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectVerifyTemplateByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), query), countVerifyTemplateByCondition(query))
                .flatMap(tuple2 -> {
                    List<VerifyTemplate> templates = tuple2.getT1();
                    Long count = tuple2.getT2();
                    return isNotEmpty(templates) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(OPERATORS_GETTER.apply(templates))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndMemberNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));

                                        return just(templates.stream().map(t ->
                                                VERIFY_TEMPLATE_2_VERIFY_TEMPLATE_MANAGER_INFO_CONVERTER.apply(t, idAndMemberNameMapping)).collect(toList()));
                                    }).flatMap(configManagerInfos ->
                                            just(new PageModelResponse<>(configManagerInfos, count)))
                            :
                            just(new PageModelResponse<>(emptyList(), count));
                });
    }

}
