package com.blue.auth.service.impl;

import com.blue.auth.repository.entity.MemberRoleRelation;
import com.blue.auth.repository.mapper.MemberRoleRelationMapper;
import com.blue.auth.service.inter.MemberRoleRelationService;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import io.seata.spring.annotation.GlobalLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.SyncKeyPrefix.MEMBER_ROLE_REL_UPDATE_PRE;
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
 * @author liuyunfei
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

    private static final Function<Long, String> SYNC_KEY_WRAPPER = key -> MEMBER_ROLE_REL_UPDATE_PRE.prefix + key;

    /**
     * get role id by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Optional<Long> getRoleIdByMemberId(Long memberId) {
        LOGGER.info("Optional<Long> getRoleIdByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(memberRoleRelationMapper.getRoleIdByMemberId(memberId));
    }

    /**
     * get role id mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<Optional<Long>> getRoleIdMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<Optional<Long>> getRoleIdMonoByMemberId(Long memberId), memberId = {}", memberId);

        return just(getRoleIdByMemberId(memberId));
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

    /**
     * insert member role relation
     *
     * @param memberRoleRelation
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    @GlobalLock
    public int insertMemberRoleRelation(MemberRoleRelation memberRoleRelation) {
        LOGGER.info("insertMemberRoleRelation(MemberRoleRelation memberRoleRelation), memberRoleRelation = {}", memberRoleRelation);
        if (isNull(memberRoleRelation))
            throw new BlueException(DATA_NOT_EXIST);

        Long memberId = memberRoleRelation.getMemberId();
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        memberRoleRelation.setId(blueIdentityProcessor.generate(MemberRoleRelation.class));

        RLock lock = redissonClient.getLock(SYNC_KEY_WRAPPER.apply(memberId));
        lock.lock();

        try {
            MemberRoleRelation existRelation = memberRoleRelationMapper.getByMemberId(memberId);
            if (isNotNull(existRelation))
                throw new BlueException(MEMBER_ALREADY_HAS_A_ROLE.status);

            return memberRoleRelationMapper.insertSelective(memberRoleRelation);
        } catch (Exception e) {
            LOGGER.error("void insertMemberRoleRelation(MemberRoleRelation memberRoleRelation) failed, memberRoleRelation = {}, e = {}", memberRoleRelation, e);
            throw e;
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                LOGGER.error("insertMemberRoleRelation, lock.unlock() failed, e = {}", e);
            }
        }
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
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    @GlobalLock
    public int insertMemberRoleRelation(Long memberId, Long roleId, Long operatorId) {
        LOGGER.info("int insertMemberRoleRelation(Long memberId, Long roleId, Long operatorId), memberId = {}, roleId = {}, operatorId = {}", memberId, roleId, operatorId);
        if (isInvalidIdentity(memberId) || isInvalidIdentity(roleId) || isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);

        long epochSecond = TIME_STAMP_GETTER.get();

        MemberRoleRelation memberRoleRelation = new MemberRoleRelation();

        memberRoleRelation.setMemberId(memberId);
        memberRoleRelation.setRoleId(roleId);
        memberRoleRelation.setCreateTime(epochSecond);
        memberRoleRelation.setUpdateTime(epochSecond);
        memberRoleRelation.setCreator(operatorId);
        memberRoleRelation.setUpdater(operatorId);

        memberRoleRelation.setId(blueIdentityProcessor.generate(MemberRoleRelation.class));

        RLock lock = redissonClient.getLock(SYNC_KEY_WRAPPER.apply(memberId));
        lock.lock();

        try {
            MemberRoleRelation existRelation = memberRoleRelationMapper.getByMemberId(memberId);
            if (isNotNull(existRelation))
                throw new BlueException(MEMBER_ALREADY_HAS_A_ROLE.status);

            return memberRoleRelationMapper.insertSelective(memberRoleRelation);
        } catch (Exception e) {
            LOGGER.error("int insertMemberRoleRelation(Long memberId, Long roleId, Long operatorId), memberId = {}, roleId = {}, operatorId = {}, e = {}", memberId, roleId, operatorId, e);
            throw e;
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                LOGGER.warn("lock.unlock() fail, e = {0}", e);
            }
        }
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
    public int updateMemberRoleRelation(Long memberId, Long roleId, Long operatorId) {
        LOGGER.info("int updateMemberRoleRelation(Long memberId, Long roleId, Long operatorId), memberId = {}, roleId = {}, operatorId = {}", memberId, roleId, operatorId);
        if (isInvalidIdentity(memberId) || isInvalidIdentity(roleId) || isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);

        RLock lock = redissonClient.getLock(SYNC_KEY_WRAPPER.apply(memberId));
        lock.lock();

        try {
            MemberRoleRelation memberRoleRelation = memberRoleRelationMapper.getByMemberId(memberId);
            if (isNull(memberRoleRelation))
                throw new BlueException(MEMBER_NOT_HAS_A_ROLE.status);

            memberRoleRelation.setRoleId(roleId);
            memberRoleRelation.setUpdateTime(TIME_STAMP_GETTER.get());
            memberRoleRelation.setUpdater(operatorId);

            return memberRoleRelationMapper.updateByPrimaryKeySelective(memberRoleRelation);
        } catch (Exception e) {
            LOGGER.error("void updateMemberRoleRelation(Long memberId, Long roleId, Long operatorId) failed, memberId = {}, roleId = {}, operatorId = {}, e = {]", memberId, roleId, operatorId);
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
