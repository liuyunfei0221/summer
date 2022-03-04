package com.blue.member.service.inter;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.member.api.model.MemberBusinessInfo;
import com.blue.member.model.MemberBusinessCondition;
import com.blue.member.repository.entity.MemberBusiness;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * member business service
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface MemberBusinessService {

    /**
     * query member business by id
     *
     * @param id
     * @return
     */
    Optional<MemberBusiness> selectMemberBusinessByPrimaryKey(Long id);

    /**
     * query member business mono by id
     *
     * @param id
     * @return
     */
    Mono<Optional<MemberBusiness>> selectMemberBusinessMonoByPrimaryKey(Long id);

    /**
     * query member business by member id
     *
     * @param memberId
     * @return
     */
    List<MemberBusiness> selectMemberBusinessByMemberId(Long memberId);

    /**
     * query member business mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<List<MemberBusiness>> selectMemberBusinessMonoByMemberId(Long memberId);

    /**
     * query member business by id with assert
     *
     * @param id
     * @return
     */
    Mono<MemberBusinessInfo> selectMemberBusinessInfoMonoByPrimaryKeyWithAssert(Long id);

    /**
     * insert member business
     *
     * @param memberBusiness
     * @return
     */
    MemberBusinessInfo insertMemberBusiness(MemberBusiness memberBusiness);

    /**
     * select member business by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberBusiness>> selectMemberBusinessMonoByIds(List<Long> ids);

    /**
     * select member business by page and condition
     *
     * @param limit
     * @param rows
     * @param memberBusinessCondition
     * @return
     */
    Mono<List<MemberBusiness>> selectMemberBusinessMonoByLimitAndCondition(Long limit, Long rows, MemberBusinessCondition memberBusinessCondition);

    /**
     * count member business by condition
     *
     * @param memberBusinessCondition
     * @return
     */
    Mono<Long> countMemberBusinessMonoByCondition(MemberBusinessCondition memberBusinessCondition);

    /**
     * select member business info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<MemberBusinessInfo>> selectMemberBusinessInfoPageMonoByPageAndCondition(PageModelRequest<MemberBusinessCondition> pageModelRequest);

}
