package com.blue.secure.remote.provider;

import com.blue.base.model.base.Access;
import com.blue.base.model.exps.BlueException;
import com.blue.secure.api.inter.RpcRoleService;
import com.blue.secure.api.model.MemberRoleRelationInfo;
import com.blue.secure.api.model.RoleInfo;
import com.blue.secure.repository.entity.MemberRoleRelation;
import com.blue.secure.repository.entity.Role;
import com.blue.secure.service.inter.MemberRoleRelationService;
import com.blue.secure.service.inter.RoleService;
import com.blue.secure.service.inter.SecureService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.secure.converter.SecureModelConverters.ROLE_2_ROLE_INFO_CONVERTER;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;


/**
 * rpc role provider
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "DefaultAnnotationParam", "AliControlFlowStatementWithoutBraces"})
@DubboService(interfaceClass = RpcRoleService.class, version = "1.0", methods = {
        @Method(name = "insertDefaultMemberRoleRelation", async = false),
        @Method(name = "updateMemberRoleByAccess", async = false),
        @Method(name = "updateMemberRoleById", async = false),
        @Method(name = "getRoleInfoByMemberId", async = true),
        @Method(name = "selectRoleInfoByMemberIds", async = true)
})
public class RpcRoleServiceProvider implements RpcRoleService {

    private static final Logger LOGGER = getLogger(RpcRoleServiceProvider.class);

    private final SecureService secureService;

    private final RoleService roleService;

    private final MemberRoleRelationService memberRoleRelationService;

    public RpcRoleServiceProvider(SecureService secureService, RoleService roleService, MemberRoleRelationService memberRoleRelationService) {
        this.secureService = secureService;
        this.roleService = roleService;
        this.memberRoleRelationService = memberRoleRelationService;
    }

    /**
     * assign default roles to member
     *
     * @param memberId
     */
    @Override
    public void insertDefaultMemberRoleRelation(Long memberId) {
        LOGGER.info("void insertDefaultMemberRoleRelation(Long memberId), memberId = {}", memberId);
        secureService.insertDefaultMemberRoleRelation(memberId);
    }

    /**
     * update member's auth by access
     *
     * @param access
     * @param roleId
     * @return
     */
    @Override
    public void updateMemberRoleByAccess(Access access, Long roleId) {
        LOGGER.info("void updateMemberRoleByAccess(Access access, Long roleId), access = {}, roleId = {}", access, roleId);
        secureService.updateMemberRoleByAccess(access, roleId);
    }

    /**
     * update member's auth by member id
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    @Override
    public void updateMemberRoleById(Long memberId, Long roleId, Long operatorId) {
        LOGGER.info("void updateMemberRoleById(Long memberId, Long roleId, Long operatorId), memberId = {}, roleId = {}, operatorId = {}", memberId, roleId, operatorId);
        secureService.updateMemberRoleById(memberId, roleId, operatorId);
    }

    /**
     * get member's role info by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public CompletableFuture<RoleInfo> getRoleInfoByMemberId(Long memberId) {
        LOGGER.info("RoleInfo getRoleInfoByMemberId(Long memberId), memberId = {}", memberId);
        return just(memberRoleRelationService.getRoleIdByMemberId(memberId))
                .flatMap(roleIdOpt ->
                        roleIdOpt.map(aLong -> just(roleService.getRoleById(aLong))).orElseGet(() -> error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "roleId can't be null")))
                )
                .flatMap(roleOpt ->
                        roleOpt.map(role -> just(ROLE_2_ROLE_INFO_CONVERTER.apply(role))).orElseGet(() -> error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "role can't be null")))
                ).toFuture();
    }

    /**
     * get member's roles info by member ids
     *
     * @param memberIds
     * @return
     */
    @Override
    public CompletableFuture<List<MemberRoleRelationInfo>> selectRoleInfoByMemberIds(List<Long> memberIds) {
        LOGGER.info("List<RoleInfo> selectRoleInfoByMemberIds(List<Long> memberIds), memberIds = {}", memberIds);

        return just(memberRoleRelationService.selectRelationByMemberIds(memberIds))
                .flatMap(relations -> {

                    Map<Long, Role> idAndRoleMapping = roleService.selectRoleByIds(relations.stream().map(MemberRoleRelation::getRoleId).collect(toList()))
                            .stream().collect(toMap(Role::getId, r -> r, (a, b) -> a));

                    return just(relations.stream()
                            .map(rel -> {
                                MemberRoleRelationInfo memberRoleRelationInfo = new MemberRoleRelationInfo();
                                memberRoleRelationInfo.setMemberId(rel.getMemberId());

                                Role role = idAndRoleMapping.get(rel.getRoleId());
                                if (role == null)
                                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, " the role with id " + rel.getRoleId() + " can't be null");

                                memberRoleRelationInfo.setRoleInfo(new RoleInfo(role.getId(), role.getName(), role.getDescription(), role.getIsDefault()));
                                return memberRoleRelationInfo;
                            }).collect(toList()));
                }).toFuture();
    }
}
