package com.blue.secure.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.secure.config.deploy.BlockingDeploy;
import com.blue.secure.repository.entity.Role;
import com.blue.secure.repository.mapper.RoleMapper;
import com.blue.secure.service.inter.MemberRoleRelationService;
import com.blue.secure.service.inter.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
import static reactor.util.Loggers.getLogger;

/**
 * 角色业务实现
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = getLogger(RoleServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final RoleMapper roleMapper;

    private final MemberRoleRelationService memberRoleRelationService;

    private final ExecutorService executorService;

    private final BlockingDeploy blockingDeploy;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RoleServiceImpl(BlueIdentityProcessor blueIdentityProcessor, RoleMapper roleMapper,
                           MemberRoleRelationService memberRoleRelationService, ExecutorService executorService,
                           BlockingDeploy blockingDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.roleMapper = roleMapper;
        this.memberRoleRelationService = memberRoleRelationService;
        this.executorService = executorService;
        this.blockingDeploy = blockingDeploy;
    }

    /**
     * 刷新资源时最大等待时间
     */
    private static long MAX_WAITING_FOR_REFRESH;

    /**
     * 默认角色
     */
    private static Role defaultRole;

    /**
     * 默认角色是否刷新中的标记位
     */
    private static volatile boolean defaultRoleRefreshing = false;

    /**
     * 默认角色信息动态刷新时的阻塞器
     */
    private static final Supplier<String> DEFAULT_ROLE_REFRESHING_BLOCKER = () -> {
        if (defaultRoleRefreshing) {
            long start = currentTimeMillis();
            while (defaultRoleRefreshing) {
                if (currentTimeMillis() - start > MAX_WAITING_FOR_REFRESH)
                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "默认角色信息刷新超时");
                onSpinWait();
            }
        }
        return "refreshed";
    };

    /**
     * 初始化免认证资源信息与角色资源映射等
     */
    @SuppressWarnings("UnusedReturnValue")
    private boolean generateDefaultRoleInfo() {
        try {
            //查询所有角色信息
            CompletableFuture<List<Role>> roleListCf =
                    supplyAsync(roleMapper::listRoles, executorService);

            List<Role> roleList = roleListCf.join();
            List<Role> defaultRoles = roleList.stream().filter(Role::getIsDefault)
                    .collect(toList());

            if (CollectionUtils.isEmpty(defaultRoles) || defaultRoles.size() > 1)
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "默认角色信息不存在或多于1条");

            Role tempDefaultRole = defaultRoles.get(0);

            defaultRoleRefreshing = true;
            defaultRole = tempDefaultRole;
            defaultRoleRefreshing = false;

            LOGGER.info("generateDefaultRoleInfo() -> SUCCESS");
        } catch (Exception e) {
            LOGGER.error("generateDefaultRoleInfo() -> FAILED,error = " + e);
            return false;
        }
        return true;
    }

    /**
     * 默认角色获取器
     */
    private static final Supplier<Role> DEFAULT_ROLE_GETTER = () -> {
        DEFAULT_ROLE_REFRESHING_BLOCKER.get();
        return defaultRole;
    };

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        MAX_WAITING_FOR_REFRESH = blockingDeploy.getBlockingMillis();
        generateDefaultRoleInfo();
    }

    /**
     * 根据主键查询角色信息
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Role> getRoleById(Long id) {
        LOGGER.info("getRoleById(Long id), id = {}", id);
        if (id == null || id < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "id不能为空或小于1");

        Role role = roleMapper.selectByPrimaryKey(id);
        return ofNullable(role);
    }

    /**
     * 根据成员id获取角色id
     *
     * @param memberId
     * @return
     */
    @Override
    public Optional<Long> getRoleIdByMemberId(Long memberId) {
        LOGGER.info("getRoleIdByMemberId(Long memberId), memberId = {}", memberId);
        if (memberId == null || memberId < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "memberId不能为空或小于1");

        LOGGER.info("memberId = {}", memberId);
        return memberRoleRelationService.getRoleIdByMemberId(memberId);
    }

    /**
     * 根据成员id获取角色信息
     *
     * @param memberId
     * @return
     */
    @Override
    public Optional<Role> getRoleByMemberId(Long memberId) {
        LOGGER.info("getRoleByMemberId(Long memberId), memberId = {}", memberId);

        Optional<Long> roleIdOpt = this.getRoleIdByMemberId(memberId);
        if (roleIdOpt.isEmpty()) {
            LOGGER.error("memberId 为 " + memberId + " 对应的角色不存在");
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "角色不存在");
        }

        return ofNullable(roleMapper.selectByPrimaryKey(roleIdOpt.get()));
    }

    /**
     * 获取全部角色信息
     *
     * @return
     */
    @Override
    public List<Role> listRoles() {
        LOGGER.info("listRoles()");
        return roleMapper.listRoles();
    }

    /**
     * 查询默认角色
     *
     * @return
     */
    @Override
    public Role getDefaultRole() {
        LOGGER.info("getDefaultRole()");

        Role role = DEFAULT_ROLE_GETTER.get();

        LOGGER.info("role = {}", role);
        if (role == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "默认角色不存在");

        return role;
    }


}
