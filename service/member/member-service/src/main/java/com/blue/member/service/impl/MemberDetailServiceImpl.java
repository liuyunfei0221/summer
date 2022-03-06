package com.blue.member.service.impl;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.member.api.model.MemberDetailInfo;
import com.blue.member.repository.entity.MemberDetail;
import com.blue.member.repository.mapper.MemberDetailMapper;
import com.blue.member.service.inter.MemberDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.member.converter.MemberModelConverters.MEMBER_DETAIL_2_MEMBER_DETAIL_INFO;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * member detail service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class MemberDetailServiceImpl implements MemberDetailService {

    private static final Logger LOGGER = getLogger(MemberDetailServiceImpl.class);

    private MemberDetailMapper memberDetailMapper;

    private final BlueIdentityProcessor blueIdentityProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MemberDetailServiceImpl(MemberDetailMapper memberDetailMapper, BlueIdentityProcessor blueIdentityProcessor) {
        this.memberDetailMapper = memberDetailMapper;
        this.blueIdentityProcessor = blueIdentityProcessor;
    }

    /**
     * is a number detail exist?
     */
    private final Consumer<MemberDetail> MEMBER_DETAIL_EXIST_VALIDATOR = md ->
            ofNullable(md.getMemberId())
                    .filter(BlueChecker::isValidIdentity)
                    .ifPresent(memberId -> {
                        if (isNotNull(memberDetailMapper.selectByMemberId(memberId)))
                            throw new BlueException(DATA_ALREADY_EXIST);
                    });

    /**
     * query member detail by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<MemberDetail> selectMemberDetailByPrimaryKey(Long id) {
        LOGGER.info("Optional<MemberDetail> selectMemberDetailByPrimaryKey(Long id), id = {}", id);
        if (isValidIdentity(id))
            return ofNullable(memberDetailMapper.selectByPrimaryKey(id));
        throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * query member detail mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<MemberDetail>> selectMemberDetailMonoByPrimaryKey(Long id) {
        LOGGER.info("Mono<Optional<MemberDetail>> selectMemberDetailMonoByPrimaryKey(Long id), id = {}", id);
        if (isValidIdentity(id))
            return just(ofNullable(memberDetailMapper.selectByPrimaryKey(id)));
        throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * query member detail by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Optional<MemberDetail> selectMemberDetailByMemberId(Long memberId) {
        LOGGER.info("Optional<MemberDetail>l selectMemberDetailByMemberId(Long memberId), memberId = {}", memberId);
        if (isValidIdentity(memberId))
            return ofNullable(memberDetailMapper.selectByMemberId(memberId));
        throw new BlueException(BAD_REQUEST);
    }

    /**
     * query member detail mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<Optional<MemberDetail>> selectMemberDetailMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<Optional<MemberBusiness>> selectMemberAddressMonoByMemberId(Long memberId), memberId = {}", memberId);
        if (isValidIdentity(memberId))
            return just(ofNullable(memberDetailMapper.selectByMemberId(memberId)));
        throw new BlueException(BAD_REQUEST);
    }

    /**
     * query member detail by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<MemberDetailInfo> selectMemberDetailInfoMonoByPrimaryKeyWithAssert(Long id) {
        LOGGER.info("Mono<MemberDetailInfo> selectMemberDetailInfoMonoByPrimaryKeyWithAssert(Long id), id = {}", id);
        //noinspection DuplicatedCode
        if (isValidIdentity(id))
            return just(id)
                    .flatMap(this::selectMemberDetailMonoByPrimaryKey)
                    .flatMap(mdOpt ->
                            mdOpt.map(Mono::just)
                                    .orElseGet(() ->
                                            error(() -> new BlueException(DATA_NOT_EXIST)))
                    ).flatMap(md -> {
                        if (isInvalidStatus(md.getStatus()))
                            return error(() -> new BlueException(DATA_NOT_EXIST));
                        LOGGER.info("md = {}", md);
                        return just(md);
                    }).flatMap(mb ->
                            just(MEMBER_DETAIL_2_MEMBER_DETAIL_INFO.apply(mb))
                    );

        throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * query member detail by member id with assert
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<MemberDetailInfo> selectMemberDetailInfoMonoByMemberIdWithAssert(Long memberId) {
        LOGGER.info("Mono<MemberDetailInfo> selectMemberDetailInfoMonoByMemberIdWithAssert(Long memberId), memberId = {}", memberId);
        //noinspection DuplicatedCode
        if (isValidIdentity(memberId))
            return just(memberId)
                    .flatMap(this::selectMemberDetailMonoByMemberId)
                    .flatMap(mdOpt ->
                            mdOpt.map(Mono::just)
                                    .orElseGet(() ->
                                            error(() -> new BlueException(DATA_NOT_EXIST)))
                    ).flatMap(md -> {
                        if (isInvalidStatus(md.getStatus()))
                            return error(() -> new BlueException(DATA_NOT_EXIST));
                        LOGGER.info("md = {}", md);
                        return just(md);
                    }).flatMap(md ->
                            just(MEMBER_DETAIL_2_MEMBER_DETAIL_INFO.apply(md))
                    );

        throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * insert member detail
     *
     * @param memberDetail
     * @return
     */
    @Override
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 30)
    public MemberDetailInfo insertMemberDetail(MemberDetail memberDetail) {
        LOGGER.info("MemberDetailInfo insertMemberDetail(MemberDetail memberDetail), memberDetail = {}", memberDetail);
        if (isNull(memberDetail))
            throw new BlueException(EMPTY_PARAM);

        MEMBER_DETAIL_EXIST_VALIDATOR.accept(memberDetail);

        if (isInvalidIdentity(memberDetail.getId()))
            memberDetail.setId(blueIdentityProcessor.generate(MemberDetail.class));

        memberDetailMapper.insert(memberDetail);

        return MEMBER_DETAIL_2_MEMBER_DETAIL_INFO.apply(memberDetail);
    }

    /**
     * select member detail by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<MemberDetailInfo>> selectMemberDetailInfoMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<MemberDetailInfo>> selectMemberDetailInfoMonoByIds(List<Long> ids), ids = {}", ids);
        return isValidIdentities(ids) ? just(allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(memberDetailMapper::selectByIds)
                .flatMap(l -> l.stream().map(MEMBER_DETAIL_2_MEMBER_DETAIL_INFO))
                .collect(toList()))
                :
                just(emptyList());
    }

}
