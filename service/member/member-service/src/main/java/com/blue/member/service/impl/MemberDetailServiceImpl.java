package com.blue.member.service.impl;

import com.blue.base.api.model.CityInfo;
import com.blue.base.api.model.CityRegion;
import com.blue.base.api.model.CountryInfo;
import com.blue.base.api.model.StateInfo;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.common.StatusParam;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberDetailInfo;
import com.blue.member.constant.MemberDetailSortAttribute;
import com.blue.member.model.MemberDetailCondition;
import com.blue.member.model.MemberDetailUpdateParam;
import com.blue.member.remote.consumer.RpcCityServiceConsumer;
import com.blue.member.repository.entity.MemberBasic;
import com.blue.member.repository.entity.MemberDetail;
import com.blue.member.repository.mapper.MemberDetailMapper;
import com.blue.member.service.inter.MemberBasicService;
import com.blue.member.service.inter.MemberDetailService;
import com.blue.redisson.component.SynchronizedProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.*;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_SELECT;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_SERVICE_SELECT;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Status.INVALID;
import static com.blue.basic.constant.common.Status.VALID;
import static com.blue.basic.constant.common.SummerAttr.DATE_FORMATTER;
import static com.blue.basic.constant.common.Symbol.PERCENT;
import static com.blue.basic.constant.common.SyncKeyPrefix.MEMBER_DETAIL_INSERT_SYNC_PRE;
import static com.blue.basic.constant.member.MemberThreshold.*;
import static com.blue.database.common.ConditionSortProcessor.process;
import static com.blue.member.converter.MemberModelConverters.MEMBER_DETAILS_2_MEMBER_DETAILS_INFO;
import static com.blue.member.converter.MemberModelConverters.MEMBER_DETAIL_2_MEMBER_DETAIL_INFO;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * member detail service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode", "BlockingMethodInNonBlockingContext"})
@Service
public class MemberDetailServiceImpl implements MemberDetailService {

    private static final Logger LOGGER = getLogger(MemberDetailServiceImpl.class);

    private RpcCityServiceConsumer rpcCityServiceConsumer;

    private MemberBasicService memberBasicService;

    private final SynchronizedProcessor synchronizedProcessor;

    private BlueIdentityProcessor blueIdentityProcessor;

    private MemberDetailMapper memberDetailMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MemberDetailServiceImpl(RpcCityServiceConsumer rpcCityServiceConsumer, MemberBasicService memberBasicService, SynchronizedProcessor synchronizedProcessor,
                                   BlueIdentityProcessor blueIdentityProcessor, MemberDetailMapper memberDetailMapper) {
        this.rpcCityServiceConsumer = rpcCityServiceConsumer;
        this.memberBasicService = memberBasicService;
        this.synchronizedProcessor = synchronizedProcessor;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.memberDetailMapper = memberDetailMapper;
    }

    private static final Function<Long, String> MEMBER_DETAIL_INSERT_SYNC_KEY_GEN = memberId -> {
        if (isValidIdentity(memberId))
            return MEMBER_DETAIL_INSERT_SYNC_PRE.prefix + memberId;

        throw new BlueException(BAD_REQUEST);
    };

    private final Function<Long, MemberDetail> INIT_MEMBER_DETAIL_GEN = mid -> {
        if (isInvalidIdentity(mid))
            throw new BlueException(INVALID_IDENTITY);

        MemberBasic memberBasic = memberBasicService.getMemberBasicOpt(mid)
                .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));
        if (isNull(memberBasic))
            throw new BlueException(INVALID_PARAM);

        MemberDetail memberDetail = new MemberDetail();
        memberDetail.setId(blueIdentityProcessor.generate(MemberDetail.class));
        memberDetail.setMemberId(mid);
        memberDetail.setStatus(INVALID.status);

        Long stamp = TIME_STAMP_GETTER.get();
        memberDetail.setCreateTime(stamp);
        memberDetail.setUpdateTime(stamp);

        return memberDetail;
    };

    private final Consumer<MemberDetail> INSERT_ITEM_VALIDATOR = t -> {
        if (isNull(t))
            throw new BlueException(EMPTY_PARAM);

        if (isInvalidIdentity(t.getId()) || isInvalidIdentity(t.getMemberId()))
            throw new BlueException(INVALID_PARAM);

        assertStatus(t.getStatus(), false);

        if (isNull(t.getCreateTime()) || isNull(t.getUpdateTime()))
            throw new BlueException(INVALID_PARAM);

        if (isNotNull(memberDetailMapper.selectByPrimaryKey(t.getId())))
            throw new BlueException(DATA_ALREADY_EXIST);

        if (isNotNull(memberDetailMapper.selectByMemberId(t.getMemberId())))
            throw new BlueException(DATA_ALREADY_EXIST);
    };

    private final BiConsumer<MemberDetailUpdateParam, MemberDetail> UPDATE_ITEM_WITH_ASSERT_PACKAGER = (p, t) -> {
        if (isNull(p) || isNull(t))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        boolean alteration = false;

        String name = p.getName();
        if (isNotBlank(name) && !name.equals(t.getName())) {
            t.setName(name);
            alteration = true;
        }

        Integer gender = p.getGender();
        if (isNotNull(gender) && !gender.equals(t.getGender())) {
            assertGender(gender, false);
            t.setGender(gender);
            alteration = true;
        }

        String phone = p.getPhone();
        if (isNotBlank(phone) && !phone.equals(t.getPhone())) {
            t.setPhone(phone);
            alteration = true;
        }

        String email = p.getEmail();
        if (isNotBlank(email) && !email.equals(t.getEmail())) {
            t.setEmail(email);
            alteration = true;
        }

        String birthDay = p.getBirthDay();
        if (isNotBlank(birthDay)) {
            LocalDate localDate;
            try {
                localDate = LocalDate.parse(birthDay, DATE_FORMATTER);
            } catch (Exception e) {
                throw new BlueException(INVALID_PARAM);
            }

            Integer yearOfBirth = localDate.getYear();
            Integer monthOfBirth = localDate.getMonthValue();
            Integer dayOfBirth = localDate.getDayOfMonth();

            if (!yearOfBirth.equals(t.getYearOfBirth()) || !monthOfBirth.equals(t.getMonthOfBirth()) || !dayOfBirth.equals(t.getDayOfBirth())) {
                t.setYearOfBirth(yearOfBirth);
                t.setMonthOfBirth(monthOfBirth);
                t.setDayOfBirth(dayOfBirth);
                alteration = true;
            }
        }

        Integer chineseZodiac = p.getChineseZodiac();
        if (isNotNull(chineseZodiac) && !chineseZodiac.equals(t.getChineseZodiac())) {
            t.setChineseZodiac(chineseZodiac);
            alteration = true;
        }

        Integer zodiacSign = p.getZodiacSign();
        if (isNotNull(zodiacSign) && !zodiacSign.equals(t.getZodiacSign())) {
            t.setZodiacSign(zodiacSign);
            alteration = true;
        }

        Integer height = p.getHeight();
        if (isNotNull(height) && !height.equals(t.getHeight())) {
            if (height < MIN_HEIGHT.threshold || height > MAX_HEIGHT.threshold)
                throw new BlueException(INVALID_PARAM);

            t.setHeight(height);
            alteration = true;
        }

        Integer weight = p.getWeight();
        if (isNotNull(weight) && !weight.equals(t.getWeight())) {
            if (weight < MIN_WEIGHT.threshold || weight > MAX_WEIGHT.threshold)
                throw new BlueException(INVALID_PARAM);

            t.setWeight(weight);
            alteration = true;
        }

        Long cityId = p.getCityId();
        if (isNotNull(cityId) && !cityId.equals(t.getCityId())) {
            if (isInvalidIdentity(cityId))
                throw new BlueException(INVALID_IDENTITY);

            CityRegion cityRegion = rpcCityServiceConsumer.getCityRegionById(cityId).toFuture().join();
            if (isNull(cityRegion))
                throw new BlueException(DATA_NOT_EXIST);

            CountryInfo countryInfo = cityRegion.getCountry();
            t.setCountryId(countryInfo.getId());
            t.setCountry(countryInfo.getName());

            StateInfo stateInfo = cityRegion.getState();
            t.setStateId(stateInfo.getId());
            t.setState(stateInfo.getName());

            CityInfo cityInfo = cityRegion.getCity();
            t.setCityId(cityInfo.getId());
            t.setCity(cityInfo.getName());

            alteration = true;
        }

        String address = p.getAddress();
        if (isNotBlank(address) && !address.equals(t.getAddress())) {
            t.setAddress(address);
            alteration = true;
        }

        String introduction = p.getIntroduction();
        if (isNotBlank(introduction) && !introduction.equals(t.getIntroduction())) {
            t.setIntroduction(introduction);
            alteration = true;
        }

        String hobby = p.getHobby();
        if (isNotBlank(hobby) && !hobby.equals(t.getHobby())) {
            t.setHobby(hobby);
            alteration = true;
        }

        String homepage = p.getHomepage();
        if (isNotBlank(homepage) && !homepage.equals(t.getHomepage())) {
            t.setHomepage(homepage);
            alteration = true;
        }

        String extra = p.getExtra();
        if (isNotBlank(extra) && !extra.equals(t.getExtra())) {
            t.setExtra(extra);
            alteration = true;
        }

        if (!alteration)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        t.setStatus(VALID.status);
        t.setUpdateTime(TIME_STAMP_GETTER.get());
    };

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(MemberDetailSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<MemberDetailCondition> CONDITION_PROCESSOR = c -> {
        MemberDetailCondition mdc = isNotNull(c) ? c : new MemberDetailCondition();

        process(mdc, SORT_ATTRIBUTE_MAPPING, MemberDetailSortAttribute.CREATE_TIME.column);

        ofNullable(mdc.getHobbyLike()).filter(BlueChecker::isNotBlank).ifPresent(hobbyLike ->
                mdc.setHobbyLike(PERCENT.identity + hobbyLike + PERCENT.identity));

        return mdc;
    };

    /**
     * init member detail
     *
     * @param memberId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public MemberDetail initMemberDetail(Long memberId) {
        LOGGER.info("memberId = {}", memberId);

        MemberDetail memberDetail = INIT_MEMBER_DETAIL_GEN.apply(memberId);

        synchronizedProcessor.handleTaskWithSync(MEMBER_DETAIL_INSERT_SYNC_KEY_GEN.apply(memberId), () -> {
            INSERT_ITEM_VALIDATOR.accept(memberDetail);

            int inserted = memberDetailMapper.insert(memberDetail);
            LOGGER.info("inserted = {}", inserted);
        });

        return memberDetail;
    }

    /**
     * update member detail
     *
     * @param memberId
     * @param memberDetailUpdateParam
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public MemberDetailInfo updateMemberDetail(Long memberId, MemberDetailUpdateParam memberDetailUpdateParam) {
        LOGGER.info("memberId = {}, memberDetailUpdateParam = {}",
                memberId, memberDetailUpdateParam);
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);
        if (isNull(memberDetailUpdateParam))
            throw new BlueException(EMPTY_PARAM);
        memberDetailUpdateParam.asserts();

        MemberDetail memberDetail = memberDetailMapper.selectByMemberId(memberId);
        if (isNull(memberDetail))
            memberDetail = this.initMemberDetail(memberId);

        if (isNull(memberDetail))
            throw new BlueException(INTERNAL_SERVER_ERROR);

        UPDATE_ITEM_WITH_ASSERT_PACKAGER.accept(memberDetailUpdateParam, memberDetail);

        memberDetailMapper.updateByPrimaryKey(memberDetail);

        return MEMBER_DETAIL_2_MEMBER_DETAIL_INFO.apply(memberDetail);
    }

    /**
     * update member detail status
     *
     * @param memberId
     * @param statusParam
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public MemberDetailInfo updateMemberDetailStatus(Long memberId, StatusParam statusParam) {
        LOGGER.info("memberId = {}, statusParam = {}", memberId, statusParam);
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);
        statusParam.asserts();

        MemberDetail memberDetail = memberDetailMapper.selectByMemberId(memberId);
        if (isNull(memberDetail))
            memberDetail = this.initMemberDetail(memberId);

        if (isNull(memberDetail))
            throw new BlueException(INTERNAL_SERVER_ERROR);

        Integer status = statusParam.getStatus();
        if (memberDetail.getStatus().equals(status))
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        memberDetail.setStatus(status);

        memberDetailMapper.updateStatusByMemberId(memberId, status, TIME_STAMP_GETTER.get());

        return MEMBER_DETAIL_2_MEMBER_DETAIL_INFO.apply(memberDetail);
    }

    /**
     * get member detail by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<MemberDetail> getMemberDetail(Long id) {
        LOGGER.info("id = {}", id);
        return justOrEmpty(memberDetailMapper.selectByPrimaryKey(id));
    }

    /**
     * query member by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<MemberDetailInfo> getMemberDetailInfoWithAssert(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return getMemberDetail(id)
                .flatMap(md ->
                        isValidStatus(md.getStatus()) ?
                                just(md)
                                :
                                error(() -> new BlueException(DATA_HAS_BEEN_FROZEN))
                ).flatMap(md ->
                        just(MEMBER_DETAIL_2_MEMBER_DETAIL_INFO.apply(md))
                );
    }

    /**
     * get member detail info mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<MemberDetailInfo> getMemberDetailInfoByMemberId(Long memberId) {
        LOGGER.info("memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return justOrEmpty(memberDetailMapper.selectByMemberId(memberId))
                .map(MEMBER_DETAIL_2_MEMBER_DETAIL_INFO)
                .switchIfEmpty(defer(() -> justOrEmpty(this.initMemberDetail(memberId)).map(MEMBER_DETAIL_2_MEMBER_DETAIL_INFO)))
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))));
    }

    /**
     * query member detail by member id with assert
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<MemberDetailInfo> getMemberDetailInfoByMemberIdWithAssert(Long memberId) {
        LOGGER.info("memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return getMemberDetailInfoByMemberId(memberId)
                .flatMap(mdi ->
                        isValidStatus(mdi.getStatus()) ?
                                just(mdi)
                                :
                                error(() -> new BlueException(DATA_HAS_BEEN_FROZEN))
                );
    }

    /**
     * select details mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<MemberDetail>> selectMemberDetailByIds(List<Long> ids) {
        LOGGER.info("ids = {}", ids);
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return fromIterable(allotByMax(ids, (int) DB_SELECT.value, false))
                .map(memberDetailMapper::selectByIds)
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
    public Mono<List<MemberDetailInfo>> selectMemberDetailInfoByIds(List<Long> ids) {
        LOGGER.info("ids = {}", ids);
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return fromIterable(allotByMax(ids, (int) DB_SELECT.value, false))
                .map(shardMemberIds -> memberDetailMapper.selectByIds(shardMemberIds)
                        .stream().map(MEMBER_DETAIL_2_MEMBER_DETAIL_INFO).collect(toList()))
                .reduceWith(LinkedList::new, (a, b) -> {
                    a.addAll(b);
                    return a;
                });
    }

    /**
     * select details mono by member ids
     *
     * @param memberIds
     * @return
     */
    @Override
    public Mono<List<MemberDetail>> selectMemberDetailByMemberIds(List<Long> memberIds) {
        LOGGER.info("memberIds = {}", memberIds);
        if (isEmpty(memberIds))
            return just(emptyList());
        if (memberIds.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return fromIterable(allotByMax(memberIds, (int) DB_SELECT.value, false))
                .map(memberDetailMapper::selectByMemberIds)
                .reduceWith(LinkedList::new, (a, b) -> {
                    a.addAll(b);
                    return a;
                });
    }

    /**
     * select details by member ids
     *
     * @param memberIds
     * @return
     */
    @Override
    public Mono<List<MemberDetailInfo>> selectMemberDetailInfoByMemberIds(List<Long> memberIds) {
        LOGGER.info("memberIds = {}", memberIds);
        if (isEmpty(memberIds))
            return just(emptyList());
        if (memberIds.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return fromIterable(allotByMax(memberIds, (int) DB_SELECT.value, false))
                .map(shardMemberIds -> memberDetailMapper.selectByMemberIds(shardMemberIds)
                        .stream().map(MEMBER_DETAIL_2_MEMBER_DETAIL_INFO).collect(toList()))
                .reduceWith(LinkedList::new, (a, b) -> {
                    a.addAll(b);
                    return a;
                });
    }

    /**
     * select member detail by page and condition
     *
     * @param limit
     * @param rows
     * @param memberDetailCondition
     * @return
     */
    @Override
    public Mono<List<MemberDetail>> selectMemberDetailByLimitAndCondition(Long limit, Long rows, MemberDetailCondition memberDetailCondition) {
        LOGGER.info("limit = {}, rows = {}, memberDetailCondition = {}", limit, rows, memberDetailCondition);
        if (isInvalidLimit(limit) || isInvalidRows(rows))
            throw new BlueException(INVALID_PARAM);

        return just(memberDetailMapper.selectByLimitAndCondition(limit, rows, memberDetailCondition));
    }

    /**
     * count member detail by condition
     *
     * @param memberDetailCondition
     * @return
     */
    @Override
    public Mono<Long> countMemberDetailByCondition(MemberDetailCondition memberDetailCondition) {
        LOGGER.info("memberDetailCondition = {}", memberDetailCondition);
        return just(ofNullable(memberDetailMapper.countByCondition(memberDetailCondition)).orElse(0L));
    }

    /**
     * select member detail info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<MemberDetailInfo>> selectMemberDetailInfoPageByPageAndCondition(PageModelRequest<MemberDetailCondition> pageModelRequest) {
        LOGGER.info("pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        MemberDetailCondition memberDetailCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectMemberDetailByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), memberDetailCondition), countMemberDetailByCondition(memberDetailCondition))
                .flatMap(tuple2 ->
                        just(new PageModelResponse<>(MEMBER_DETAILS_2_MEMBER_DETAILS_INFO.apply(tuple2.getT1()), tuple2.getT2()))
                );
    }

}
