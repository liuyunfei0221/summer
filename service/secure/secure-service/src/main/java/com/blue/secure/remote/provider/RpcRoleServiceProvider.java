package com.blue.secure.remote.provider;

import com.blue.secure.api.inter.RpcRoleService;
import com.blue.secure.api.model.MemberRoleRelationInfo;
import com.blue.secure.api.model.RoleInfo;
import com.blue.secure.repository.entity.MemberRoleRelation;
import com.blue.secure.repository.entity.Role;
import com.blue.secure.service.inter.MemberRoleRelationService;
import com.blue.secure.service.inter.RoleService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.blue.base.constant.base.CommonException.*;
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
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcRoleService.class, version = "1.0", methods = {
        @Method(name = "selectRoleInfoByMemberId", async = true),
        @Method(name = "selectRoleInfoByMemberIds", async = true)
})
public class RpcRoleServiceProvider implements RpcRoleService {

    private static final Logger LOGGER = getLogger(RpcRoleServiceProvider.class);

    private final RoleService roleService;

    private final MemberRoleRelationService memberRoleRelationService;

    public RpcRoleServiceProvider(RoleService roleService, MemberRoleRelationService memberRoleRelationService) {
        this.roleService = roleService;
        this.memberRoleRelationService = memberRoleRelationService;
    }

    /**
     * get member's role info by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public CompletableFuture<RoleInfo> selectRoleInfoByMemberId(Long memberId) {
        LOGGER.info("CompletableFuture<RoleInfo> selectRoleInfoByMemberId(Long memberId), memberId = {}", memberId);
        return memberRoleRelationService.getRoleIdMonoByMemberId(memberId)
                .flatMap(roleIdOpt ->
                        roleIdOpt.map(roleService::getRoleMonoById).orElseGet(() -> error(INVALID_IDENTITY_EXP.exp))
                )
                .flatMap(roleOpt ->
                        roleOpt.map(role -> just(ROLE_2_ROLE_INFO_CONVERTER.apply(role))).orElseGet(() -> error(DATA_NOT_EXIST_EXP.exp))
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
        LOGGER.info("CompletableFuture<List<MemberRoleRelationInfo>> selectRoleInfoByMemberIds(List<Long> memberIds), memberIds = {}", memberIds);
        return memberRoleRelationService.selectRelationMonoByMemberIds(memberIds)
                .flatMap(relations ->
                        roleService.selectRoleMonoByIds(relations.stream().map(MemberRoleRelation::getRoleId).collect(toList()))
                                .flatMap(roles -> {
                                    Map<Long, Role> idAndRoleMapping = roles.stream().collect(toMap(Role::getId, r -> r, (a, b) -> a));
                                    return just(relations.stream()
                                            .map(rel -> {
                                                MemberRoleRelationInfo memberRoleRelationInfo = new MemberRoleRelationInfo();
                                                memberRoleRelationInfo.setMemberId(rel.getMemberId());

                                                Role role = idAndRoleMapping.get(rel.getRoleId());
                                                if (role != null) {
                                                    memberRoleRelationInfo.setRoleInfo(new RoleInfo(role.getId(), role.getName(), role.getDescription(), role.getIsDefault()));
                                                    return memberRoleRelationInfo;
                                                }

                                                LOGGER.error("the role with id {} can't be null", rel.getRoleId());
                                                throw INTERNAL_SERVER_ERROR_EXP.exp;
                                            }).collect(toList()));
                                })).toFuture();
    }

}
