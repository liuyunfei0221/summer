package com.blue.portal.service.impl;

import com.blue.base.common.base.ConstantProcessor;
import com.blue.base.constant.portal.BulletinType;
import com.blue.base.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConf;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.portal.api.model.BulletinInfo;
import com.blue.portal.config.blue.BlueRedisConfig;
import com.blue.portal.config.deploy.CaffeineDeploy;
import com.blue.portal.repository.entity.Bulletin;
import com.blue.portal.service.inter.BulletinService;
import com.blue.portal.service.inter.PortalService;
import com.github.benmanes.caffeine.cache.Cache;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.constant.base.BlueCacheKey.PORTALS_PRE;
import static com.blue.base.constant.base.BlueNumericalValue.MAX_WAIT_MILLIS_FOR_REDISSON_SYNC;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.SyncKeyPrefix.PORTALS_REFRESH_PRE;
import static com.blue.base.constant.portal.BulletinType.POPULAR;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static com.blue.portal.converter.PortalModelConverters.BULLETINS_2_BULLETIN_INFOS_CONVERTER;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.justOrEmpty;

/**
 * portal service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class PortalServiceImpl implements PortalService {

    private static final Logger LOGGER = Loggers.getLogger(PortalServiceImpl.class);

    private BulletinService bulletinService;

    private StringRedisTemplate stringRedisTemplate;

    private RedissonClient redissonClient;

    private static long redisExpire;

    private final static TimeUnit EXPIRE_UNIT = TimeUnit.SECONDS;

    public PortalServiceImpl(BulletinService bulletinService, ExecutorService executorService, StringRedisTemplate stringRedisTemplate,
                             RedissonClient redissonClient, BlueRedisConfig blueRedisConfig, CaffeineDeploy caffeineDeploy) {
        this.bulletinService = bulletinService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redissonClient = redissonClient;

        redisExpire = blueRedisConfig.getEntryTtl();
        CaffeineConf caffeineConf = new CaffeineConfParams(
                caffeineDeploy.getMaximumSize(), Duration.of(caffeineDeploy.getExpireSeconds(), ChronoUnit.SECONDS),
                AFTER_WRITE, executorService);

        LOCAL_CACHE = generateCache(caffeineConf);
        of(BulletinType.values())
                .forEach(LOCAL_CACHE_PORTAL_FUNC::apply);
    }

    private static Cache<BulletinType, List<BulletinInfo>> LOCAL_CACHE;

    private static final Function<Integer, BulletinType> TYPE_CONVERTER = ConstantProcessor::getBulletinTypeByIdentity;

    private static final Function<BulletinType, String> BULLETIN_CACHE_KEY_GENERATOR = type -> PORTALS_PRE.key + type.identity;

    private static final Function<BulletinType, String> PORTAL_LOAD_SYNC_KEY_GEN = type -> PORTALS_REFRESH_PRE.prefix + type.identity;

    /**
     * list bulletin infos from db
     */
    private final Function<BulletinType, List<BulletinInfo>> DB_PORTAL_GETTER = type -> {
        List<Bulletin> bulletins = bulletinService.selectTargetActiveBulletinByType(type);
        LOGGER.info("DB_PORTAL_GETTER, bulletins = {}, type = {}", bulletins, type);
        return BULLETINS_2_BULLETIN_INFOS_CONVERTER.apply(bulletins);
    };

    /**
     * list bulletin infos from redis
     */
    private final Function<BulletinType, List<BulletinInfo>> REDIS_PORTAL_GETTER = type -> {
        List<String> bulletins = ofNullable(stringRedisTemplate.opsForList().range(ofNullable(type)
                .map(BULLETIN_CACHE_KEY_GENERATOR)
                .orElseGet(() -> PORTALS_PRE.key + POPULAR.identity), 0, -1))
                .orElseGet(Collections::emptyList);
        LOGGER.info("REDIS_PORTAL_GETTER, type = {}, bulletins = {}", type, bulletins);
        return bulletins
                .stream().map(s -> GSON.fromJson(s, BulletinInfo.class)).collect(toList());
    };

    /**
     * set bulletin info to redis
     */
    private final BiConsumer<BulletinType, List<BulletinInfo>> REDIS_PORTAL_SETTER = (type, list) -> {
        if (isEmpty(list))
            return;

        String key = BULLETIN_CACHE_KEY_GENERATOR.apply(type);
        stringRedisTemplate.opsForList().rightPushAll(key, list.stream().map(GSON::toJson).collect(toList()));
        stringRedisTemplate.expire(key, redisExpire, EXPIRE_UNIT);
        LOGGER.info("REDIS_CACHE_PORTAL_SETTER, key = {},list = {}", key, list);
    };

    /**
     * list bulletin infos from redis, if not exist, set
     */
    private final Function<BulletinType, List<BulletinInfo>> REDIS_PORTAL_GETTER_WITH_CACHE = type ->
            ofNullable(REDIS_PORTAL_GETTER.apply(type))
                    .filter(bvs -> bvs.size() > 0)
                    .orElseGet(() -> {
                        RLock lock = redissonClient.getLock(PORTAL_LOAD_SYNC_KEY_GEN.apply(type));
                        boolean tryLock = true;
                        try {
                            tryLock = lock.tryLock();
                            if (tryLock) {
                                List<BulletinInfo> vos = DB_PORTAL_GETTER.apply(type);
                                REDIS_PORTAL_SETTER.accept(type, vos);
                                return vos;
                            }

                            long start = currentTimeMillis();
                            while (!(tryLock = lock.tryLock()) && currentTimeMillis() - start <= MAX_WAIT_MILLIS_FOR_REDISSON_SYNC.value)
                                onSpinWait();

                            return tryLock ? REDIS_PORTAL_GETTER.apply(type) : emptyList();
                        } catch (Exception e) {
                            return emptyList();
                        } finally {
                            if (tryLock)
                                try {
                                    lock.unlock();
                                } catch (Exception e) {
                                    LOGGER.warn("REDIS_PORTAL_GETTER_WITH_CACHE, lock.unlock() failed, e = {}", e);
                                }
                        }
                    });


    /**
     * list bulletin infos from local cache
     */
    private final Function<BulletinType, Mono<List<BulletinInfo>>> LOCAL_CACHE_PORTAL_FUNC = type -> {
        if (isNull(type))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "type can't be null");

        return justOrEmpty(LOCAL_CACHE.get(type, REDIS_PORTAL_GETTER_WITH_CACHE)).switchIfEmpty(just(emptyList()));
    };

    /**
     * refresh bulletin infos
     *
     * @return
     */
    @Override
    public void invalidBulletinInfosCache() {
        of(BulletinType.values())
                .forEach(type -> {
                    stringRedisTemplate.delete(BULLETIN_CACHE_KEY_GENERATOR.apply(type));
                    LOCAL_CACHE.invalidate(type);
                });
    }

    /**
     * list bulletin infos
     *
     * @param bulletinType
     * @return
     */
    @Override
    public Mono<List<BulletinInfo>> selectBulletinInfo(Integer bulletinType) {
        LOGGER.info("listBulletin(BulletinType bulletinType), bulletinType = {}", bulletinType);
        return LOCAL_CACHE_PORTAL_FUNC.apply(TYPE_CONVERTER.apply(bulletinType));
    }

}
