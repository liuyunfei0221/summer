package com.blue.auth.remote.provider;

import com.blue.auth.api.inter.RpcRoleService;
import com.blue.auth.api.model.MemberRoleInfo;
import com.blue.auth.api.model.RoleInfo;
import com.blue.auth.repository.entity.MemberRoleRelation;
import com.blue.auth.service.inter.MemberRoleRelationService;
import com.blue.auth.service.inter.RoleService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.blue.auth.converter.AuthModelConverters.ROLE_2_ROLE_INFO_CONVERTER;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;

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

    private final RoleService roleService;

    private final MemberRoleRelationService memberRoleRelationService;

    public RpcRoleServiceProvider(RoleService roleService, MemberRoleRelationService memberRoleRelationService) {
        this.roleService = roleService;
        this.memberRoleRelationService = memberRoleRelationService;
    }

    /**
     * select all role info
     *
     * @return
     */
    @Override
    public CompletableFuture<List<RoleInfo>> selectRoleInfo() {
        return roleService.selectRole()
                .flatMap(roles -> just(roles.stream().map(ROLE_2_ROLE_INFO_CONVERTER).collect(toList())))
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
        return memberRoleRelationService.selectRoleIdsByMemberId(memberId)
                .flatMap(roleService::selectRoleByIds)
                .flatMap(roles -> just(roles.stream().map(ROLE_2_ROLE_INFO_CONVERTER).collect(toList())))
                .flatMap(roleInfos -> just(new MemberRoleInfo(memberId, roleInfos)))
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
        return memberRoleRelationService.selectRelationByMemberIds(memberIds)
                .flatMap(relations ->
                        zip(roleService.selectRoleByIds(relations.stream().map(MemberRoleRelation::getRoleId).collect(toList()))
                                        .flatMap(roles -> just(roles.stream().map(ROLE_2_ROLE_INFO_CONVERTER).collect(toMap(RoleInfo::getId, identity(), (a, b) -> a)))),
                                just(relations.stream().collect(groupingBy(MemberRoleRelation::getMemberId))))
                                .flatMap(tuple2 -> {
                                    Map<Long, RoleInfo> roleInfoMapping = tuple2.getT1();
                                    return just(tuple2.getT2().entrySet()
                                            .stream().map(entry -> new MemberRoleInfo(entry.getKey(),
                                                    entry.getValue().stream().map(rel -> roleInfoMapping.get(rel.getRoleId())).collect(toList())
                                            )).collect(toList()));
                                })
                ).toFuture();
    }

}
