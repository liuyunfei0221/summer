package com.blue.portal.service.impl;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.constant.portal.BulletinType;
import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConf;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.portal.api.model.BulletinInfo;
import com.blue.portal.api.model.BulletinManagerInfo;
import com.blue.portal.config.blue.BlueRedisConfig;
import com.blue.portal.config.deploy.CaffeineDeploy;
import com.blue.portal.constant.BulletinSortAttribute;
import com.blue.portal.model.BulletinCondition;
import com.blue.portal.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.portal.repository.entity.Bulletin;
import com.blue.portal.repository.mapper.BulletinMapper;
import com.blue.portal.service.inter.BulletinService;
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
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.common.base.ConditionSortProcessor.process;
import static com.blue.base.common.base.ConstantProcessor.assertBulletinType;
import static com.blue.base.constant.base.CacheKeyPrefix.BULLETINS_PRE;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Status.VALID;
import static com.blue.base.constant.base.SyncKeyPrefix.BULLETINS_CACHE_PRE;
import static com.blue.base.constant.portal.BulletinType.POPULAR;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static com.blue.portal.converter.PortalModelConverters.BULLETINS_2_BULLETIN_INFOS_CONVERTER;
import static com.blue.portal.converter.PortalModelConverters.bulletinToBulletinManagerInfo;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;

/**
 * bulletin service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
@Service
public class BulletinServiceImpl implements BulletinService {

    private static final Logger LOGGER = Loggers.getLogger(BulletinServiceImpl.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private BulletinMapper bulletinMapper;

    private StringRedisTemplate stringRedisTemplate;

    private SynchronizedProcessor synchronizedProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public BulletinServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, BulletinMapper bulletinMapper, StringRedisTemplate stringRedisTemplate,
                               SynchronizedProcessor synchronizedProcessor, ExecutorService executorService, BlueRedisConfig blueRedisConfig, CaffeineDeploy caffeineDeploy) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.bulletinMapper = bulletinMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.synchronizedProcessor = synchronizedProcessor;

        redisExpire = blueRedisConfig.getEntryTtl();
        CaffeineConf caffeineConf = new CaffeineConfParams(
                caffeineDeploy.getMaximumSize(), Duration.of(caffeineDeploy.getExpireSeconds(), ChronoUnit.SECONDS),
                AFTER_WRITE, executorService);

        LOCAL_CACHE = generateCache(caffeineConf);
        of(BulletinType.values()).map(e -> e.identity)
                .forEach(BULLETIN_INFOS_WITH_ALL_CACHE_GETTER::apply);
    }

    private static long redisExpire;

    private final static TimeUnit EXPIRE_UNIT = TimeUnit.SECONDS;

    private static Cache<Integer, List<BulletinInfo>> LOCAL_CACHE;

    private static final Function<Integer, String> BULLETIN_CACHE_KEY_GENERATOR = type -> BULLETINS_PRE.prefix + type;

    private static final Function<Integer, String> PORTAL_LOAD_SYNC_KEY_GEN = type -> BULLETINS_CACHE_PRE.prefix + type;

    private final Consumer<Integer> CACHE_DELETER = type -> {
        stringRedisTemplate.delete(BULLETIN_CACHE_KEY_GENERATOR.apply(type));
        LOCAL_CACHE.invalidate(type);
    };

    private final Function<Integer, List<BulletinInfo>> BULLETIN_INFOS_DB_GETTER = type ->
            BULLETINS_2_BULLETIN_INFOS_CONVERTER.apply(
                    bulletinMapper.selectAllByCondition(TIME_STAMP_GETTER.get(), type, VALID.status));

    private final Function<Integer, List<BulletinInfo>> BULLETIN_INFOS_REDIS_GETTER = type ->
            ofNullable(stringRedisTemplate.opsForList().range(ofNullable(type).map(BULLETIN_CACHE_KEY_GENERATOR)
                    .orElseGet(() -> BULLETIN_CACHE_KEY_GENERATOR.apply(POPULAR.identity)), 0, -1))
                    .filter(BlueChecker::isNotEmpty)
                    .map(sl -> sl.stream().map(s -> GSON.fromJson(s, BulletinInfo.class)).collect(toList()))
                    .orElseGet(Collections::emptyList);

    private final BiConsumer<Integer, List<BulletinInfo>> BULLETIN_INFOS_REDIS_SETTER = (type, bulletinInfos) -> {
        String cacheKey = BULLETIN_CACHE_KEY_GENERATOR.apply(type);
        CACHE_DELETER.accept(type);
        stringRedisTemplate.opsForList().rightPushAll(cacheKey, bulletinInfos.stream().map(GSON::toJson).collect(toList()));
        stringRedisTemplate.expire(cacheKey, redisExpire, EXPIRE_UNIT);
    };

    private final Function<Integer, List<BulletinInfo>> BULLETIN_INFOS_WITH_REDIS_CACHE_GETTER = type ->
            synchronizedProcessor.handleSupByOrderedWithSetter(() -> BULLETIN_INFOS_REDIS_GETTER.apply(type),
                    BlueChecker::isNotEmpty, () -> BULLETIN_INFOS_DB_GETTER.apply(type),
                    bulletinInfos -> BULLETIN_INFOS_REDIS_SETTER.accept(type, bulletinInfos));

    private final Function<Integer, List<BulletinInfo>> BULLETIN_INFOS_WITH_ALL_CACHE_GETTER = type -> {
        if (isNull(type))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "type can't be null");

        return LOCAL_CACHE.get(type, BULLETIN_INFOS_WITH_REDIS_CACHE_GETTER);
    };

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(BulletinSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<BulletinCondition> CONDITION_PROCESSOR = condition -> {
        if (isNull(condition))
            return new BulletinCondition();

        process(condition, SORT_ATTRIBUTE_MAPPING, BulletinSortAttribute.ID.column);

        ofNullable(condition.getTitleLike())
                .filter(StringUtils::hasText).ifPresent(titleLike -> condition.setTitleLike("%" + titleLike + "%"));
        ofNullable(condition.getLinkLike())
                .filter(StringUtils::hasText).ifPresent(linkLike -> condition.setLinkLike("%" + linkLike + "%"));

        return condition;
    };

    private static final Function<List<Bulletin>, List<Long>> OPERATORS_GETTER = bulletins -> {
        Set<Long> operatorIds = new HashSet<>(bulletins.size());

        for (Bulletin b : bulletins) {
            operatorIds.add(b.getCreator());
            operatorIds.add(b.getUpdater());
        }

        return new ArrayList<>(operatorIds);
    };

    /**
     * insert bulletin
     *
     * @param bulletin
     * @return
     */
    @Override
    public int insertBulletin(Bulletin bulletin) {
        LOGGER.info("int insertBulletin(Bulletin bulletin), bulletin = {}", bulletin);
        return ofNullable(bulletin)
                .map(bulletinMapper::insert)
                .orElse(0);
    }


    /**
     * expire bulletin infos
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
     * get attachment mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<Bulletin>> getBulletinMono(Long id) {
        return just(this.getBulletin(id));
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
     * list bulletin infos
     *
     * @param bulletinType
     * @return
     */
    @Override
    public Mono<List<BulletinInfo>> selectBulletinInfoMonoByType(Integer bulletinType) {
        LOGGER.info("Mono<List<BulletinInfo>> selectBulletin(Integer bulletinType), bulletinType = {}", bulletinType);
        assertBulletinType(bulletinType, false);
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

        BulletinCondition bulletinCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(selectBulletinMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), bulletinCondition),
                countBulletinMonoByCondition(bulletinCondition))
                .flatMap(tuple2 -> {
                    List<Bulletin> bulletins = tuple2.getT1();
                    return isNotEmpty(bulletins) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoMonoByIds(OPERATORS_GETTER.apply(bulletins))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(bulletins.stream().map(b ->
                                                bulletinToBulletinManagerInfo(b, ofNullable(idAndNameMapping.get(b.getCreator())).orElse(""),
                                                        ofNullable(idAndNameMapping.get(b.getUpdater())).orElse(""))).collect(toList()));
                                    }).flatMap(bulletinManagerInfos ->
                                            just(new PageModelResponse<>(bulletinManagerInfos, tuple2.getT2())))
                            :
                            just(new PageModelResponse<>(emptyList(), tuple2.getT2()));
                });
    }

}
