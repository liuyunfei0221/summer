package com.blue.portal.service.impl;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.constant.portal.StyleType;
import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConf;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.portal.api.model.StyleInfo;
import com.blue.portal.api.model.StyleManagerInfo;
import com.blue.portal.config.blue.BlueRedisConfig;
import com.blue.portal.config.deploy.CaffeineDeploy;
import com.blue.portal.constant.BulletinSortAttribute;
import com.blue.portal.constant.StyleSortAttribute;
import com.blue.portal.model.StyleCondition;
import com.blue.portal.model.StyleInsertParam;
import com.blue.portal.model.StyleUpdateParam;
import com.blue.portal.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.portal.repository.entity.Bulletin;
import com.blue.portal.repository.entity.Style;
import com.blue.portal.repository.mapper.StyleMapper;
import com.blue.portal.service.inter.StyleService;
import com.blue.redisson.component.SynchronizedProcessor;
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
import java.util.function.*;
import java.util.stream.Stream;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.common.base.ConditionSortProcessor.process;
import static com.blue.base.common.base.ConstantProcessor.assertBulletinType;
import static com.blue.base.common.base.ConstantProcessor.assertStyleType;
import static com.blue.base.constant.base.BlueBoolean.FALSE;
import static com.blue.base.constant.base.BlueBoolean.TRUE;
import static com.blue.base.constant.base.CacheKeyPrefix.ACTIVE_STYLE_PRE;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.SyncKeyPrefix.STYLES_CACHE_PRE;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static com.blue.portal.converter.PortalModelConverters.*;
import static java.util.Collections.*;
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

    private final BlueIdentityProcessor blueIdentityProcessor;

    private StyleMapper styleMapper;

    private StringRedisTemplate stringRedisTemplate;

    private SynchronizedProcessor synchronizedProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public StyleServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, BlueIdentityProcessor blueIdentityProcessor,
                            StyleMapper styleMapper, StringRedisTemplate stringRedisTemplate, SynchronizedProcessor synchronizedProcessor,
                            ExecutorService executorService, BlueRedisConfig blueRedisConfig, CaffeineDeploy caffeineDeploy) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.styleMapper = styleMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.synchronizedProcessor = synchronizedProcessor;

        this.expireDuration = Duration.of(blueRedisConfig.getEntryTtl(), ChronoUnit.SECONDS);

        CaffeineConf caffeineConf = new CaffeineConfParams(
                caffeineDeploy.getMaximumSize(), Duration.of(caffeineDeploy.getExpireSeconds(), ChronoUnit.SECONDS),
                AFTER_WRITE, executorService);

        LOCAL_CACHE = generateCache(caffeineConf);
        of(StyleType.values()).map(e -> e.identity)
                .forEach(ACTIVE_STYLE_INFO_WITH_ALL_CACHE_GETTER::apply);
    }

    private Duration expireDuration;

    private static Cache<Integer, StyleInfo> LOCAL_CACHE;

    private static final Function<Integer, String> STYLE_CACHE_KEY_GENERATOR = type -> ACTIVE_STYLE_PRE.prefix + type;

    private static final Function<Integer, String> STYLE_LOAD_SYNC_KEY_GEN = type -> STYLES_CACHE_PRE.prefix + type;

    private final Consumer<Integer> REDIS_CACHE_DELETER = type ->
            stringRedisTemplate.delete(STYLE_CACHE_KEY_GENERATOR.apply(type));

    private final Consumer<Integer> ALL_CACHE_DELETER = type -> {
        REDIS_CACHE_DELETER.accept(type);
        LOCAL_CACHE.invalidate(type);
    };

    private final Function<Integer, Style> ACTIVE_STYLE_DB_GETTER = type -> {
        assertStyleType(type, false);

        List<Style> activeStyles = this.selectByTypeAndActive(type, TRUE.bool);
        LOGGER.info("ACTIVE_STYLE_INFO_DB_GETTER, activeStyles = {}, type = {}", activeStyles, type);

        if (isEmpty(activeStyles))
            activeStyles = singletonList(new Style());

        if (activeStyles.size() > 1)
            LOGGER.error("active style by type more than 1, type = {}", type);

        return activeStyles.get(0);
    };

    private final Function<Integer, StyleInfo> ACTIVE_STYLE_INFO_DB_GETTER = type ->
            STYLE_2_STYLE_INFO_CONVERTER.apply(ACTIVE_STYLE_DB_GETTER.apply(type));

    private final Function<Integer, StyleInfo> ACTIVE_STYLE_INFO_REDIS_GETTER = type ->
            ofNullable(stringRedisTemplate.opsForValue().get(STYLE_CACHE_KEY_GENERATOR.apply(type)))
                    .map(s -> GSON.fromJson(s, StyleInfo.class)).orElse(null);

    private final BiConsumer<Integer, StyleInfo> ACTIVE_STYLE_INFO_REDIS_SETTER = (type, styleInfo) -> {
        String cacheKey = STYLE_CACHE_KEY_GENERATOR.apply(type);
        REDIS_CACHE_DELETER.accept(type);
        stringRedisTemplate.opsForValue().set(cacheKey, GSON.toJson(styleInfo), expireDuration);
    };

    private final Function<Integer, StyleInfo> ACTIVE_STYLE_INFO_WITH_REDIS_CACHE_GETTER = type ->
            synchronizedProcessor.handleSupByOrderedWithSetter(() -> ACTIVE_STYLE_INFO_REDIS_GETTER.apply(type),
                    BlueChecker::isNotNull, () -> ACTIVE_STYLE_INFO_DB_GETTER.apply(type),
                    bulletinInfos -> ACTIVE_STYLE_INFO_REDIS_SETTER.accept(type, bulletinInfos));

    private final Function<Integer, StyleInfo> ACTIVE_STYLE_INFO_WITH_ALL_CACHE_GETTER = type -> {
        if (isNull(type))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "type can't be null");

        return LOCAL_CACHE.get(type, ACTIVE_STYLE_INFO_WITH_REDIS_CACHE_GETTER);
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
     * is a style exist?
     */
    private final Consumer<StyleInsertParam> INSERT_STYLE_VALIDATOR = sip -> {
        if (isNull(sip))
            throw new BlueException(EMPTY_PARAM);
        sip.asserts();

        if (isNotNull(styleMapper.selectByName(sip.getName())))
            throw new BlueException(RESOURCE_NAME_ALREADY_EXIST);
    };

    /**
     * is a style exist?
     */
    private final Function<StyleUpdateParam, Style> UPDATE_STYLE_VALIDATOR_AND_ORIGIN_RETURNER = sup -> {
        if (isNull(sup))
            throw new BlueException(EMPTY_PARAM);
        sup.asserts();

        Long id = sup.getId();

        ofNullable(sup.getName())
                .filter(BlueChecker::isNotBlank)
                .map(styleMapper::selectByName)
                .map(Style::getId)
                .ifPresent(eid -> {
                    if (!id.equals(eid))
                        throw new BlueException(STYLE_NAME_ALREADY_EXIST, new String[]{sup.getName()});
                });

        Style style = styleMapper.selectByPrimaryKey(id);
        if (isNull(style))
            throw new BlueException(DATA_NOT_EXIST);

        return style;
    };

    /**
     * for style
     */
    public static final BiFunction<StyleUpdateParam, Style, Boolean> UPDATE_STYLE_VALIDATOR = (p, t) -> {
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

        return alteration;
    };


    /**
     * insert style
     *
     * @param styleInsertParam
     * @param operatorId
     * @return
     */
    @Override
    public StyleInfo insertStyle(StyleInsertParam styleInsertParam, Long operatorId) {
        LOGGER.info("StyleInfo insertStyle(StyleInsertParam styleInsertParam, Long operatorId), styleInsertParam = {}, operatorId = {}",
                styleInsertParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        INSERT_STYLE_VALIDATOR.accept(styleInsertParam);

        Style style = STYLE_INSERT_PARAM_2_STYLE_CONVERTER.apply(styleInsertParam);

        style.setId(blueIdentityProcessor.generate(Bulletin.class));
        style.setCreator(operatorId);
        style.setUpdater(operatorId);

        styleMapper.insert(style);
        ALL_CACHE_DELETER.accept(style.getType());

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
    public StyleInfo updateStyle(StyleUpdateParam styleUpdateParam, Long operatorId) {
        LOGGER.info("StyleInfo updateStyle(StyleUpdateParam styleUpdateParam, Long operatorId), styleUpdateParam = {}, operatorId = {}",
                styleUpdateParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        Style style = UPDATE_STYLE_VALIDATOR_AND_ORIGIN_RETURNER.apply(styleUpdateParam);

        List<Integer> changedTypes = new LinkedList<>();
        changedTypes.add(style.getType());

        Boolean changed = UPDATE_STYLE_VALIDATOR.apply(styleUpdateParam, style);
        if (changed != null && !changed)
            throw new BlueException(DATA_HAS_NOT_CHANGED);
        changedTypes.add(style.getType());

        styleMapper.updateByPrimaryKeySelective(style);
        changedTypes.forEach(ALL_CACHE_DELETER);

        return STYLE_2_STYLE_INFO_CONVERTER.apply(style);
    }

    /**
     * delete style
     *
     * @param id
     * @return
     */
    @Override
    public StyleInfo deleteStyle(Long id) {
        LOGGER.info("BulletinInfo deleteBulletinById(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        Style style = styleMapper.selectByPrimaryKey(id);
        if (isNull(style))
            throw new BlueException(DATA_NOT_EXIST);

        styleMapper.deleteByPrimaryKey(id);
        ALL_CACHE_DELETER.accept(style.getType());

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
    public StyleManagerInfo updateActiveStyle(Long id, Long operatorId) {
        LOGGER.info("void updateDefaultRole(Long id), id = {}", id);
        if (isInvalidIdentity(id) || isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);

        Style newActiveStyle = styleMapper.selectByPrimaryKey(id);
        if (isNull(newActiveStyle))
            throw new BlueException(DATA_NOT_EXIST);

        Integer type = newActiveStyle.getType();

        Style oldActiveStyle = ACTIVE_STYLE_DB_GETTER.apply(type);
        if (newActiveStyle.getId().equals(oldActiveStyle.getId()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "style is already actived");

        Long stamp = TIME_STAMP_GETTER.get();

        newActiveStyle.setIsActive(TRUE.bool);
        newActiveStyle.setUpdater(operatorId);
        newActiveStyle.setUpdateTime(stamp);

        oldActiveStyle.setIsActive(FALSE.bool);
        oldActiveStyle.setUpdater(operatorId);
        oldActiveStyle.setUpdateTime(stamp);

        ALL_CACHE_DELETER.accept(type);
        styleMapper.updateByPrimaryKey(newActiveStyle);
        styleMapper.updateByPrimaryKey(oldActiveStyle);
        ALL_CACHE_DELETER.accept(type);

        Map<Long, String> idAndNameMapping;
        try {
            idAndNameMapping = rpcMemberBasicServiceConsumer.selectMemberBasicInfoMonoByIds(OPERATORS_GETTER.apply(singletonList(newActiveStyle)))
                    .toFuture().join().parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
        } catch (Exception e) {
            LOGGER.error("StyleManagerInfo updateActiveStyle(Long id, Long operatorId), generate idAndNameMapping failed, e = {}", e);
            idAndNameMapping = emptyMap();
        }

        return styleToStyleManagerInfo(newActiveStyle, ofNullable(idAndNameMapping.get(newActiveStyle.getCreator())).orElse(""),
                ofNullable(idAndNameMapping.get(newActiveStyle.getUpdater())).orElse(""));
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
                        ALL_CACHE_DELETER.accept(type.identity));
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
     * list style by type
     *
     * @param styleType
     * @return
     */
    @Override
    public List<Style> selectByTypeAndActive(Integer styleType, Boolean isActive) {
        LOGGER.info("List<Style> selectByTypeAndActive(Integer styleType, Boolean isActive), styleType = {}, isActive = {}",
                styleType, isActive);
        assertBulletinType(styleType, false);
        if (isNull(isActive))
            throw new BlueException(INVALID_IDENTITY);

        List<Style> styles = styleMapper.selectByTypeAndActive(styleType, isActive);
        LOGGER.info("List<Style> selectActiveStyleByType(Integer styleType), styles = {}", styles);

        return styles;
    }

    /**
     * get active style
     *
     * @param styleType
     * @return
     */
    @Override
    public Mono<StyleInfo> getActiveStyleInfoMonoByTypeWithCache(Integer styleType) {
        LOGGER.info("Mono<StyleInfo> getActiveStyleInfoMonoByTypeWithCache(Integer styleType), styleType = {}", styleType);
        return just(ACTIVE_STYLE_INFO_WITH_ALL_CACHE_GETTER.apply(styleType));
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
