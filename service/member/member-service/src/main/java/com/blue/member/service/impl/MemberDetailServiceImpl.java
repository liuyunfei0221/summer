package com.blue.member.service.impl;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.base.model.exps.BlueException;
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

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.common.base.ConditionSortProcessor.process;
import static com.blue.base.common.base.ConstantProcessor.assertGenderIdentity;
import static com.blue.base.common.base.ConstantProcessor.assertStatus;
import static com.blue.base.constant.common.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.common.BlueNumericalValue.MAX_SERVICE_SELECT;
import static com.blue.base.constant.common.ResponseElement.*;
import static com.blue.base.constant.common.Status.INVALID;
import static com.blue.base.constant.common.Symbol.DATABASE_WILDCARD;
import static com.blue.base.constant.member.MemberThreshold.MIN_YEAR_OF_BIRTH;
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

    private final Function<Long, MemberDetail> MEMBER_DETAIL_INITIALIZER_WITHOUT_EXIST_VALIDATE = memberId -> {
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        MemberDetail memberDetail = new MemberDetail();
        memberDetail.setId(blueIdentityProcessor.generate(RealName.class));
        memberDetail.setMemberId(memberId);
        memberDetail.setStatus(INVALID.status);
        Long timeStamp = TIME_STAMP_GETTER.get();
        memberDetail.setCreateTime(timeStamp);
        memberDetail.setUpdateTime(timeStamp);

        return memberDetail;
    };


    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(MemberDetailSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<MemberDetailCondition> CONDITION_PROCESSOR = condition -> {
        if (isNull(condition))
            return new MemberDetailCondition();

        process(condition, SORT_ATTRIBUTE_MAPPING, MemberDetailSortAttribute.ID.column);

        ofNullable(condition.getHobbyLike()).filter(BlueChecker::isNotBlank).ifPresent(hobbyLike ->
                condition.setHobbyLike(DATABASE_WILDCARD.identity + hobbyLike + DATABASE_WILDCARD.identity));

        return condition;
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

    private static final BiConsumer<MemberDetailUpdateParam, MemberDetail> ATTR_PACKAGER = (memberDetailUpdateParam, memberDetail) -> {
        if (isNull(memberDetailUpdateParam) || isNull(memberDetail))
            throw new BlueException(EMPTY_PARAM);

        ofNullable(memberDetailUpdateParam.getName()).filter(BlueChecker::isNotBlank)
                .ifPresent(memberDetail::setName);
        ofNullable(memberDetailUpdateParam.getGender()).ifPresent(gender -> {
            assertGenderIdentity(gender, false);
            memberDetail.setGender(gender);
        });
        ofNullable(memberDetailUpdateParam.getPhone()).filter(BlueChecker::isNotBlank)
                .ifPresent(memberDetail::setPhone);
        ofNullable(memberDetailUpdateParam.getEmail()).filter(BlueChecker::isNotBlank)
                .ifPresent(memberDetail::setEmail);

        ofNullable(memberDetailUpdateParam.getYearOfBirth())
                .ifPresent(yearOfBirth -> {
                    if (yearOfBirth < MIN_YEAR_OF_BIRTH.threshold || yearOfBirth > LocalDate.now().getYear())
                        throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid year of birth");

                    memberDetail.setYearOfBirth(yearOfBirth);
//                    memberDetail.setChineseZodiac();
                });


        //rpcCityServiceConsumer



//        private Integer monthOfBirth;
//        private Integer dayOfBirth;
//        private Integer chineseZodiac;
//        private Integer zodiacSign;
//        private Integer height;
//        private Integer weight;
//        private Long countryId;
//        private String country;
//        private Long stateId;
//        private String state;
//        private Long cityId;
//        private String city;
//        private String address;
//        private String profile;
//        private String hobby;
//        private String homepage;
//        private String extra;
//        private Integer status;
//        private Long createTime;
//        private Long updateTime;

    };

    /**
     * update member detail
     *
     * @param memberDetailUpdateParam
     * @param memberId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public MemberDetailInfo updateMemberDetail(MemberDetailUpdateParam memberDetailUpdateParam, Long memberId) {
        LOGGER.info("MemberDetailInfo updateMemberDetail(MemberDetailUpdateParam memberDetailUpdateParam, Long memberId), memberDetailUpdateParam = {}, memberId = {}",
                memberDetailUpdateParam, memberId);
        if (isNull(memberDetailUpdateParam))
            throw new BlueException(EMPTY_PARAM);
        memberDetailUpdateParam.asserts();
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        MemberDetail memberDetail = memberDetailMapper.selectByPrimaryKey(memberDetailUpdateParam.getId());
        if (isNull(memberDetail))
            throw new BlueException(DATA_NOT_EXIST);
        if (!memberDetail.getMemberId().equals(memberId))
            throw new BlueException(DATA_NOT_BELONG_TO_YOU);

        ATTR_PACKAGER.accept(memberDetailUpdateParam, memberDetail);

        memberDetailMapper.updateByPrimaryKey(memberDetail);

        return MEMBER_DETAIL_2_MEMBER_DETAIL_INFO.apply(memberDetail);
    }

    /**
     * update member detail status
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public MemberDetailInfo updateMemberDetailStatus(Long id, Integer status) {
        LOGGER.info("MemberDetailInfo updateMemberDetailStatus(Long id, Integer status), id = {}, status = {}", id, status);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        assertStatus(status, false);

        MemberDetail memberDetail = memberDetailMapper.selectByPrimaryKey(id);
        if (isNull(memberDetail))
            throw new BlueException(DATA_NOT_EXIST);

        if (status.equals(memberDetail.getStatus()))
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        memberDetail.setStatus(status);

        memberDetailMapper.updateStatus(id, status, TIME_STAMP_GETTER.get());

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
                .flatMap(md -> {
                    if (isInvalidStatus(md.getStatus()))
                        return error(() -> new BlueException(DATA_HAS_BEEN_FROZEN));
                    LOGGER.info("md = {}", md);
                    return just(md);
                }).flatMap(md ->
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
                .flatMap(md -> {
                    if (isInvalidStatus(md.getStatus()))
                        return error(() -> new BlueException(DATA_HAS_BEEN_FROZEN));
                    LOGGER.info("md = {}", md);
                    return just(md);
                }).flatMap(md ->
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

        MemberDetailCondition memberDetailCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(selectMemberDetailMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), memberDetailCondition), countMemberDetailMonoByCondition(memberDetailCondition))
                .flatMap(tuple2 -> {
                    List<MemberDetail> memberDetails = tuple2.getT1();
                    Mono<List<MemberDetailInfo>> memberDetailInfosMono = memberDetails.size() > 0 ?
                            just(memberDetails.stream().map(MEMBER_DETAIL_2_MEMBER_DETAIL_INFO).collect(toList()))
                            :
                            just(emptyList());

                    return memberDetailInfosMono
                            .flatMap(memberInfos ->
                                    just(new PageModelResponse<>(memberInfos, tuple2.getT2())));
                });
    }

}
