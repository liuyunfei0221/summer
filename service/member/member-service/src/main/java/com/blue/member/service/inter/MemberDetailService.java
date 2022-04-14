package com.blue.member.service.inter;

import com.blue.member.api.model.MemberDetailInfo;
import com.blue.member.repository.entity.MemberDetail;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * member detail service
 *
 * @author liuyunfei
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
    Optional<MemberDetail> selectMemberDetailByMemberId(Long memberId);

    /**
     * query member detail mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<Optional<MemberDetail>> selectMemberDetailMonoByMemberId(Long memberId);

    /**
     * query member detail by id with assert
     *
     * @param id
     * @return
     */
    Mono<MemberDetailInfo> selectMemberDetailInfoMonoByPrimaryKeyWithAssert(Long id);

    /**
     * query member detail by member id with assert
     *
     * @param memberId
     * @return
     */
    Mono<MemberDetailInfo> selectMemberDetailInfoMonoByMemberIdWithAssert(Long memberId);

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
    Mono<List<MemberDetailInfo>> selectMemberDetailInfoMonoByIds(List<Long> ids);

}
