package com.blue.member.service.impl;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.constant.MemberBasicSortAttribute;
import com.blue.member.model.MemberBasicCondition;
import com.blue.member.repository.entity.MemberBasic;
import com.blue.member.repository.mapper.MemberBasicMapper;
import com.blue.member.service.inter.MemberBasicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.ConstantProcessor.getSortTypeByIdentity;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.member.converter.MemberModelConverters.MEMBER_BASIC_2_MEMBER_BASIC_INFO;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * member basic service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class MemberBasicServiceImpl implements MemberBasicService {

    private static final Logger LOGGER = getLogger(MemberBasicServiceImpl.class);

    private MemberBasicMapper memberBasicMapper;

    private final BlueIdentityProcessor blueIdentityProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MemberBasicServiceImpl(MemberBasicMapper memberBasicMapper, BlueIdentityProcessor blueIdentityProcessor) {
        this.memberBasicMapper = memberBasicMapper;
        this.blueIdentityProcessor = blueIdentityProcessor;
    }

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

    private static final UnaryOperator<MemberBasicCondition> MEMBER_CONDITION_PROCESSOR = condition -> {
        if (condition != null) {
            condition.setSortAttribute(
                    ofNullable(condition.getSortAttribute())
                            .filter(BlueChecker::isNotBlank)
                            .map(SORT_ATTRIBUTE_MAPPING::get)
                            .filter(BlueChecker::isNotBlank)
                            .orElseThrow(() -> new BlueException(INVALID_PARAM)));

            condition.setSortType(getSortTypeByIdentity(condition.getSortType()).identity);

            return condition;
        }

        return new MemberBasicCondition();
    };

    /**
     * query member by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<MemberBasic>> selectMemberBasicMonoByPrimaryKey(Long id) {
        LOGGER.info("Mono<Optional<MemberBasic>> getMemberBasicMonoByPrimaryKey(Long id), id = {}", id);
        if (isValidIdentity(id))
            return just(ofNullable(memberBasicMapper.selectByPrimaryKey(id)));
        throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * query member by phone
     *
     * @param phone
     * @return
     */
    @Override
    public Optional<MemberBasic> selectMemberBasicByPhone(String phone) {
        LOGGER.info("Optional<MemberBasic> getMemberBasicByPhone(String phone), phone = {}", phone);
        if (isNotBlank(phone))
            return ofNullable(memberBasicMapper.selectByPhone(phone));
        throw new BlueException(BAD_REQUEST);
    }

    /**
     * query member by email
     *
     * @param email
     * @return
     */
    @Override
    public Optional<MemberBasic> selectMemberBasicByEmail(String email) {
        LOGGER.info("Optional<MemberBasic> getMemberBasicByEmail(String email), email = {}", email);
        if (isNotBlank(email))
            return ofNullable(memberBasicMapper.selectByEmail(email));
        throw new BlueException(BAD_REQUEST);
    }

    /**
     * query member by phone
     *
     * @param phone
     * @return
     */
    @Override
    public Mono<Optional<MemberBasic>> selectMemberBasicMonoByPhone(String phone) {
        LOGGER.info("Mono<Optional<MemberBasic>> getMemberBasicMonoByPhone(String phone), phone = {}", phone);
        if (isNotBlank(phone))
            return just(ofNullable(memberBasicMapper.selectByPhone(phone)));
        throw new BlueException(BAD_REQUEST);
    }

    /**
     * query member by email
     *
     * @param email
     * @return
     */
    @Override
    public Mono<Optional<MemberBasic>> selectMemberBasicMonoByEmail(String email) {
        LOGGER.info("Mono<Optional<MemberBasic>> getMemberBasicMonoByEmail(String email), email = {}", email);
        if (isNotBlank(email))
            return just(ofNullable(memberBasicMapper.selectByEmail(email)));
        throw new BlueException(BAD_REQUEST);
    }

    /**
     * query member by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<MemberBasicInfo> selectMemberInfoMonoByPrimaryKeyWithAssert(Long id) {
        LOGGER.info("Mono<MemberInfo> getMemberInfoMonoByPrimaryKeyWithAssert(Long id), id = {}", id);
        if (isValidIdentity(id))
            //noinspection DuplicatedCode
            return just(id)
                    .flatMap(this::selectMemberBasicMonoByPrimaryKey)
                    .flatMap(mbOpt ->
                            mbOpt.map(Mono::just)
                                    .orElseGet(() ->
                                            error(() -> new BlueException(UNAUTHORIZED)))
                    ).flatMap(mb -> {
                        if (isInvalidStatus(mb.getStatus()))
                            return error(() -> new BlueException(ACCOUNT_HAS_BEEN_FROZEN));
                        LOGGER.info("mb = {}", mb);
                        return just(mb);
                    }).flatMap(mb ->
                            just(MEMBER_BASIC_2_MEMBER_BASIC_INFO.apply(mb))
                    );

        throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * insert member
     *
     * @param memberBasic
     * @return
     */
    @Override
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public MemberBasicInfo insertMemberBasic(MemberBasic memberBasic) {
        LOGGER.info("void insert(MemberBasic memberBasic), memberBasic = {}", memberBasic);
        if (isNull(memberBasic))
            throw new BlueException(EMPTY_PARAM);

        MEMBER_EXIST_VALIDATOR.accept(memberBasic);

        if (isInvalidIdentity(memberBasic.getId()))
            memberBasic.setId(blueIdentityProcessor.generate(MemberBasic.class));

        memberBasicMapper.insert(memberBasic);

        return MEMBER_BASIC_2_MEMBER_BASIC_INFO.apply(memberBasic);
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
        return isValidIdentities(ids) ? just(allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(memberBasicMapper::selectByIds)
                .flatMap(l -> l.stream().map(MEMBER_BASIC_2_MEMBER_BASIC_INFO))
                .collect(toList()))
                :
                just(emptyList());
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

        if (limit != null && limit >= 0 && rows != null && rows >= 1)
            return just(memberBasicMapper.selectByLimitAndCondition(limit, rows, memberBasicCondition));

        throw new BlueException(INVALID_PARAM);
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

        MemberBasicCondition memberBasicCondition = MEMBER_CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(selectMemberBasicMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), memberBasicCondition), countMemberBasicMonoByCondition(memberBasicCondition))
                .flatMap(tuple2 -> {
                    List<MemberBasic> members = tuple2.getT1();
                    Mono<List<MemberBasicInfo>> memberInfosMono = members.size() > 0 ?
                            just(members.stream()
                                    .map(MEMBER_BASIC_2_MEMBER_BASIC_INFO).collect(toList()))
                            :
                            just(emptyList());

                    return memberInfosMono
                            .flatMap(memberInfos ->
                                    just(new PageModelResponse<>(memberInfos, tuple2.getT2())));
                });
    }

}
