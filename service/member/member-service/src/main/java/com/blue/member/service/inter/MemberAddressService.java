package com.blue.member.service.inter;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.member.api.model.MemberAddressInfo;
import com.blue.member.model.MemberAddressCondition;
import com.blue.member.repository.entity.MemberAddress;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * member address service
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface MemberAddressService {

    /**
     * query address by id
     *
     * @param id
     * @return
     */
    Optional<MemberAddress> selectMemberAddressByPrimaryKey(Long id);

    /**
     * query address mono by id
     *
     * @param id
     * @return
     */
    Mono<Optional<MemberAddress>> selectMemberAddressMonoByPrimaryKey(Long id);

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
     * query address by id with assert
     *
     * @param id
     * @return
     */
    Mono<MemberAddressInfo> selectMemberAddressInfoMonoByPrimaryKeyWithAssert(Long id);

    /**
     * insert address
     *
     * @param memberAddress
     * @return
     */
    MemberAddressInfo insertMemberAddress(MemberAddress memberAddress);

    /**
     * select address by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberAddress>> selectMemberAddressMonoByIds(List<Long> ids);

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
