package com.blue.member.service.impl;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.common.InvalidAuthEvent;
import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.base.model.common.StringDataParam;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.config.deploy.MemberDeploy;
import com.blue.member.constant.MemberBasicSortAttribute;
import com.blue.member.event.producer.InvalidAuthProducer;
import com.blue.member.model.MemberBasicCondition;
import com.blue.member.repository.entity.MemberBasic;
import com.blue.member.repository.mapper.MemberBasicMapper;
import com.blue.member.service.inter.MemberBasicService;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

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

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.common.base.ConditionSortProcessor.process;
import static com.blue.base.common.base.ConstantProcessor.assertStatus;
import static com.blue.base.constant.common.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.common.BlueNumericalValue.MAX_SERVICE_SELECT;
import static com.blue.base.constant.common.CacheKeyPrefix.MEMBER_PRE;
import static com.blue.base.constant.common.ResponseElement.*;
import static com.blue.base.constant.common.Status.VALID;
import static com.blue.member.converter.MemberModelConverters.MEMBER_BASIC_2_MEMBER_BASIC_INFO;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.redis.connection.RedisStringCommands.SetOption.UPSERT;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * member basic service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class MemberBasicServiceImpl implements MemberBasicService {

    private static final Logger LOGGER = getLogger(MemberBasicServiceImpl.class);

    private final InvalidAuthProducer invalidAuthProducer;

    private MemberBasicMapper memberBasicMapper;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private StringRedisTemplate stringRedisTemplate;

    private ExecutorService executorService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MemberBasicServiceImpl(InvalidAuthProducer invalidAuthProducer, MemberBasicMapper memberBasicMapper, BlueIdentityProcessor blueIdentityProcessor,
                                  StringRedisTemplate stringRedisTemplate, ExecutorService executorService, MemberDeploy memberDeploy) {
        this.invalidAuthProducer = invalidAuthProducer;
        this.memberBasicMapper = memberBasicMapper;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.stringRedisTemplate = stringRedisTemplate;
        this.executorService = executorService;

        Long cacheExpireSeconds = memberDeploy.getCacheExpireSeconds();
        if (isNull(cacheExpireSeconds) || cacheExpireSeconds < 1L)
            throw new RuntimeException("cacheExpireSeconds can't be null or less than 1");

        this.expireDuration = Duration.of(cacheExpireSeconds, ChronoUnit.SECONDS);
        this.expiration = Expiration.from(cacheExpireSeconds, TimeUnit.SECONDS);
    }

    private Duration expireDuration;
    private Expiration expiration;

    private static final Function<Long, String> MEMBER_CACHE_KEY_GENERATOR = id -> MEMBER_PRE.prefix + id;

    private final Consumer<Long> REDIS_CACHE_DELETER = id ->
            stringRedisTemplate.delete(MEMBER_CACHE_KEY_GENERATOR.apply(id));

    private final Function<Long, MemberBasic> MEMBER_BASIC_DB_GETTER = id -> {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(memberBasicMapper.selectByPrimaryKey(id)).orElseThrow(() -> new BlueException(DATA_NOT_EXIST));
    };

    private final BiConsumer<Long, MemberBasic> MEMBER_BASIC_REDIS_SETTER = (id, memberBasic) -> {
        if (isInvalidIdentity(id) || isNull(memberBasic))
            throw new BlueException(EMPTY_PARAM);

        stringRedisTemplate.opsForValue().set(MEMBER_CACHE_KEY_GENERATOR.apply(id), GSON.toJson(memberBasic), expireDuration);
    };

    private final Function<Long, MemberBasic> MEMBER_BASIC_WITH_REDIS_CACHE_GETTER = id -> {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(stringRedisTemplate.opsForValue().get(MEMBER_CACHE_KEY_GENERATOR.apply(id)))
                .filter(BlueChecker::isNotBlank)
                .map(s -> GSON.fromJson(s, MemberBasic.class))
                .orElseGet(() -> {
                    MemberBasic memberBasic = MEMBER_BASIC_DB_GETTER.apply(id);
                    MEMBER_BASIC_REDIS_SETTER.accept(id, memberBasic);

                    return memberBasic;
                });
    };

    private final Function<List<Long>, List<MemberBasic>> MEMBER_BASICS_DB_GETTER = ids ->
            isNotEmpty(ids) ? memberBasicMapper.selectByIds(ids) : emptyList();

    private final BiConsumer<List<MemberBasic>, Map<Long, String>> MEMBERS_BASIC_REDIS_SETTER = (memberBasics, idAndCacheKeyMapping) -> {
        if (isEmpty(memberBasics) || isEmpty(idAndCacheKeyMapping))
            return;

        stringRedisTemplate.executePipelined((RedisCallback<Void>) connection -> {
            for (MemberBasic memberBasic : memberBasics)
                connection.set(idAndCacheKeyMapping.get(memberBasic.getId()).getBytes(UTF_8),
                        GSON.toJson(memberBasic).getBytes(UTF_8), expiration, UPSERT);

            return null;
        });
    };

    private final Function<List<Long>, List<MemberBasic>> MEMBER_BASICS_WITH_REDIS_CACHE_GETTER = ids -> {
        if (isEmpty(ids))
            return emptyList();
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        Map<Long, String> idAndCacheKeyMapping = ids.stream().distinct().collect(toMap(id -> id, MEMBER_CACHE_KEY_GENERATOR, (a, b) -> a));

        List<Object> strObjs = stringRedisTemplate.executePipelined((RedisCallback<String>) connection -> {
            for (String cacheKey : idAndCacheKeyMapping.values())
                connection.get(cacheKey.getBytes(UTF_8));
            return null;
        });

        List<MemberBasic> result = new ArrayList<>(idAndCacheKeyMapping.size());

        MemberBasic memberBasic;
        for (Object strObj : strObjs)
            if (isNotNull(strObj)) {
                memberBasic = GSON.fromJson(String.valueOf(strObj), MemberBasic.class);
                result.add(memberBasic);
                idAndCacheKeyMapping.remove(memberBasic.getId());
            }

        if (idAndCacheKeyMapping.size() == 0)
            return result;

        List<MemberBasic> missCacheMemberBasics = MEMBER_BASICS_DB_GETTER.apply(new ArrayList<>(idAndCacheKeyMapping.keySet()));
        executorService.execute(() -> MEMBERS_BASIC_REDIS_SETTER.accept(missCacheMemberBasics, idAndCacheKeyMapping));
        result.addAll(missCacheMemberBasics);

        return result;
    };

    /**
     * is a number exist?
     */
    private final Consumer<MemberBasic> MEMBER_EXIST_VALIDATOR = mb -> {
        ofNullable(mb.getPhone())
                .filter(BlueChecker::isNotBlank)
                .ifPresent(phone -> {
                    if (isNotNull(memberBasicMapper.selectByPhone(phone)))
                        throw new BlueException(PHONE_ALREADY_EXIST);
                });

        ofNullable(mb.getEmail())
                .filter(BlueChecker::isNotBlank)
                .ifPresent(email -> {
                    if (isNotNull(memberBasicMapper.selectByEmail(email)))
                        throw new BlueException(EMAIL_ALREADY_EXIST);
                });

        ofNullable(mb.getName())
                .filter(BlueChecker::isNotBlank)
                .ifPresent(name -> {
                    if (isNotNull(memberBasicMapper.selectByName(name)))
                        throw new BlueException(NAME_ALREADY_EXIST);
                });
    };

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(MemberBasicSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<MemberBasicCondition> CONDITION_PROCESSOR = condition -> {
        if (isNull(condition))
            return new MemberBasicCondition();

        process(condition, SORT_ATTRIBUTE_MAPPING, MemberBasicSortAttribute.ID.column);

        return condition;
    };

    /**
     * insert member
     *
     * @param memberBasic
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public MemberBasicInfo insertMemberBasic(MemberBasic memberBasic) {
        LOGGER.info("MemberBasicInfo insertMemberBasic(MemberBasic memberBasic), memberBasic = {}", memberBasic);
        if (isNull(memberBasic))
            throw new BlueException(EMPTY_PARAM);

        MEMBER_EXIST_VALIDATOR.accept(memberBasic);

        if (isInvalidIdentity(memberBasic.getId()))
            memberBasic.setId(blueIdentityProcessor.generate(MemberBasic.class));

        memberBasicMapper.insert(memberBasic);

        return MEMBER_BASIC_2_MEMBER_BASIC_INFO.apply(memberBasic);
    }

    /**
     * update member
     *
     * @param memberBasic
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public MemberBasicInfo updateMemberBasic(MemberBasic memberBasic) {
        LOGGER.info("void insert(MemberBasic memberBasic), memberBasic = {}", memberBasic);
        if (isNull(memberBasic))
            throw new BlueException(EMPTY_PARAM);
        Long id = memberBasic.getId();
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        REDIS_CACHE_DELETER.accept(id);
        memberBasicMapper.updateByPrimaryKey(memberBasic);
        REDIS_CACHE_DELETER.accept(id);

        return MEMBER_BASIC_2_MEMBER_BASIC_INFO.apply(memberBasic);
    }

    /**
     * update member's icon
     *
     * @param id
     * @param stringDataParam
     * @return
     */
    @Override
    public Mono<MemberBasicInfo> updateMemberBasicIcon(Long id, StringDataParam stringDataParam) {
        LOGGER.info("Mono<MemberBasicInfo> updateMemberBasicIcon(Long id, StringDataParam stringDataParam), id = {}, stringDataParam = {}",
                id, stringDataParam);
        if (isInvalidIdentity(id))
            throw new BlueException(UNAUTHORIZED);
        if (isNull(stringDataParam))
            throw new BlueException(EMPTY_PARAM);

        String icon = stringDataParam.getData();
        if (isBlank(icon))
            throw new BlueException(EMPTY_PARAM);

        return justOrEmpty(memberBasicMapper.selectByPrimaryKey(id))
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(mb -> {
                    REDIS_CACHE_DELETER.accept(id);
                    memberBasicMapper.updateIcon(id, icon, TIME_STAMP_GETTER.get());
                    REDIS_CACHE_DELETER.accept(id);

                    mb.setIcon(icon);
                    return just(mb);
                })
                .map(MEMBER_BASIC_2_MEMBER_BASIC_INFO);
    }

    /**
     * update member's profile
     *
     * @param id
     * @param stringDataParam
     * @return
     */
    @Override
    public Mono<MemberBasicInfo> updateMemberBasicProfile(Long id, StringDataParam stringDataParam) {
        LOGGER.info("Mono<MemberBasicInfo> updateMemberBasicProfile(Long id, StringDataParam stringDataParam), id = {}, stringDataParam = {}", id, stringDataParam);
        if (isInvalidIdentity(id))
            throw new BlueException(UNAUTHORIZED);
        if (isNull(stringDataParam))
            throw new BlueException(EMPTY_PARAM);

        String profile = stringDataParam.getData();
        if (isBlank(profile))
            throw new BlueException(EMPTY_PARAM);

        return justOrEmpty(memberBasicMapper.selectByPrimaryKey(id))
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(mb -> {
                    REDIS_CACHE_DELETER.accept(id);
                    memberBasicMapper.updateProfile(id, profile, TIME_STAMP_GETTER.get());
                    REDIS_CACHE_DELETER.accept(id);

                    mb.setProfile(profile);
                    return just(mb);
                })
                .map(MEMBER_BASIC_2_MEMBER_BASIC_INFO);
    }

    /**
     * update member status
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public MemberBasicInfo updateMemberBasicStatus(Long id, Integer status) {
        LOGGER.info("MemberBasicInfo updateMemberBasicStatus(Long id, Integer status), id = {}, status = {}", id, status);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        assertStatus(status, false);

        MemberBasic memberBasic = memberBasicMapper.selectByPrimaryKey(id);
        if (isNull(memberBasic))
            throw new BlueException(DATA_NOT_EXIST);

        if (status.equals(memberBasic.getStatus()))
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        memberBasic.setStatus(status);

        REDIS_CACHE_DELETER.accept(id);
        memberBasicMapper.updateStatus(id, status, TIME_STAMP_GETTER.get());
        REDIS_CACHE_DELETER.accept(id);

        if (VALID.status != status)
            invalidAuthProducer.send(new InvalidAuthEvent(id));

        return MEMBER_BASIC_2_MEMBER_BASIC_INFO.apply(memberBasic);
    }

    /**
     * get opt by id
     *
     * @param id
     * @return
     */
    @Override
    public MemberBasic getMemberBasic(Long id) {
        LOGGER.info("MemberBasic getMemberBasicByPrimaryKey(Long id), id = {}", id);
        return MEMBER_BASIC_WITH_REDIS_CACHE_GETTER.apply(id);
    }

    /**
     * query member by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<MemberBasic> getMemberBasicMono(Long id) {
        LOGGER.info("Mono<Optional<MemberBasic>> getMemberBasicMonoByPrimaryKey(Long id), id = {}", id);
        return just(getMemberBasic(id));
    }

    /**
     * query member by phone
     *
     * @param phone
     * @return
     */
    @Override
    public Optional<MemberBasic> getMemberBasicByPhone(String phone) {
        LOGGER.info("Optional<MemberBasic> getMemberBasicByPhone(String phone), phone = {}", phone);
        if (isBlank(phone))
            throw new BlueException(BAD_REQUEST);

        return ofNullable(memberBasicMapper.selectByPhone(phone));
    }

    /**
     * query member by email
     *
     * @param email
     * @return
     */
    @Override
    public Optional<MemberBasic> getMemberBasicByEmail(String email) {
        LOGGER.info("Optional<MemberBasic> getMemberBasicByEmail(String email), email = {}", email);
        if (isBlank(email))
            throw new BlueException(BAD_REQUEST);

        return ofNullable(memberBasicMapper.selectByEmail(email));
    }

    /**
     * query member by phone
     *
     * @param phone
     * @return
     */
    @Override
    public Mono<Optional<MemberBasic>> getMemberBasicMonoByPhone(String phone) {
        LOGGER.info("Mono<Optional<MemberBasic>> getMemberBasicMonoByPhone(String phone), phone = {}", phone);
        if (isBlank(phone))
            throw new BlueException(BAD_REQUEST);

        return just(ofNullable(memberBasicMapper.selectByPhone(phone)));
    }

    /**
     * query member by email
     *
     * @param email
     * @return
     */
    @Override
    public Mono<Optional<MemberBasic>> getMemberBasicMonoByEmail(String email) {
        LOGGER.info("Mono<Optional<MemberBasic>> getMemberBasicMonoByEmail(String email), email = {}", email);
        if (isBlank(email))
            throw new BlueException(BAD_REQUEST);

        return just(ofNullable(memberBasicMapper.selectByEmail(email)));
    }

    /**
     * query member by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<MemberBasicInfo> getMemberBasicInfoMonoWithAssert(Long id) {
        LOGGER.info("Mono<MemberInfo> getMemberInfoMonoByPrimaryKeyWithAssert(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return getMemberBasicMono(id)
                .flatMap(mb -> {
                    if (isInvalidStatus(mb.getStatus()))
                        return error(() -> new BlueException(ACCOUNT_HAS_BEEN_FROZEN));
                    LOGGER.info("mb = {}", mb);
                    return just(mb);
                }).flatMap(mb ->
                        just(MEMBER_BASIC_2_MEMBER_BASIC_INFO.apply(mb))
                );
    }

    /**
     * select members by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<MemberBasic> selectMemberBasicByIds(List<Long> ids) {
        LOGGER.info("List<MemberBasic> selectMemberBasicByIds(List<Long> ids), ids = {}", ids);
        if (isEmpty(ids))
            return emptyList();
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .parallelStream().map(MEMBER_BASICS_WITH_REDIS_CACHE_GETTER)
                .flatMap(List::stream)
                .collect(toList());
    }

    /**
     * select members mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<MemberBasic>> selectMemberBasicMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<MemberBasic>> selectMemberBasicMonoByIds(List<Long> ids), ids = {}", ids);
        if (isValidIdentities(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            return error(() -> new BlueException(PAYLOAD_TOO_LARGE));

        return fromIterable(allotByMax(ids, (int) DB_SELECT.value, false))
                .map(MEMBER_BASICS_WITH_REDIS_CACHE_GETTER)
                .reduceWith(LinkedList::new, (a, b) -> {
                    a.addAll(b);
                    return a;
                });
    }

    /**
     * select members by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<MemberBasicInfo>> selectMemberBasicInfoMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<MemberBasic>> selectMemberBasicMonoByIds(List<Long> ids), ids = {}", ids);
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            return error(() -> new BlueException(PAYLOAD_TOO_LARGE));

        return fromIterable(allotByMax(ids, (int) DB_SELECT.value, false))
                .map(shardIds -> MEMBER_BASICS_WITH_REDIS_CACHE_GETTER.apply(shardIds)
                        .stream().map(MEMBER_BASIC_2_MEMBER_BASIC_INFO).collect(toList()))
                .reduceWith(LinkedList::new, (a, b) -> {
                    a.addAll(b);
                    return a;
                });
    }

    /**
     * select member by page and condition
     *
     * @param limit
     * @param rows
     * @param memberBasicCondition
     * @return
     */
    @Override
    public Mono<List<MemberBasic>> selectMemberBasicMonoByLimitAndCondition(Long limit, Long rows, MemberBasicCondition memberBasicCondition) {
        LOGGER.info("Mono<List<MemberBasic>> selectMemberBasicMonoByLimitAndCondition(Long limit, Long rows, MemberCondition memberCondition), " +
                "limit = {}, rows = {}, memberCondition = {}", limit, rows, memberBasicCondition);
        if (isNull(limit) || limit < 0 || isNull(rows) || rows < 1)
            throw new BlueException(INVALID_PARAM);

        return just(memberBasicMapper.selectByLimitAndCondition(limit, rows, memberBasicCondition));
    }

    /**
     * count member by condition
     *
     * @param memberBasicCondition
     * @return
     */
    @Override
    public Mono<Long> countMemberBasicMonoByCondition(MemberBasicCondition memberBasicCondition) {
        LOGGER.info("Mono<Long> countMemberBasicMonoByCondition(MemberCondition memberCondition), memberCondition = {}", memberBasicCondition);
        return just(ofNullable(memberBasicMapper.countByCondition(memberBasicCondition)).orElse(0L));
    }

    /**
     * select member info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<MemberBasicInfo>> selectMemberBasicInfoPageMonoByPageAndCondition(PageModelRequest<MemberBasicCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<MemberInfo>> selectMemberInfoPageMonoByPageAndCondition(PageModelRequest<MemberCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        MemberBasicCondition memberBasicCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(selectMemberBasicMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), memberBasicCondition), countMemberBasicMonoByCondition(memberBasicCondition))
                .flatMap(tuple2 -> {
                    List<MemberBasic> members = tuple2.getT1();
                    Mono<List<MemberBasicInfo>> memberInfosMono = members.size() > 0 ?
                            just(members.stream().map(MEMBER_BASIC_2_MEMBER_BASIC_INFO).collect(toList()))
                            :
                            just(emptyList());

                    return memberInfosMono
                            .flatMap(memberInfos ->
                                    just(new PageModelResponse<>(memberInfos, tuple2.getT2())));
                });
    }

}
