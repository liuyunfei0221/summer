package com.blue.member.service.inter;

import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.member.api.model.MemberAddressInfo;
import com.blue.member.model.MemberAddressCondition;
import com.blue.member.model.MemberAddressInsertParam;
import com.blue.member.model.MemberAddressUpdateParam;
import com.blue.member.repository.entity.MemberAddress;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;

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
    Mono<MemberAddressInfo> insertMemberAddress(MemberAddressInsertParam memberAddressInsertParam, Long memberId);

    /**
     * update a exist address
     *
     * @param addressUpdateParam
     * @param memberId
     * @return
     */
    Mono<MemberAddressInfo> updateMemberAddress(MemberAddressUpdateParam addressUpdateParam, Long memberId);

    /**
     * delete address
     *
     * @param id
     * @param memberId
     * @return
     */
    Mono<MemberAddressInfo> deleteMemberAddress(Long id, Long memberId);

    /**
     * query address mono by id
     *
     * @param id
     * @return
     */
    Mono<MemberAddress> getMemberAddressMono(Long id);

    /**
     * query address mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<List<MemberAddress>> selectMemberAddressMonoByMemberId(Long memberId);

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
     * select address by page and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    Mono<List<MemberAddress>> selectMemberAddressMonoByLimitAndQuery(Long limit, Long rows, Query query);

    /**
     * count address by query
     *
     * @param query
     * @return
     */
    Mono<Long> countMemberAddressMonoByQuery(Query query);

    /**
     * select address info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<MemberAddressInfo>> selectMemberAddressInfoPageMonoByPageAndCondition(PageModelRequest<MemberAddressCondition> pageModelRequest);

}
