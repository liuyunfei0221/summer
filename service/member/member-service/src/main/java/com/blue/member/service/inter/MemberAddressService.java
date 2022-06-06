package com.blue.member.service.inter;

import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.member.api.model.MemberAddressInfo;
import com.blue.member.model.MemberAddressInsertParam;
import com.blue.member.model.MemberAddressUpdateParam;
import com.blue.member.model.MemberAddressCondition;
import com.blue.member.repository.entity.MemberAddress;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * member address service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface MemberAddressService {

    /**
     * insert address
     *
     * @param memberAddressInsertParam
     * @param memberId
     * @return
     */
    MemberAddressInfo insertMemberAddress(MemberAddressInsertParam memberAddressInsertParam, Long memberId);

    /**
     * update a exist address
     *
     * @param addressUpdateParam
     * @param memberId
     * @return
     */
    MemberAddressInfo updateMemberAddress(MemberAddressUpdateParam addressUpdateParam, Long memberId);

    /**
     * delete address
     *
     * @param id
     * @param memberId
     * @return
     */
    MemberAddressInfo deleteMemberAddress(Long id, Long memberId);

    /**
     * query address by id
     *
     * @param id
     * @return
     */
    Optional<MemberAddress> getMemberAddress(Long id);

    /**
     * query address mono by id
     *
     * @param id
     * @return
     */
    Mono<Optional<MemberAddress>> getMemberAddressMono(Long id);

    /**
     * query address by member id
     *
     * @param memberId
     * @return
     */
    List<MemberAddress> selectMemberAddressByMemberId(Long memberId);

    /**
     * query address mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<List<MemberAddress>> selectMemberAddressMonoByMemberId(Long memberId);

    /**
     * query address info by member id
     *
     * @param memberId
     * @return
     */
    List<MemberAddressInfo> selectMemberAddressInfoByMemberId(Long memberId);

    /**
     * query address info mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<List<MemberAddressInfo>> selectMemberAddressInfoMonoByMemberId(Long memberId);

    /**
     * query address by id with assert
     *
     * @param id
     * @return
     */
    Mono<MemberAddressInfo> selectMemberAddressInfoMonoByPrimaryKeyWithAssert(Long id);

    /**
     * select address by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberAddressInfo>> selectMemberAddressInfoMonoByIds(List<Long> ids);

    /**
     * select address by page and condition
     *
     * @param limit
     * @param rows
     * @param memberAddressCondition
     * @return
     */
    Mono<List<MemberAddress>> selectMemberAddressMonoByLimitAndCondition(Long limit, Long rows, MemberAddressCondition memberAddressCondition);

    /**
     * count address by condition
     *
     * @param memberAddressCondition
     * @return
     */
    Mono<Long> countMemberAddressMonoByCondition(MemberAddressCondition memberAddressCondition);

    /**
     * select address info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<MemberAddressInfo>> selectMemberAddressInfoPageMonoByPageAndCondition(PageModelRequest<MemberAddressCondition> pageModelRequest);

}
