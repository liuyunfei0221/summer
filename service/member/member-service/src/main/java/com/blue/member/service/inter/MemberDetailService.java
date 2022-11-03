package com.blue.member.service.inter;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.common.StatusParam;
import com.blue.member.api.model.MemberDetailInfo;
import com.blue.member.model.MemberDetailCondition;
import com.blue.member.model.MemberDetailUpdateParam;
import com.blue.member.repository.entity.MemberDetail;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * member detail service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface MemberDetailService {

    /**
     * init member detail
     *
     * @param memberId
     * @return
     */
    MemberDetail initMemberDetail(Long memberId);

    /**
     * update member detail
     *
     * @param memberId
     * @param memberDetailUpdateParam
     * @return
     */
    MemberDetailInfo updateMemberDetail(Long memberId, MemberDetailUpdateParam memberDetailUpdateParam);

    /**
     * update member detail status
     *
     * @param memberId
     * @param statusParam
     * @return
     */
    MemberDetailInfo updateMemberDetailStatus(Long memberId, StatusParam statusParam);

    /**
     * get member detail by id
     *
     * @param id
     * @return
     */
    Mono<MemberDetail> getMemberDetail(Long id);

    /**
     * query member by id with assert
     *
     * @param id
     * @return
     */
    Mono<MemberDetailInfo> getMemberDetailInfoWithAssert(Long id);

    /**
     * get member detail info mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<MemberDetailInfo> getMemberDetailInfoByMemberId(Long memberId);

    /**
     * query member detail by member id with assert
     *
     * @param memberId
     * @return
     */
    Mono<MemberDetailInfo> getMemberDetailInfoByMemberIdWithAssert(Long memberId);

    /**
     * select members mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberDetail>> selectMemberDetailByIds(List<Long> ids);

    /**
     * select members by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberDetailInfo>> selectMemberDetailInfoByIds(List<Long> ids);

    /**
     * select details mono by member ids
     *
     * @param memberIds
     * @return
     */
    Mono<List<MemberDetail>> selectMemberDetailByMemberIds(List<Long> memberIds);

    /**
     * select details by member ids
     *
     * @param memberIds
     * @return
     */
    Mono<List<MemberDetailInfo>> selectMemberDetailInfoByMemberIds(List<Long> memberIds);

    /**
     * select member detail by page and condition
     *
     * @param limit
     * @param rows
     * @param memberDetailCondition
     * @return
     */
    Mono<List<MemberDetail>> selectMemberDetailByLimitAndCondition(Long limit, Long rows, MemberDetailCondition memberDetailCondition);

    /**
     * count member detail by condition
     *
     * @param memberDetailCondition
     * @return
     */
    Mono<Long> countMemberDetailByCondition(MemberDetailCondition memberDetailCondition);

    /**
     * select member detail info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<MemberDetailInfo>> selectMemberDetailInfoPageByPageAndCondition(PageModelRequest<MemberDetailCondition> pageModelRequest);

}
