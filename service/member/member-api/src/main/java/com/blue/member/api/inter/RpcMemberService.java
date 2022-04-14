package com.blue.member.api.inter;

import com.blue.member.api.model.MemberBasicInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * rpc member interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcMemberService {

    /**
     * query member by id
     *
     * @param id
     * @return
     */
    CompletableFuture<MemberBasicInfo> selectMemberBasicInfoMonoByPrimaryKey(Long id);

    /**
     * select member basic by ids
     *
     * @param ids
     * @return
     */
    CompletableFuture<List<MemberBasicInfo>> selectMemberBasicInfoMonoByIds(List<Long> ids);

    /**
     * query member basic by phone
     *
     * @param phone
     * @return
     */
    CompletableFuture<MemberBasicInfo> selectMemberBasicInfoByPhone(String phone);

    /**
     * query member basic by email
     *
     * @param email
     * @return
     */
    CompletableFuture<MemberBasicInfo> selectMemberBasicInfoByEmail(String email);

}
