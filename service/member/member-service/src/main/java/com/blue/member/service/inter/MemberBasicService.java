package com.blue.member.service.inter;

import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
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
     * update member status
     *
     * @param id
     * @param status
     * @return
     */
    MemberBasicInfo updateMemberBasicStatus(Long id, Integer status);

    /**
     * get opt by id
     *
     * @param id
     * @return
     */
    Optional<MemberBasic> getMemberBasic(Long id);

    /**
     * get member by id
     *
     * @param id
     * @return
     */
    Mono<Optional<MemberBasic>> getMemberBasicMono(Long id);

    /**
     * query member by phone
     *
     * @param phone
     * @return
     */
    Optional<MemberBasic> getMemberBasicByPhone(String phone);

    /**
     * query member by email
     *
     * @param email
     * @return
     */
    Optional<MemberBasic> getMemberBasicByEmail(String email);

    /**
     * query member mono by phone
     *
     * @param phone
     * @return
     */
    Mono<Optional<MemberBasic>> getMemberBasicMonoByPhone(String phone);

    /**
     * query member mono by email
     *
     * @param email
     * @return
     */
    Mono<Optional<MemberBasic>> getMemberBasicMonoByEmail(String email);

    /**
     * query member by id with assert
     *
     * @param id
     * @return
     */
    Mono<MemberBasicInfo> getMemberBasicInfoMonoWithAssert(Long id);

    /**
     * select members by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberBasicInfo>> selectMemberBasicInfoMonoByIds(List<Long> ids);

    /**
     * select member by page and condition
     *
     * @param limit
     * @param rows
     * @param memberBasicCondition
     * @return
     */
    Mono<List<MemberBasic>> selectMemberBasicMonoByLimitAndCondition(Long limit, Long rows, MemberBasicCondition memberBasicCondition);

    /**
     * count member by condition
     *
     * @param memberBasicCondition
     * @return
     */
    Mono<Long> countMemberBasicMonoByCondition(MemberBasicCondition memberBasicCondition);

    /**
     * select member basic info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<MemberBasicInfo>> selectMemberBasicInfoPageMonoByPageAndCondition(PageModelRequest<MemberBasicCondition> pageModelRequest);

}
