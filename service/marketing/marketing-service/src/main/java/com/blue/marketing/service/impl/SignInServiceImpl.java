package com.blue.marketing.service.impl;

import com.blue.base.constant.base.BlueCacheKey;
import com.blue.base.constant.base.BlueNumericalValue;
import com.blue.base.constant.base.Symbol;
import com.blue.base.model.exps.BlueException;
import com.blue.marketing.api.model.*;
import com.blue.marketing.config.deploy.BlockingDeploy;
import com.blue.marketing.event.producer.MarketingEventProducer;
import com.blue.marketing.repository.entity.Reward;
import com.blue.marketing.repository.entity.SignRewardTodayRelation;
import com.blue.marketing.service.inter.RewardService;
import com.blue.marketing.service.inter.SignInService;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.marketing.MarketingEventType.SIGN_IN_REWARD;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.SET_BIT_WITH_EXPIRE;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
import static java.nio.ByteBuffer.wrap;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * sign in service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class SignInServiceImpl implements SignInService {

    private static final Logger LOGGER = getLogger(SignInServiceImpl.class);

    private RewardService rewardService;

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private final MarketingEventProducer marketingEventProducer;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SignInServiceImpl(RewardService rewardService, ReactiveStringRedisTemplate reactiveStringRedisTemplate,
                             MarketingEventProducer marketingEventProducer, BlockingDeploy blockingDeploy) {
        this.rewardService = rewardService;
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.marketingEventProducer = marketingEventProducer;

        MAX_WAITING_FOR_REFRESH = blockingDeploy.getMillis();
        LocalDate now = LocalDate.now();
        DAY_REWARD_INITIALIZER.accept(now.getYear(), now.getMonthValue());
    }

    /**
     * sign in redis key expire/day
     */
    private static final int MAX_EXPIRE_DAYS_FOR_SIGN = (int) BlueNumericalValue.MAX_EXPIRE_DAYS_FOR_SIGN.value;

    private static final int SECONDS_OF_DAY = 60 * 60 * 24;

    private volatile boolean rewardInfoRefreshing = false;

    private static long MAX_WAITING_FOR_REFRESH;

    /**
     * current month
     */
    private static volatile int CURRENT_MONTH;

    private static volatile Map<Integer, SignInReward> TODAY_REWARD_MAPPING;

    private static final String SIGN_IN_KEY_PREFIX = BlueCacheKey.SIGN_IN_KEY_PRE.key;

    private static final String PAR_CONCATENATION = Symbol.PAR_CONCATENATION.identity;

    private static final Function<Reward, SignInReward> REWARD_CONVERTER = r ->
            new SignInReward(r != null ? new RewardInfo(r.getId(), r.getName(), r.getDetail(), r.getLink()) : null);

    private final BiConsumer<Integer, Integer> DAY_REWARD_INITIALIZER = (year, month) -> {
        List<SignRewardTodayRelation> relations = rewardService.selectRelationByYearAndMonth(year, month);
        if (isEmpty(relations))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "The reward information of the current month is not configured");

        List<Reward> rewards = rewardService.selectRewardByIds(relations.stream()
                .map(SignRewardTodayRelation::getRewardId).collect(toList()));
        Map<Long, Reward> rewardMap = rewards.stream().collect(toMap(Reward::getId, r -> r, (a, b) -> a));

        LocalDate currentDate = LocalDate.of(year, month, 1);
        int dayOfMonth = currentDate.lengthOfMonth();

        Map<Integer, SignInReward> tempMapping = relations.stream()
                .collect(toMap(SignRewardTodayRelation::getDay, r ->
                        REWARD_CONVERTER.apply(rewardMap.get(r.getRewardId())), (a, b) -> b));

        Map<Integer, SignInReward> relationMapping = new HashMap<>(dayOfMonth);
        for (int i = 1; i <= dayOfMonth; i++)
            relationMapping.put(i, ofNullable(tempMapping.get(i)).orElseGet(() -> new SignInReward(null)));

        rewardInfoRefreshing = true;
        TODAY_REWARD_MAPPING = relationMapping;
        CURRENT_MONTH = month;
        rewardInfoRefreshing = false;

        //noinspection UnusedAssignment
        tempMapping = null;
    };

    private final Supplier<String> BLOCKER = () -> {
        if (rewardInfoRefreshing) {
            long start = currentTimeMillis();
            while (rewardInfoRefreshing) {
                if (currentTimeMillis() - start > MAX_WAITING_FOR_REFRESH)
                    throw new BlueException(INTERNAL_SERVER_ERROR);
                onSpinWait();
            }
        }
        return "refreshed";
    };

    private final Function<Integer, SignInReward> TODAY_REWARD_GETTER = day -> {
        BLOCKER.get();
        return TODAY_REWARD_MAPPING.get(day);
    };

    /**
     * get reward by date
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    private SignInReward getDayReward(int year, int month, int day) {
        if (CURRENT_MONTH != month)
            synchronized (SignInServiceImpl.class) {
                if (CURRENT_MONTH != month)
                    DAY_REWARD_INITIALIZER.accept(year, month);
            }
        return TODAY_REWARD_GETTER.apply(day);
    }

    /**
     * generate sign in key
     *
     * @param memberId
     * @param year
     * @param month
     * @return
     */
    private static String generateSignKey(long memberId, int year, int month) {
        return SIGN_IN_KEY_PREFIX + memberId + PAR_CONCATENATION + year + PAR_CONCATENATION + month;
    }

    private static final long MARK_BIT = 1L;
    private static final int BIT_TRUE = 1;
    private static final int DAY_0F_MONTH_START = 1;

    private static final int FLUX_ELEMENT_INDEX = 0, LIST_ELEMENT_INDEX = 0;

    private final BiFunction<String, Integer, Mono<Long>> BITMAP_LIMIT_GETTER = (key, limit) ->
            reactiveStringRedisTemplate.execute(con ->
                            con.stringCommands()
                                    .bitField(wrap(key.getBytes()),
                                            BitFieldSubCommands.create()
                                                    .get(BitFieldSubCommands.BitFieldType.signed(limit))
                                                    .valueAt(MARK_BIT)))
                    .elementAt(FLUX_ELEMENT_INDEX)
                    .flatMap(l -> just(l.get(LIST_ELEMENT_INDEX)));

    private final BiFunction<String, Long, Mono<Boolean>> BITMAP_BIT_GETTER = (key, offset) ->
            reactiveStringRedisTemplate.opsForValue().getBit(key, offset);

    private static final RedisScript<Boolean> BIT_SET_SCRIPT = generateScriptByScriptStr(SET_BIT_WITH_EXPIRE.str, Boolean.class);

    private static final Function<String, List<String>> SCRIPT_KEYS_WRAPPER = Arrays::asList;

    private static final BiFunction<Integer, Integer, List<String>> SCRIPT_ARGS_WRAPPER = (offset, bit) ->
            asList(String.valueOf(offset), String.valueOf(bit), String.valueOf((MAX_EXPIRE_DAYS_FOR_SIGN - offset) * SECONDS_OF_DAY));

    private final BiFunction<String, Integer, Mono<Boolean>> BITMAP_TRUE_BIT_SETTER = (key, offset) ->
            reactiveStringRedisTemplate.execute(BIT_SET_SCRIPT, SCRIPT_KEYS_WRAPPER.apply(key),
                            SCRIPT_ARGS_WRAPPER.apply(offset, BIT_TRUE))
                    .elementAt(FLUX_ELEMENT_INDEX);

    private static final BiFunction<Integer, Boolean, SignInRewardRecord> DAY_REWARD_RECORD_GENERATOR = (day, sign) ->
            new SignInRewardRecord(TODAY_REWARD_MAPPING.get(day), sign);

    private static final BiFunction<Long, Integer, MonthSignInRewardRecord> MONTH_REWARD_RECORD_GENERATOR = (record, lengthOfMonth) -> {
        Map<Integer, SignInRewardRecord> recordInfo = new TreeMap<>();
        int total = 0;

        boolean signed;
        long bit = 1L;

        for (int d = lengthOfMonth; d >= DAY_0F_MONTH_START; d--) {
            signed = (record & bit) != 0L;
            bit <<= 1;

            recordInfo.put(d, DAY_REWARD_RECORD_GENERATOR.apply(d, signed));
            if (signed)
                total++;
        }

        return new MonthSignInRewardRecord(recordInfo, total);
    };

    /**
     * sign in today
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<SignInReward> insertSignIn(Long memberId) {
        LOGGER.info("Mono<SignInReward> insertSignIn(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        String key = generateSignKey(memberId, year, month);
        int dayOfMonth = now.getDayOfMonth();

        LOGGER.info("key = {}, year = {}, month = {}, dayOfMonth = {}", key, year, month, dayOfMonth);

        return BITMAP_BIT_GETTER.apply(key, (long) dayOfMonth)
                .flatMap(b -> b ?
                        error(new BlueException(REPEAT_SIGN_IN))
                        :
                        BITMAP_TRUE_BIT_SETTER.apply(key, dayOfMonth)
                                .flatMap(f -> f ?
                                        just(getDayReward(year, month, dayOfMonth))
                                        :
                                        error(() -> new BlueException(REPEAT_SIGN_IN))
                                )
                                .flatMap(signInReward -> {
                                    MarketingEvent marketingEvent = new MarketingEvent(SIGN_IN_REWARD, memberId,
                                            GSON.toJson(new SignRewardEvent(memberId, year, month, dayOfMonth, signInReward)), TIME_STAMP_GETTER.get());
                                    try {
                                        LOGGER.info("sign in success, marketingEvent = {}", marketingEvent);
                                        marketingEventProducer.send(marketingEvent);
                                    } catch (Exception e) {
                                        LOGGER.error("marketingEventProducer send sign event failed, marketingEvent = {}, e = {}", marketingEvent, e);
                                    } finally {
                                        //noinspection UnusedAssignment
                                        marketingEvent = null;
                                    }
                                    return just(signInReward);
                                })
                );
    }

    /**
     * query sign in records
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<MonthSignInRewardRecord> getSignInRecord(Long memberId) {
        LOGGER.info("Mono<MonthSignInRewardRecord> getSignInRecord(Long memberId), memberId = {}", memberId);

        LocalDate now = LocalDate.now();
        int lengthOfMonth = now.lengthOfMonth();

        return BITMAP_LIMIT_GETTER.apply(generateSignKey(memberId, now.getYear(), now.getMonthValue()), lengthOfMonth)
                .flatMap(record -> {
                            MonthSignInRewardRecord monthSignInRewardRecord = MONTH_REWARD_RECORD_GENERATOR.apply(record, lengthOfMonth);
                            LOGGER.info("memberId = {}, monthRecordDTO = {}", memberId, monthSignInRewardRecord);
                            return just(monthSignInRewardRecord);
                        }
                );
    }

}
