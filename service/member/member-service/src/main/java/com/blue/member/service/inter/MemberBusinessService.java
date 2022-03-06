package com.blue.member.service.inter;

import com.blue.member.api.model.MemberBusinessInfo;
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
    Optional<MemberBusiness> selectMemberBusinessByMemberId(Long memberId);

    /**
     * query member business mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<Optional<MemberBusiness>> selectMemberBusinessMonoByMemberId(Long memberId);

    /**
     * query member business by id with assert
     *
     * @param id
     * @return
     */
    Mono<MemberBusinessInfo> selectMemberBusinessInfoMonoByPrimaryKeyWithAssert(Long id);

    /**
     * query member business by member id with assert
     *
     * @param memberId
     * @return
     */
    Mono<MemberBusinessInfo> selectMemberBusinessInfoMonoByMemberIdWithAssert(Long memberId);

    /**
     * insert member business
     *
     * @param memberBusiness
     * @return
     */
    MemberBusinessInfo insertMemberBusiness(MemberBusiness memberBusiness);

    /**
     * select business by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberBusinessInfo>> selectMemberBusinessInfoMonoByIds(List<Long> ids);

}
