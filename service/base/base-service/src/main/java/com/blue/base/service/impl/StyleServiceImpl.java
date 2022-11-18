package com.blue.base.service.impl;

import com.blue.base.api.model.StyleInfo;
import com.blue.base.config.blue.BlueRedisConfig;
import com.blue.base.config.deploy.CaffeineDeploy;
import com.blue.base.constant.StyleSortAttribute;
import com.blue.base.model.StyleCondition;
import com.blue.base.model.StyleInsertParam;
import com.blue.base.model.StyleManagerInfo;
import com.blue.base.model.StyleUpdateParam;
import com.blue.base.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.base.repository.entity.Style;
import com.blue.base.repository.mapper.StyleMapper;
import com.blue.base.service.inter.StyleService;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.portal.StyleType;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConf;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.redisson.component.SynchronizedProcessor;
import com.github.benmanes.caffeine.cache.AsyncCache;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.*;
import java.util.stream.Stream;

import static com.blue.base.converter.BaseModelConverters.*;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.assertBulletinType;
import static com.blue.basic.common.base.ConstantProcessor.assertStyleType;
import static com.blue.basic.constant.common.BlueBoolean.FALSE;
import static com.blue.basic.constant.common.BlueBoolean.TRUE;
import static com.blue.basic.constant.common.CacheKeyPrefix.ACTIVE_STYLE_PRE;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Symbol.PERCENT;
import static com.blue.basic.constant.common.SyncKeyPrefix.STYLES_UPDATE_SYNC_PRE;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCacheAsyncCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static com.blue.database.common.ConditionSortProcessor.process;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.*;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.*;

/**
 * style service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
@Service
public class StyleServiceImpl implements StyleService {

    private static final Logger LOGGER = Loggers.getLogger(StyleServiceImpl.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private SynchronizedProcessor synchronizedProcessor;

    private StringRedisTemplate stringRedisTemplate;

    private StyleMapper styleMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public StyleServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, BlueIdentityProcessor blueIdentityProcessor,
                            SynchronizedProcessor synchronizedProcessor, StringRedisTemplate stringRedisTemplate, StyleMapper styleMapper,
                            ExecutorService executorService, BlueRedisConfig blueRedisConfig, CaffeineDeploy caffeineDeploy) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.synchronizedProcessor = synchronizedProcessor;
        this.stringRedisTemplate = stringRedisTemplate;
        this.styleMapper = styleMapper;

        this.expireDuration = Duration.of(blueRedisConfig.getEntryTtl(), SECONDS);

        CaffeineConf caffeineConf = new CaffeineConfParams(
                StyleType.values().length, Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_WRITE, executorService);

        typeStyleInfoCache = generateCacheAsyncCache(caffeineConf);
        of(StyleType.values()).map(e -> e.identity)
                .forEach(t -> ACTIVE_STYLE_INFO_WITH_ALL_CACHE_GETTER.apply(t).join());
    }

    private Duration expireDuration;

    private AsyncCache<Integer, StyleInfo> typeStyleInfoCache;

    private static final Function<Integer, String> STYLE_CACHE_KEY_GENERATOR = t -> ACTIVE_STYLE_PRE.prefix + t;

    private static final Function<Integer, String> STYLES_UPDATE_SYNC_KEY_GEN = t -> STYLES_UPDATE_SYNC_PRE.prefix + t;

    private final Consumer<Integer> REDIS_CACHE_DELETER = type ->
            stringRedisTemplate.delete(STYLE_CACHE_KEY_GENERATOR.apply(type));

    private final Consumer<Integer> CACHE_DELETER = type -> {
        REDIS_CACHE_DELETER.accept(type);
        typeStyleInfoCache.synchronous().invalidate(type);
    };

    private final Function<Integer, Style> ACTIVE_STYLE_DB_GETTER = t -> {
        assertStyleType(t, false);

        List<Style> activeStyles = this.selectStyleByTypeAndActive(t, TRUE.bool);
        LOGGER.info("activeStyles = {}, t = {}", activeStyles, t);

        if (isEmpty(activeStyles))
            throw new BlueException(DATA_NOT_EXIST);

        if (activeStyles.size() > 1)
            LOGGER.error("active style by type more than 1, t = {}", t);

        return activeStyles.get(0);
    };

    private final Function<Integer, StyleInfo> ACTIVE_STYLE_INFO_DB_GETTER = t ->
            STYLE_2_STYLE_INFO_CONVERTER.apply(ACTIVE_STYLE_DB_GETTER.apply(t));

    private final Function<Integer, StyleInfo> ACTIVE_STYLE_INFO_REDIS_GETTER = t ->
            ofNullable(stringRedisTemplate.opsForValue().get(STYLE_CACHE_KEY_GENERATOR.apply(t)))
                    .map(s -> GSON.fromJson(s, StyleInfo.class)).orElse(null);

    private final BiConsumer<Integer, StyleInfo> ACTIVE_STYLE_INFO_REDIS_SETTER = (t, si) -> {
        REDIS_CACHE_DELETER.accept(t);
        stringRedisTemplate.opsForValue().set(STYLE_CACHE_KEY_GENERATOR.apply(t), GSON.toJson(si), expireDuration);
    };

    private final BiFunction<Integer, Executor, CompletableFuture<StyleInfo>> ACTIVE_STYLE_INFO_WITH_REDIS_CACHE_GETTER = (t, executor) ->
            supplyAsync(() -> synchronizedProcessor.handleSupByOrderedWithSetter(STYLES_UPDATE_SYNC_KEY_GEN.apply(t),
                    () -> ACTIVE_STYLE_INFO_REDIS_GETTER.apply(t), () -> ACTIVE_STYLE_INFO_DB_GETTER.apply(t),
                    bulletinInfos -> ACTIVE_STYLE_INFO_REDIS_SETTER.accept(t, bulletinInfos), BlueChecker::isNotNull), executor);

    private final Function<Integer, CompletableFuture<StyleInfo>> ACTIVE_STYLE_INFO_WITH_ALL_CACHE_GETTER = t -> {
        assertStyleType(t, false);

        return typeStyleInfoCache.get(t, ACTIVE_STYLE_INFO_WITH_REDIS_CACHE_GETTER);
    };

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(StyleSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<StyleCondition> CONDITION_PROCESSOR = c -> {
        StyleCondition sc = isNotNull(c) ? c : new StyleCondition();

        process(sc, SORT_ATTRIBUTE_MAPPING, StyleSortAttribute.CREATE_TIME.column);

        ofNullable(sc.getNameLike())
                .filter(StringUtils::hasText).ifPresent(titleLike -> sc.setNameLike(PERCENT.identity + titleLike + PERCENT.identity));

        return sc;
    };

    private static final Function<List<Style>, List<Long>> OPERATORS_GETTER = styles -> {
        Set<Long> operatorIds = new HashSet<>(styles.size());

        for (Style s : styles) {
            operatorIds.add(s.getCreator());
            operatorIds.add(s.getUpdater());
        }

        return new ArrayList<>(operatorIds);
    };

    private final Consumer<StyleInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        if (isNotNull(styleMapper.selectByName(p.getName())))
            throw new BlueException(RESOURCE_NAME_ALREADY_EXIST);
    };

    private final Function<StyleUpdateParam, Style> UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long id = p.getId();

        ofNullable(p.getName())
                .filter(BlueChecker::isNotBlank)
                .map(styleMapper::selectByName)
                .map(Style::getId)
                .ifPresent(eid -> {
                    if (!id.equals(eid))
                        throw new BlueException(STYLE_NAME_ALREADY_EXIST, new String[]{p.getName()});
                });

        Style style = styleMapper.selectByPrimaryKey(id);
        if (isNull(style))
            throw new BlueException(DATA_NOT_EXIST);

        return style;
    };

    public static final BiConsumer<StyleUpdateParam, Style> UPDATE_ITEM_WITH_ASSERT_PACKAGER = (p, t) -> {
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

        String attributes = p.getAttributes();
        if (isNotBlank(attributes) && !attributes.equals(t.getAttributes())) {
            t.setAttributes(attributes);
            alteration = true;
        }

        Integer type = p.getType();
        assertStyleType(type, true);
        if (type != null && !type.equals(t.getType())) {
            t.setType(type);
            alteration = true;
        }

        if (!alteration)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        t.setUpdateTime(TIME_STAMP_GETTER.get());
    };

    /**
     * insert style
     *
     * @param styleInsertParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public StyleInfo insertStyle(StyleInsertParam styleInsertParam, Long operatorId) {
        LOGGER.info("styleInsertParam = {}, operatorId = {}", styleInsertParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        INSERT_ITEM_VALIDATOR.accept(styleInsertParam);

        Style style = STYLE_INSERT_PARAM_2_STYLE_CONVERTER.apply(styleInsertParam);

        style.setId(blueIdentityProcessor.generate(Style.class));
        style.setCreator(operatorId);
        style.setUpdater(operatorId);

        styleMapper.insert(style);
        CACHE_DELETER.accept(style.getType());

        return STYLE_2_STYLE_INFO_CONVERTER.apply(style);
    }

    /**
     * update style
     *
     * @param styleUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public StyleInfo updateStyle(StyleUpdateParam styleUpdateParam, Long operatorId) {
        LOGGER.info("styleUpdateParam = {}, operatorId = {}", styleUpdateParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        Style style = UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER.apply(styleUpdateParam);

        List<Integer> changedTypes = new LinkedList<>();
        changedTypes.add(style.getType());

        UPDATE_ITEM_WITH_ASSERT_PACKAGER.accept(styleUpdateParam, style);
        changedTypes.add(style.getType());

        style.setUpdater(operatorId);

        styleMapper.updateByPrimaryKeySelective(style);
        changedTypes.forEach(CACHE_DELETER);

        return STYLE_2_STYLE_INFO_CONVERTER.apply(style);
    }

    /**
     * delete style
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public StyleInfo deleteStyle(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        Style style = styleMapper.selectByPrimaryKey(id);
        if (isNull(style))
            throw new BlueException(DATA_NOT_EXIST);
        if (style.getIsActive())
            throw new BlueException(STYLE_STILL_ACTIVE);

        styleMapper.deleteByPrimaryKey(id);
        CACHE_DELETER.accept(style.getType());

        return STYLE_2_STYLE_INFO_CONVERTER.apply(style);
    }

    /**
     * update active style by id
     *
     * @param id
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public StyleManagerInfo updateActiveStyle(Long id, Long operatorId) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        Style newActiveStyle = styleMapper.selectByPrimaryKey(id);
        if (isNull(newActiveStyle))
            throw new BlueException(DATA_NOT_EXIST);

        Integer type = newActiveStyle.getType();

        Style oldActiveStyle = ACTIVE_STYLE_DB_GETTER.apply(type);
        if (isNotNull(oldActiveStyle) && newActiveStyle.getId().equals(oldActiveStyle.getId()))
            throw new BlueException(STYLE_ALREADY_ACTIVE);

        Long stamp = TIME_STAMP_GETTER.get();

        newActiveStyle.setIsActive(TRUE.bool);
        newActiveStyle.setUpdater(operatorId);
        newActiveStyle.setUpdateTime(stamp);

        oldActiveStyle.setIsActive(FALSE.bool);
        oldActiveStyle.setUpdater(operatorId);
        oldActiveStyle.setUpdateTime(stamp);

        CACHE_DELETER.accept(type);
        styleMapper.updateByPrimaryKey(newActiveStyle);
        styleMapper.updateByPrimaryKey(oldActiveStyle);
        CACHE_DELETER.accept(type);

        Map<Long, String> idAndMemberNameMapping;
        try {
            idAndMemberNameMapping = rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(OPERATORS_GETTER.apply(singletonList(newActiveStyle)))
                    .toFuture().join().parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
        } catch (Exception e) {
            LOGGER.error("generate idAndNameMapping failed, e = {}", e);
            idAndMemberNameMapping = emptyMap();
        }

        return STYLE_2_STYLE_MANAGER_INFO_CONVERTER.apply(newActiveStyle, idAndMemberNameMapping);
    }

    /**
     * expire style info
     *
     * @return
     */
    @Override
    public void invalidStyleInfosCache() {
        of(StyleType.values())
                .forEach(type ->
                        CACHE_DELETER.accept(type.identity));
    }

    /**
     * get style mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Style> getStyle(Long id) {
        return justOrEmpty(styleMapper.selectByPrimaryKey(id));
    }

    /**
     * select all style
     *
     * @return
     */
    @Override
    public Mono<List<Style>> selectStyle() {
        return just(styleMapper.select());
    }

    /**
     * list style by type
     *
     * @param styleType
     * @return
     */
    @Override
    public List<Style> selectStyleByTypeAndActive(Integer styleType, Boolean isActive) {
        LOGGER.info("styleType = {}, isActive = {}",
                styleType, isActive);
        assertBulletinType(styleType, false);
        if (isNull(isActive))
            throw new BlueException(INVALID_IDENTITY);

        return styleMapper.selectByTypeAndActive(styleType, isActive);
    }

    /**
     * get active style
     *
     * @param styleType
     * @return
     */
    @Override
    public Mono<StyleInfo> getActiveStyleInfoByTypeWithCache(Integer styleType) {
        LOGGER.info("styleType = {}", styleType);
        return fromFuture(ACTIVE_STYLE_INFO_WITH_ALL_CACHE_GETTER.apply(styleType));
    }

    /**
     * select style by page and condition
     *
     * @param limit
     * @param rows
     * @param styleCondition
     * @return
     */
    @Override
    public Mono<List<Style>> selectStyleByLimitAndCondition(Long limit, Long rows, StyleCondition styleCondition) {
        LOGGER.info("limit = {}, rows = {}, styleCondition = {}", limit, rows, styleCondition);
        if (isInvalidLimit(limit) || isInvalidRows(rows))
            throw new BlueException(INVALID_PARAM);

        return just(styleMapper.selectByLimitAndCondition(limit, rows, styleCondition));
    }

    /**
     * count style by condition
     *
     * @param styleCondition
     * @return
     */
    @Override
    public Mono<Long> countStyleByCondition(StyleCondition styleCondition) {
        LOGGER.info("styleCondition = {}", styleCondition);
        return just(ofNullable(styleMapper.countByCondition(styleCondition)).orElse(0L));
    }

    /**
     * select style manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<StyleManagerInfo>> selectStyleManagerInfoPageByPageAndCondition(PageModelRequest<StyleCondition> pageModelRequest) {
        LOGGER.info("pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        StyleCondition styleCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectStyleByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), styleCondition),
                countStyleByCondition(styleCondition))
                .flatMap(tuple2 -> {
                    List<Style> styles = tuple2.getT1();
                    Long count = tuple2.getT2();
                    return isNotEmpty(styles) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(OPERATORS_GETTER.apply(styles))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndMemberNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(styles.stream().map(s ->
                                                STYLE_2_STYLE_MANAGER_INFO_CONVERTER.apply(s, idAndMemberNameMapping)).collect(toList()));
                                    }).flatMap(styleManagerInfos ->
                                            just(new PageModelResponse<>(styleManagerInfos, count)))
                            :
                            just(new PageModelResponse<>(emptyList(), count));
                });
    }

}
