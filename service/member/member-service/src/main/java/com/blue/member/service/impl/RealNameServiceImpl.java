package com.blue.member.service.impl;

import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.base.model.common.StatusParam;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.RealNameInfo;
import com.blue.member.constant.RealNameSortAttribute;
import com.blue.member.model.RealNameCondition;
import com.blue.member.model.RealNameUpdateParam;
import com.blue.member.repository.entity.RealName;
import com.blue.member.repository.mapper.RealNameMapper;
import com.blue.member.service.inter.RealNameService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

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
import static com.blue.base.constant.common.BlueNumericalValue.*;
import static com.blue.base.constant.common.ResponseElement.*;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.base.constant.common.Status.INVALID;
import static com.blue.base.constant.common.Status.VALID;
import static com.blue.base.constant.member.Gender.UNKNOWN;
import static com.blue.member.converter.MemberModelConverters.REAL_NAME_2_REAL_NAME_INFO;
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
public class RealNameServiceImpl implements RealNameService {

    private static final Logger LOGGER = getLogger(RealNameServiceImpl.class);

    private final RealNameMapper realNameMapper;

    private BlueIdentityProcessor blueIdentityProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RealNameServiceImpl(RealNameMapper realNameMapper, BlueIdentityProcessor blueIdentityProcessor) {
        this.realNameMapper = realNameMapper;
        this.blueIdentityProcessor = blueIdentityProcessor;
    }

    private final Function<Long, RealName> REAL_NAME_INITIALIZER_WITHOUT_EXIST_VALIDATE = memberId -> {
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        RealName realName = new RealName();

        realName.setId(blueIdentityProcessor.generate(RealName.class));
        realName.setMemberId(memberId);
        realName.setRealName(EMPTY_DATA.value);
        realName.setGender(UNKNOWN.identity);
        realName.setBirthday(EMPTY_DATA.value);
        realName.setNationalityId(BLUE_ID.value);
        realName.setEthnic(EMPTY_DATA.value);
        realName.setIdCardNo(EMPTY_DATA.value);
        realName.setResidenceAddress(EMPTY_DATA.value);
        realName.setIssuingAuthority(EMPTY_DATA.value);
        realName.setSinceDate(EMPTY_DATA.value);
        realName.setExpireDate(EMPTY_DATA.value);
        realName.setExtra(EMPTY_DATA.value);
        realName.setStatus(INVALID.status);
        Long timeStamp = TIME_STAMP_GETTER.get();
        realName.setCreateTime(timeStamp);
        realName.setUpdateTime(timeStamp);

        return realName;
    };

    private final BiConsumer<RealNameUpdateParam, RealName> ATTR_PACKAGER = (realNameUpdateParam, realName) -> {
        if (isNull(realNameUpdateParam) || isNull(realName))
            throw new BlueException(EMPTY_PARAM);


        realName.setStatus(VALID.status);
        realName.setUpdateTime(TIME_STAMP_GETTER.get());
    };

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(RealNameSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<RealNameCondition> CONDITION_PROCESSOR = condition -> {
        if (isNull(condition))
            return new RealNameCondition();

        process(condition, SORT_ATTRIBUTE_MAPPING, RealNameSortAttribute.ID.column);

        return condition;
    };

    /**
     * init real name
     *
     * @param memberId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public RealNameInfo initRealName(Long memberId) {
        LOGGER.info("RealNameInfo initRealName(Long memberId), memberId = {}", memberId);

        RealName realName = REAL_NAME_INITIALIZER_WITHOUT_EXIST_VALIDATE.apply(memberId);
        realNameMapper.insert(realName);

        return REAL_NAME_2_REAL_NAME_INFO.apply(realName);
    }

    /**
     * update real name
     *
     * @param memberId
     * @param realNameUpdateParam
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public RealNameInfo updateRealName(Long memberId, RealNameUpdateParam realNameUpdateParam) {
        LOGGER.info("RealNameInfo updateRealName(Long memberId, RealNameUpdateParam realNameUpdateParam),  memberId = {}, realNameUpdateParam = {}",
                memberId, realNameUpdateParam);
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);
        if (isNull(realNameUpdateParam))
            throw new BlueException(EMPTY_PARAM);
        realNameUpdateParam.asserts();

        RealName realName = realNameMapper.selectByMemberId(memberId);
        if (isNull(realName))
            throw new BlueException(DATA_NOT_EXIST);

        ATTR_PACKAGER.accept(realNameUpdateParam, realName);

        realNameMapper.updateByPrimaryKey(realName);

        return REAL_NAME_2_REAL_NAME_INFO.apply(realName);
    }

    /**
     * update real name status
     *
     * @param id
     * @param statusParam
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public RealNameInfo updateRealNameStatus(Long id, StatusParam statusParam) {
        LOGGER.info("RealNameInfo updateRealNameStatus(Long id, StatusParam statusParam), id = {}, statusParam = {}", id, statusParam);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        statusParam.asserts();

        RealName realName = realNameMapper.selectByPrimaryKey(id);
        if (isNull(realName))
            throw new BlueException(DATA_NOT_EXIST);

        Integer status = statusParam.getStatus();
        if (realName.getStatus().equals(status))
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        realName.setStatus(status);

        realNameMapper.updateStatus(id, status, TIME_STAMP_GETTER.get());

        return REAL_NAME_2_REAL_NAME_INFO.apply(realName);
    }

    /**
     * get by id
     *
     * @param id
     * @return
     */
    @Override
    public RealName getRealName(Long id) {
        LOGGER.info("RealName getRealName(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(realNameMapper.selectByPrimaryKey(id)).orElseThrow(() -> new BlueException(DATA_NOT_EXIST));
    }

    /**
     * get mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<RealName> getRealNameMono(Long id) {
        LOGGER.info("Mono<RealName> getRealNameMono(Long id), id = {}", id);
        return just(getRealName(id));
    }

    /**
     * query real name by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<RealNameInfo> getRealNameInfoMonoWithAssert(Long id) {
        LOGGER.info("Mono<RealNameInfo> getRealNameInfoMonoWithAssert(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return getRealNameMono(id)
                .flatMap(rn -> {
                    if (isInvalidStatus(rn.getStatus()))
                        return error(() -> new BlueException(DATA_HAS_BEEN_FROZEN));
                    LOGGER.info("rn = {}", rn);
                    return just(rn);
                }).flatMap(rn ->
                        just(REAL_NAME_2_REAL_NAME_INFO.apply(rn))
                );
    }

    /**
     * get by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public RealName getRealNameByMemberId(Long memberId) {
        LOGGER.info("RealName getRealNameByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(realNameMapper.selectByMemberId(memberId)).orElseThrow(() -> new BlueException(DATA_NOT_EXIST));
    }

    /**
     * get mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<RealName> getRealNameMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<RealName> getRealNameMonoByMemberId(Long memberId), memberId = {}", memberId);
        return just(getRealNameByMemberId(memberId));
    }

    /**
     * query real name info by member id with assert
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<RealNameInfo> getRealNameInfoMonoByMemberIdWithAssert(Long memberId) {
        LOGGER.info("Mono<MemberDetailInfo> getMemberDetailInfoMonoByMemberIdWithAssert(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return getRealNameMonoByMemberId(memberId)
                .flatMap(rn -> {
                    if (isInvalidStatus(rn.getStatus()))
                        return error(() -> new BlueException(DATA_HAS_BEEN_FROZEN));
                    LOGGER.info("rn = {}", rn);
                    return just(rn);
                }).flatMap(rn ->
                        just(REAL_NAME_2_REAL_NAME_INFO.apply(rn))
                );
    }

    /**
     * select real name by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<RealName> selectRealNameByIds(List<Long> ids) {
        LOGGER.info("List<RealName> selectRealNameByIds(List<Long> ids), ids = {}", ids);
        if (isEmpty(ids))
            return emptyList();
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(realNameMapper::selectByIds)
                .flatMap(List::stream)
                .collect(toList());
    }

    /**
     * select real name mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<RealName>> selectRealNameMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<RealName>> selectRealNameMonoByIds(List<Long> ids), ids = {}", ids);
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return fromIterable(allotByMax(ids, (int) DB_SELECT.value, false))
                .map(realNameMapper::selectByIds)
                .reduceWith(LinkedList::new, (a, b) -> {
                    a.addAll(b);
                    return a;
                });
    }

    /**
     * select real name info by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<RealNameInfo>> selectRealNameInfoMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<RealNameInfo>> selectRealNameInfoMonoByIds(List<Long> ids), ids = {}", ids);
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return fromIterable(allotByMax(ids, (int) DB_SELECT.value, false))
                .map(shardMemberIds -> realNameMapper.selectByIds(shardMemberIds)
                        .stream().map(REAL_NAME_2_REAL_NAME_INFO).collect(toList()))
                .reduceWith(LinkedList::new, (a, b) -> {
                    a.addAll(b);
                    return a;
                });
    }

    /**
     * select real name by member ids
     *
     * @param memberIds
     * @return
     */
    @Override
    public List<RealName> selectRealNameByMemberIds(List<Long> memberIds) {
        LOGGER.info("List<RealName> selectRealNameByMemberIds(List<Long> memberIds), memberIds = {}", memberIds);
        if (isEmpty(memberIds))
            return emptyList();
        if (memberIds.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return allotByMax(memberIds, (int) DB_SELECT.value, false)
                .stream().map(realNameMapper::selectByMemberIds)
                .flatMap(List::stream)
                .collect(toList());
    }

    /**
     * select real name mono by member ids
     *
     * @param memberIds
     * @return
     */
    @Override
    public Mono<List<RealName>> selectRealNameMonoByMemberIds(List<Long> memberIds) {
        LOGGER.info("Mono<List<RealName>> selectRealNameMonoByMemberIds(List<Long> memberIds), memberIds = {}", memberIds);
        if (isEmpty(memberIds))
            return just(emptyList());
        if (memberIds.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return fromIterable(allotByMax(memberIds, (int) DB_SELECT.value, false))
                .map(realNameMapper::selectByMemberIds)
                .reduceWith(LinkedList::new, (a, b) -> {
                    a.addAll(b);
                    return a;
                });
    }

    /**
     * select real name info by member ids
     *
     * @param memberIds
     * @return
     */
    @Override
    public Mono<List<RealNameInfo>> selectRealNameInfoMonoByMemberIds(List<Long> memberIds) {
        LOGGER.info("Mono<List<RealNameInfo>> selectRealNameInfoMonoByMemberIds(List<Long> memberIds), memberIds = {}", memberIds);
        if (isEmpty(memberIds))
            return just(emptyList());
        if (memberIds.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return fromIterable(allotByMax(memberIds, (int) DB_SELECT.value, false))
                .map(shardMemberIds -> realNameMapper.selectByMemberIds(shardMemberIds)
                        .stream().map(REAL_NAME_2_REAL_NAME_INFO).collect(toList()))
                .reduceWith(LinkedList::new, (a, b) -> {
                    a.addAll(b);
                    return a;
                });
    }

    /**
     * select real name by page and condition
     *
     * @param limit
     * @param rows
     * @param realNameCondition
     * @return
     */
    @Override
    public Mono<List<RealName>> selectRealNameMonoByLimitAndCondition(Long limit, Long rows, RealNameCondition realNameCondition) {
        LOGGER.info("Mono<List<RealName>> selectRealNameMonoByLimitAndCondition(Long limit, Long rows, RealNameCondition realNameCondition), " +
                "limit = {}, rows = {}, realNameCondition = {}", limit, rows, realNameCondition);
        if (isNull(limit) || limit < 0 || isNull(rows) || rows < 1)
            throw new BlueException(INVALID_PARAM);

        return just(realNameMapper.selectByLimitAndCondition(limit, rows, realNameCondition));
    }

    /**
     * count real name by condition
     *
     * @param realNameCondition
     * @return
     */
    @Override
    public Mono<Long> countRealNameMonoByCondition(RealNameCondition realNameCondition) {
        LOGGER.info("Mono<Long> countRealNameMonoByCondition(RealNameCondition realNameCondition), realNameCondition = {}", realNameCondition);
        return just(ofNullable(realNameMapper.countByCondition(realNameCondition)).orElse(0L));
    }

    /**
     * select real name info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<RealNameInfo>> selectRealNameInfoPageMonoByPageAndCondition(PageModelRequest<RealNameCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<RealNameInfo>> selectRealNameInfoPageMonoByPageAndCondition(PageModelRequest<RealNameCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        RealNameCondition realNameCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(selectRealNameMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), realNameCondition), countRealNameMonoByCondition(realNameCondition))
                .flatMap(tuple2 -> {
                    List<RealName> realNames = tuple2.getT1();
                    Mono<List<RealNameInfo>> realNameInfosMono = realNames.size() > 0 ?
                            just(realNames.stream().map(REAL_NAME_2_REAL_NAME_INFO).collect(toList()))
                            :
                            just(emptyList());

                    return realNameInfosMono
                            .flatMap(realNameInfos ->
                                    just(new PageModelResponse<>(realNameInfos, tuple2.getT2())));
                });
    }

}
