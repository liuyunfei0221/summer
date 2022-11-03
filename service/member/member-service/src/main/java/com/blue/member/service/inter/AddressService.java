package com.blue.member.service.inter;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.member.api.model.AddressInfo;
import com.blue.member.model.AddressCondition;
import com.blue.member.model.AddressInsertParam;
import com.blue.member.model.AddressUpdateParam;
import com.blue.member.repository.entity.Address;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * address service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface AddressService {

    /**
     * insert address
     *
     * @param addressInsertParam
     * @param memberId
     * @return
     */
    Mono<AddressInfo> insertAddress(AddressInsertParam addressInsertParam, Long memberId);

    /**
     * update a exist address
     *
     * @param addressUpdateParam
     * @param memberId
     * @return
     */
    Mono<AddressInfo> updateAddress(AddressUpdateParam addressUpdateParam, Long memberId);

    /**
     * delete address
     *
     * @param id
     * @param memberId
     * @return
     */
    Mono<AddressInfo> deleteAddress(Long id, Long memberId);

    /**
     * query address mono by id
     *
     * @param id
     * @return
     */
    Mono<Address> getAddress(Long id);

    /**
     * query address mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<List<Address>> selectAddressByMemberId(Long memberId);

    /**
     * query address info mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<List<AddressInfo>> selectAddressInfoByMemberId(Long memberId);

    /**
     * query address info mono by id with assert
     *
     * @param id
     * @return
     */
    Mono<AddressInfo> getAddressInfoWithAssert(Long id);

    /**
     * select address info by ids
     *
     * @param ids
     * @return
     */
    Mono<List<AddressInfo>> selectAddressInfoByIds(List<Long> ids);

    /**
     * select address by page and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    Mono<List<Address>> selectAddressByLimitAndQuery(Long limit, Long rows, Query query);

    /**
     * count address by query
     *
     * @param query
     * @return
     */
    Mono<Long> countAddressByQuery(Query query);

    /**
     * select address info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<AddressInfo>> selectAddressInfoPageByPageAndCondition(PageModelRequest<AddressCondition> pageModelRequest);

}
