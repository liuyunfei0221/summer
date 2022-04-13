package com.blue.member.service.impl;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.member.api.model.MemberRealNameInfo;
import com.blue.member.repository.entity.MemberRealName;
import com.blue.member.repository.mapper.MemberRealNameMapper;
import com.blue.member.service.inter.MemberRealNameService;
import org.springframework.stereotype.Service;
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
import static com.blue.member.converter.MemberModelConverters.MEMBER_REAL_NAME_2_MEMBER_REAL_NAME_INFO;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
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
public class MemberRealNameServiceImpl implements MemberRealNameService {

    private static final Logger LOGGER = getLogger(MemberRealNameServiceImpl.class);

    private MemberRealNameMapper memberRealNameMapper;

    private final BlueIdentityProcessor blueIdentityProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MemberRealNameServiceImpl(MemberRealNameMapper memberRealNameMapper, BlueIdentityProcessor blueIdentityProcessor) {
        this.memberRealNameMapper = memberRealNameMapper;
        this.blueIdentityProcessor = blueIdentityProcessor;
    }

    /**
     * is a number detail exist?
     */
    private final Consumer<MemberRealName> MEMBER_REAL_NAME_EXIST_VALIDATOR = mrn ->
            ofNullable(mrn.getMemberId())
                    .filter(BlueChecker::isValidIdentity)
                    .ifPresent(memberId -> {
                        if (isNotNull(memberRealNameMapper.selectByMemberId(memberId)))
                            throw new BlueException(DATA_ALREADY_EXIST);
                    });

    /**
     * query member real name by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<MemberRealName> selectMemberRealNameByPrimaryKey(Long id) {
        LOGGER.info("Optional<MemberRealName> selectMemberRealNameByPrimaryKey(Long id), id = {}", id);
        if (isValidIdentity(id))
            return ofNullable(memberRealNameMapper.selectByPrimaryKey(id));
        throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * query member real name mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<MemberRealName>> selectMemberRealNameMonoByPrimaryKey(Long id) {
        LOGGER.info("Mono<Optional<MemberRealName>> selectMemberRealNameMonoByPrimaryKey(Long id), id = {}", id);
        if (isValidIdentity(id))
            return just(ofNullable(memberRealNameMapper.selectByPrimaryKey(id)));
        throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * query member real name by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Optional<MemberRealName> selectMemberRealNameByMemberId(Long memberId) {
        LOGGER.info("Optional<MemberRealName> selectMemberRealNameByMemberId(Long memberId), memberId = {}", memberId);
        if (isValidIdentity(memberId))
            return ofNullable(memberRealNameMapper.selectByMemberId(memberId));
        throw new BlueException(BAD_REQUEST);
    }

    /**
     * query member real name mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<Optional<MemberRealName>> selectMemberRealNameMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<Optional<MemberRealName>> selectMemberRealNameMonoByMemberId(Long memberId), memberId = {}", memberId);
        if (isValidIdentity(memberId))
            return just(ofNullable(memberRealNameMapper.selectByMemberId(memberId)));
        throw new BlueException(BAD_REQUEST);
    }

    /**
     * query member real name by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<MemberRealNameInfo> selectMemberRealNameInfoMonoByPrimaryKeyWithAssert(Long id) {
        LOGGER.info("Mono<MemberRealNameInfo> selectMemberRealNameInfoMonoByPrimaryKeyWithAssert(Long id), id = {}", id);
        //noinspection DuplicatedCode
        if (isValidIdentity(id))
            return just(id)
                    .flatMap(this::selectMemberRealNameMonoByPrimaryKey)
                    .flatMap(mrnOpt ->
                            mrnOpt.map(Mono::just)
                                    .orElseGet(() ->
                                            error(() -> new BlueException(DATA_NOT_EXIST)))
                    ).flatMap(mrn -> {
                        if (isInvalidStatus(mrn.getStatus()))
                            return error(() -> new BlueException(DATA_NOT_EXIST));
                        LOGGER.info("mrn = {}", mrn);
                        return just(mrn);
                    }).flatMap(mrn ->
                            just(MEMBER_REAL_NAME_2_MEMBER_REAL_NAME_INFO.apply(mrn))
                    );

        throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * query member real name by id with assert
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<MemberRealNameInfo> selectMemberRealNameInfoMonoByMemberIdWithAssert(Long memberId) {
        LOGGER.info("Mono<MemberRealNameInfo> selectMemberRealNameInfoMonoByMemberIdWithAssert(Long memberId), memberId = {}", memberId);
        //noinspection DuplicatedCode
        if (isValidIdentity(memberId))
            return just(memberId)
                    .flatMap(this::selectMemberRealNameMonoByMemberId)
                    .flatMap(mrnOpt ->
                            mrnOpt.map(Mono::just)
                                    .orElseGet(() ->
                                            error(() -> new BlueException(DATA_NOT_EXIST)))
                    ).flatMap(mrn -> {
                        if (isInvalidStatus(mrn.getStatus()))
                            return error(() -> new BlueException(DATA_NOT_EXIST));
                        LOGGER.info("mrn = {}", mrn);
                        return just(mrn);
                    }).flatMap(mrn ->
                            just(MEMBER_REAL_NAME_2_MEMBER_REAL_NAME_INFO.apply(mrn))
                    );

        throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * insert member real name
     *
     * @param memberRealName
     * @return
     */
    @Override
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public MemberRealNameInfo insertMemberRealName(MemberRealName memberRealName) {
        LOGGER.info("MemberRealNameInfo insertMemberRealName(MemberRealName memberRealName), memberRealName = {}", memberRealName);
        if (isNull(memberRealName))
            throw new BlueException(EMPTY_PARAM);

        MEMBER_REAL_NAME_EXIST_VALIDATOR.accept(memberRealName);

        if (isInvalidIdentity(memberRealName.getId()))
            memberRealName.setId(blueIdentityProcessor.generate(MemberRealName.class));

        memberRealNameMapper.insert(memberRealName);

        return MEMBER_REAL_NAME_2_MEMBER_REAL_NAME_INFO.apply(memberRealName);
    }

    /**
     * select member real name by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<MemberRealNameInfo>> selectMemberRealNameInfoMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<MemberRealNameInfo>> selectMemberRealNameInfoMonoByIds(List<Long> ids), ids = {}", ids);
        return isValidIdentities(ids) ? just(allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(memberRealNameMapper::selectByIds)
                .flatMap(l -> l.stream().map(MEMBER_REAL_NAME_2_MEMBER_REAL_NAME_INFO))
                .collect(toList()))
                :
                just(emptyList());
    }

}
