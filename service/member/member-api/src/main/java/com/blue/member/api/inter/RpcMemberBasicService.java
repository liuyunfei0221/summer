package com.blue.member.api.inter;

import com.blue.member.api.model.MemberBasicInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * rpc member basic interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcMemberBasicService {

    /**
     * get member by id
     *
     * @param id
     * @return
     */
    CompletableFuture<MemberBasicInfo> getMemberBasicInfo(Long id);

    /**
     * select member basic by ids
     *
     * @param ids
     * @return
     */
    CompletableFuture<List<MemberBasicInfo>> selectMemberBasicInfoByIds(List<Long> ids);

    /**
     * get member basic by phone
     *
     * @param phone
     * @return
     */
    CompletableFuture<MemberBasicInfo> getMemberBasicInfoByPhone(String phone);

    /**
     * get member basic by email
     *
     * @param email
     * @return
     */
    CompletableFuture<MemberBasicInfo> getMemberBasicInfoByEmail(String email);

}
