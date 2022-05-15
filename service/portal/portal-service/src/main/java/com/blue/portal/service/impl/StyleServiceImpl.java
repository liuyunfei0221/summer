package com.blue.portal.service.impl;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.constant.portal.StyleType;
import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConf;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.portal.api.model.StyleInfo;
import com.blue.portal.api.model.StyleManagerInfo;
import com.blue.portal.config.blue.BlueRedisConfig;
import com.blue.portal.config.deploy.CaffeineDeploy;
import com.blue.portal.constant.BulletinSortAttribute;
import com.blue.portal.constant.StyleSortAttribute;
import com.blue.portal.model.StyleCondition;
import com.blue.portal.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.portal.repository.entity.Style;
import com.blue.portal.repository.mapper.StyleMapper;
import com.blue.portal.service.inter.StyleService;
import com.blue.redisson.common.SynchronizedProcessor;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.common.base.ConditionSortProcessor.process;
import static com.blue.base.common.base.ConstantProcessor.assertBulletinType;
import static com.blue.base.common.base.ConstantProcessor.assertStyleType;
import static com.blue.base.constant.base.CacheKeyPrefix.STYLES_PRE;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Status.VALID;
import static com.blue.base.constant.base.SyncKeyPrefix.STYLES_CACHE_PRE;
import static com.blue.base.constant.portal.BulletinType.POPULAR;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static com.blue.portal.converter.PortalModelConverters.STYLES_2_STYLE_INFOS_CONVERTER;
import static com.blue.portal.converter.PortalModelConverters.styleToStyleManagerInfo;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;

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

    private StyleMapper styleMapper;

    private StringRedisTemplate stringRedisTemplate;

    private SynchronizedProcessor synchronizedProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public StyleServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, StyleMapper styleMapper, StringRedisTemplate stringRedisTemplate,
                            SynchronizedProcessor synchronizedProcessor, ExecutorService executorService, BlueRedisConfig blueRedisConfig, CaffeineDeploy caffeineDeploy) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.styleMapper = styleMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.synchronizedProcessor = synchronizedProcessor;

        redisExpire = blueRedisConfig.getEntryTtl();
        CaffeineConf caffeineConf = new CaffeineConfParams(
                caffeineDeploy.getMaximumSize(), Duration.of(caffeineDeploy.getExpireSeconds(), ChronoUnit.SECONDS),
                AFTER_WRITE, executorService);

        LOCAL_CACHE = generateCache(caffeineConf);
        of(StyleType.values()).map(e -> e.identity)
                .forEach(STYLE_INFOS_WITH_ALL_CACHE_GETTER::apply);
    }

    private static long redisExpire;

    private final static TimeUnit EXPIRE_UNIT = TimeUnit.SECONDS;

    private static Cache<Integer, List<StyleInfo>> LOCAL_CACHE;

    private static final Function<Integer, String> STYLE_CACHE_KEY_GENERATOR = type -> STYLES_PRE.prefix + type;

    private static final Function<Integer, String> STYLE_LOAD_SYNC_KEY_GEN = type -> STYLES_CACHE_PRE.prefix + type;

    private final Consumer<Integer> CACHE_DELETER = type -> {
        stringRedisTemplate.delete(STYLE_CACHE_KEY_GENERATOR.apply(type));
        LOCAL_CACHE.invalidate(type);
    };

    private final Function<Integer, List<StyleInfo>> STYLE_INFOS_DB_GETTER = type ->
            STYLES_2_STYLE_INFOS_CONVERTER.apply(
                    styleMapper.selectAllByCondition(type, VALID.status));

    private final Function<Integer, List<StyleInfo>> STYLE_INFOS_REDIS_GETTER = type ->
            ofNullable(stringRedisTemplate.opsForList().range(ofNullable(type).map(STYLE_CACHE_KEY_GENERATOR)
                    .orElseGet(() -> STYLE_CACHE_KEY_GENERATOR.apply(POPULAR.identity)), 0, -1))
                    .filter(BlueChecker::isNotEmpty)
                    .map(sl -> sl.stream().map(s -> GSON.fromJson(s, StyleInfo.class)).collect(toList()))
                    .orElseGet(Collections::emptyList);

    private final BiConsumer<Integer, List<StyleInfo>> STYLE_INFOS_REDIS_SETTER = (type, styleInfos) -> {
        String cacheKey = STYLE_CACHE_KEY_GENERATOR.apply(type);
        CACHE_DELETER.accept(type);
        stringRedisTemplate.opsForList().rightPushAll(cacheKey, styleInfos.stream().map(GSON::toJson).collect(toList()));
        stringRedisTemplate.expire(cacheKey, redisExpire, EXPIRE_UNIT);
    };

    private final Function<Integer, List<StyleInfo>> STYLE_INFOS_WITH_REDIS_CACHE_GETTER = type ->
            synchronizedProcessor.handleSupByOrderedWithSetter(() -> STYLE_INFOS_REDIS_GETTER.apply(type),
                    BlueChecker::isNotEmpty, () -> STYLE_INFOS_DB_GETTER.apply(type),
                    bulletinInfos -> STYLE_INFOS_REDIS_SETTER.accept(type, bulletinInfos));

    private final Function<Integer, List<StyleInfo>> STYLE_INFOS_WITH_ALL_CACHE_GETTER = type -> {
        if (isNull(type))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "type can't be null");

        return LOCAL_CACHE.get(type, STYLE_INFOS_WITH_REDIS_CACHE_GETTER);
    };

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(StyleSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<StyleCondition> CONDITION_PROCESSOR = condition -> {
        if (isNull(condition))
            return new StyleCondition();

        process(condition, SORT_ATTRIBUTE_MAPPING, BulletinSortAttribute.ID.column);

        ofNullable(condition.getNameLike())
                .filter(StringUtils::hasText).ifPresent(titleLike -> condition.setNameLike("%" + titleLike + "%"));

        return condition;
    };

    private static final Function<List<Style>, List<Long>> OPERATORS_GETTER = styles -> {
        Set<Long> operatorIds = new HashSet<>(styles.size());

        for (Style s : styles) {
            operatorIds.add(s.getCreator());
            operatorIds.add(s.getUpdater());
        }

        return new ArrayList<>(operatorIds);
    };

    /**
     * insert style
     *
     * @param style
     * @return
     */
    @Override
    public int insertStyle(Style style) {
        LOGGER.info("int insertStyle(Style style), style = {}", style);
        return ofNullable(style)
                .map(styleMapper::insert)
                .orElse(0);
    }

    /**
     * expire style infos
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
     * get style by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Style> getStyle(Long id) {
        LOGGER.info("Optional<Style> getBulletin(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(styleMapper.selectByPrimaryKey(id));
    }

    /**
     * get style mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<Style>> getStyleMono(Long id) {
        return just(this.getStyle(id));
    }

    /**
     * select all style
     *
     * @return
     */
    @Override
    public Mono<List<Style>> selectStyle() {
        LOGGER.info("Mono<List<Style>> selectStyle()");
        return just(styleMapper.select());
    }

    /**
     * list active style by type
     *
     * @param styleType
     * @return
     */
    @Override
    public List<Style> selectActiveStyleByType(Integer styleType) {
        LOGGER.info("List<Style> selectActiveStyleByType(Integer styleType), styleType = {}", styleType);
        assertBulletinType(styleType, false);

        List<Style> styles = styleMapper.selectAllByCondition(styleType, VALID.status);
        LOGGER.info("List<Style> selectActiveStyleByType(Integer styleType), styles = {}", styles);

        return styles;
    }

    /**
     * list style infos
     *
     * @param styleType
     * @return
     */
    @Override
    public Mono<List<StyleInfo>> selectStyleInfoMonoByType(Integer styleType) {
        LOGGER.info("Mono<List<StyleInfo>> selectStyleInfoMonoByType(Integer styleType), styleType = {}", styleType);
        assertStyleType(styleType, false);
        return just(STYLE_INFOS_WITH_ALL_CACHE_GETTER.apply(styleType));
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
    public Mono<List<Style>> selectStyleMonoByLimitAndCondition(Long limit, Long rows, StyleCondition styleCondition) {
        LOGGER.info("Mono<List<Style>> selectStyleMonoByLimitAndCondition(Long limit, Long rows, StyleCondition styleCondition), " +
                "limit = {}, rows = {}, styleCondition = {}", limit, rows, styleCondition);
        return just(styleMapper.selectByLimitAndCondition(limit, rows, styleCondition));
    }

    /**
     * count style by condition
     *
     * @param styleCondition
     * @return
     */
    @Override
    public Mono<Long> countStyleMonoByCondition(StyleCondition styleCondition) {
        LOGGER.info("Mono<Long> countStyleMonoByCondition(StyleCondition styleCondition), bulletinCondition = {}", styleCondition);
        return just(ofNullable(styleMapper.countByCondition(styleCondition)).orElse(0L));
    }

    /**
     * select style manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<StyleManagerInfo>> selectStyleManagerInfoPageMonoByPageAndCondition(PageModelRequest<StyleCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<StyleManagerInfo>> selectStyleManagerInfoPageMonoByPageAndCondition(PageModelRequest<StyleCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        StyleCondition styleCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(selectStyleMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), styleCondition),
                countStyleMonoByCondition(styleCondition))
                .flatMap(tuple2 -> {
                    List<Style> styles = tuple2.getT1();
                    return isNotEmpty(styles) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoMonoByIds(OPERATORS_GETTER.apply(styles))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(styles.stream().map(s ->
                                                styleToStyleManagerInfo(s, ofNullable(idAndNameMapping.get(s.getCreator())).orElse(""),
                                                        ofNullable(idAndNameMapping.get(s.getUpdater())).orElse(""))).collect(toList()));
                                    }).flatMap(styleManagerInfos ->
                                            just(new PageModelResponse<>(styleManagerInfos, tuple2.getT2())))
                            :
                            just(new PageModelResponse<>(emptyList(), tuple2.getT2()));
                });
    }

}
