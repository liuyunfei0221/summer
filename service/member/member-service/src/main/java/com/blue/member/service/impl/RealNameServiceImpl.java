package com.blue.member.service.impl;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.common.StatusParam;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.RealNameInfo;
import com.blue.member.api.model.RealNameValidateResult;
import com.blue.member.component.realname.validator.RealNameProcessor;
import com.blue.member.constant.RealNameSortAttribute;
import com.blue.member.model.RealNameCondition;
import com.blue.member.model.RealNameUpdateParam;
import com.blue.member.repository.entity.MemberBasic;
import com.blue.member.repository.entity.RealName;
import com.blue.member.repository.mapper.RealNameMapper;
import com.blue.member.service.inter.MemberBasicService;
import com.blue.member.service.inter.RealNameService;
import com.blue.redisson.component.SynchronizedProcessor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

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
import static com.blue.basic.common.base.ConstantProcessor.assertGender;
import static com.blue.basic.common.base.ConstantProcessor.assertStatus;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_SELECT;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_SERVICE_SELECT;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Status.INVALID;
import static com.blue.basic.constant.common.Status.VALID;
import static com.blue.basic.constant.common.SyncKeyPrefix.MEMBER_DETAIL_INSERT_SYNC_PRE;
import static com.blue.database.common.ConditionSortProcessor.process;
import static com.blue.member.converter.MemberModelConverters.REAL_NAMES_2_REAL_NAMES_INFO;
import static com.blue.member.converter.MemberModelConverters.REAL_NAME_2_REAL_NAME_INFO;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.*;

/**
 * member detail service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode", "BlockingMethodInNonBlockingContext"})
@Service
public class RealNameServiceImpl implements RealNameService {

    private static final Logger LOGGER = getLogger(RealNameServiceImpl.class);

    private MemberBasicService memberBasicService;

    private final SynchronizedProcessor synchronizedProcessor;

    private RealNameProcessor realNameProcessor;

    private BlueIdentityProcessor blueIdentityProcessor;

    private RealNameMapper realNameMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RealNameServiceImpl(MemberBasicService memberBasicService, SynchronizedProcessor synchronizedProcessor, RealNameProcessor realNameProcessor, RealNameMapper realNameMapper, BlueIdentityProcessor blueIdentityProcessor) {
        this.memberBasicService = memberBasicService;
        this.synchronizedProcessor = synchronizedProcessor;
        this.realNameProcessor = realNameProcessor;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.realNameMapper = realNameMapper;
    }

    private static final Function<Long, String> MEMBER_DETAIL_INSERT_SYNC_KEY_GEN = memberId -> {
        if (isValidIdentity(memberId))
            return MEMBER_DETAIL_INSERT_SYNC_PRE.prefix + memberId;

        throw new BlueException(BAD_REQUEST);
    };

    private final Function<Long, RealName> INIT_REAL_NAME_GEN = mid -> {
        if (isInvalidIdentity(mid))
            throw new BlueException(INVALID_IDENTITY);

        MemberBasic memberBasic = memberBasicService.getMemberBasicOpt(mid)
                .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));
        if (isNull(memberBasic))
            throw new BlueException(INVALID_PARAM);

        RealName realName = new RealName();
        realName.setId(blueIdentityProcessor.generate(RealName.class));
        realName.setMemberId(mid);
        realName.setStatus(INVALID.status);

        Long stamp = TIME_STAMP_GETTER.get();
        realName.setCreateTime(stamp);
        realName.setUpdateTime(stamp);

        return realName;
    };

    private final Consumer<RealName> INSERT_ITEM_VALIDATOR = t -> {
        if (isNull(t))
            throw new BlueException(EMPTY_PARAM);

        if (isInvalidIdentity(t.getId()) || isInvalidIdentity(t.getMemberId()))
            throw new BlueException(INVALID_PARAM);

        assertStatus(t.getStatus(), false);

        if (isNull(t.getCreateTime()) || isNull(t.getUpdateTime()))
            throw new BlueException(INVALID_PARAM);

        if (isNotNull(realNameMapper.selectByPrimaryKey(t.getId())))
            throw new BlueException(DATA_ALREADY_EXIST);

        if (isNotNull(realNameMapper.selectByMemberId(t.getMemberId())))
            throw new BlueException(DATA_ALREADY_EXIST);
    };

    private final BiConsumer<RealNameUpdateParam, RealName> UPDATE_ITEM_WITH_ASSERT_PACKAGER = (p, t) -> {
        if (isNull(p) || isNull(t))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        boolean alteration = false;

        String realName = p.getRealName();
        if (isNotBlank(realName) && !realName.equals(t.getRealName())) {
            t.setRealName(realName);
            alteration = true;
        }

        Integer gender = p.getGender();
        if (isNotNull(gender) && !gender.equals(t.getGender())) {
            assertGender(gender, false);
            t.setGender(gender);
            alteration = true;
        }

        String birthday = p.getBirthday();
        if (isNotBlank(birthday) && !birthday.equals(t.getBirthday())) {
            t.setBirthday(birthday);
            alteration = true;
        }

        String nationality = p.getNationality();
        if (isNotBlank(nationality) && !nationality.equals(t.getNationality())) {
            t.setNationality(nationality);
            alteration = true;
        }

        String ethnic = p.getEthnic();
        if (isNotBlank(ethnic) && !ethnic.equals(t.getEthnic())) {
            t.setEthnic(ethnic);
            alteration = true;
        }

        String idCardNo = p.getIdCardNo();
        if (isNotBlank(idCardNo) && !idCardNo.equals(t.getIdCardNo())) {
            t.setIdCardNo(idCardNo);
            alteration = true;
        }

        String residenceAddress = p.getResidenceAddress();
        if (isNotBlank(residenceAddress) && !residenceAddress.equals(t.getResidenceAddress())) {
            t.setResidenceAddress(residenceAddress);
            alteration = true;
        }

        String issuingAuthority = p.getIssuingAuthority();
        if (isNotBlank(issuingAuthority) && !issuingAuthority.equals(t.getIssuingAuthority())) {
            t.setIssuingAuthority(issuingAuthority);
            alteration = true;
        }

        String sinceDate = p.getSinceDate();
        if (isNotBlank(sinceDate) && !sinceDate.equals(t.getSinceDate())) {
            t.setSinceDate(sinceDate);
            alteration = true;
        }

        String expireDate = p.getExpireDate();
        if (isNotBlank(expireDate) && !expireDate.equals(t.getExpireDate())) {
            t.setExpireDate(expireDate);
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

    private final Consumer<RealName> REAL_NAME_INFO_VALIDATOR = realName -> {
        RealNameValidateResult validateResult = realNameProcessor.validate(realName);
        if (!validateResult.isAllow())
            throw new BlueException(INVALID_PARAM);
    };

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(RealNameSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<RealNameCondition> CONDITION_PROCESSOR = c -> {
        RealNameCondition rnc = isNotNull(c) ? c : new RealNameCondition();

        process(rnc, SORT_ATTRIBUTE_MAPPING, RealNameSortAttribute.CREATE_TIME.column);

        return rnc;
    };

    /**
     * init real name
     *
     * @param memberId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public RealName initRealName(Long memberId) {
        LOGGER.info("memberId = {}", memberId);

        RealName realName = INIT_REAL_NAME_GEN.apply(memberId);

        synchronizedProcessor.handleTaskWithSync(MEMBER_DETAIL_INSERT_SYNC_KEY_GEN.apply(memberId), () -> {
            INSERT_ITEM_VALIDATOR.accept(realName);

            int inserted = realNameMapper.insert(realName);
            LOGGER.info("inserted = {}", inserted);
        });

        return realName;
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
        LOGGER.info("memberId = {}, realNameUpdateParam = {}", memberId, realNameUpdateParam);
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);
        if (isNull(realNameUpdateParam))
            throw new BlueException(EMPTY_PARAM);
        realNameUpdateParam.asserts();

        RealName realName = realNameMapper.selectByMemberId(memberId);
        if (isNull(realName))
            realName = this.initRealName(memberId);

        if (isNull(realName))
            throw new BlueException(INTERNAL_SERVER_ERROR);

        UPDATE_ITEM_WITH_ASSERT_PACKAGER.accept(realNameUpdateParam, realName);

        REAL_NAME_INFO_VALIDATOR.accept(realName);

        realNameMapper.updateByPrimaryKey(realName);

        return REAL_NAME_2_REAL_NAME_INFO.apply(realName);
    }

    /**
     * update real name status
     *
     * @param memberId
     * @param statusParam
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public RealNameInfo updateRealNameStatus(Long memberId, StatusParam statusParam) {
        LOGGER.info("memberId = {}, statusParam = {}", memberId, statusParam);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);
        statusParam.asserts();

        RealName realName = realNameMapper.selectByMemberId(memberId);
        if (isNull(realName))
            realName = this.initRealName(memberId);

        if (isNull(realName))
            throw new BlueException(INTERNAL_SERVER_ERROR);

        Integer status = statusParam.getStatus();
        if (realName.getStatus().equals(status))
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        realName.setStatus(status);

        realNameMapper.updateStatusByMemberId(memberId, status, TIME_STAMP_GETTER.get());

        return REAL_NAME_2_REAL_NAME_INFO.apply(realName);
    }

    /**
     * get mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<RealName> getRealName(Long id) {
        LOGGER.info("id = {}", id);
        return justOrEmpty(realNameMapper.selectByPrimaryKey(id));
    }

    /**
     * query real name by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<RealNameInfo> getRealNameInfoWithAssert(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return getRealName(id)
                .flatMap(rn ->
                        isValidStatus(rn.getStatus()) ?
                                just(rn)
                                :
                                error(() -> new BlueException(DATA_HAS_BEEN_FROZEN))
                ).flatMap(rn ->
                        just(REAL_NAME_2_REAL_NAME_INFO.apply(rn))
                );
    }

    /**
     * get real name info mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<RealNameInfo> getRealNameInfoByMemberId(Long memberId) {
        LOGGER.info("memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return justOrEmpty(realNameMapper.selectByMemberId(memberId))
                .map(REAL_NAME_2_REAL_NAME_INFO);
    }

    /**
     * select real name mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<RealName>> selectRealNameByIds(List<Long> ids) {
        LOGGER.info("ids = {}", ids);
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
    public Mono<List<RealNameInfo>> selectRealNameInfoByIds(List<Long> ids) {
        LOGGER.info("ids = {}", ids);
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
     * select real name mono by member ids
     *
     * @param memberIds
     * @return
     */
    @Override
    public Mono<List<RealName>> selectRealNameMonoByMemberIds(List<Long> memberIds) {
        LOGGER.info("memberIds = {}", memberIds);
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
    public Mono<List<RealNameInfo>> selectRealNameInfoByMemberIds(List<Long> memberIds) {
        LOGGER.info("memberIds = {}", memberIds);
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
    public Mono<List<RealName>> selectRealNameByLimitAndCondition(Long limit, Long rows, RealNameCondition realNameCondition) {
        LOGGER.info("limit = {}, rows = {}, realNameCondition = {}", limit, rows, realNameCondition);
        if (isInvalidLimit(limit) || isInvalidRows(rows))
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
    public Mono<Long> countRealNameByCondition(RealNameCondition realNameCondition) {
        LOGGER.info("realNameCondition = {}", realNameCondition);
        return just(ofNullable(realNameMapper.countByCondition(realNameCondition)).orElse(0L));
    }

    /**
     * select real name info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<RealNameInfo>> selectRealNameInfoPageByPageAndCondition(PageModelRequest<RealNameCondition> pageModelRequest) {
        LOGGER.info("pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        RealNameCondition realNameCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectRealNameByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), realNameCondition), countRealNameByCondition(realNameCondition))
                .flatMap(tuple2 ->
                        just(new PageModelResponse<>(REAL_NAMES_2_REAL_NAMES_INFO.apply(tuple2.getT1()), tuple2.getT2()))
                );
    }

}
