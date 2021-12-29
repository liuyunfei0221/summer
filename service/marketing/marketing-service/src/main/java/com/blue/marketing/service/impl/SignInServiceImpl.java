package com.blue.marketing.service.impl;

import com.blue.base.constant.base.BlueNumericalValue;
import com.blue.base.constant.base.BlueCacheKey;
import com.blue.base.constant.base.Symbol;
import com.blue.base.model.base.KeyExpireParam;
import com.blue.base.model.exps.BlueException;
import com.blue.marketing.api.model.*;
import com.blue.marketing.config.deploy.BlockingDeploy;
import com.blue.marketing.event.producer.MarketingEventProducer;
import com.blue.marketing.event.producer.SignExpireProducer;
import com.blue.marketing.repository.entity.Reward;
import com.blue.marketing.repository.entity.SignRewardTodayRelation;
import com.blue.marketing.service.inter.RewardService;
import com.blue.marketing.service.inter.SignInService;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.base.common.base.Asserter.isEmpty;
import static com.blue.base.common.base.Asserter.isInvalidIdentity;
import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static com.blue.base.constant.marketing.MarketingEventType.SIGN_IN_REWARD;
import static com.blue.marketing.constant.MarketingCommonException.REPEAT_SIGN_IN_EXP;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
import static java.nio.ByteBuffer.wrap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * sign in service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class SignInServiceImpl implements SignInService {

    private static final Logger LOGGER = getLogger(SignInServiceImpl.class);

    private RewardService rewardService;

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private final SignExpireProducer signExpireProducer;

    private final MarketingEventProducer marketingEventProducer;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SignInServiceImpl(RewardService rewardService, ReactiveStringRedisTemplate reactiveStringRedisTemplate,
                             SignExpireProducer signExpireProducer, MarketingEventProducer marketingEventProducer, BlockingDeploy blockingDeploy) {
        this.rewardService = rewardService;
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.signExpireProducer = signExpireProducer;
        this.marketingEventProducer = marketingEventProducer;

        MAX_WAITING_FOR_REFRESH = blockingDeploy.getMillis();
        LocalDate now = LocalDate.now();
        DAY_REWARD_INITIALIZER.accept(now.getYear(), now.getMonthValue());
    }

    /**
     * sign in redis key expire/day
     */
    private static final int MAX_EXPIRE_DAYS_FOR_SIGN = (int) BlueNumericalValue.MAX_EXPIRE_DAYS_FOR_SIGN.value;

    private static final ChronoUnit EXPIRE_UNIT = ChronoUnit.DAYS;

    private static volatile boolean rewardInfoRefreshing = false;

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

    private static final Supplier<String> BLOCKER = () -> {
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

    private static final Function<Integer, SignInReward> TODAY_REWARD_GETTER = day -> {
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

    private final BiFunction<String, Long, Mono<Boolean>> BITMAP_TRUE_BIT_SETTER = (key, offset) ->
            reactiveStringRedisTemplate.opsForValue().setBit(key, offset, true);

    private static final BiFunction<Integer, Boolean, SignInRewardRecord> DAY_REWARD_RECORD_GENERATOR = (day, sign) ->
            new SignInRewardRecord(TODAY_REWARD_MAPPING.get(day), sign);

    private static final BiFunction<Long, Integer, MonthSignInRewardRecord> MONTH_REWARD_RECORD_GENERATOR = (record, lengthOfMonth) -> {
        Map<Integer, SignInRewardRecord> recordInfo = new TreeMap<>();
        int total = 0;

        boolean signed;
        long bit = 1L;

        for (int d = lengthOfMonth; d > 0; d--) {
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
        LOGGER.info("insertSignIn(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        String key = generateSignKey(memberId, year, month);
        int dayOfMonth = now.getDayOfMonth();

        LOGGER.info("key = {}, year = {}, month = {}, dayOfMonth = {}", key, year, month, dayOfMonth);

        return BITMAP_BIT_GETTER.apply(key, (long) dayOfMonth)
                .flatMap(b -> {
                    if (b)
                        return error(REPEAT_SIGN_IN_EXP.exp);

                    return BITMAP_TRUE_BIT_SETTER.apply(key, (long) dayOfMonth)
                            .flatMap(f -> {
                                if (f)
                                    return error(REPEAT_SIGN_IN_EXP.exp);

                                try {
                                    signExpireProducer.send(new KeyExpireParam(key, (long) (MAX_EXPIRE_DAYS_FOR_SIGN - dayOfMonth), EXPIRE_UNIT));
                                } catch (Exception e) {
                                    LOGGER.error("sign in key expire failed, key = {}, expire = {}, e = {}", key, (MAX_EXPIRE_DAYS_FOR_SIGN - dayOfMonth), e);
                                }

                                return just(getDayReward(year, month, dayOfMonth));
                            })
                            .flatMap(signInReward -> {
                                try {
                                    LOGGER.info("sign in success, memberId = {}, year = {}, month = {}, day of month = {}, reward = {}",
                                            memberId, year, month, dayOfMonth, signInReward);
                                    marketingEventProducer.send(new MarketingEvent(SIGN_IN_REWARD, memberId,
                                            GSON.toJson(new SignRewardEvent(memberId, year, month, dayOfMonth, signInReward)), TIME_STAMP_GETTER.get()));
                                } catch (Exception e) {
                                    LOGGER.info("sign in failed, memberId = {}, year = {}, month = {}, day of month = {}, reward = {}, e = {}",
                                            memberId, year, month, dayOfMonth, signInReward, e);
                                }
                                return just(signInReward);
                            });
                });
    }

    /**
     * query sign in records
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<MonthSignInRewardRecord> getSignInRecord(Long memberId) {
        LOGGER.info("getSignInRecord(Long memberId), memberId = {}", memberId);

        LocalDate now = LocalDate.now();
        int lengthOfMonth = now.lengthOfMonth();

        LOGGER.info("memberId = {}, lengthOfMonth = {}", memberId, lengthOfMonth);

        return BITMAP_LIMIT_GETTER.apply(generateSignKey(memberId, now.getYear(), now.getMonthValue()), lengthOfMonth)
                .flatMap(record -> {
                            MonthSignInRewardRecord monthSignInRewardRecord = MONTH_REWARD_RECORD_GENERATOR.apply(record, lengthOfMonth);
                            LOGGER.info("memberId = {}, monthRecordDTO = {}", memberId, monthSignInRewardRecord);
                            return just(monthSignInRewardRecord);
                        }
                );
    }

}
