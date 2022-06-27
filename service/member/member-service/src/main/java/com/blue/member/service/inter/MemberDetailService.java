package com.blue.member.service.inter;

import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.member.api.model.MemberDetailInfo;
import com.blue.member.model.MemberDetailCondition;
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
    MemberDetailInfo initMemberDetail(Long memberId);

    /**
     * update member detail
     *
     * @param memberDetail
     * @return
     */
    MemberDetailInfo updateMemberDetail(MemberDetail memberDetail);

    /**
     * update member detail status
     *
     * @param id
     * @param status
     * @return
     */
    MemberDetailInfo updateMemberDetailStatus(Long id, Integer status);

    /**
     * get by id
     *
     * @param id
     * @return
     */
    MemberDetail getMemberDetail(Long id);

    /**
     * get member detail by id
     *
     * @param id
     * @return
     */
    Mono<MemberDetail> getMemberDetailMono(Long id);

    /**
     * query member by id with assert
     *
     * @param id
     * @return
     */
    Mono<MemberDetailInfo> getMemberDetailInfoMonoWithAssert(Long id);

    /**
     * get by member id
     *
     * @param memberId
     * @return
     */
    MemberDetail getMemberDetailByMemberId(Long memberId);

    /**
     * get member detail mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<MemberDetail> getMemberDetailMonoByMemberId(Long memberId);

    /**
     * query member detail by member id with assert
     *
     * @param memberId
     * @return
     */
    Mono<MemberDetailInfo> getMemberDetailInfoMonoByMemberIdWithAssert(Long memberId);

    /**
     * select members by ids
     *
     * @param ids
     * @return
     */
    List<MemberDetail> selectMemberDetailByIds(List<Long> ids);

    /**
     * select members mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberDetail>> selectMemberDetailMonoByIds(List<Long> ids);

    /**
     * select members by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberDetailInfo>> selectMemberDetailInfoMonoByIds(List<Long> ids);

    /**
     * select details by member ids
     *
     * @param memberIds
     * @return
     */
    List<MemberDetail> selectMemberDetailByMemberIds(List<Long> memberIds);

    /**
     * select details mono by member ids
     *
     * @param memberIds
     * @return
     */
    Mono<List<MemberDetail>> selectMemberDetailMonoByMemberIds(List<Long> memberIds);

    /**
     * select details by member ids
     *
     * @param memberIds
     * @return
     */
    Mono<List<MemberDetailInfo>> selectMemberDetailInfoMonoByMemberIds(List<Long> memberIds);

    /**
     * select member detail by page and condition
     *
     * @param limit
     * @param rows
     * @param memberDetailCondition
     * @return
     */
    Mono<List<MemberDetail>> selectMemberDetailMonoByLimitAndCondition(Long limit, Long rows, MemberDetailCondition memberDetailCondition);

    /**
     * count member detail by condition
     *
     * @param memberDetailCondition
     * @return
     */
    Mono<Long> countMemberDetailMonoByCondition(MemberDetailCondition memberDetailCondition);

    /**
     * select member detail info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<MemberDetailInfo>> selectMemberDetailInfoPageMonoByPageAndCondition(PageModelRequest<MemberDetailCondition> pageModelRequest);

}
