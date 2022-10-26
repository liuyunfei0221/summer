package com.blue.portal.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.portal.BulletinType;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConf;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.portal.api.model.BulletinInfo;
import com.blue.portal.api.model.BulletinManagerInfo;
import com.blue.portal.config.blue.BlueRedisConfig;
import com.blue.portal.config.deploy.BulletinDeploy;
import com.blue.portal.constant.BulletinSortAttribute;
import com.blue.portal.model.BulletinCondition;
import com.blue.portal.model.BulletinInsertParam;
import com.blue.portal.model.BulletinUpdateParam;
import com.blue.portal.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.portal.repository.entity.Bulletin;
import com.blue.portal.repository.mapper.BulletinMapper;
import com.blue.portal.service.inter.BulletinService;
import com.blue.redisson.component.SynchronizedProcessor;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.*;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.assertBulletinType;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Status.VALID;
import static com.blue.basic.constant.common.Symbol.PERCENT;
import static com.blue.basic.constant.common.SyncKeyPrefix.BULLETINS_CACHE_PRE;
import static com.blue.basic.constant.common.SyncKeyPrefix.BULLETINS_UPDATE_SYNC_PRE;
import static com.blue.basic.constant.portal.BulletinType.POPULAR;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static com.blue.database.common.ConditionSortProcessor.process;
import static com.blue.portal.converter.PortalModelConverters.*;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.*;

/**
 * bulletin service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection", "DuplicatedCode"})
@Service
public class BulletinServiceImpl implements BulletinService {

    private static final Logger LOGGER = Loggers.getLogger(BulletinServiceImpl.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private StringRedisTemplate stringRedisTemplate;

    private SynchronizedProcessor synchronizedProcessor;

    private BulletinMapper bulletinMapper;

    public BulletinServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, BlueIdentityProcessor blueIdentityProcessor,
                               StringRedisTemplate stringRedisTemplate, SynchronizedProcessor synchronizedProcessor, BulletinMapper bulletinMapper,
                               BlueRedisConfig blueRedisConfig, ExecutorService executorService, BulletinDeploy bulletinDeploy) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.stringRedisTemplate = stringRedisTemplate;
        this.synchronizedProcessor = synchronizedProcessor;
        this.bulletinMapper = bulletinMapper;

        this.expireDuration = Duration.of(blueRedisConfig.getEntryTtl(), SECONDS);

        CaffeineConf caffeineConf = new CaffeineConfParams(
                bulletinDeploy.getMaximumSize(), Duration.of(bulletinDeploy.getExpiresSecond(), SECONDS),
                AFTER_WRITE, executorService);

        LOCAL_CACHE = generateCache(caffeineConf);
        of(BulletinType.values()).map(e -> e.identity)
                .forEach(BULLETIN_INFOS_WITH_ALL_CACHE_GETTER::apply);
    }

    private Duration expireDuration;

    private static Cache<Integer, List<BulletinInfo>> LOCAL_CACHE;

    private static final Function<Integer, String> BULLETIN_CACHE_KEY_GENERATOR = t -> BULLETINS_CACHE_PRE.prefix + t;
    private static final Function<Integer, String> BULLETIN_UPDATE_SYNC_KEY_GEN = t -> BULLETINS_UPDATE_SYNC_PRE.prefix + t;

    private final Consumer<Integer> REDIS_CACHE_DELETER = t ->
            stringRedisTemplate.delete(BULLETIN_CACHE_KEY_GENERATOR.apply(t));

    private final Consumer<Integer> CACHE_DELETER = t -> {
        REDIS_CACHE_DELETER.accept(t);
        LOCAL_CACHE.invalidate(t);
    };

    private final Function<Integer, List<BulletinInfo>> BULLETIN_INFOS_DB_GETTER = t ->
            BULLETINS_2_BULLETIN_INFOS_CONVERTER.apply(
                    bulletinMapper.selectAllByCondition(TIME_STAMP_GETTER.get(), t, VALID.status));

    private final Function<Integer, List<BulletinInfo>> BULLETIN_INFOS_REDIS_GETTER = t ->
            ofNullable(stringRedisTemplate.opsForList().range(ofNullable(t).map(BULLETIN_CACHE_KEY_GENERATOR)
                    .orElseGet(() -> BULLETIN_CACHE_KEY_GENERATOR.apply(POPULAR.identity)), 0, -1))
                    .filter(BlueChecker::isNotEmpty)
                    .map(sl -> sl.stream().map(s -> GSON.fromJson(s, BulletinInfo.class)).collect(toList()))
                    .orElseGet(Collections::emptyList);

    private final BiConsumer<Integer, List<BulletinInfo>> BULLETIN_INFOS_REDIS_SETTER = (t, bis) -> {
        String cacheKey = BULLETIN_CACHE_KEY_GENERATOR.apply(t);
        REDIS_CACHE_DELETER.accept(t);
        stringRedisTemplate.opsForList().rightPushAll(cacheKey, bis.stream().map(GSON::toJson).collect(toList()));
        stringRedisTemplate.expire(cacheKey, expireDuration);
    };

    private final Function<Integer, List<BulletinInfo>> BULLETIN_INFOS_WITH_REDIS_CACHE_GETTER = t ->
            synchronizedProcessor.handleSupByOrderedWithSetter(BULLETIN_UPDATE_SYNC_KEY_GEN.apply(t),
                    () -> BULLETIN_INFOS_REDIS_GETTER.apply(t), () -> BULLETIN_INFOS_DB_GETTER.apply(t),
                    bulletinInfos -> BULLETIN_INFOS_REDIS_SETTER.accept(t, bulletinInfos), BlueChecker::isNotEmpty);

    private final Function<Integer, List<BulletinInfo>> BULLETIN_INFOS_WITH_ALL_CACHE_GETTER = t -> {
        if (isNull(t))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "type can't be null");

        return LOCAL_CACHE.get(t, BULLETIN_INFOS_WITH_REDIS_CACHE_GETTER);
    };

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(BulletinSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<BulletinCondition> CONDITION_PROCESSOR = c -> {
        if (isNull(c))
            return new BulletinCondition();

        process(c, SORT_ATTRIBUTE_MAPPING, BulletinSortAttribute.ID.column);

        ofNullable(c.getTitleLike())
                .filter(StringUtils::hasText).ifPresent(titleLike -> c.setTitleLike(PERCENT.identity + titleLike + PERCENT.identity));
        ofNullable(c.getLinkLike())
                .filter(StringUtils::hasText).ifPresent(linkLike -> c.setLinkLike(PERCENT.identity + linkLike + PERCENT.identity));

        return c;
    };

    private static final Function<List<Bulletin>, List<Long>> OPERATORS_GETTER = bs -> {
        Set<Long> oIds = new HashSet<>(bs.size());

        for (Bulletin b : bs) {
            oIds.add(b.getCreator());
            oIds.add(b.getUpdater());
        }

        return new ArrayList<>(oIds);
    };

    private final Consumer<BulletinInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        if (isNotNull(bulletinMapper.selectByTitle(p.getTitle())))
            throw new BlueException(RESOURCE_NAME_ALREADY_EXIST);
    };

    private final Function<BulletinUpdateParam, Bulletin> UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long id = p.getId();

        ofNullable(p.getTitle())
                .filter(BlueChecker::isNotBlank)
                .map(bulletinMapper::selectByTitle)
                .map(Bulletin::getId)
                .ifPresent(eid -> {
                    if (!id.equals(eid))
                        throw new BlueException(BULLETIN_TITLE_ALREADY_EXIST, new String[]{p.getTitle()});
                });

        Bulletin bulletin = bulletinMapper.selectByPrimaryKey(id);
        if (isNull(bulletin))
            throw new BlueException(DATA_NOT_EXIST);

        return bulletin;
    };

    public static final BiConsumer<BulletinUpdateParam, Bulletin> UPDATE_ITEM_WITH_ASSERT_PACKAGER = (p, t) -> {
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

        Integer priority = p.getPriority();
        if (priority != null && !priority.equals(t.getPriority())) {
            t.setPriority(priority);
            alteration = true;
        }

        Long activeTime = p.getActiveTime();
        if (activeTime != null && !activeTime.equals(t.getActiveTime())) {
            t.setActiveTime(activeTime);
            alteration = true;
        }

        Long expireTime = p.getExpireTime();
        if (expireTime != null && !expireTime.equals(t.getExpireTime())) {
            t.setExpireTime(expireTime);
            alteration = true;
        }

        if (!alteration)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        t.setUpdateTime(TIME_STAMP_GETTER.get());
    };

    /**
     * insert bulletin
     *
     * @param bulletinInsertParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public BulletinInfo insertBulletin(BulletinInsertParam bulletinInsertParam, Long operatorId) {
        LOGGER.info("BulletinInfo insertBulletin(BulletinInsertParam bulletinInsertParam, Long operatorId), bulletinInsertParam = {}, operatorId = {}",
                bulletinInsertParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        INSERT_ITEM_VALIDATOR.accept(bulletinInsertParam);
        Bulletin bulletin = BULLETIN_INSERT_PARAM_2_BULLETIN_CONVERTER.apply(bulletinInsertParam);

        bulletin.setId(blueIdentityProcessor.generate(Bulletin.class));
        bulletin.setCreator(operatorId);
        bulletin.setUpdater(operatorId);

        Integer type = bulletin.getType();

        CACHE_DELETER.accept(type);
        bulletinMapper.insert(bulletin);
        CACHE_DELETER.accept(type);

        return BULLETIN_2_BULLETIN_INFO_CONVERTER.apply(bulletin);
    }

    /**
     * update bulletin
     *
     * @param bulletinUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public BulletinInfo updateBulletin(BulletinUpdateParam bulletinUpdateParam, Long operatorId) {
        LOGGER.info("BulletinInfo updateBulletin(BulletinUpdateParam bulletinUpdateParam, Long operatorId), bulletinUpdateParam = {}, operatorId = {}",
                bulletinUpdateParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        Bulletin bulletin = UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER.apply(bulletinUpdateParam);

        List<Integer> changedTypes = new LinkedList<>();
        changedTypes.add(bulletin.getType());

        UPDATE_ITEM_WITH_ASSERT_PACKAGER.accept(bulletinUpdateParam, bulletin);

        changedTypes.add(bulletin.getType());
        bulletin.setUpdater(operatorId);

        changedTypes.forEach(CACHE_DELETER);
        bulletinMapper.updateByPrimaryKeySelective(bulletin);
        changedTypes.forEach(CACHE_DELETER);

        return BULLETIN_2_BULLETIN_INFO_CONVERTER.apply(bulletin);
    }

    /**
     * delete bulletin
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public BulletinInfo deleteBulletin(Long id) {
        LOGGER.info("BulletinInfo deleteBulletinById(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        Bulletin bulletin = bulletinMapper.selectByPrimaryKey(id);
        if (isNull(bulletin))
            throw new BlueException(DATA_NOT_EXIST);

        Integer type = bulletin.getType();

        CACHE_DELETER.accept(type);
        bulletinMapper.deleteByPrimaryKey(id);
        CACHE_DELETER.accept(type);

        return BULLETIN_2_BULLETIN_INFO_CONVERTER.apply(bulletin);
    }

    /**
     * expire bulletin info
     *
     * @return
     */
    @Override
    public void invalidBulletinInfosCache() {
        of(BulletinType.values())
                .forEach(type ->
                        CACHE_DELETER.accept(type.identity));
    }

    /**
     * get bulletin by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Bulletin> getBulletin(Long id) {
        LOGGER.info("Optional<Bulletin> getBulletin(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(bulletinMapper.selectByPrimaryKey(id));
    }

    /**
     * get bulletin mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Bulletin> getBulletinMono(Long id) {
        LOGGER.info("Mono<Bulletin> getBulletinMono(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        return justOrEmpty(bulletinMapper.selectByPrimaryKey(id));
    }

    /**
     * select all bulletins
     *
     * @return
     */
    @Override
    public Mono<List<Bulletin>> selectBulletin() {
        LOGGER.info("Mono<List<Bulletin>> selectBulletin()");
        return just(bulletinMapper.select());
    }

    /**
     * list active bulletins by type
     *
     * @param bulletinType
     * @return
     */
    @Override
    public List<Bulletin> selectActiveBulletinByType(Integer bulletinType) {
        LOGGER.info("List<Bulletin> selectTargetActiveBulletinByType(Integer bulletinType), bulletinType = {}", bulletinType);
        assertBulletinType(bulletinType, false);

        List<Bulletin> bulletins = bulletinMapper.selectAllByCondition(TIME_STAMP_GETTER.get(), bulletinType, VALID.status);
        LOGGER.info("List<Bulletin> selectActiveBulletinByType(BulletinType bulletinType), bulletins = {}", bulletins);

        return bulletins;
    }

    /**
     * list bulletin info
     *
     * @param bulletinType
     * @return
     */
    @Override
    public Mono<List<BulletinInfo>> selectActiveBulletinInfoMonoByTypeWithCache(Integer bulletinType) {
        LOGGER.info("Mono<List<BulletinInfo>> selectBulletin(Integer bulletinType), bulletinType = {}", bulletinType);
        return just(BULLETIN_INFOS_WITH_ALL_CACHE_GETTER.apply(bulletinType));
    }

    /**
     * select bulletin by page and condition
     *
     * @param limit
     * @param rows
     * @param bulletinCondition
     * @return
     */
    @Override
    public Mono<List<Bulletin>> selectBulletinMonoByLimitAndCondition(Long limit, Long rows, BulletinCondition bulletinCondition) {
        LOGGER.info("Mono<List<Bulletin>> selectBulletinMonoByLimitAndCondition(Long limit, Long rows, BulletinCondition bulletinCondition), " +
                "limit = {}, rows = {}, bulletinCondition = {}", limit, rows, bulletinCondition);
        return just(bulletinMapper.selectByLimitAndCondition(limit, rows, bulletinCondition));
    }

    /**
     * count bulletin by condition
     *
     * @param bulletinCondition
     * @return
     */
    @Override
    public Mono<Long> countBulletinMonoByCondition(BulletinCondition bulletinCondition) {
        LOGGER.info("Mono<Long> countBulletinMonoByCondition(BulletinCondition bulletinCondition), bulletinCondition = {}", bulletinCondition);
        return just(ofNullable(bulletinMapper.countByCondition(bulletinCondition)).orElse(0L));
    }

    /**
     * select bulletin info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<BulletinManagerInfo>> selectBulletinManagerInfoPageMonoByPageAndCondition(PageModelRequest<BulletinCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<BulletinManagerInfo>> selectBulletinInfoPageMonoByPageAndCondition(PageModelRequest<BulletinCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        BulletinCondition bulletinCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectBulletinMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), bulletinCondition),
                countBulletinMonoByCondition(bulletinCondition))
                .flatMap(tuple2 -> {
                    List<Bulletin> bulletins = tuple2.getT1();
                    Long count = tuple2.getT2();
                    return isNotEmpty(bulletins) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(OPERATORS_GETTER.apply(bulletins))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndMemberNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(bulletins.stream().map(b ->
                                                bulletinToBulletinManagerInfo(b, idAndMemberNameMapping)).collect(toList()));
                                    }).flatMap(bulletinManagerInfos ->
                                            just(new PageModelResponse<>(bulletinManagerInfos, count)))
                            :
                            just(new PageModelResponse<>(emptyList(), count));
                });
    }

}
