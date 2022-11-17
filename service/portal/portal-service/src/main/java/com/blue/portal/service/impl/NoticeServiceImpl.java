package com.blue.portal.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.portal.NoticeType;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConf;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.portal.api.model.NoticeInfo;
import com.blue.portal.config.blue.BlueRedisConfig;
import com.blue.portal.config.deploy.NoticeDeploy;
import com.blue.portal.constant.NoticeSortAttribute;
import com.blue.portal.model.NoticeCondition;
import com.blue.portal.model.NoticeInsertParam;
import com.blue.portal.model.NoticeManagerInfo;
import com.blue.portal.model.NoticeUpdateParam;
import com.blue.portal.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.portal.repository.entity.Notice;
import com.blue.portal.repository.mapper.NoticeMapper;
import com.blue.portal.service.inter.NoticeService;
import com.blue.redisson.component.SynchronizedProcessor;
import com.github.benmanes.caffeine.cache.AsyncCache;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.*;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.*;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_JSON;
import static com.blue.basic.constant.common.Symbol.PERCENT;
import static com.blue.basic.constant.common.SyncKeyPrefix.NOTICE_CACHE_PRE;
import static com.blue.basic.constant.common.SyncKeyPrefix.NOTICE_UPDATE_SYNC_PRE;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCacheAsyncCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static com.blue.database.common.ConditionSortProcessor.process;
import static com.blue.portal.converter.PortalModelConverters.*;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * notice service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection", "DuplicatedCode"})
@Service
public class NoticeServiceImpl implements NoticeService {

    private static final Logger LOGGER = getLogger(BulletinServiceImpl.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private StringRedisTemplate stringRedisTemplate;

    private SynchronizedProcessor synchronizedProcessor;

    private NoticeMapper noticeMapper;

    public NoticeServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, BlueIdentityProcessor blueIdentityProcessor,
                             StringRedisTemplate stringRedisTemplate, SynchronizedProcessor synchronizedProcessor,
                             ExecutorService executorService, BlueRedisConfig blueRedisConfig, NoticeMapper noticeMapper, NoticeDeploy noticeDeploy) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.stringRedisTemplate = stringRedisTemplate;
        this.synchronizedProcessor = synchronizedProcessor;
        this.noticeMapper = noticeMapper;

        this.expireDuration = Duration.of(blueRedisConfig.getEntryTtl(), SECONDS);

        CaffeineConf caffeineConf = new CaffeineConfParams(
                NoticeType.values().length, Duration.of(noticeDeploy.getExpiresSecond(), SECONDS),
                AFTER_WRITE, executorService);

        typeNoticeInfoCache = generateCacheAsyncCache(caffeineConf);
        DEFAULT_NOTICE_INFO = new NoticeInfo(noticeDeploy.getId(), noticeDeploy.getTitle(), noticeDeploy.getContent(), noticeDeploy.getLink(), noticeDeploy.getType());

        of(NoticeType.values()).map(e -> e.identity)
                .forEach(t -> NOTICE_INFO_WITH_ALL_CACHE_GETTER.apply(t).join());
    }

    private NoticeInfo DEFAULT_NOTICE_INFO;

    private Duration expireDuration;

    private AsyncCache<Integer, NoticeInfo> typeNoticeInfoCache;

    private static final Function<Integer, String> NOTICE_CACHE_KEY_GENERATOR = t -> NOTICE_CACHE_PRE.prefix + t;
    private static final Function<Integer, String> NOTICE_UPDATE_SYNC_KEY_GEN = t -> NOTICE_UPDATE_SYNC_PRE.prefix + t;

    private final Consumer<Integer> REDIS_CACHE_DELETER = t ->
            stringRedisTemplate.delete(NOTICE_CACHE_KEY_GENERATOR.apply(t));

    private final Consumer<Integer> CACHE_DELETER = t -> {
        REDIS_CACHE_DELETER.accept(t);
        typeNoticeInfoCache.synchronous().invalidate(t);
    };

    private final Function<Integer, NoticeInfo> NOTICE_INFO_DB_GETTER = t ->
            ofNullable(noticeMapper.selectByType(t)).map(NOTICE_2_NOTICE_INFO_CONVERTER).orElse(DEFAULT_NOTICE_INFO);

    private final Function<Integer, NoticeInfo> NOTICE_INFO_REDIS_GETTER = t -> {
        assertNoticeType(t, false);
        return ofNullable(stringRedisTemplate.opsForValue().get(NOTICE_CACHE_KEY_GENERATOR.apply(t)))
                .filter(BlueChecker::isNotBlank)
                .map(s -> GSON.fromJson(s, NoticeInfo.class))
                .orElse(null);
    };

    private final BiConsumer<Integer, NoticeInfo> NOTICE_INFO_REDIS_SETTER = (t, ni) -> {
        String cacheKey = NOTICE_CACHE_KEY_GENERATOR.apply(t);
        REDIS_CACHE_DELETER.accept(t);
        stringRedisTemplate.opsForValue().set(cacheKey, ofNullable(ni).map(GSON::toJson).orElse(EMPTY_JSON.value), expireDuration);
        stringRedisTemplate.expire(cacheKey, expireDuration);
    };

    private final BiFunction<Integer, Executor, CompletableFuture<NoticeInfo>> NOTICE_INFO_WITH_REDIS_CACHE_GETTER = (t, executor) ->
            supplyAsync(() -> synchronizedProcessor.handleSupByOrderedWithSetter(NOTICE_UPDATE_SYNC_KEY_GEN.apply(t),
                    () -> NOTICE_INFO_REDIS_GETTER.apply(t), () -> NOTICE_INFO_DB_GETTER.apply(t),
                    noticeInfo -> NOTICE_INFO_REDIS_SETTER.accept(t, noticeInfo), BlueChecker::isNotNull), executor);

    private final Function<Integer, CompletableFuture<NoticeInfo>> NOTICE_INFO_WITH_ALL_CACHE_GETTER = t -> {
        assertNoticeType(t, false);

        return typeNoticeInfoCache.get(t, NOTICE_INFO_WITH_REDIS_CACHE_GETTER);
    };

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(NoticeSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<NoticeCondition> CONDITION_PROCESSOR = c -> {
        NoticeCondition nc = isNotNull(c) ? c : new NoticeCondition();

        process(nc, SORT_ATTRIBUTE_MAPPING, NoticeSortAttribute.CREATE_TIME.column);

        ofNullable(nc.getTitleLike())
                .filter(StringUtils::hasText).ifPresent(titleLike -> nc.setTitleLike(PERCENT.identity + titleLike + PERCENT.identity));
        ofNullable(nc.getLinkLike())
                .filter(StringUtils::hasText).ifPresent(linkLike -> nc.setLinkLike(PERCENT.identity + linkLike + PERCENT.identity));

        return nc;
    };

    private static final Function<List<Notice>, List<Long>> OPERATORS_GETTER = ns -> {
        Set<Long> oIds = new HashSet<>(ns.size());

        for (Notice n : ns) {
            oIds.add(n.getCreator());
            oIds.add(n.getUpdater());
        }

        return new ArrayList<>(oIds);
    };

    private final Consumer<NoticeInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        if (isNotNull(noticeMapper.selectByType(p.getType())))
            throw new BlueException(NOTICE_TYPE_ALREADY_EXIST, new String[]{String.valueOf(p.getType())});
    };

    private final Function<NoticeUpdateParam, Notice> UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long id = p.getId();

        ofNullable(p.getType())
                .map(t -> {
                    assertNoticeType(t, false);

                    return noticeMapper.selectByType(t);
                })
                .map(Notice::getId)
                .ifPresent(eid -> {
                    if (!id.equals(eid))
                        throw new BlueException(NOTICE_TYPE_ALREADY_EXIST, new String[]{getNoticeTypeByIdentity(p.getType()).disc});
                });

        Notice notice = noticeMapper.selectByPrimaryKey(id);
        if (isNull(notice))
            throw new BlueException(DATA_NOT_EXIST);

        return notice;
    };

    public static final BiConsumer<NoticeUpdateParam, Notice> UPDATE_ITEM_WITH_ASSERT_PACKAGER = (p, t) -> {
        if (isNull(p) || isNull(t))
            throw new BlueException(BAD_REQUEST);

        if (!p.getId().equals(t.getId()))
            throw new BlueException(BAD_REQUEST);

        boolean alteration = false;

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

        String link = p.getLink();
        if (isNotBlank(link) && !link.equals(t.getLink())) {
            t.setLink(link);
            alteration = true;
        }

        Integer type = p.getType();
        assertBulletinType(type, true);
        if (type != null && !type.equals(t.getType())) {
            t.setType(type);
            alteration = true;
        }

        if (!alteration)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        t.setUpdateTime(TIME_STAMP_GETTER.get());
    };

    /**
     * insert notice
     *
     * @param noticeInsertParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public NoticeInfo insertNotice(NoticeInsertParam noticeInsertParam, Long operatorId) {
        LOGGER.info("noticeInsertParam = {}, operatorId = {}", noticeInsertParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        INSERT_ITEM_VALIDATOR.accept(noticeInsertParam);
        Notice notice = NOTICE_INSERT_PARAM_2_NOTICE_CONVERTER.apply(noticeInsertParam);

        notice.setId(blueIdentityProcessor.generate(Notice.class));
        notice.setCreator(operatorId);
        notice.setUpdater(operatorId);

        Integer type = notice.getType();

        CACHE_DELETER.accept(type);
        noticeMapper.insert(notice);
        CACHE_DELETER.accept(type);

        return NOTICE_2_NOTICE_INFO_CONVERTER.apply(notice);
    }

    /**
     * update notice
     *
     * @param noticeUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public NoticeInfo updateNotice(NoticeUpdateParam noticeUpdateParam, Long operatorId) {
        LOGGER.info("noticeUpdateParam = {}, operatorId = {}", noticeUpdateParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        Notice notice = UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER.apply(noticeUpdateParam);

        List<Integer> changedTypes = new LinkedList<>();
        changedTypes.add(notice.getType());

        UPDATE_ITEM_WITH_ASSERT_PACKAGER.accept(noticeUpdateParam, notice);
        changedTypes.add(notice.getType());

        notice.setUpdater(operatorId);

        changedTypes.forEach(CACHE_DELETER);
        noticeMapper.updateByPrimaryKeySelective(notice);
        changedTypes.forEach(CACHE_DELETER);

        return NOTICE_2_NOTICE_INFO_CONVERTER.apply(notice);
    }

    /**
     * delete notice
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public NoticeInfo deleteNotice(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        Notice notice = noticeMapper.selectByPrimaryKey(id);
        if (isNull(notice))
            throw new BlueException(DATA_NOT_EXIST);

        Integer type = notice.getType();

        CACHE_DELETER.accept(type);
        noticeMapper.deleteByPrimaryKey(id);
        CACHE_DELETER.accept(type);

        return NOTICE_2_NOTICE_INFO_CONVERTER.apply(notice);
    }

    /**
     * expire notice info
     *
     * @return
     */
    @Override
    public void invalidNoticeInfosCache() {
        of(NoticeType.values())
                .forEach(type ->
                        CACHE_DELETER.accept(type.identity));
    }

    /**
     * get notice mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Notice> getNotice(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return justOrEmpty(noticeMapper.selectByPrimaryKey(id));
    }

    /**
     * get notice by type
     *
     * @param noticeType
     * @return
     */
    @Override
    public Notice getNoticeByType(Integer noticeType) {
        LOGGER.info("noticeType = {}", noticeType);
        assertNoticeType(noticeType, false);

        Notice notice = noticeMapper.selectByType(noticeType);
        LOGGER.info("notice = {}", notice);

        return notice;
    }

    /**
     * get notice info
     *
     * @param noticeType
     * @return
     */
    @Override
    public Mono<NoticeInfo> getNoticeInfoByTypeWithCache(Integer noticeType) {
        LOGGER.info("noticeType = {}", noticeType);
        return fromFuture(NOTICE_INFO_WITH_ALL_CACHE_GETTER.apply(noticeType));
    }

    /**
     * select notice by page and condition
     *
     * @param limit
     * @param rows
     * @param noticeCondition
     * @return
     */
    @Override
    public Mono<List<Notice>> selectNoticeByLimitAndCondition(Long limit, Long rows, NoticeCondition noticeCondition) {
        LOGGER.info("limit = {}, rows = {}, noticeCondition = {}", limit, rows, noticeCondition);
        return just(noticeMapper.selectByLimitAndCondition(limit, rows, noticeCondition));
    }

    /**
     * count notice by condition
     *
     * @param noticeCondition
     * @return
     */
    @Override
    public Mono<Long> countNoticeByCondition(NoticeCondition noticeCondition) {
        LOGGER.info("noticeCondition = {}", noticeCondition);
        return just(ofNullable(noticeMapper.countByCondition(noticeCondition)).orElse(0L));
    }

    /**
     * select notice info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<NoticeManagerInfo>> selectNoticeManagerInfoPageByPageAndCondition(PageModelRequest<NoticeCondition> pageModelRequest) {
        LOGGER.info("pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        NoticeCondition noticeCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectNoticeByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), noticeCondition),
                countNoticeByCondition(noticeCondition))
                .flatMap(tuple2 -> {
                    List<Notice> notices = tuple2.getT1();
                    Long count = tuple2.getT2();
                    return isNotEmpty(notices) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(OPERATORS_GETTER.apply(notices))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndMemberNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(notices.stream().map(n ->
                                                NOTICES_2_NOTICE_MANAGER_INFOS_CONVERTER.apply(n, idAndMemberNameMapping)).collect(toList()));
                                    }).flatMap(noticeManagerInfos ->
                                            just(new PageModelResponse<>(noticeManagerInfos, count)))
                            :
                            just(new PageModelResponse<>(emptyList(), count));
                });
    }

}
