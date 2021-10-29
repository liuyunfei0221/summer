package com.blue.secure.service.impl;

import com.blue.base.common.base.CommonFunctions;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.secure.repository.entity.MemberRoleRelation;
import com.blue.secure.repository.mapper.MemberRoleRelationMapper;
import com.blue.secure.service.inter.MemberRoleRelationService;
import io.seata.spring.annotation.GlobalLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.Asserter.*;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.*;
import static com.blue.base.constant.base.SyncKey.MEMBER_ROLE_REL_UPDATE_PRE;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * member role relation service
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "Duplicates", "AliControlFlowStatementWithoutBraces"})
@Service
public class MemberRoleRelationServiceImpl implements MemberRoleRelationService {

    private static final Logger LOGGER = getLogger(MemberRoleRelationServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final MemberRoleRelationMapper memberRoleRelationMapper;

    private final RedissonClient redissonClient;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MemberRoleRelationServiceImpl(BlueIdentityProcessor blueIdentityProcessor, MemberRoleRelationMapper memberRoleRelationMapper, RedissonClient redissonClient) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.memberRoleRelationMapper = memberRoleRelationMapper;
        this.redissonClient = redissonClient;
    }

    private static final String MEMBER_ROLE_REL_UPDATE_PRE_SYNC_KEY = MEMBER_ROLE_REL_UPDATE_PRE.key;

    /**
     * get role id by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<Optional<Long>> getRoleIdMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<Optional<Long>> getRoleIdMonoByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        Long roleId = memberRoleRelationMapper.getRoleIdByMemberId(memberId);
        LOGGER.info("Mono<Optional<Long>> getRoleIdMonoByMemberId(Long memberId), memberId = {}, roleId = {}", memberId, roleId);

        return just(ofNullable(roleId));
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
     * update member role relation
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public void updateMemberRoleRelation(Long memberId, Long roleId, Long operatorId) {
        LOGGER.info("void updateMemberRoleRelation(Long memberId, Long roleId, Long operatorId), memberId = {}, roleId = {}, operatorId = {}", memberId, roleId, operatorId);
        if (isInvalidIdentity(memberId) || isInvalidIdentity(roleId) || isInvalidIdentity(operatorId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        MemberRoleRelation memberRoleRelation = memberRoleRelationMapper.getByMemberId(memberId);
        if (isNull(memberRoleRelation))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, MEMBER_NOT_HAS_A_ROLE.message);

        memberRoleRelation.setRoleId(roleId);
        memberRoleRelation.setUpdateTime(CommonFunctions.TIME_STAMP_GETTER.get());
        memberRoleRelation.setUpdater(operatorId);

        memberRoleRelationMapper.updateByPrimaryKeySelective(memberRoleRelation);
    }

    /**
     * insert member role relation
     *
     * @param memberRoleRelation
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    @GlobalLock
    public void insertMemberRoleRelation(MemberRoleRelation memberRoleRelation) {
        LOGGER.info("insertMemberRoleRelation(MemberRoleRelation memberRoleRelation), memberRoleRelation = {}", memberRoleRelation);
        if (isNull(memberRoleRelation))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "memberRoleRelation can't be null");

        Long memberId = memberRoleRelation.getMemberId();
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        memberRoleRelation.setId(blueIdentityProcessor.generate(MemberRoleRelation.class));
        String syncKey = MEMBER_ROLE_REL_UPDATE_PRE_SYNC_KEY + memberId;

        RLock lock = redissonClient.getLock(syncKey);
        lock.lock();

        try {
            MemberRoleRelation existRelation = memberRoleRelationMapper.getByMemberId(memberId);
            if (isNotNull(existRelation))
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, MEMBER_ALREADY_HAS_A_ROLE.message);

            memberRoleRelationMapper.insertSelective(memberRoleRelation);
//            if (1 == 1) {
//                throw new BlueException(500, 500, "test rollback on exception");
//            }
            LOGGER.info("insertMemberRoleRelation(MemberRoleRelation memberRoleRelation) success, memberRoleRelation = {}", memberRoleRelation);
        } catch (Exception e) {
            LOGGER.error("insertMemberRoleRelation(MemberRoleRelation memberRoleRelation) failed, memberRoleRelation = {},e = {}", memberRoleRelation, e);
            throw e;
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                LOGGER.error("lock.unlock() fail, e = {0}", e);
            }
        }
    }


}
