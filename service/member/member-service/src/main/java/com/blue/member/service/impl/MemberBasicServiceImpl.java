package com.blue.member.service.impl;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.common.InvalidAuthEvent;
import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.constant.MemberBasicSortAttribute;
import com.blue.member.event.producer.InvalidAuthProducer;
import com.blue.member.model.MemberBasicCondition;
import com.blue.member.repository.entity.MemberBasic;
import com.blue.member.repository.mapper.MemberBasicMapper;
import com.blue.member.service.inter.MemberBasicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
import static com.blue.base.common.base.ConditionSortProcessor.process;
import static com.blue.base.common.base.ConstantProcessor.assertStatus;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Status.VALID;
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
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class MemberBasicServiceImpl implements MemberBasicService {

    private static final Logger LOGGER = getLogger(MemberBasicServiceImpl.class);

    private final InvalidAuthProducer invalidAuthProducer;

    private MemberBasicMapper memberBasicMapper;

    private final BlueIdentityProcessor blueIdentityProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MemberBasicServiceImpl(InvalidAuthProducer invalidAuthProducer, MemberBasicMapper memberBasicMapper, BlueIdentityProcessor blueIdentityProcessor) {
        this.invalidAuthProducer = invalidAuthProducer;
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
        if (isInvalidIdentity(memberBasic.getId()))
            throw new BlueException(INVALID_IDENTITY);

        memberBasicMapper.updateByPrimaryKey(memberBasic);

        return MEMBER_BASIC_2_MEMBER_BASIC_INFO.apply(memberBasic);
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
        memberBasicMapper.updateStatus(id, status);

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
    public Optional<MemberBasic> getMemberBasic(Long id) {
        LOGGER.info("Optional<MemberBasic> getMemberBasicByPrimaryKey(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(memberBasicMapper.selectByPrimaryKey(id));
    }

    /**
     * query member by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<MemberBasic>> getMemberBasicMono(Long id) {
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

        //noinspection DuplicatedCode
        return just(id)
                .flatMap(this::getMemberBasicMono)
                .flatMap(mbOpt ->
                        mbOpt.map(Mono::just)
                                .orElseGet(() ->
                                        error(() -> new BlueException(DATA_NOT_EXIST)))
                ).flatMap(mb -> {
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
