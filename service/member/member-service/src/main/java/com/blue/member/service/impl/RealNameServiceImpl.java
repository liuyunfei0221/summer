package com.blue.member.service.impl;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberRealNameInfo;
import com.blue.member.repository.entity.RealName;
import com.blue.member.repository.mapper.RealNameMapper;
import com.blue.member.service.inter.RealNameService;
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
import static com.blue.base.constant.common.BlueNumericalValue.MAX_SERVICE_SELECT;
import static com.blue.base.constant.common.ResponseElement.*;
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
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class RealNameServiceImpl implements RealNameService {

    private static final Logger LOGGER = getLogger(RealNameServiceImpl.class);

    private RealNameMapper realNameMapper;

    private final BlueIdentityProcessor blueIdentityProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RealNameServiceImpl(RealNameMapper realNameMapper, BlueIdentityProcessor blueIdentityProcessor) {
        this.realNameMapper = realNameMapper;
        this.blueIdentityProcessor = blueIdentityProcessor;
    }

    /**
     * is a number detail exist?
     */
    private final Consumer<RealName> MEMBER_REAL_NAME_EXIST_VALIDATOR = mrn ->
            ofNullable(mrn.getMemberId())
                    .filter(BlueChecker::isValidIdentity)
                    .ifPresent(memberId -> {
                        if (isNotNull(realNameMapper.selectByMemberId(memberId)))
                            throw new BlueException(DATA_ALREADY_EXIST);
                    });

    /**
     * query member real name by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<RealName> getMemberRealName(Long id) {
        LOGGER.info("Optional<MemberRealName> selectMemberRealNameByPrimaryKey(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(realNameMapper.selectByPrimaryKey(id));
    }

    /**
     * query member real name mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<RealName>> getMemberRealNameMono(Long id) {
        LOGGER.info("Mono<Optional<MemberRealName>> selectMemberRealNameMonoByPrimaryKey(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return just(ofNullable(realNameMapper.selectByPrimaryKey(id)));
    }

    /**
     * query member real name by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Optional<RealName> getMemberRealNameByMemberId(Long memberId) {
        LOGGER.info("Optional<MemberRealName> selectMemberRealNameByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        return ofNullable(realNameMapper.selectByMemberId(memberId));
    }

    /**
     * query member real name mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<Optional<RealName>> getMemberRealNameMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<Optional<MemberRealName>> selectMemberRealNameMonoByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        return just(ofNullable(realNameMapper.selectByMemberId(memberId)));
    }

    /**
     * query member real name by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<MemberRealNameInfo> getMemberRealNameInfoMonoWithAssert(Long id) {
        LOGGER.info("Mono<MemberRealNameInfo> selectMemberRealNameInfoMonoByPrimaryKeyWithAssert(Long id), id = {}", id);
        //noinspection DuplicatedCode
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return just(id)
                .flatMap(this::getMemberRealNameMono)
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
    }

    /**
     * query member real name by id with assert
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<MemberRealNameInfo> getMemberRealNameInfoMonoByMemberIdWithAssert(Long memberId) {
        LOGGER.info("Mono<MemberRealNameInfo> selectMemberRealNameInfoMonoByMemberIdWithAssert(Long memberId), memberId = {}", memberId);
        //noinspection DuplicatedCode
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return just(memberId)
                .flatMap(this::getMemberRealNameMonoByMemberId)
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
    }

    /**
     * insert member real name
     *
     * @param realName
     * @return
     */
    @Override
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 60)
    public MemberRealNameInfo insertMemberRealName(RealName realName) {
        LOGGER.info("MemberRealNameInfo insertMemberRealName(MemberRealName memberRealName), memberRealName = {}", realName);
        if (isNull(realName))
            throw new BlueException(EMPTY_PARAM);

        MEMBER_REAL_NAME_EXIST_VALIDATOR.accept(realName);

        if (isInvalidIdentity(realName.getId()))
            realName.setId(blueIdentityProcessor.generate(RealName.class));

        realNameMapper.insert(realName);

        return MEMBER_REAL_NAME_2_MEMBER_REAL_NAME_INFO.apply(realName);
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
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            return error(() -> new BlueException(PAYLOAD_TOO_LARGE));

        return just(allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(realNameMapper::selectByIds)
                .flatMap(l -> l.stream().map(MEMBER_REAL_NAME_2_MEMBER_REAL_NAME_INFO))
                .collect(toList()));
    }

}
