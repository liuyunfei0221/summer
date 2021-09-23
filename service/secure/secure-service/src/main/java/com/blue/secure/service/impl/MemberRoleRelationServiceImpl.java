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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.Logger;

import java.util.Optional;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.SyncKey.MEMBER_ROLE_REL_UPDATE_PRE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * 用户角色关联业务实现
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

    /**
     * 公告列表同步刷新缓存key
     */
    private static final String MEMBER_ROLE_REL_UPDATE_PRE_SYNC_KEY = MEMBER_ROLE_REL_UPDATE_PRE.key;

    /**
     * 根据成员id获取用户角色id
     *
     * @param memberId
     * @return
     */
    @Override
    public Optional<Long> getRoleIdByMemberId(Long memberId) {
        LOGGER.info("getRoleIdByMemberId(Long memberId), memberId = {}", memberId);
        if (memberId == null || memberId < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "成员主键不能为空或小于1");

        Long roleId = memberRoleRelationMapper.getRoleIdByMemberId(memberId);
        LOGGER.info("memberId = {},roleId = {}", memberId, roleId);

        return ofNullable(roleId);
    }

    /**
     * 更新成员对应的角色id
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 15)
    public void updateMemberRoleRelation(Long memberId, Long roleId, Long operatorId) {
        LOGGER.info("updateMemberRoleRelation(Long memberId, Long roleId, Long operatorId), memberId = {}, roleId = {}, operatorId = {}", memberId, roleId, operatorId);
        if (memberId == null || memberId < 1L || roleId == null || roleId < 1L || operatorId == null || operatorId < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "成员id或角色id或操作人id不能为空或小于1");

        MemberRoleRelation memberRoleRelation = memberRoleRelationMapper.getMemberRoleRelationByMemberId(memberId);
        if (memberRoleRelation == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "成员角色不存在");

        memberRoleRelation.setRoleId(roleId);
        memberRoleRelation.setUpdateTime(CommonFunctions.TIME_STAMP_GETTER.get());
        memberRoleRelation.setUpdater(operatorId);

        memberRoleRelationMapper.updateByPrimaryKeySelective(memberRoleRelation);
    }

    /**
     * 添加成员角色关联
     *
     * @param memberRoleRelation
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 15)
    @GlobalLock
    public void insertMemberRoleRelation(MemberRoleRelation memberRoleRelation) {
        LOGGER.info("insertMemberRoleRelation(MemberRoleRelation memberRoleRelation), memberRoleRelation = {}", memberRoleRelation);

        Long memberId = memberRoleRelation.getMemberId();
        if (memberId == null || memberId < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "memberId不能为空或小于1");

        memberRoleRelation.setId(blueIdentityProcessor.generate(MemberRoleRelation.class));
        String syncKey = MEMBER_ROLE_REL_UPDATE_PRE_SYNC_KEY + memberId;

        RLock lock = redissonClient.getLock(syncKey);
        lock.lock();

        try {
            MemberRoleRelation existRelation = memberRoleRelationMapper.getMemberRoleRelationByMemberId(memberId);
            if (existRelation != null)
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "该成员已拥有角色");

            memberRoleRelationMapper.insertSelective(memberRoleRelation);
//            if (1 == 1) {
//                throw new BlueException(500, 500, "测试异常回滚");
//            }
            LOGGER.info("添加角色成功,memberRoleRelation = {}", memberRoleRelation);
        } catch (Exception e) {
            LOGGER.error("添加角色失败,memberRoleRelation = {},e = {}", memberRoleRelation, e);
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
