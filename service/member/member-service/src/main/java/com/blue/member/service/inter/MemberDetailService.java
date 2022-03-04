package com.blue.member.service.inter;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.member.api.model.MemberDetailInfo;
import com.blue.member.model.MemberDetailCondition;
import com.blue.member.repository.entity.MemberDetail;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * member detail service
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface MemberDetailService {

    /**
     * query member detail by id
     *
     * @param id
     * @return
     */
    Optional<MemberDetail> selectMemberDetailByPrimaryKey(Long id);

    /**
     * query member detail mono by id
     *
     * @param id
     * @return
     */
    Mono<Optional<MemberDetail>> selectMemberDetailMonoByPrimaryKey(Long id);

    /**
     * query member detail by member id
     *
     * @param memberId
     * @return
     */
    List<MemberDetail> selectMemberDetailByMemberId(Long memberId);

    /**
     * query member detail mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<List<MemberDetail>> selectMemberDetailMonoByMemberId(Long memberId);

    /**
     * query member detail by id with assert
     *
     * @param id
     * @return
     */
    Mono<MemberDetailInfo> selectMemberDetailInfoMonoByPrimaryKeyWithAssert(Long id);

    /**
     * insert member detail
     *
     * @param memberDetail
     * @return
     */
    MemberDetailInfo insertMemberDetail(MemberDetail memberDetail);

    /**
     * select member detail by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberDetail>> selectMemberDetailMonoByIds(List<Long> ids);

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
