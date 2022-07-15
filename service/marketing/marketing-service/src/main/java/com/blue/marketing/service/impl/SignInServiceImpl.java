package com.blue.marketing.service.impl;

import com.blue.basic.constant.marketing.BlueMarketingThreshold;
import com.blue.basic.model.exps.BlueException;
import com.blue.marketing.api.model.*;
import com.blue.marketing.config.deploy.BlockingDeploy;
import com.blue.marketing.event.producer.MarketingEventProducer;
import com.blue.marketing.repository.entity.Reward;
import com.blue.marketing.repository.entity.SignRewardTodayRelation;
import com.blue.marketing.service.inter.RewardService;
import com.blue.marketing.service.inter.SignInService;
import com.blue.redis.component.BlueBitMarker;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.CacheKeyPrefix.SIGN_IN_PRE;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Symbol.PAR_CONCATENATION;
import static com.blue.basic.constant.marketing.MarketingEventType.SIGN_IN_REWARD;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
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

    private final BlueBitMarker blueBitMarker;

    private final MarketingEventProducer marketingEventProducer;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SignInServiceImpl(RewardService rewardService, BlueBitMarker blueBitMarker,
                             MarketingEventProducer marketingEventProducer, BlockingDeploy blockingDeploy) {
        this.rewardService = rewardService;
        this.blueBitMarker = blueBitMarker;
        this.marketingEventProducer = marketingEventProducer;

        MAX_WAITING_FOR_REFRESH = blockingDeploy.getMillis();
        LocalDate now = LocalDate.now();
        DAY_REWARD_INITIALIZER.accept(now.getYear(), now.getMonthValue());
    }

    private static final int CURRENT_MONTH_RECORD_IDX = 0;

    /**
     * sign in redis key expire/day
     */
    private static final int MAX_EXPIRE_DAYS_FOR_SIGN = (int) BlueMarketingThreshold.MAX_EXPIRE_DAYS_FOR_SIGN.value;

    private static final int SECONDS_OF_DAY = 60 * 60 * 24;

    private volatile boolean rewardInfoRefreshing = false;

    private static long MAX_WAITING_FOR_REFRESH;

    /**
     * current month
     */
    private static volatile int CURRENT_MONTH;

    private static volatile Map<Integer, SignInReward> TODAY_REWARD_MAPPING;

    private static final Function<Reward, SignInReward> REWARD_CONVERTER = r ->
            new SignInReward(isNotNull(r) ? new RewardInfo(r.getId(), r.getName(), r.getDetail(), r.getLink()) : null);

    private final BiConsumer<Integer, Integer> DAY_REWARD_INITIALIZER = (year, month) -> {
        List<SignRewardTodayRelation> relations = rewardService.selectRelationByYearAndMonth(year, month);
        if (isEmpty(relations))
            throw new RuntimeException("The reward information of the current month is not configured");

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
                    throw new BlueException(REQUEST_TIMEOUT);
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
        return SIGN_IN_PRE.prefix + memberId + PAR_CONCATENATION.identity + year + PAR_CONCATENATION.identity + month;
    }

    private static final int MIN_OFFSET = 1;

    private static final BiFunction<Integer, Boolean, SignInRewardRecord> DAY_REWARD_RECORD_GENERATOR = (day, sign) ->
            new SignInRewardRecord(TODAY_REWARD_MAPPING.get(day), sign);

    private static final BiFunction<Long, Integer, MonthSignInRewardRecord> MONTH_REWARD_RECORD_GENERATOR = (record, lengthOfMonth) -> {
        Map<Integer, SignInRewardRecord> recordInfo = new TreeMap<>();
        int total = 0;

        boolean signed;
        long bit = 1L;

        for (int d = lengthOfMonth; d >= MIN_OFFSET; d--) {
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
    public Mono<SignInReward> signIn(Long memberId) {
        LOGGER.info("Mono<SignInReward> signIn(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        String key = generateSignKey(memberId, year, month);
        int dayOfMonth = now.getDayOfMonth();

        LOGGER.info("key = {}, year = {}, month = {}, dayOfMonth = {}", key, year, month, dayOfMonth);

        return blueBitMarker.getBit(key, dayOfMonth)
                .flatMap(b -> b ?
                        error(new BlueException(REPEAT_SIGN_IN))
                        :
                        blueBitMarker.setBit(key, dayOfMonth, true, (MAX_EXPIRE_DAYS_FOR_SIGN - dayOfMonth) * SECONDS_OF_DAY)
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
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        LocalDate now = LocalDate.now();
        int lengthOfMonth = now.lengthOfMonth();

        return blueBitMarker.getBitsValueByLimitUpTo64(generateSignKey(memberId, now.getYear(), now.getMonthValue()), lengthOfMonth)
                .flatMap(records -> {
                            MonthSignInRewardRecord monthSignInRewardRecord =
                                    MONTH_REWARD_RECORD_GENERATOR.apply(records.get(CURRENT_MONTH_RECORD_IDX), lengthOfMonth);
                            LOGGER.info("memberId = {}, monthRecordDTO = {}", memberId, monthSignInRewardRecord);
                            return just(monthSignInRewardRecord);
                        }
                );
    }

}
