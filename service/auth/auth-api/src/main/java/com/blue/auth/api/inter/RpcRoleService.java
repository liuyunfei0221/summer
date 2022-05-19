package com.blue.auth.api.inter;

import com.blue.auth.api.model.MemberRoleRelationInfo;
import com.blue.auth.api.model.RoleInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * rpc role interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcRoleService {

    /**
     * select all role infos
     *
     * @return
     */
    CompletableFuture<List<RoleInfo>> selectRoleInfo();

    /**
     * get member's role info by member id
     *
     * @param memberId
     * @return
     */
    CompletableFuture<MemberRoleRelationInfo> selectRoleInfoByMemberId(Long memberId);

    /**
     * get member's roles info by member ids
     *
     * @param memberIds
     * @return
     */
    CompletableFuture<List<MemberRoleRelationInfo>> selectRoleInfoByMemberIds(List<Long> memberIds);

}
