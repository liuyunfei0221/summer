package com.blue.auth.service.impl;

import com.blue.auth.repository.entity.MemberRoleRelation;
import com.blue.auth.repository.mapper.MemberRoleRelationMapper;
import com.blue.auth.service.inter.MemberRoleRelationService;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import io.seata.spring.annotation.GlobalLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_SELECT;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_SERVICE_SELECT;
import static com.blue.basic.constant.common.ResponseElement.*;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * member role relation service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "Duplicates", "AliControlFlowStatementWithoutBraces"})
@Service
public class MemberRoleRelationServiceImpl implements MemberRoleRelationService {

    private static final Logger LOGGER = getLogger(MemberRoleRelationServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final MemberRoleRelationMapper memberRoleRelationMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MemberRoleRelationServiceImpl(BlueIdentityProcessor blueIdentityProcessor, MemberRoleRelationMapper memberRoleRelationMapper) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.memberRoleRelationMapper = memberRoleRelationMapper;
    }

    /**
     * insert member role relation
     *
     * @param memberRoleRelation
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    @GlobalLock
    public int insertMemberRoleRelation(MemberRoleRelation memberRoleRelation) {
        LOGGER.info("memberRoleRelation = {}", memberRoleRelation);
        if (isNull(memberRoleRelation))
            throw new BlueException(DATA_NOT_EXIST);

        Long memberId = memberRoleRelation.getMemberId();
        Long roleId = memberRoleRelation.getRoleId();
        if (isInvalidIdentity(memberId) || isInvalidIdentity(roleId))
            throw new BlueException(INVALID_IDENTITY);

        List<Long> existRoleIds = memberRoleRelationMapper.selectRoleIdsByMemberId(memberId);
        if (isNotNull(existRoleIds) && existRoleIds.stream().anyMatch(rid -> rid.equals(roleId)))
            throw new BlueException(MEMBER_ALREADY_HAS_A_ROLE);

        memberRoleRelation.setId(blueIdentityProcessor.generate(MemberRoleRelation.class));

        return memberRoleRelationMapper.insertSelective(memberRoleRelation);
    }

    /**
     * insert member role relation
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    @GlobalLock
    public int insertMemberRoleRelation(Long memberId, Long roleId, Long operatorId) {
        LOGGER.info("memberId = {}, roleId = {}, operatorId = {}", memberId, roleId, operatorId);
        if (isInvalidIdentity(memberId) || isInvalidIdentity(roleId) || isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);

        Optional<MemberRoleRelation> existRelationOpt = ofNullable(memberRoleRelationMapper.selectByMemberIdAndRoleId(memberId, roleId));
        if (existRelationOpt.isPresent())
            throw new BlueException(MEMBER_ALREADY_HAS_A_ROLE);

        MemberRoleRelation memberRoleRelation = new MemberRoleRelation();

        memberRoleRelation.setId(blueIdentityProcessor.generate(MemberRoleRelation.class));
        memberRoleRelation.setMemberId(memberId);
        memberRoleRelation.setRoleId(roleId);
        memberRoleRelation.setCreateTime(TIME_STAMP_GETTER.get());
        memberRoleRelation.setCreator(operatorId);

        return memberRoleRelationMapper.insertSelective(memberRoleRelation);
    }

    /**
     * update member role relation
     *
     * @param memberId
     * @param roleIds
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public int updateMemberRoleRelations(Long memberId, List<Long> roleIds, Long operatorId) {
        LOGGER.info("memberId = {}, roleIds = {}, operatorId = {}", memberId, roleIds, operatorId);
        if (isInvalidIdentity(memberId) || isInvalidIdentities(roleIds) || isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);

        List<Long> distinctRoleIds = roleIds.stream().distinct().collect(toList());

        Long timeStamp = TIME_STAMP_GETTER.get();
        List<MemberRoleRelation> memberRoleRelations = distinctRoleIds.stream()
                .map(roleId -> {
                    MemberRoleRelation memberRoleRelation = new MemberRoleRelation();

                    memberRoleRelation.setId(blueIdentityProcessor.generate(MemberRoleRelation.class));
                    memberRoleRelation.setMemberId(memberId);
                    memberRoleRelation.setRoleId(roleId);
                    memberRoleRelation.setCreateTime(timeStamp);
                    memberRoleRelation.setCreator(operatorId);

                    return memberRoleRelation;
                }).collect(toList());

        int deleted = memberRoleRelationMapper.deleteByMemberId(memberId);
        int inserted = memberRoleRelationMapper.insertBatch(memberRoleRelations);

        LOGGER.info("deleted = {}, inserted = {}", deleted, inserted);

        return inserted;
    }

    /**
     * update member role relation
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public int deleteMemberRoleRelation(Long memberId, Long roleId, Long operatorId) {
        LOGGER.info("memberId = {}, roleId = {}, operatorId = {}", memberId, roleId, operatorId);
        if (isInvalidIdentity(memberId) || isInvalidIdentity(roleId) || isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);

        Optional<MemberRoleRelation> existRelationOpt = ofNullable(memberRoleRelationMapper.selectByMemberIdAndRoleId(memberId, roleId));
        if (existRelationOpt.isEmpty())
            throw new BlueException(MEMBER_NOT_HAS_A_ROLE);

        return memberRoleRelationMapper.deleteByPrimaryKey(existRelationOpt.get().getId());
    }

    /**
     * get role id mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<Long>> selectRoleIdsByMemberId(Long memberId) {
        LOGGER.info("memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return just(memberRoleRelationMapper.selectRoleIdsByMemberId(memberId));
    }

    /**
     * select member-role-relation by member ids
     *
     * @param memberIds
     * @return
     */
    @Override
    public Mono<List<MemberRoleRelation>> selectRelationByMemberIds(List<Long> memberIds) {
        LOGGER.info("memberIds = {}", memberIds);
        if (isEmpty(memberIds))
            return just(emptyList());
        if (memberIds.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return fromIterable(allotByMax(memberIds, (int) DB_SELECT.value, false))
                .map(memberRoleRelationMapper::selectByMemberIds)
                .reduceWith(LinkedList::new, (a, b) -> {
                    a.addAll(b);
                    return a;
                });
    }

}
