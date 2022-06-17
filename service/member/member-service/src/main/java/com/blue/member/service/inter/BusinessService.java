package com.blue.member.service.inter;

import com.blue.member.api.model.MemberBusinessInfo;
import com.blue.member.repository.entity.Business;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * member business service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface BusinessService {

    /**
     * get member business by id
     *
     * @param id
     * @return
     */
    Optional<Business> getMemberBusiness(Long id);

    /**
     * get member business mono by id
     *
     * @param id
     * @return
     */
    Mono<Optional<Business>> getMemberBusinessMono(Long id);

    /**
     * get member business by member id
     *
     * @param memberId
     * @return
     */
    Optional<Business> getMemberBusinessByMemberId(Long memberId);

    /**
     * query member business mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<Optional<Business>> getMemberBusinessMonoByMemberId(Long memberId);

    /**
     * query member business by id with assert
     *
     * @param id
     * @return
     */
    Mono<MemberBusinessInfo> getMemberBusinessInfoMonoWithAssert(Long id);

    /**
     * query member business by member id with assert
     *
     * @param memberId
     * @return
     */
    Mono<MemberBusinessInfo> getMemberBusinessInfoMonoByMemberIdWithAssert(Long memberId);

    /**
     * insert member business
     *
     * @param business
     * @return
     */
    MemberBusinessInfo insertMemberBusiness(Business business);

    /**
     * select business by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberBusinessInfo>> selectMemberBusinessInfoMonoByIds(List<Long> ids);

}
