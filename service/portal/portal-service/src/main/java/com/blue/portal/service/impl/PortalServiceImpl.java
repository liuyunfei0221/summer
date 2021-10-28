package com.blue.portal.service.impl;

import com.blue.base.common.base.CommonFunctions;
import com.blue.base.constant.base.BlueNumericalValue;
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
import com.google.gson.Gson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.base.common.base.ConstantProcessor.getBulletinTypeByIdentity;
import static com.blue.base.constant.base.CacheKey.PORTALS_PRE;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.SyncKey.PORTALS_REFRESH_PRE;
import static com.blue.base.constant.portal.BulletinType.POPULAR;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static java.lang.Integer.parseInt;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.core.publisher.Mono.just;

/**
 * portal service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces", "CatchMayIgnoreException"})
@Service
public class PortalServiceImpl implements PortalService {

    private static final Logger LOGGER = Loggers.getLogger(PortalServiceImpl.class);

    private final BulletinService bulletinService;

    private final ExecutorService executorService;

    private StringRedisTemplate stringRedisTemplate;

    private final RedissonClient redissonClient;

    private final BlueRedisConfig blueRedisConfig;

    private final CaffeineDeploy caffeineDeploy;

    private static long redisExpire;

    private final static TimeUnit EXPIRE_UNIT = TimeUnit.SECONDS;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public PortalServiceImpl(BulletinService bulletinService, ExecutorService executorService, StringRedisTemplate stringRedisTemplate,
                             RedissonClient redissonClient, BlueRedisConfig blueRedisConfig, CaffeineDeploy caffeineDeploy) {
        this.bulletinService = bulletinService;
        this.executorService = executorService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redissonClient = redissonClient;
        this.blueRedisConfig = blueRedisConfig;
        this.caffeineDeploy = caffeineDeploy;
    }

    private static final Gson GSON = CommonFunctions.GSON;

    private static final long WAIT_MILLIS_FOR_THREAD_SLEEP = BlueNumericalValue.WAIT_MILLIS_FOR_THREAD_SLEEP.value;
    private static final long MAX_WAIT_MILLIS_FOR_REDISSON_SYNC = BlueNumericalValue.MAX_WAIT_MILLIS_FOR_REDISSON_SYNC.value;

    private static final Function<List<Bulletin>, List<BulletinInfo>> VO_LIST_CONVERTER = bl ->
            bl != null && bl.size() > 0 ? bl.stream()
                    .map(b -> new BulletinInfo(b.getId(), b.getTitle(), b.getContent(), b.getLink(), b.getType()))
                    .collect(toList()) : emptyList();

    private static Cache<BulletinType, List<BulletinInfo>> LOCAL_CACHE;

    private static final Function<String, BulletinType> TYPE_CONVERTER = typeStr -> {
        if (isBlank(typeStr)) {
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "typeStr can't be blank");
        }

        int type;
        try {
            type = parseInt(typeStr);
        } catch (NumberFormatException e) {
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid bulletin type");
        }

        return getBulletinTypeByIdentity(type);
    };

    private static final Function<BulletinType, String> BULLETIN_CACHE_KEY_GENERATOR = type -> PORTALS_PRE.key + type.identity;

    @PostConstruct
    public void init() {
        redisExpire = blueRedisConfig.getEntryTtl();
        CaffeineConf caffeineConf = new CaffeineConfParams(
                caffeineDeploy.getMaximumSize(), Duration.of(caffeineDeploy.getExpireSeconds(), ChronoUnit.SECONDS),
                AFTER_WRITE, executorService);

        LOCAL_CACHE = generateCache(caffeineConf);
        Stream.of(BulletinType.values())
                .forEach(this::getBulletinFromLocalCache);
    }

    /**
     * refresh bulletin infos
     *
     * @return
     */
    @Override
    public void invalidBulletinInfosCache() {
        of(BulletinType.values())
                .forEach(type -> stringRedisTemplate.delete(BULLETIN_CACHE_KEY_GENERATOR.apply(type)));

        of(BulletinType.values())
                .forEach(LOCAL_CACHE::invalidate);
    }

    /**
     * list bulletin infos from db
     *
     * @param bulletinType
     * @return
     */
    private List<BulletinInfo> getBulletinFromDataBase(BulletinType bulletinType) {
        List<Bulletin> bulletins = bulletinService.selectTargetActiveBulletinByType(bulletinType);
        LOGGER.info("getBulletinFromDataBase(BulletinType bulletinType), bulletins = {}", bulletins);
        return VO_LIST_CONVERTER.apply(bulletins);
    }

    /**
     * list bulletin infos from redis
     */
    private final Function<BulletinType, List<BulletinInfo>> REDIS_CACHE_PORTAL_FUNC = type -> {
        List<String> bulletins = ofNullable(stringRedisTemplate.opsForList().range(ofNullable(type)
                .map(BULLETIN_CACHE_KEY_GENERATOR)
                .orElse(PORTALS_PRE.key + POPULAR.identity), 0, -1))
                .orElse(emptyList());
        LOGGER.info("REDIS_CACHE_PORTAL_FUNC, type = {}, bulletins = {}", type, bulletins);
        return bulletins
                .stream().map(s -> GSON.fromJson(s, BulletinInfo.class)).collect(toList());
    };


    /**
     * set bulletin info to redis
     */
    private final BiConsumer<BulletinType, List<BulletinInfo>> REDIS_CACHE_PORTAL_CACHER = (type, list) -> {
        if (type != null && !isEmpty(list)) {
            String key = BULLETIN_CACHE_KEY_GENERATOR.apply(type);
            stringRedisTemplate.opsForList().rightPushAll(key, list.stream().map(GSON::toJson).collect(toList()));
            stringRedisTemplate.expire(key, redisExpire, EXPIRE_UNIT);
            LOGGER.info("REDIS_CACHE_PORTAL_CACHER, key = {},list = {}", key, list);
        }
    };

    /**
     * list bulletin infos from redis
     *
     * @param bulletinType
     * @return
     */
    private List<BulletinInfo> getBulletinFromRedisCache(BulletinType bulletinType) {
        return ofNullable(REDIS_CACHE_PORTAL_FUNC.apply(bulletinType))
                .filter(bvs -> bvs.size() > 0)
                .orElseGet(() -> {
                    String syncKey = PORTALS_REFRESH_PRE.key + bulletinType.identity;
                    RLock lock = redissonClient.getLock(syncKey);
                    boolean tryLock = true;
                    try {
                        tryLock = lock.tryLock();
                        if (tryLock) {
                            List<BulletinInfo> vos = getBulletinFromDataBase(bulletinType);
                            REDIS_CACHE_PORTAL_CACHER.accept(bulletinType, vos);
                            return vos;
                        }

                        long start = currentTimeMillis();
                        while (!(tryLock = lock.tryLock()) && currentTimeMillis() - start <= MAX_WAIT_MILLIS_FOR_REDISSON_SYNC)
                            onSpinWait();

                        return tryLock ? REDIS_CACHE_PORTAL_FUNC.apply(bulletinType) : emptyList();
                    } catch (Exception e) {
                        return emptyList();
                    } finally {
                        if (tryLock)
                            try {
                                lock.unlock();
                            } catch (Exception e) {
                            }
                    }
                });
    }

    /**
     * list bulletin infos from local cache
     */
    private final Function<BulletinType, List<BulletinInfo>> LOCAL_CACHE_PORTAL_FUNC = type -> {
        List<BulletinInfo> bulletins = LOCAL_CACHE.get(type, this::getBulletinFromRedisCache);
        LOGGER.info("LOCAL_CACHE_PORTAL_FUNC, type = {}, bulletins = {}", type, bulletins);
        return bulletins;
    };

    /**
     * list bulletin infos from local cache
     *
     * @param bulletinType
     * @return
     */
    private List<BulletinInfo> getBulletinFromLocalCache(BulletinType bulletinType) {
        return LOCAL_CACHE_PORTAL_FUNC.apply(bulletinType);
    }

    /**
     * list bulletin infos
     *
     * @param bulletinType
     * @return
     */
    @Override
    public Mono<List<BulletinInfo>> selectBulletinInfo(String bulletinType) {
        LOGGER.info("listBulletin(BulletinType bulletinType), bulletinType = {}", bulletinType);

        List<BulletinInfo> vos = ofNullable(getBulletinFromLocalCache(TYPE_CONVERTER.apply(bulletinType))).orElse(emptyList());
        LOGGER.info("vos = {}", vos);
        return just(vos);
    }

}
