package com.blue.agreement.service.impl;

import com.blue.agreement.api.model.AgreementInfo;
import com.blue.agreement.config.blue.BlueRedisConfig;
import com.blue.agreement.config.deploy.AgreementDeploy;
import com.blue.agreement.constant.AgreementSortAttribute;
import com.blue.agreement.model.AgreementCondition;
import com.blue.agreement.model.AgreementInsertParam;
import com.blue.agreement.model.AgreementManagerInfo;
import com.blue.agreement.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.agreement.repository.entity.Agreement;
import com.blue.agreement.repository.mapper.AgreementMapper;
import com.blue.agreement.service.inter.AgreementService;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.agreement.AgreementType;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.redisson.component.SynchronizedProcessor;
import com.github.benmanes.caffeine.cache.AsyncCache;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.*;
import java.util.stream.Stream;

import static com.blue.agreement.constant.AgreementSortAttribute.CREATE_TIME;
import static com.blue.agreement.converter.AgreementModelConverters.*;
import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.common.base.ConstantProcessor.assertAgreementType;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_SELECT;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_SERVICE_SELECT;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_JSON;
import static com.blue.basic.constant.common.Symbol.PERCENT;
import static com.blue.basic.constant.common.SyncKeyPrefix.AGREEMENT_CACHE_PRE;
import static com.blue.basic.constant.common.SyncKeyPrefix.AGREEMENT_UPDATE_SYNC_PRE;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCacheAsyncCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static com.blue.database.common.ConditionSortProcessor.process;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Flux.concat;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * agreement service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode", "SpringJavaInjectionPointsAutowiringInspection", "GrazieInspection"})
@Service
public class AgreementServiceImpl implements AgreementService {

    private static final Logger LOGGER = getLogger(AgreementServiceImpl.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private StringRedisTemplate stringRedisTemplate;

    private SynchronizedProcessor synchronizedProcessor;

    private AgreementMapper agreementMapper;

    public AgreementServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, BlueIdentityProcessor blueIdentityProcessor, StringRedisTemplate stringRedisTemplate,
                                SynchronizedProcessor synchronizedProcessor, ExecutorService executorService, BlueRedisConfig blueRedisConfig, AgreementMapper agreementMapper, AgreementDeploy agreementDeploy) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.stringRedisTemplate = stringRedisTemplate;
        this.synchronizedProcessor = synchronizedProcessor;
        this.agreementMapper = agreementMapper;

        this.expireDuration = Duration.of(blueRedisConfig.getEntryTtl(), SECONDS);

        typeAgreementInfoCache = generateCacheAsyncCache(new CaffeineConfParams(
                AgreementType.values().length, Duration.of(agreementDeploy.getExpiresSecond(), SECONDS),
                AFTER_WRITE, executorService));
    }

    private static final List<Integer> ALL_TYPE_IDENTITIES = Stream.of(AgreementType.values()).map(at -> at.identity).collect(toList());

    private Duration expireDuration;

    private AsyncCache<Integer, AgreementInfo> typeAgreementInfoCache;

    private static final Function<Integer, String> AGREEMENT_CACHE_KEY_GENERATOR = t -> AGREEMENT_CACHE_PRE.prefix + t;
    private static final Function<Integer, String> AGREEMENT_UPDATE_SYNC_KEY_GEN = t -> AGREEMENT_UPDATE_SYNC_PRE.prefix + t;

    private final Consumer<Integer> REDIS_CACHE_DELETER = t ->
            stringRedisTemplate.delete(AGREEMENT_CACHE_KEY_GENERATOR.apply(t));

    private final Consumer<Integer> CACHE_DELETER = t -> {
        REDIS_CACHE_DELETER.accept(t);
        typeAgreementInfoCache.synchronous().invalidate(t);
    };

    private final Function<Integer, AgreementInfo> AGREEMENT_INFO_DB_GETTER = t -> {
        assertAgreementType(t, false);

        return ofNullable(agreementMapper.selectNewestByType(t))
                .map(AGREEMENT_2_AGREEMENT_INFO)
                .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));
    };

    private final Function<Integer, AgreementInfo> AGREEMENT_INFO_REDIS_GETTER = t -> {
        assertAgreementType(t, false);
        return ofNullable(stringRedisTemplate.opsForValue().get(AGREEMENT_CACHE_KEY_GENERATOR.apply(t)))
                .filter(BlueChecker::isNotBlank)
                .map(s -> GSON.fromJson(s, AgreementInfo.class))
                .orElse(null);
    };

    private final BiConsumer<Integer, AgreementInfo> AGREEMENT_INFO_REDIS_SETTER = (t, ni) -> {
        String cacheKey = AGREEMENT_CACHE_KEY_GENERATOR.apply(t);
        REDIS_CACHE_DELETER.accept(t);
        stringRedisTemplate.opsForValue().set(cacheKey, ofNullable(ni).map(GSON::toJson).orElse(EMPTY_JSON.value), expireDuration);
        stringRedisTemplate.expire(cacheKey, expireDuration);
    };

    private final BiFunction<Integer, Executor, CompletableFuture<AgreementInfo>> AGREEMENT_INFO_WITH_REDIS_CACHE_GETTER = (t, executor) ->
            supplyAsync(() -> synchronizedProcessor.handleSupByOrderedWithSetter(AGREEMENT_UPDATE_SYNC_KEY_GEN.apply(t),
                    () -> AGREEMENT_INFO_REDIS_GETTER.apply(t), () -> AGREEMENT_INFO_DB_GETTER.apply(t),
                    agreementInfo -> AGREEMENT_INFO_REDIS_SETTER.accept(t, agreementInfo), BlueChecker::isNotNull), executor);

    private final Function<Integer, CompletableFuture<AgreementInfo>> AGREEMENT_INFO_WITH_ALL_CACHE_GETTER = t -> {
        assertAgreementType(t, false);

        return typeAgreementInfoCache.get(t, AGREEMENT_INFO_WITH_REDIS_CACHE_GETTER);
    };

    private final Supplier<Mono<List<AgreementInfo>>> NEWEST_AGREEMENTS_SUP = () ->
            concat(ALL_TYPE_IDENTITIES.stream()
                    .map(AGREEMENT_INFO_WITH_ALL_CACHE_GETTER)
                    .map(Mono::fromFuture).collect(toList()))
                    .collectList();

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(AgreementSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<AgreementCondition> CONDITION_PROCESSOR = c -> {
        AgreementCondition ac = isNotNull(c) ? c : new AgreementCondition();

        process(ac, SORT_ATTRIBUTE_MAPPING, CREATE_TIME.column);

        ofNullable(ac.getTitleLike())
                .filter(StringUtils::hasText).ifPresent(titleLike -> ac.setTitleLike(PERCENT.identity + titleLike + PERCENT.identity));
        ofNullable(ac.getLinkLike())
                .filter(StringUtils::hasText).ifPresent(linkLike -> ac.setLinkLike(PERCENT.identity + linkLike + PERCENT.identity));

        return ac;
    };

    private final Consumer<AgreementInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();
    };

    /**
     * insert agreement
     *
     * @param agreementInsertParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public AgreementInfo insertAgreement(AgreementInsertParam agreementInsertParam, Long operatorId) {
        LOGGER.info("agreementInsertParam = {}, operatorId = {}", agreementInsertParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        INSERT_ITEM_VALIDATOR.accept(agreementInsertParam);
        Agreement agreement = AGREEMENT_INSERT_PARAM_2_AGREEMENT_CONVERTER.apply(agreementInsertParam);

        agreement.setId(blueIdentityProcessor.generate(Agreement.class));
        agreement.setCreator(operatorId);

        Integer type = agreement.getType();

        CACHE_DELETER.accept(type);
        agreementMapper.insert(agreement);
        CACHE_DELETER.accept(type);

        return AGREEMENT_2_AGREEMENT_INFO.apply(agreement);
    }

    /**
     * expire agreement info
     *
     * @return
     */
    @Override
    public void invalidAgreementInfosCache() {
        ALL_TYPE_IDENTITIES.forEach(CACHE_DELETER);
    }

    /**
     * get agreement mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Agreement> getAgreement(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return justOrEmpty(agreementMapper.selectByPrimaryKey(id));
    }

    /**
     * get newest agreement by type
     *
     * @param agreementType
     * @return
     */
    @Override
    public Mono<Agreement> getNewestAgreementByType(Integer agreementType) {
        LOGGER.info("agreementType = {}", agreementType);
        assertAgreementType(agreementType, false);

        return justOrEmpty(agreementMapper.selectNewestByType(agreementType));
    }

    /**
     * get newest agreement info
     *
     * @param agreementType
     * @return
     */
    @Override
    public Mono<AgreementInfo> getNewestAgreementInfoByTypeWithCache(Integer agreementType) {
        LOGGER.info("agreementType = {}", agreementType);
        return fromFuture(AGREEMENT_INFO_WITH_ALL_CACHE_GETTER.apply(agreementType));
    }

    /**
     * select all newest agreement info
     *
     * @return
     */
    @Override
    public Mono<List<AgreementInfo>> selectNewestAgreementInfosByAllTypeWithCache() {
        return NEWEST_AGREEMENTS_SUP.get();
    }

    /**
     * select agreements by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<AgreementInfo>> selectAgreementInfoByIds(List<Long> ids) {
        LOGGER.info("ids = {}", ids);
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return just(allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(agreementMapper::selectByIds)
                .flatMap(List::stream)
                .map(AGREEMENT_2_AGREEMENT_INFO)
                .collect(toList()));
    }

    /**
     * select agreement by page and condition
     *
     * @param limit
     * @param rows
     * @param agreementCondition
     * @return
     */
    @Override
    public Mono<List<Agreement>> selectAgreementByLimitAndCondition(Long limit, Long rows, AgreementCondition agreementCondition) {
        LOGGER.info("limit = {}, rows = {}, agreementCondition = {}", limit, rows, agreementCondition);
        return justOrEmpty(agreementMapper.selectByLimitAndCondition(limit, rows, agreementCondition));
    }

    /**
     * count agreement by condition
     *
     * @param agreementCondition
     * @return
     */
    @Override
    public Mono<Long> countAgreementByCondition(AgreementCondition agreementCondition) {
        LOGGER.info("agreementCondition = {}", agreementCondition);
        return justOrEmpty(ofNullable(agreementMapper.countByCondition(agreementCondition)).orElse(0L));
    }

    /**
     * select agreement info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<AgreementManagerInfo>> selectAgreementManagerInfoPageByPageAndCondition(PageModelRequest<AgreementCondition> pageModelRequest) {
        LOGGER.info("pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        AgreementCondition agreementCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectAgreementByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), agreementCondition),
                countAgreementByCondition(agreementCondition))
                .flatMap(tuple2 -> {
                    List<Agreement> agreements = tuple2.getT1();
                    Long count = tuple2.getT2();
                    return isNotEmpty(agreements) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(agreements.stream().map(Agreement::getCreator).distinct().collect(toList()))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndMemberNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(agreements.stream().map(a ->
                                                AGREEMENT_2_AGREEMENT_MANAGER_INFO.apply(a, idAndMemberNameMapping)).collect(toList()));
                                    }).flatMap(agreementManagerInfos ->
                                            just(new PageModelResponse<>(agreementManagerInfos, count)))
                            :
                            just(new PageModelResponse<>(emptyList(), count));
                });
    }

}
