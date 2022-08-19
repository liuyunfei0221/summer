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
import com.blue.member.repository.entity.MemberDetail;
import com.blue.member.repository.entity.RealName;
import com.blue.member.repository.mapper.MemberDetailMapper;
import com.blue.member.service.inter.MemberDetailService;
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
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.database.common.ConditionSortProcessor.process;
import static com.blue.basic.common.base.ConstantProcessor.*;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_SELECT;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_SERVICE_SELECT;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Status.INVALID;
import static com.blue.basic.constant.common.Status.VALID;
import static com.blue.basic.constant.common.SummerAttr.DATE_FORMATTER;
import static com.blue.basic.constant.common.Symbol.PERCENT;
import static com.blue.basic.constant.member.MemberThreshold.*;
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
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class MemberDetailServiceImpl implements MemberDetailService {

    private static final Logger LOGGER = getLogger(MemberDetailServiceImpl.class);

    private RpcCityServiceConsumer rpcCityServiceConsumer;

    private final MemberDetailMapper memberDetailMapper;

    private BlueIdentityProcessor blueIdentityProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MemberDetailServiceImpl(RpcCityServiceConsumer rpcCityServiceConsumer, MemberDetailMapper memberDetailMapper,
                                   BlueIdentityProcessor blueIdentityProcessor) {
        this.rpcCityServiceConsumer = rpcCityServiceConsumer;
        this.memberDetailMapper = memberDetailMapper;
        this.blueIdentityProcessor = blueIdentityProcessor;
    }

    private final Function<Long, MemberDetail> MEMBER_DETAIL_INITIALIZER_WITHOUT_EXIST_VALIDATE = mid -> {
        if (isInvalidIdentity(mid))
            throw new BlueException(INVALID_IDENTITY);

        MemberDetail memberDetail = new MemberDetail();
        memberDetail.setId(blueIdentityProcessor.generate(RealName.class));
        memberDetail.setMemberId(mid);
        memberDetail.setStatus(INVALID.status);

        Long stamp = TIME_STAMP_GETTER.get();
        memberDetail.setCreateTime(stamp);
        memberDetail.setUpdateTime(stamp);

        return memberDetail;
    };

    private final BiConsumer<MemberDetailUpdateParam, MemberDetail> ATTR_PACKAGER = (p, t) -> {
        if (isNull(p) || isNull(t))
            throw new BlueException(EMPTY_PARAM);

        ofNullable(p.getName()).filter(BlueChecker::isNotBlank)
                .ifPresent(t::setName);
        ofNullable(p.getGender()).ifPresent(gender -> {
            assertGender(gender, false);
            t.setGender(gender);
        });
        ofNullable(p.getPhone()).filter(BlueChecker::isNotBlank)
                .ifPresent(t::setPhone);
        ofNullable(p.getEmail()).filter(BlueChecker::isNotBlank)
                .ifPresent(t::setEmail);

        ofNullable(p.getBirthDay()).filter(BlueChecker::isNotBlank)
                .ifPresent(birthDay -> {
                    LocalDate localDate;
                    try {
                        localDate = LocalDate.parse(birthDay, DATE_FORMATTER);
                    } catch (Exception e) {
                        throw new BlueException(INVALID_PARAM);
                    }

                    Integer yearOfBirth = localDate.getYear();
                    Integer monthOfBirth = localDate.getMonthValue();
                    Integer dayOfBirth = localDate.getDayOfMonth();

                    if (yearOfBirth.equals(t.getYearOfBirth()) && monthOfBirth.equals(t.getMonthOfBirth()) && dayOfBirth.equals(t.getDayOfBirth()))
                        return;

                    t.setYearOfBirth(yearOfBirth);
                    t.setMonthOfBirth(monthOfBirth);
                    t.setDayOfBirth(dayOfBirth);
                });

        ofNullable(p.getChineseZodiac())
                .ifPresent(chineseZodiac -> {
                    assertChineseZodiac(chineseZodiac, false);
                    t.setChineseZodiac(chineseZodiac);
                });

        ofNullable(p.getZodiacSign())
                .ifPresent(zodiacSign -> {
                    assertZodiacSign(zodiacSign, false);
                    t.setZodiacSign(zodiacSign);
                });

        ofNullable(p.getHeight())
                .ifPresent(height -> {
                    if (height < MIN_HEIGHT.threshold || height > MAX_HEIGHT.threshold)
                        throw new BlueException(INVALID_PARAM);
                    t.setHeight(height);
                });

        ofNullable(p.getWeight())
                .ifPresent(weight -> {
                    if (weight < MIN_WEIGHT.threshold || weight > MAX_WEIGHT.threshold)
                        throw new BlueException(INVALID_PARAM);
                    t.setWeight(weight);
                });

        ofNullable(p.getCityId())
                .filter(cityId -> !cityId.equals(t.getCityId()))
                .ifPresent(cityId -> {
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
                });

        ofNullable(p.getAddress())
                .filter(BlueChecker::isNotBlank)
                .ifPresent(t::setAddress);

        ofNullable(p.getProfile())
                .filter(BlueChecker::isNotBlank)
                .ifPresent(t::setProfile);

        ofNullable(p.getHobby())
                .filter(BlueChecker::isNotBlank)
                .ifPresent(t::setHobby);

        ofNullable(p.getHomepage())
                .filter(BlueChecker::isNotBlank)
                .ifPresent(t::setHomepage);

        ofNullable(p.getExtra())
                .filter(BlueChecker::isNotBlank)
                .ifPresent(t::setExtra);

        t.setStatus(VALID.status);
        t.setUpdateTime(TIME_STAMP_GETTER.get());
    };

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(MemberDetailSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<MemberDetailCondition> CONDITION_PROCESSOR = c -> {
        if (isNull(c))
            return new MemberDetailCondition();

        process(c, SORT_ATTRIBUTE_MAPPING, MemberDetailSortAttribute.CREATE_TIME.column);

        ofNullable(c.getHobbyLike()).filter(BlueChecker::isNotBlank).ifPresent(hobbyLike ->
                c.setHobbyLike(PERCENT.identity + hobbyLike + PERCENT.identity));

        return c;
    };

    /**
     * init member detail
     *
     * @param memberId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public MemberDetailInfo initMemberDetail(Long memberId) {
        LOGGER.info("MemberDetailInfo initMemberDetail(Long memberId), memberId = {}", memberId);

        MemberDetail memberDetail = MEMBER_DETAIL_INITIALIZER_WITHOUT_EXIST_VALIDATE.apply(memberId);
        memberDetailMapper.insert(memberDetail);

        return MEMBER_DETAIL_2_MEMBER_DETAIL_INFO.apply(memberDetail);
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
        LOGGER.info("MemberDetailInfo updateMemberDetail(Long memberId, MemberDetailUpdateParam memberDetailUpdateParam),  memberId = {}, memberDetailUpdateParam = {}",
                memberId, memberDetailUpdateParam);
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);
        if (isNull(memberDetailUpdateParam))
            throw new BlueException(EMPTY_PARAM);
        memberDetailUpdateParam.asserts();

        MemberDetail memberDetail = memberDetailMapper.selectByMemberId(memberId);
        if (isNull(memberDetail))
            throw new BlueException(DATA_NOT_EXIST);

        ATTR_PACKAGER.accept(memberDetailUpdateParam, memberDetail);

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
        LOGGER.info("MemberDetailInfo updateMemberDetailStatus(Long memberId, StatusParam statusParam), memberId = {}, statusParam = {}", memberId, statusParam);
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);
        statusParam.asserts();

        MemberDetail memberDetail = memberDetailMapper.selectByMemberId(memberId);
        if (isNull(memberDetail))
            throw new BlueException(DATA_NOT_EXIST);

        Integer status = statusParam.getStatus();
        if (memberDetail.getStatus().equals(status))
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        memberDetail.setStatus(status);

        memberDetailMapper.updateStatus(memberId, status, TIME_STAMP_GETTER.get());

        return MEMBER_DETAIL_2_MEMBER_DETAIL_INFO.apply(memberDetail);
    }

    /**
     * get by id
     *
     * @param id
     * @return
     */
    @Override
    public MemberDetail getMemberDetail(Long id) {
        LOGGER.info("MemberDetail getMemberDetail(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(memberDetailMapper.selectByPrimaryKey(id)).orElseThrow(() -> new BlueException(DATA_NOT_EXIST));
    }

    /**
     * get member detail by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<MemberDetail> getMemberDetailMono(Long id) {
        LOGGER.info("Mono<MemberDetail> getMemberDetailMono(Long id), id = {}", id);
        return just(getMemberDetail(id));
    }

    /**
     * query member by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<MemberDetailInfo> getMemberDetailInfoMonoWithAssert(Long id) {
        LOGGER.info("Mono<MemberDetailInfo> getMemberDetailInfoMonoWithAssert(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return getMemberDetailMono(id)
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
     * get by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public MemberDetail getMemberDetailByMemberId(Long memberId) {
        LOGGER.info("MemberDetail getMemberDetailByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(memberDetailMapper.selectByMemberId(memberId)).orElseThrow(() -> new BlueException(DATA_NOT_EXIST));
    }

    /**
     * get member detail mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<MemberDetail> getMemberDetailMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<MemberDetail> getMemberDetailMonoByMemberId(Long memberId), memberId = {}", memberId);
        return just(getMemberDetailByMemberId(memberId));
    }

    /**
     * query member detail by member id with assert
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<MemberDetailInfo> getMemberDetailInfoMonoByMemberIdWithAssert(Long memberId) {
        LOGGER.info("Mono<MemberDetailInfo> getMemberDetailInfoMonoByMemberIdWithAssert(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return getMemberDetailMonoByMemberId(memberId)
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
     * select details by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<MemberDetail> selectMemberDetailByIds(List<Long> ids) {
        LOGGER.info("List<MemberDetail> selectMemberDetailByIds(List<Long> ids), ids = {}", ids);
        if (isEmpty(ids))
            return emptyList();
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(memberDetailMapper::selectByIds)
                .flatMap(List::stream)
                .collect(toList());
    }

    /**
     * select details mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<MemberDetail>> selectMemberDetailMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<MemberDetail>> selectMemberDetailMonoByIds(List<Long> ids), ids = {}", ids);
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
    public Mono<List<MemberDetailInfo>> selectMemberDetailInfoMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<MemberDetailInfo>> selectMemberDetailInfoMonoByIds(List<Long> ids), ids = {}", ids);
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
     * select details by member ids
     *
     * @param memberIds
     * @return
     */
    @Override
    public List<MemberDetail> selectMemberDetailByMemberIds(List<Long> memberIds) {
        LOGGER.info("List<MemberDetail> selectMemberDetailByMemberIds(List<Long> memberIds), memberIds = {}", memberIds);
        if (isEmpty(memberIds))
            return emptyList();
        if (memberIds.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return allotByMax(memberIds, (int) DB_SELECT.value, false)
                .stream().map(memberDetailMapper::selectByMemberIds)
                .flatMap(List::stream)
                .collect(toList());
    }

    /**
     * select details mono by member ids
     *
     * @param memberIds
     * @return
     */
    @Override
    public Mono<List<MemberDetail>> selectMemberDetailMonoByMemberIds(List<Long> memberIds) {
        LOGGER.info("Mono<List<MemberDetail>> selectMemberDetailMonoByMemberIds(List<Long> memberIds), memberIds = {}", memberIds);
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
    public Mono<List<MemberDetailInfo>> selectMemberDetailInfoMonoByMemberIds(List<Long> memberIds) {
        LOGGER.info("Mono<List<MemberDetailInfo>> selectMemberDetailInfoMonoByMemberIds(List<Long> memberIds), memberIds = {}", memberIds);
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
    public Mono<List<MemberDetail>> selectMemberDetailMonoByLimitAndCondition(Long limit, Long rows, MemberDetailCondition memberDetailCondition) {
        LOGGER.info("Mono<List<MemberDetail>> selectMemberDetailMonoByLimitAndCondition(Long limit, Long rows, MemberDetailCondition memberDetailCondition), " +
                "limit = {}, rows = {}, memberDetailCondition = {}", limit, rows, memberDetailCondition);
        if (isNull(limit) || limit < 0 || isNull(rows) || rows < 1)
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
    public Mono<Long> countMemberDetailMonoByCondition(MemberDetailCondition memberDetailCondition) {
        LOGGER.info("Mono<Long> countMemberDetailMonoByCondition(MemberDetailCondition memberDetailCondition), memberDetailCondition = {}", memberDetailCondition);
        return just(ofNullable(memberDetailMapper.countByCondition(memberDetailCondition)).orElse(0L));
    }

    /**
     * select member detail info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<MemberDetailInfo>> selectMemberDetailInfoPageMonoByPageAndCondition(PageModelRequest<MemberDetailCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<MemberDetailInfo>> selectMemberDetailInfoPageMonoByPageAndCondition(PageModelRequest<MemberDetailCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        MemberDetailCondition memberDetailCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectMemberDetailMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), memberDetailCondition), countMemberDetailMonoByCondition(memberDetailCondition))
                .flatMap(tuple2 ->
                        just(new PageModelResponse<>(MEMBER_DETAILS_2_MEMBER_DETAILS_INFO.apply(tuple2.getT1()), tuple2.getT2()))
                );
    }

}
