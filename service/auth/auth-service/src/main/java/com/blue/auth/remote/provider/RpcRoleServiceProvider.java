package com.blue.auth.remote.provider;

import com.blue.auth.converter.AuthModelConverters;
import com.blue.auth.repository.entity.MemberRoleRelation;
import com.blue.auth.repository.entity.Role;
import com.blue.auth.service.inter.MemberRoleRelationService;
import com.blue.auth.service.inter.RoleService;
import com.blue.base.model.exps.BlueException;
import com.blue.auth.api.inter.RpcRoleService;
import com.blue.auth.api.model.MemberRoleInfo;
import com.blue.auth.api.model.RoleInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.blue.base.constant.common.ResponseElement.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * rpc role provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcRoleService.class,
        version = "1.0",
        methods = {
                @Method(name = "selectRoleInfo", async = true),
                @Method(name = "selectRoleInfoByMemberId", async = true),
                @Method(name = "selectRoleInfoByMemberIds", async = true)
        })
public class RpcRoleServiceProvider implements RpcRoleService {

    private static final Logger LOGGER = getLogger(RpcRoleServiceProvider.class);

    private final RoleService roleService;

    private final MemberRoleRelationService memberRoleRelationService;

    private final Scheduler scheduler;

    public RpcRoleServiceProvider(RoleService roleService, MemberRoleRelationService memberRoleRelationService, Scheduler scheduler) {
        this.roleService = roleService;
        this.memberRoleRelationService = memberRoleRelationService;
        this.scheduler = scheduler;
    }

    /**
     * select all role info
     *
     * @return
     */
    @Override
    public CompletableFuture<List<RoleInfo>> selectRoleInfo() {
        LOGGER.info("CompletableFuture<RoleInfo> selectRoleInfo()");
        return just(true)
                .subscribeOn(scheduler)
                .flatMap(v -> roleService.selectRole())
                .flatMap(roles -> just(roles.stream().map(AuthModelConverters.ROLE_2_ROLE_INFO_CONVERTER).collect(toList())))
                .toFuture();
    }

    /**
     * get member's role info by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public CompletableFuture<MemberRoleInfo> selectRoleInfoByMemberId(Long memberId) {
        LOGGER.info("CompletableFuture<MemberRoleRelationInfo> selectRoleInfoByMemberId(Long memberId), memberId = {}", memberId);
        return just(memberId).subscribeOn(scheduler)
                .flatMap(memberRoleRelationService::getRoleIdMonoByMemberId)
                .flatMap(roleIdOpt ->
                        roleIdOpt.map(roleService::getRoleMono).orElseGet(() -> error(() -> new BlueException(INVALID_IDENTITY)))
                )
                .flatMap(roleOpt ->
                        roleOpt.map(role -> just(AuthModelConverters.ROLE_2_ROLE_INFO_CONVERTER.apply(role))).orElseGet(() -> error(() -> new BlueException(DATA_NOT_EXIST)))
                )
                .flatMap(roleInfo ->
                        just(new MemberRoleInfo(memberId, roleInfo)))
                .toFuture();
    }

    /**
     * get member's roles info by member ids
     *
     * @param memberIds
     * @return
     */
    @Override
    public CompletableFuture<List<MemberRoleInfo>> selectRoleInfoByMemberIds(List<Long> memberIds) {
        LOGGER.info("CompletableFuture<List<MemberRoleRelationInfo>> selectRoleInfoByMemberIds(List<Long> memberIds), memberIds = {}", memberIds);
        return just(memberIds).subscribeOn(scheduler)
                .flatMap(memberRoleRelationService::selectRelationMonoByMemberIds)
                .flatMap(relations ->
                        roleService.selectRoleMonoByIds(relations.stream().map(MemberRoleRelation::getRoleId).collect(toList()))
                                .flatMap(roles -> {
                                    Map<Long, Role> idAndRoleMapping = roles.stream().collect(toMap(Role::getId, r -> r, (a, b) -> a));
                                    return just(relations.stream()
                                            .map(rel -> {
                                                MemberRoleInfo memberRoleInfo = new MemberRoleInfo();
                                                memberRoleInfo.setMemberId(rel.getMemberId());

                                                Role role = idAndRoleMapping.get(rel.getRoleId());
                                                if (role != null) {
                                                    memberRoleInfo.setRoleInfo(new RoleInfo(role.getId(), role.getName(), role.getDescription(), role.getLevel(), role.getIsDefault()));
                                                    return memberRoleInfo;
                                                }

                                                LOGGER.error("the role with id {} can't be null", rel.getRoleId());
                                                throw new BlueException(INTERNAL_SERVER_ERROR);
                                            }).collect(toList()));
                                })).toFuture();
    }

}
