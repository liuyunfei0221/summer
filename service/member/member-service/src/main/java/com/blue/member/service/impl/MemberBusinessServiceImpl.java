package com.blue.member.service.impl;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBusinessInfo;
import com.blue.member.repository.entity.MemberBusiness;
import com.blue.member.repository.mapper.MemberBusinessMapper;
import com.blue.member.service.inter.MemberBusinessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.common.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.common.ResponseElement.*;
import static com.blue.member.converter.MemberModelConverters.MEMBER_BUSINESS_2_MEMBER_BUSINESS_INFO;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * member business service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class MemberBusinessServiceImpl implements MemberBusinessService {

    private static final Logger LOGGER = getLogger(MemberBusinessServiceImpl.class);

    private MemberBusinessMapper memberBusinessMapper;

    private final BlueIdentityProcessor blueIdentityProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MemberBusinessServiceImpl(MemberBusinessMapper memberBusinessMapper, BlueIdentityProcessor blueIdentityProcessor) {
        this.memberBusinessMapper = memberBusinessMapper;
        this.blueIdentityProcessor = blueIdentityProcessor;
    }

    /**
     * is a number business exist?
     */
    private final Consumer<MemberBusiness> MEMBER_BUSINESS_EXIST_VALIDATOR = mb ->
            ofNullable(mb.getMemberId())
                    .filter(BlueChecker::isValidIdentity)
                    .ifPresent(memberId -> {
                        if (isNotNull(memberBusinessMapper.selectByMemberId(memberId)))
                            throw new BlueException(DATA_ALREADY_EXIST);
                    });

    /**
     * query member business by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<MemberBusiness> getMemberBusiness(Long id) {
        LOGGER.info("Optional<MemberBusiness> selectMemberBusinessByPrimaryKey(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(memberBusinessMapper.selectByPrimaryKey(id));
    }

    /**
     * query member business mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<MemberBusiness>> getMemberBusinessMono(Long id) {
        LOGGER.info("Mono<Optional<MemberBusiness>> selectMemberBusinessMonoByPrimaryKey(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return just(ofNullable(memberBusinessMapper.selectByPrimaryKey(id)));
    }

    /**
     * query member business by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Optional<MemberBusiness> getMemberBusinessByMemberId(Long memberId) {
        LOGGER.info("Optional<MemberBusiness> selectMemberBusinessByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        return ofNullable(memberBusinessMapper.selectByMemberId(memberId));
    }

    /**
     * query member business mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<Optional<MemberBusiness>> getMemberBusinessMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<Optional<MemberBusiness>> selectMemberAddressMonoByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        return just(ofNullable(memberBusinessMapper.selectByMemberId(memberId)));
    }

    /**
     * query member business by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<MemberBusinessInfo> getMemberBusinessInfoMonoWithAssert(Long id) {
        LOGGER.info("Mono<MemberBusinessInfo> selectMemberBusinessInfoMonoByPrimaryKeyWithAssert(Long id), id = {}", id);
        //noinspection DuplicatedCode
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return just(id)
                .flatMap(this::getMemberBusinessMono)
                .flatMap(mbOpt ->
                        mbOpt.map(Mono::just)
                                .orElseGet(() ->
                                        error(() -> new BlueException(DATA_NOT_EXIST)))
                ).flatMap(mb -> {
                    if (isInvalidStatus(mb.getStatus()))
                        return error(() -> new BlueException(DATA_NOT_EXIST));
                    LOGGER.info("mb = {}", mb);
                    return just(mb);
                }).flatMap(mb ->
                        just(MEMBER_BUSINESS_2_MEMBER_BUSINESS_INFO.apply(mb))
                );
    }

    /**
     * query member business by member id with assert
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<MemberBusinessInfo> getMemberBusinessInfoMonoByMemberIdWithAssert(Long memberId) {
        LOGGER.info("Mono<MemberBusinessInfo> selectMemberBusinessInfoMonoByMemberIdWithAssert(Long memberId), memberId = {}", memberId);
        //noinspection DuplicatedCode
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return just(memberId)
                .flatMap(this::getMemberBusinessMonoByMemberId)
                .flatMap(mbOpt ->
                        mbOpt.map(Mono::just)
                                .orElseGet(() ->
                                        error(() -> new BlueException(DATA_NOT_EXIST)))
                ).flatMap(mb -> {
                    if (isInvalidStatus(mb.getStatus()))
                        return error(() -> new BlueException(DATA_NOT_EXIST));
                    LOGGER.info("mb = {}", mb);
                    return just(mb);
                }).flatMap(mb ->
                        just(MEMBER_BUSINESS_2_MEMBER_BUSINESS_INFO.apply(mb))
                );
    }

    /**
     * insert member business
     *
     * @param memberBusiness
     * @return
     */
    @Override
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public MemberBusinessInfo insertMemberBusiness(MemberBusiness memberBusiness) {
        LOGGER.info("MemberBusinessInfo insertMemberBusiness(MemberBusiness memberBusiness), memberBusiness = {}", memberBusiness);
        if (isNull(memberBusiness))
            throw new BlueException(EMPTY_PARAM);

        MEMBER_BUSINESS_EXIST_VALIDATOR.accept(memberBusiness);

        if (isInvalidIdentity(memberBusiness.getId()))
            memberBusiness.setId(blueIdentityProcessor.generate(MemberBusiness.class));

        memberBusinessMapper.insert(memberBusiness);

        return MEMBER_BUSINESS_2_MEMBER_BUSINESS_INFO.apply(memberBusiness);
    }

    /**
     * select business by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<MemberBusinessInfo>> selectMemberBusinessInfoMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<MemberBusiness>> selectMemberBusinessInfoMonoByIds(List<Long> ids), ids = {}", ids);
        return isValidIdentities(ids) ? just(allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(memberBusinessMapper::selectByIds)
                .flatMap(l -> l.stream().map(MEMBER_BUSINESS_2_MEMBER_BUSINESS_INFO))
                .collect(toList()))
                :
                just(emptyList());
    }

}