package com.blue.media.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.common.SortType;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.common.SortElement;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.media.api.model.MessageTemplateInfo;
import com.blue.media.api.model.MessageTemplateManagerInfo;
import com.blue.media.config.deploy.MessageTemplateDeploy;
import com.blue.media.constant.MessageTemplateSortAttribute;
import com.blue.media.constant.QrCodeConfigSortAttribute;
import com.blue.media.model.MessageTemplateCondition;
import com.blue.media.model.MessageTemplateInsertParam;
import com.blue.media.model.MessageTemplateUpdateParam;
import com.blue.media.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.media.repository.entity.MessageTemplate;
import com.blue.media.repository.entity.QrCodeConfig;
import com.blue.media.repository.template.MessageTemplateRepository;
import com.blue.media.service.inter.MessageTemplateService;
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
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.assertMessageBusinessType;
import static com.blue.basic.common.base.ConstantProcessor.assertMessageType;
import static com.blue.basic.constant.common.CacheKeyPrefix.VERIFY_TEMPLATE_PRE;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Symbol.PAR_CONCATENATION;
import static com.blue.basic.constant.common.Symbol.TEXT_PLACEHOLDER;
import static com.blue.basic.constant.common.SyncKey.MESSAGE_TEMPLATE_UPDATE_SYNC;
import static com.blue.media.constant.MessageTemplateColumnName.*;
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
 * message template service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {

    private static final Logger LOGGER = Loggers.getLogger(MessageTemplateServiceImpl.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final SynchronizedProcessor synchronizedProcessor;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private Scheduler scheduler;

    private MessageTemplateRepository messageTemplateRepository;

    public MessageTemplateServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, ReactiveStringRedisTemplate reactiveStringRedisTemplate,
                                      BlueIdentityProcessor blueIdentityProcessor, SynchronizedProcessor synchronizedProcessor,
                                      ReactiveMongoTemplate reactiveMongoTemplate, Scheduler scheduler, MessageTemplateRepository messageTemplateRepository, MessageTemplateDeploy messageTemplateDeploy) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.synchronizedProcessor = synchronizedProcessor;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.scheduler = scheduler;
        this.messageTemplateRepository = messageTemplateRepository;

        Long cacheExpiresSecond = messageTemplateDeploy.getCacheExpiresSecond();
        if (isNull(cacheExpiresSecond) || cacheExpiresSecond < 1L)
            throw new RuntimeException("cacheExpiresSecond can't be null or less than 1");

        this.expireDuration = Duration.of(cacheExpiresSecond, ChronoUnit.SECONDS);
    }

    private Duration expireDuration;

    private static final BiFunction<Integer, Integer, String> INFO_CACHE_KEY_GENERATOR = (type, businessType) -> VERIFY_TEMPLATE_PRE.prefix + type + PAR_CONCATENATION.identity + businessType;

    private final BiConsumer<Integer, Integer> REDIS_CACHE_DELETER = (type, businessType) ->
            reactiveStringRedisTemplate.delete(INFO_CACHE_KEY_GENERATOR.apply(type, businessType))
                    .subscribe(size -> LOGGER.info("REDIS_CACHE_DELETER, type = {}, businessType = {}, size = {}", type, businessType, size));

    private final BiFunction<Integer, Integer, Mono<MessageTemplateInfo>> INFO_DB_GETTER = (type, businessType) -> {
        assertMessageType(type, false);
        assertMessageBusinessType(businessType, false);

        MessageTemplate probe = new MessageTemplate();
        probe.setType(type);
        probe.setBusinessType(businessType);

        return messageTemplateRepository.findOne(Example.of(probe)).publishOn(scheduler)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .map(MESSAGE_TEMPLATE_2_MESSAGE_TEMPLATE_INFO_CONVERTER);
    };

    private final BiFunction<Integer, Integer, Mono<MessageTemplateInfo>> INFO_WITH_REDIS_CACHE_GETTER = (type, businessType) -> {
        assertMessageType(type, false);
        assertMessageBusinessType(businessType, false);

        String key = INFO_CACHE_KEY_GENERATOR.apply(type, businessType);

        return reactiveStringRedisTemplate.opsForValue().get(key)
                .map(s -> GSON.fromJson(s, MessageTemplateInfo.class))
                .switchIfEmpty(defer(() ->
                        INFO_DB_GETTER.apply(type, businessType)
                                .flatMap(messageTemplateInfo ->
                                        reactiveStringRedisTemplate.opsForValue().set(key, GSON.toJson(messageTemplateInfo), expireDuration)
                                                .flatMap(success -> {
                                                    LOGGER.info("reactiveStringRedisTemplate.opsForValue().set(key, GSON.toJson(messageTemplateInfo), expireDuration), success = {}", success);
                                                    return just(messageTemplateInfo);
                                                }))
                ));
    };

    private final Consumer<MessageTemplateInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        MessageTemplate probe = new MessageTemplate();
        probe.setType(p.getType());
        probe.setBusinessType(p.getBusinessType());

        if (ofNullable(messageTemplateRepository.count(Example.of(probe)).publishOn(scheduler).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(DATA_ALREADY_EXIST);
    };

    private final Function<MessageTemplateUpdateParam, MessageTemplate> UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long id = p.getId();

        MessageTemplate probe = new MessageTemplate();
        probe.setType(p.getType());
        probe.setBusinessType(p.getBusinessType());

        List<MessageTemplate> configs = ofNullable(messageTemplateRepository.findAll(Example.of(probe)).collectList()
                .publishOn(scheduler).toFuture().join())
                .orElseGet(Collections::emptyList);

        if (configs.stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(DATA_ALREADY_EXIST);

        MessageTemplate messageTemplate = messageTemplateRepository.findById(id).publishOn(scheduler).toFuture().join();
        if (isNull(messageTemplate))
            throw new BlueException(DATA_NOT_EXIST);

        return messageTemplate;
    };

    public static final BiConsumer<MessageTemplateUpdateParam, MessageTemplate> UPDATE_ITEM_WITH_ASSERT_PACKAGER = (p, t) -> {
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

        Integer businessType = p.getBusinessType();
        if (isNotNull(businessType) && !businessType.equals(t.getType())) {
            t.setBusinessType(businessType);
            alteration = true;
        }

        String title = p.getTitle();
        if (isNotBlank(title) && !title.equals(t.getTitle())) {
            t.setTitle(title);
            t.setTitlePlaceholderCount(countMatches(title, TEXT_PLACEHOLDER.identity));
            alteration = true;
        }

        String content = p.getContent();
        if (isNotBlank(content) && !content.equals(t.getContent())) {
            t.setContent(content);
            t.setContentPlaceholderCount(countMatches(content, TEXT_PLACEHOLDER.identity));
            alteration = true;
        }

        if (!alteration)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        t.setUpdateTime(TIME_STAMP_GETTER.get());
    };

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(MessageTemplateSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Function<MessageTemplateCondition, Sort> SORTER_CONVERTER = c -> {
        String sortAttribute = ofNullable(c).map(MessageTemplateCondition::getSortAttribute)
                .map(SORT_ATTRIBUTE_MAPPING::get)
                .filter(BlueChecker::isNotBlank)
                .orElse(QrCodeConfigSortAttribute.CREATE_TIME.column);

        String sortType = ofNullable(c).map(MessageTemplateCondition::getSortType)
                .filter(BlueChecker::isNotBlank)
                .orElse(SortType.DESC.identity);

        return sortAttribute.equals(QrCodeConfigSortAttribute.ID.column) ?
                process(singletonList(new SortElement(sortAttribute, sortType)))
                :
                process(Stream.of(sortAttribute, QrCodeConfigSortAttribute.ID.column)
                        .map(attr -> new SortElement(attr, sortType)).collect(toList()));
    };

    private static final Function<MessageTemplateCondition, Query> CONDITION_PROCESSOR = c -> {
        Query query = new Query();

        if (c == null) {
            query.with(SORTER_CONVERTER.apply(new MessageTemplateCondition()));
            return query;
        }

        MessageTemplate probe = new MessageTemplate();

        ofNullable(c.getId()).ifPresent(probe::setId);
        ofNullable(c.getType()).ifPresent(probe::setType);
        ofNullable(c.getBusinessType()).ifPresent(probe::setBusinessType);
        ofNullable(c.getCreator()).ifPresent(probe::setCreator);
        ofNullable(c.getUpdater()).ifPresent(probe::setUpdater);

        query.addCriteria(byExample(probe));

        ofNullable(c.getNameLike()).ifPresent(nameLike ->
                query.addCriteria(where(NAME.name).regex(compile(PREFIX.element + nameLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getTitleLike()).ifPresent(titleLike ->
                query.addCriteria(where(TITLE.name).regex(compile(PREFIX.element + titleLike + SUFFIX.element, CASE_INSENSITIVE))));

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

    private static final Function<List<MessageTemplate>, List<Long>> OPERATORS_GETTER = ms -> {
        Set<Long> operatorIds = new HashSet<>(ms.size());

        for (MessageTemplate m : ms) {
            operatorIds.add(m.getCreator());
            operatorIds.add(m.getUpdater());
        }

        return new ArrayList<>(operatorIds);
    };

    /**
     * insert message template
     *
     * @param messageTemplateInsertParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<MessageTemplateInfo> insertMessageTemplate(MessageTemplateInsertParam messageTemplateInsertParam, Long operatorId) {
        LOGGER.info("Mono<MessageTemplateInfo> insertMessageTemplate(MessageTemplateInsertParam messageTemplateInsertParam, Long operatorId), messageTemplateInsertParam = {}, operatorId = {}",
                messageTemplateInsertParam, operatorId);
        if (isNull(messageTemplateInsertParam))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        return synchronizedProcessor.handleSupWithSync(MESSAGE_TEMPLATE_UPDATE_SYNC.key, () -> {
            INSERT_ITEM_VALIDATOR.accept(messageTemplateInsertParam);
            MessageTemplate messageTemplate = MESSAGE_TEMPLATE_INSERT_PARAM_2_MESSAGE_TEMPLATE_CONVERTER.apply(messageTemplateInsertParam);

            messageTemplate.setId(blueIdentityProcessor.generate(QrCodeConfig.class));
            messageTemplate.setCreator(operatorId);
            messageTemplate.setUpdater(operatorId);

            return messageTemplateRepository.insert(messageTemplate)
                    .publishOn(scheduler)
                    .map(MESSAGE_TEMPLATE_2_MESSAGE_TEMPLATE_INFO_CONVERTER);
        });
    }

    /**
     * update message template
     *
     * @param messageTemplateUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<MessageTemplateInfo> updateMessageTemplate(MessageTemplateUpdateParam messageTemplateUpdateParam, Long operatorId) {
        LOGGER.info("Mono<MessageTemplateInfo> updateMessageTemplate(MessageTemplateUpdateParam messageTemplateUpdateParam, Long operatorId), messageTemplateUpdateParam = {}, operatorId = {]",
                messageTemplateUpdateParam, operatorId);
        if (isNull(messageTemplateUpdateParam))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        return synchronizedProcessor.handleSupWithSync(MESSAGE_TEMPLATE_UPDATE_SYNC.key, () -> {
            MessageTemplate messageTemplate = UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER.apply(messageTemplateUpdateParam);
            Integer originalType = messageTemplate.getType();
            Integer originalBusinessType = messageTemplate.getBusinessType();

            UPDATE_ITEM_WITH_ASSERT_PACKAGER.accept(messageTemplateUpdateParam, messageTemplate);
            messageTemplate.setUpdater(operatorId);

            return messageTemplateRepository.save(messageTemplate)
                    .publishOn(scheduler)
                    .doOnSuccess(config -> {
                        Integer tarType = config.getType();
                        Integer tarBusinessType = config.getBusinessType();
                        REDIS_CACHE_DELETER.accept(tarType, tarBusinessType);

                        if (!originalType.equals(tarType) || !originalBusinessType.equals(tarBusinessType))
                            REDIS_CACHE_DELETER.accept(originalType, originalBusinessType);
                    })
                    .map(MESSAGE_TEMPLATE_2_MESSAGE_TEMPLATE_INFO_CONVERTER);
        });
    }

    /**
     * delete message template
     *
     * @param id
     * @return
     */
    @Override
    public Mono<MessageTemplateInfo> deleteMessageTemplate(Long id) {
        LOGGER.info("Mono<MessageTemplateInfo> deleteMessageTemplate(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return synchronizedProcessor.handleSupWithSync(MESSAGE_TEMPLATE_UPDATE_SYNC.key, () ->
                messageTemplateRepository.findById(id)
                        .publishOn(scheduler)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                        .flatMap(template -> messageTemplateRepository.delete(template).then(just(template)))
                        .doOnSuccess(template -> REDIS_CACHE_DELETER.accept(template.getType(), template.getBusinessType()))
                        .map(MESSAGE_TEMPLATE_2_MESSAGE_TEMPLATE_INFO_CONVERTER)
        );
    }

    /**
     * get message template mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<MessageTemplate> getMessageTemplateMono(Long id) {
        LOGGER.info("Mono<MessageTemplate> getMessageTemplateMono(Long id), id = {}", id);

        return messageTemplateRepository.findById(id).publishOn(scheduler);
    }

    /**
     * get message template mono by types
     *
     * @param type
     * @param businessType
     * @return
     */
    @Override
    public Mono<MessageTemplateInfo> getMessageTemplateInfoMonoByTypes(Integer type, Integer businessType) {
        LOGGER.info("Mono<QrCodeConfig> getQrCodeConfigMonoByType(Integer type), type = {}", type);

        return INFO_WITH_REDIS_CACHE_GETTER.apply(type, businessType);
    }

    /**
     * select message template by limit and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    @Override
    public Mono<List<MessageTemplate>> selectMessageTemplateMonoByLimitAndCondition(Long limit, Long rows, Query query) {
        LOGGER.info("Mono<List<MessageTemplate>> selectMessageTemplateMonoByLimitAndCondition(Long limit, Long rows, Query query)," +
                " limit = {}, rows = {}, query = {}", limit, rows, query);

        if (isInvalidLimit(limit) || isInvalidRows(rows))
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, MessageTemplate.class).publishOn(scheduler).collectList();
    }

    /**
     * count message template by query
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countMessageTemplateMonoByCondition(Query query) {
        LOGGER.info("Mono<Long> countMessageTemplateMonoByCondition(Query query), query = {}", query);

        return reactiveMongoTemplate.count(query, MessageTemplate.class).publishOn(scheduler);
    }

    /**
     * select message template manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<MessageTemplateManagerInfo>> selectMessageTemplateManagerInfoPageMonoByPageAndCondition(PageModelRequest<MessageTemplateCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<MessageTemplateManagerInfo>> selectMessageTemplateManagerInfoPageMonoByPageAndCondition(PageModelRequest<MessageTemplateCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectMessageTemplateMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), query), countMessageTemplateMonoByCondition(query))
                .flatMap(tuple2 -> {
                    List<MessageTemplate> templates = tuple2.getT1();
                    Long count = tuple2.getT2();
                    return isNotEmpty(templates) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(OPERATORS_GETTER.apply(templates))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(templates.stream().map(m ->
                                                        MESSAGE_TEMPLATE_2_MESSAGE_TEMPLATE_MANAGER_INFO_CONVERTER.apply(m, idAndNameMapping))
                                                .collect(toList()));
                                    }).flatMap(messageTemplateManagerInfos ->
                                            just(new PageModelResponse<>(messageTemplateManagerInfos, count)))
                            :
                            just(new PageModelResponse<>(emptyList(), count));
                });
    }

}
