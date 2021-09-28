package com.blue.marketing.service.impl;

import com.blue.base.common.base.CommonFunctions;
import com.blue.base.constant.base.BlueNumericalValue;
import com.blue.base.constant.base.CacheKey;
import com.blue.base.constant.base.Symbol;
import com.blue.base.model.exps.BlueException;
import com.blue.base.model.redis.KeyExpireParam;
import com.blue.marketing.api.model.*;
import com.blue.marketing.config.deploy.BlockingDeploy;
import com.blue.marketing.config.mq.producer.MarketingEventProducer;
import com.blue.marketing.config.mq.producer.SignExpireProducer;
import com.blue.marketing.repository.entity.Reward;
import com.blue.marketing.repository.entity.SignRewardTodayRelation;
import com.blue.marketing.service.inter.RewardService;
import com.blue.marketing.service.inter.SignService;
import com.google.gson.Gson;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.nio.ByteBuffer;
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

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;
import static com.blue.base.constant.marketing.MarketingEventType.SIGN_IN_REWARD;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * 签到业务实现
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class SignServiceImpl implements SignService {

    private static final Logger LOGGER = getLogger(SignServiceImpl.class);

    private RewardService rewardService;

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private final SignExpireProducer signExpireProducer;

    private final MarketingEventProducer marketingEventProducer;

    private final BlockingDeploy blockingDeploy;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SignServiceImpl(RewardService rewardService, ReactiveStringRedisTemplate reactiveStringRedisTemplate,
                           SignExpireProducer signExpireProducer, MarketingEventProducer marketingEventProducer, BlockingDeploy blockingDeploy) {
        this.rewardService = rewardService;
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.signExpireProducer = signExpireProducer;
        this.marketingEventProducer = marketingEventProducer;
        this.blockingDeploy = blockingDeploy;
    }

    private static final Gson GSON = CommonFunctions.GSON;

    private static final Supplier<Long> TIME_STAMP_GETTER = CommonFunctions.TIME_STAMP_GETTER;

    /**
     * 月签到信息最大过期时间/天
     */
    private static final int MAX_EXPIRE_DAYS_FOR_SIGN = (int) BlueNumericalValue.MAX_EXPIRE_DAYS_FOR_SIGN.value;

    /**
     * 签到信息过期时间单位
     */
    private static final ChronoUnit EXPIRE_UNIT = ChronoUnit.DAYS;

    /**
     * 奖励信息是否刷新中的标记位
     */
    private static volatile boolean rewardInfoRefreshing = false;

    /**
     * 刷新资源时最大等待时间
     */
    private static long MAX_WAITING_FOR_REFRESH;

    /**
     * 当前月份
     */
    private static volatile int MONTH;

    /**
     * 当日签到奖励信息映射
     */
    private static volatile Map<Integer, DayReward> TODAY_REWARD_MAPPING;

    /**
     * 签到key前缀
     */
    private static final String SIGN_IN_KEY_PRE = CacheKey.SIGN_IN_KEY_PRE.key;

    /**
     * 参数拼接符
     */
    private static final String PAR_CONCATENATION = Symbol.PAR_CONCATENATION.identity;

    /**
     * 日签到奖励信息转换器
     */
    private static final Function<Reward, DayReward> REWARD_CONVERTER = r ->
            new DayReward(r != null ? new RewardInfo(r.getId(), r.getName(), r.getDetail(), r.getLink()) : null);

    /**
     * 当月奖励信息构建器
     */
    private final BiConsumer<Integer, Integer> DAY_REWARD_INITIALIZER = (year, month) -> {
        List<SignRewardTodayRelation> relations = rewardService.listRelationByYearAndMonth(year, month);
        if (isEmpty(relations))
            LOGGER.error("未配置" + year + "年" + month + "月的每日签到奖励信息");

        List<Reward> rewards = rewardService.listRewardByIds(relations.stream()
                .map(SignRewardTodayRelation::getRewardId).collect(toList()));
        Map<Long, Reward> rewardMap = rewards.stream().collect(toMap(Reward::getId, r -> r, (a, b) -> a));

        LocalDate currentDate = LocalDate.of(year, month, 1);
        int dayOfMonth = currentDate.lengthOfMonth();

        Map<Integer, DayReward> tempMapping = relations.stream()
                .collect(toMap(SignRewardTodayRelation::getDay, r ->
                        REWARD_CONVERTER.apply(rewardMap.get(r.getRewardId())), (a, b) -> b));

        Map<Integer, DayReward> relationMapping = new HashMap<>(dayOfMonth);
        for (int i = 1; i <= dayOfMonth; i++)
            relationMapping.put(i, ofNullable(tempMapping.get(i)).orElse(new DayReward(null)));

        rewardInfoRefreshing = true;
        TODAY_REWARD_MAPPING = relationMapping;
        MONTH = month;
        rewardInfoRefreshing = false;

        //noinspection UnusedAssignment
        tempMapping = null;
    };


    /**
     * 资源信息动态刷新时的阻塞器
     */
    private static final Supplier<String> BLOCKER = () -> {
        if (rewardInfoRefreshing) {
            long start = currentTimeMillis();
            while (rewardInfoRefreshing) {
                if (currentTimeMillis() - start > MAX_WAITING_FOR_REFRESH)
                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "资源信息刷新超时");
                onSpinWait();
            }
        }
        return "refreshed";
    };

    /**
     * 当日奖励获取器
     */
    private static final Function<Integer, DayReward> TODAY_REWARD_GETTER = day -> {
        BLOCKER.get();
        return TODAY_REWARD_MAPPING.get(day);
    };

    /**
     * 根据年月日获取当日奖励信息
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    private DayReward getDayReward(int year, int month, int day) {
        if (MONTH != month)
            synchronized (SignServiceImpl.class) {
                if (MONTH != month)
                    DAY_REWARD_INITIALIZER.accept(year, month);
            }
        return TODAY_REWARD_GETTER.apply(day);
    }

    /**
     * 根据用户ID及当天日期构建当月签到KEY
     *
     * @param mid
     * @param year
     * @param month
     * @return
     */
    private static String generateSignKey(long mid, int year, int month) {
        return SIGN_IN_KEY_PRE + mid + PAR_CONCATENATION + year + PAR_CONCATENATION + month;
    }

    /**
     * 标记位空缺
     */
    private static final long MARK_BIT = 1L;

    /**
     * Flux返回的的一个元素 -> list的第一个元素即为目标bitmap
     */
    private static final int FLUX_ELEMENT_INDEX = 0, LIST_ELEMENT_INDEX = 0;

    /**
     * bitMap获取器
     */
    private final BiFunction<String, Integer, Mono<Long>> BITMAP_LIMIT_GETTER = (key, limit) ->
            reactiveStringRedisTemplate.execute(con ->
                            con.stringCommands()
                                    .bitField(ByteBuffer.wrap(key.getBytes()),
                                            BitFieldSubCommands.create()
                                                    .get(BitFieldSubCommands.BitFieldType.signed(limit))
                                                    .valueAt(MARK_BIT)))
                    .elementAt(FLUX_ELEMENT_INDEX)
                    .flatMap(l -> just(l.get(LIST_ELEMENT_INDEX)));

    /**
     * bitMap标记获取器
     */
    private final BiFunction<String, Long, Mono<Boolean>> BITMAP_BIT_GETTER = (key, offset) ->
            reactiveStringRedisTemplate.opsForValue().getBit(key, offset);

    /**
     * bitMap 签到状态标记器
     */
    private final BiFunction<String, Long, Mono<Boolean>> BITMAP_TRUE_BIT_SETTER = (key, offset) ->
            reactiveStringRedisTemplate.opsForValue().setBit(key, offset, true);

    /**
     * 日签到信息构建器
     */
    private static final BiFunction<Integer, Boolean, DayRewardRecord> DAY_REWARD_RECORD_GENERATOR = (day, sign) ->
            new DayRewardRecord(TODAY_REWARD_MAPPING.get(day), sign);

    /**
     * 月签到信息构建器
     */
    private static final BiFunction<Long, Integer, MonthRewardRecord> MONTH_REWARD_RECORD_GENERATOR = (record, lengthOfMonth) -> {
        //签到记录,总签到次数
        Map<Integer, DayRewardRecord> recordInfo = new TreeMap<>();
        int total = 0;

        //用于计算的中间变量
        boolean signed;
        long bit = 1L;

        for (int d = lengthOfMonth; d > 0; d--) {
            signed = (record & bit) != 0L;
            bit <<= 1;

            recordInfo.put(d, DAY_REWARD_RECORD_GENERATOR.apply(d, signed));
            if (signed)
                total++;
        }

        return new MonthRewardRecord(recordInfo, total);
    };

    /**
     * 初始化
     */
    @PostConstruct
    public final void init() {
        MAX_WAITING_FOR_REFRESH = blockingDeploy.getMillis();
        LocalDate now = LocalDate.now();
        DAY_REWARD_INITIALIZER.accept(now.getYear(), now.getMonthValue());
    }

    /**
     * 当日签到
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<DayReward> insertSignIn(Long memberId) {
        LOGGER.info("insertSignIn(Long memberId), memberId = {}", memberId);
        if (memberId == null || memberId < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        String key = generateSignKey(memberId, year, month);
        int dayOfMonth = now.getDayOfMonth();

        LOGGER.info("key = {}, year = {}, month = {}, dayOfMonth = {}", key, year, month, dayOfMonth);

        return BITMAP_BIT_GETTER.apply(key, (long) dayOfMonth)
                .flatMap(b -> {
                    if (b)
                        return error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "当日已签到,请勿重复签到"));

                    return BITMAP_TRUE_BIT_SETTER.apply(key, (long) dayOfMonth)
                            .flatMap(f -> {
                                if (f)
                                    return error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "当日已签到,请勿重复签到"));

                                try {
                                    signExpireProducer.send(new KeyExpireParam(key, (long) (MAX_EXPIRE_DAYS_FOR_SIGN - dayOfMonth), EXPIRE_UNIT));
                                } catch (Exception e) {
                                    LOGGER.error("延长用户签到过期时间的事件推送失败, key = {}, expire = {}, e = {}", key, (MAX_EXPIRE_DAYS_FOR_SIGN - dayOfMonth), e);
                                }

                                return just(getDayReward(year, month, dayOfMonth));
                            })
                            .flatMap(dayReward -> {
                                try {
                                    LOGGER.info("memberId 为 {} 的成员于 {} 年 {} 月 {} 日签到成功, 奖励为 {}",
                                            memberId, year, month, dayOfMonth, dayReward);
                                    marketingEventProducer.send(new MarketingEvent(SIGN_IN_REWARD, memberId,
                                            GSON.toJson(new SignRewardEvent(memberId, year, month, dayOfMonth, dayReward)), TIME_STAMP_GETTER.get()));
                                } catch (Exception e) {
                                    LOGGER.error("memberId 为 {} 的成员于 {} 年 {} 月 {} 日签到成功但推送奖励处理事件失败, 奖励为 {}, e = {}",
                                            memberId, year, month, dayOfMonth, dayReward, e);
                                }
                                return just(dayReward);
                            });
                });
    }

    /**
     * 查询当月签到
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<MonthRewardRecord> getSignInRecord(Long memberId) {
        LOGGER.info("getSignInRecord(Long memberId), memberId = {}", memberId);

        LocalDate now = LocalDate.now();
        int lengthOfMonth = now.lengthOfMonth();

        LOGGER.info("memberId = {}, lengthOfMonth = {}", memberId, lengthOfMonth);

        return BITMAP_LIMIT_GETTER.apply(generateSignKey(memberId, now.getYear(), now.getMonthValue()), lengthOfMonth)
                .flatMap(record -> {
                            MonthRewardRecord monthRewardRecord = MONTH_REWARD_RECORD_GENERATOR.apply(record, lengthOfMonth);
                            LOGGER.info("memberId = {}, monthRecordDTO = {}", memberId, monthRewardRecord);
                            return just(monthRewardRecord);
                        }
                );
    }

}
