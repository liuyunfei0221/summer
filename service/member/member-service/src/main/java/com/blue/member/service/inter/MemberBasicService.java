package com.blue.member.service.inter;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberInfo;
import com.blue.member.model.MemberBasicCondition;
import com.blue.member.repository.entity.MemberBasic;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * member basic service
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface MemberBasicService {

    /**
     * query member by id
     *
     * @param id
     * @return
     */
    Mono<Optional<MemberBasic>> selectMemberBasicMonoByPrimaryKey(Long id);

    /**
     * query member by phone
     *
     * @param phone
     * @return
     */
    Optional<MemberBasic> selectMemberBasicByPhone(String phone);

    /**
     * query member by email
     *
     * @param email
     * @return
     */
    Optional<MemberBasic> selectMemberBasicByEmail(String email);

    /**
     * query member mono by phone
     *
     * @param phone
     * @return
     */
    Mono<Optional<MemberBasic>> selectMemberBasicMonoByPhone(String phone);

    /**
     * query member mono by email
     *
     * @param email
     * @return
     */
    Mono<Optional<MemberBasic>> selectMemberBasicMonoByEmail(String email);

    /**
     * query member by id with assert
     *
     * @param id
     * @return
     */
    Mono<MemberInfo> selectMemberInfoMonoByPrimaryKeyWithAssert(Long id);

    /**
     * insert member
     *
     * @param memberBasic
     * @return
     */
    MemberInfo insertMemberBasic(MemberBasic memberBasic);

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
