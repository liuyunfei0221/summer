package com.blue.member.service.inter;

import com.blue.basic.model.common.IdentityParam;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.common.StringDataParam;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.model.MemberBasicCondition;
import com.blue.member.repository.entity.MemberBasic;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * member basic service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface MemberBasicService {

    /**
     * insert member
     *
     * @param memberBasic
     * @return
     */
    MemberBasicInfo insertMemberBasic(MemberBasic memberBasic);

    /**
     * update member
     *
     * @param memberBasic
     * @return
     */
    MemberBasicInfo updateMemberBasic(MemberBasic memberBasic);

    /**
     * update member's icon
     *
     * @param id
     * @param identityParam
     * @return
     */
    Mono<MemberBasicInfo> updateMemberBasicIcon(Long id, IdentityParam identityParam);

    /**
     * update member's qrCode
     *
     * @param id
     * @param identityParam
     * @return
     */
    Mono<MemberBasicInfo> updateMemberBasicQrCode(Long id, IdentityParam identityParam);

    /**
     * update member's profile
     *
     * @param id
     * @param stringDataParam
     * @return
     */
    Mono<MemberBasicInfo> updateMemberBasicProfile(Long id, StringDataParam stringDataParam);

    /**
     * update member status
     *
     * @param id
     * @param status
     * @return
     */
    MemberBasicInfo updateMemberBasicStatus(Long id, Integer status);

    /**
     * get by id
     *
     * @param id
     * @return
     */
    Optional<MemberBasic> getMemberBasicOpt(Long id);

    /**
     * get member mono by id
     *
     * @param id
     * @return
     */
    Mono<MemberBasic> getMemberBasic(Long id);

    /**
     * query member by phone
     *
     * @param phone
     * @return
     */
    Optional<MemberBasic> getMemberBasicOptByPhone(String phone);

    /**
     * query member by email
     *
     * @param email
     * @return
     */
    Optional<MemberBasic> getMemberBasicOptByEmail(String email);

    /**
     * query member mono by phone
     *
     * @param phone
     * @return
     */
    Mono<MemberBasic> getMemberBasicByPhone(String phone);

    /**
     * query member mono by email
     *
     * @param email
     * @return
     */
    Mono<MemberBasic> getMemberBasicByEmail(String email);

    /**
     * query member by id with assert
     *
     * @param id
     * @return
     */
    Mono<MemberBasicInfo> getMemberBasicInfoWithAssert(Long id);

    /**
     * select members mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberBasic>> selectMemberBasicMonoByIds(List<Long> ids);

    /**
     * select members by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberBasicInfo>> selectMemberBasicInfoByIds(List<Long> ids);

    /**
     * select member by page and condition
     *
     * @param limit
     * @param rows
     * @param memberBasicCondition
     * @return
     */
    Mono<List<MemberBasic>> selectMemberBasicByLimitAndCondition(Long limit, Long rows, MemberBasicCondition memberBasicCondition);

    /**
     * count member by condition
     *
     * @param memberBasicCondition
     * @return
     */
    Mono<Long> countMemberBasicByCondition(MemberBasicCondition memberBasicCondition);

    /**
     * select member basic info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<MemberBasicInfo>> selectMemberBasicInfoPageByPageAndCondition(PageModelRequest<MemberBasicCondition> pageModelRequest);

}
