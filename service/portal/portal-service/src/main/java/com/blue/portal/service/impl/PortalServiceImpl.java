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
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.createCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static java.lang.Thread.onSpinWait;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.just;

/**
 * 门户业务实现
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

    /**
     * po -> vo 转换器
     */
    private static final Function<List<Bulletin>, List<BulletinInfo>> VO_LIST_CONVERTER = bl ->
            bl != null && bl.size() > 0 ? bl.stream()
                    .map(b -> new BulletinInfo(b.getId(), b.getTitle(), b.getContent(), b.getLink(), b.getType()))
                    .collect(toList()) : emptyList();

    /**
     * 本地缓存器
     */
    private static Cache<BulletinType, List<BulletinInfo>> LOCAL_CACHE;

    /**
     * 公告类型转换
     */
    private static final Function<String, BulletinType> TYPE_CONVERTER = typeStr -> {
        if (StringUtils.isBlank(typeStr)) {
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "公告类型不能为空");
        }

        int type;
        try {
            type = Integer.parseInt(typeStr);
        } catch (NumberFormatException e) {
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "公告类型不合法");
        }

        return getBulletinTypeByIdentity(type);
    };

    @PostConstruct
    public void init() {
        redisExpire = blueRedisConfig.getEntryTtl();
        CaffeineConf caffeineConf = new CaffeineConfParams(
                caffeineDeploy.getMaximumSize(), Duration.of(caffeineDeploy.getExpireSeconds(), ChronoUnit.SECONDS),
                AFTER_WRITE, executorService);

        LOCAL_CACHE = createCache(caffeineConf);
        Stream.of(BulletinType.values())
                .forEach(this::getBulletinFromLocalCache);
    }

    /**
     * 由数据库中获取
     *
     * @param bulletinType
     * @return
     */
    private List<BulletinInfo> getBulletinFromDataBase(BulletinType bulletinType) {
        List<Bulletin> bulletins = bulletinService.listBulletin(bulletinType);
        LOGGER.info("由数据库中获取公告列表,bulletins = {}", bulletins);
        return VO_LIST_CONVERTER.apply(bulletins);
    }

    /**
     * 由redis获取公告列表
     */
    private final Function<BulletinType, List<BulletinInfo>> REDIS_CACHE_PORTAL_FUNC = type -> {
        List<String> bulletins = ofNullable(stringRedisTemplate.opsForList().range(ofNullable(type)
                .map(t -> PORTALS_PRE.key + t.identity)
                .orElse(PORTALS_PRE.key + BulletinType.POPULAR.identity), 0, -1))
                .orElse(emptyList());
        LOGGER.info("由redis中获取公告列表,bulletins = {}", bulletins);
        return bulletins
                .stream().map(s -> GSON.fromJson(s, BulletinInfo.class)).collect(toList());
    };

    /**
     * 将公告列表缓存到redis
     */
    private final BiConsumer<BulletinType, List<BulletinInfo>> REDIS_CACHE_PORTAL_CACHER = (type, list) -> {
        if (type != null && !CollectionUtils.isEmpty(list)) {
            String key = PORTALS_PRE.key + type.identity;
            stringRedisTemplate.opsForList().rightPushAll(key, list.stream().map(GSON::toJson).collect(toList()));
            stringRedisTemplate.expire(key, redisExpire, EXPIRE_UNIT);
            LOGGER.info("将公告列表缓存到redis,key = {},list = {}", key, list);
        }
    };

    /**
     * 由redis缓存中获取
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

                        long start = System.currentTimeMillis();
                        while (!(tryLock = lock.tryLock()) && System.currentTimeMillis() - start <= MAX_WAIT_MILLIS_FOR_REDISSON_SYNC)
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
     * 由本地缓存获取公告列表
     */
    private final Function<BulletinType, List<BulletinInfo>> LOCAL_CACHE_PORTAL_FUNC = type -> {
        List<BulletinInfo> bulletins = LOCAL_CACHE.get(type, this::getBulletinFromRedisCache);
        LOGGER.info("由本地缓存中获取公告列表,bulletins = {}", bulletins);
        return bulletins;
    };

    /**
     * 由本地缓存中获取
     *
     * @param bulletinType
     * @return
     */
    private List<BulletinInfo> getBulletinFromLocalCache(BulletinType bulletinType) {
        return LOCAL_CACHE_PORTAL_FUNC.apply(bulletinType);
    }

    /**
     * 获取公告信息
     *
     * @param bulletinType
     * @return
     */
    @Override
    public Mono<List<BulletinInfo>> listBulletin(String bulletinType) {
        LOGGER.info("listBulletin(BulletinType bulletinType), bulletinType = {}", bulletinType);

        List<BulletinInfo> vos = ofNullable(getBulletinFromLocalCache(TYPE_CONVERTER.apply(bulletinType))).orElse(emptyList());
        LOGGER.info("vos = {}", vos);
        return just(vos);
    }

}
