package com.blue.secure.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.secure.config.deploy.BlockingDeploy;
import com.blue.secure.config.mq.producer.AuthorityInfosRefreshProducer;
import com.blue.secure.repository.entity.Role;
import com.blue.secure.repository.mapper.RoleMapper;
import com.blue.secure.service.inter.MemberRoleRelationService;
import com.blue.secure.service.inter.RoleService;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.util.Loggers.getLogger;

/**
 * role service impl
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

    private final AuthorityInfosRefreshProducer authorityInfosRefreshProducer;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RoleServiceImpl(BlueIdentityProcessor blueIdentityProcessor, RoleMapper roleMapper,
                           MemberRoleRelationService memberRoleRelationService, ExecutorService executorService,
                           BlockingDeploy blockingDeploy, AuthorityInfosRefreshProducer authorityInfosRefreshProducer) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.roleMapper = roleMapper;
        this.memberRoleRelationService = memberRoleRelationService;
        this.executorService = executorService;
        this.blockingDeploy = blockingDeploy;
        this.authorityInfosRefreshProducer = authorityInfosRefreshProducer;
    }

    private static long MAX_WAITING_FOR_REFRESH;

    private static Role defaultRole;

    private static volatile boolean defaultRoleRefreshing = false;

    private static final Supplier<String> DEFAULT_ROLE_REFRESHING_BLOCKER = () -> {
        if (defaultRoleRefreshing) {
            long start = currentTimeMillis();
            while (defaultRoleRefreshing) {
                if (currentTimeMillis() - start > MAX_WAITING_FOR_REFRESH)
                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "waiting default role refresh timeout");
                onSpinWait();
            }
        }
        return "refreshed";
    };

    @SuppressWarnings("UnusedReturnValue")
    private boolean generateDefaultRoleInfo() {
        try {
            CompletableFuture<List<Role>> roleListCf =
                    supplyAsync(roleMapper::listRoles, executorService);

            List<Role> roleList = roleListCf.join();
            List<Role> defaultRoles = roleList.stream().filter(Role::getIsDefault)
                    .collect(toList());

            if (isEmpty(defaultRoles) || defaultRoles.size() > 1) {
                LOGGER.error("default role not exist or more than 1");
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message);
            }

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

    private static final Supplier<Role> DEFAULT_ROLE_GETTER = () -> {
        DEFAULT_ROLE_REFRESHING_BLOCKER.get();
        return defaultRole;
    };

    @PostConstruct
    public void init() {
        MAX_WAITING_FOR_REFRESH = blockingDeploy.getBlockingMillis();
        generateDefaultRoleInfo();
    }

    /**
     * get role by role id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Role> getRoleById(Long id) {
        LOGGER.info("getRoleById(Long id), id = {}", id);
        if (id == null || id < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        Role role = roleMapper.selectByPrimaryKey(id);
        return ofNullable(role);
    }

    /**
     * get role id by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Optional<Long> getRoleIdByMemberId(Long memberId) {
        LOGGER.info("getRoleIdByMemberId(Long memberId), memberId = {}", memberId);
        if (memberId == null || memberId < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        LOGGER.info("memberId = {}", memberId);
        return memberRoleRelationService.getRoleIdByMemberId(memberId);
    }

    /**
     * get role by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Optional<Role> getRoleByMemberId(Long memberId) {
        LOGGER.info("getRoleByMemberId(Long memberId), memberId = {}", memberId);

        Optional<Long> roleIdOpt = this.getRoleIdByMemberId(memberId);
        if (roleIdOpt.isEmpty()) {
            LOGGER.error("the member that id is " + memberId + " not have a role");
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message);
        }

        return ofNullable(roleMapper.selectByPrimaryKey(roleIdOpt.get()));
    }

    /**
     * select all roles
     *
     * @return
     */
    @Override
    public List<Role> listRoles() {
        LOGGER.info("listRoles()");
        return roleMapper.listRoles();
    }

    /**
     * get default role
     *
     * @return
     */
    @Override
    public Role getDefaultRole() {
        LOGGER.info("getDefaultRole()");

        Role role = DEFAULT_ROLE_GETTER.get();

        LOGGER.info("role = {}", role);
        if (role == null) {
            LOGGER.error("Role getDefaultRole(), default role not exist");
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message);
        }

        return role;
    }


}
