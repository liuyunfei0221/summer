package com.blue.auth.service.impl;

import com.blue.auth.repository.entity.MemberRoleRelation;
import com.blue.auth.repository.mapper.MemberRoleRelationMapper;
import com.blue.auth.service.inter.MemberRoleRelationService;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import io.seata.spring.annotation.GlobalLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.constant.common.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.common.ResponseElement.*;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
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
        LOGGER.info("insertMemberRoleRelation(MemberRoleRelation memberRoleRelation), memberRoleRelation = {}", memberRoleRelation);
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
        LOGGER.info("int insertMemberRoleRelation(Long memberId, Long roleId, Long operatorId), memberId = {}, roleId = {}, operatorId = {}", memberId, roleId, operatorId);
        if (isInvalidIdentity(memberId) || isInvalidIdentity(roleId) || isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);

        List<Long> existRoleIds = memberRoleRelationMapper.selectRoleIdsByMemberId(memberId);
        if (isNotNull(existRoleIds) && existRoleIds.stream().anyMatch(rid -> rid.equals(roleId)))
            throw new BlueException(MEMBER_ALREADY_HAS_A_ROLE);

        Long timeStamp = TIME_STAMP_GETTER.get();

        MemberRoleRelation memberRoleRelation = new MemberRoleRelation();

        memberRoleRelation.setId(blueIdentityProcessor.generate(MemberRoleRelation.class));
        memberRoleRelation.setMemberId(memberId);
        memberRoleRelation.setRoleId(roleId);
        memberRoleRelation.setCreateTime(timeStamp);
        memberRoleRelation.setUpdateTime(timeStamp);
        memberRoleRelation.setCreator(operatorId);
        memberRoleRelation.setUpdater(operatorId);

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
        LOGGER.info("int updateMemberRoleRelations(Long memberId, List<Long> roleIds, Long operatorId), memberId = {}, roleIds = {}, operatorId = {}", memberId, roleIds, operatorId);
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
                    memberRoleRelation.setUpdateTime(timeStamp);
                    memberRoleRelation.setCreator(operatorId);
                    memberRoleRelation.setUpdater(operatorId);

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
        LOGGER.info("int updateMemberRoleRelation(Long memberId, Long roleId, Long operatorId), memberId = {}, roleId = {}, operatorId = {}", memberId, roleId, operatorId);
        if (isInvalidIdentity(memberId) || isInvalidIdentity(roleId) || isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);

        Optional<MemberRoleRelation> optional = memberRoleRelationMapper.selectByMemberId(memberId).stream().filter(rel -> rel.getRoleId().equals(roleId)).findAny();
        if (optional.isEmpty())
            throw new BlueException(MEMBER_NOT_HAS_A_ROLE);

        return memberRoleRelationMapper.deleteByPrimaryKey(optional.get().getId());
    }

    /**
     * get role id by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public List<Long> selectRoleIdsByMemberId(Long memberId) {
        LOGGER.info("List<Long> selectRoleIdsByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return memberRoleRelationMapper.selectRoleIdsByMemberId(memberId);
    }

    /**
     * get role id mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<Long>> selectRoleIdsMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<List<Long>> selectRoleIdsMonoByMemberId(Long memberId), memberId = {}", memberId);

        return just(selectRoleIdsByMemberId(memberId));
    }

    /**
     * select member-role-relation by member ids
     *
     * @param memberIds
     * @return
     */
    @Override
    public Mono<List<MemberRoleRelation>> selectRelationMonoByMemberIds(List<Long> memberIds) {
        LOGGER.info("Mono<List<MemberRoleRelation>> selectRelationMonoByMemberIds(List<Long> memberIds), memberIds = {}", memberIds);

        return isValidIdentities(memberIds) ? just(allotByMax(memberIds, (int) DB_SELECT.value, false)
                .stream().map(memberRoleRelationMapper::selectByMemberIds)
                .flatMap(List::stream)
                .collect(toList()))
                :
                just(emptyList());
    }

    /**
     * count relation by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public long countRelationByMemberId(Long memberId) {
        LOGGER.info("long countRelationByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return memberRoleRelationMapper.countByMemberId(memberId);
    }

    /**
     * count relation by role id
     *
     * @param roleId
     * @return
     */
    @Override
    public long countRelationByRoleId(Long roleId) {
        LOGGER.info("long countRelationByRoleId(Long roleId), roleId = {}", roleId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(INVALID_IDENTITY);

        return memberRoleRelationMapper.countByRoleId(roleId);
    }

}
