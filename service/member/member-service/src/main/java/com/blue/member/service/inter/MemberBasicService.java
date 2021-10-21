package com.blue.member.service.inter;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.member.api.model.MemberInfo;
import com.blue.member.api.model.MemberRegistryParam;
import com.blue.member.model.MemberCondition;
import com.blue.member.repository.entity.MemberBasic;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * member basic service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface MemberBasicService {

    /**
     * query member by phone
     *
     * @param phone
     * @return
     */
    Mono<Optional<MemberBasic>> getMemberBasicMonoByPhone(String phone);

    /**
     * query member by email
     *
     * @param email
     * @return
     */
    Mono<Optional<MemberBasic>> getMemberBasicMonoByEmail(String email);

    /**
     * query member by id
     *
     * @param id
     * @return
     */
    Mono<Optional<MemberBasic>> getMemberBasicMonoByPrimaryKey(Long id);

    /**
     * query member by id with assert
     *
     * @param id
     * @return
     */
    Mono<MemberInfo> getMemberInfoMonoByPrimaryKeyWithAssert(Long id);

    /**
     * member registry
     *
     * @param memberRegistryParam
     * @return
     */
    void insert(MemberRegistryParam memberRegistryParam);

    /**
     * select members by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberBasic>> selectMemberBasicMonoByIds(List<Long> ids);

    /**
     * select member by page and condition
     *
     * @param limit
     * @param rows
     * @param memberCondition
     * @return
     */
    Mono<List<MemberBasic>> selectMemberBasicMonoByLimitAndCondition(Long limit, Long rows, MemberCondition memberCondition);

    /**
     * count member by condition
     *
     * @param memberCondition
     * @return
     */
    Mono<Long> countMemberBasicMonoByCondition(MemberCondition memberCondition);

    /**
     * select member info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<MemberInfo>> selectMemberInfoPageMonoByPageAndCondition(PageModelRequest<MemberCondition> pageModelRequest);

}
