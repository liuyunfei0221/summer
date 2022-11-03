package com.blue.member.service.inter;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.common.StatusParam;
import com.blue.member.api.model.RealNameInfo;
import com.blue.member.model.RealNameCondition;
import com.blue.member.model.RealNameUpdateParam;
import com.blue.member.repository.entity.RealName;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * member real name service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface RealNameService {

    /**
     * init real name
     *
     * @param memberId
     * @return
     */
    RealName initRealName(Long memberId);

    /**
     * update real name
     *
     * @param memberId
     * @param realNameUpdateParam
     * @return
     */
    RealNameInfo updateRealName(Long memberId, RealNameUpdateParam realNameUpdateParam);

    /**
     * update real name status
     *
     * @param memberId
     * @param statusParam
     * @return
     */
    RealNameInfo updateRealNameStatus(Long memberId, StatusParam statusParam);

    /**
     * get mono by id
     *
     * @param id
     * @return
     */
    Mono<RealName> getRealName(Long id);

    /**
     * query real name by id with assert
     *
     * @param id
     * @return
     */
    Mono<RealNameInfo> getRealNameInfoWithAssert(Long id);

    /**
     * get real name info mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<RealNameInfo> getRealNameInfoByMemberId(Long memberId);

    /**
     * query real name info by member id with assert
     *
     * @param memberId
     * @return
     */
    Mono<RealNameInfo> getRealNameInfoByMemberIdWithAssert(Long memberId);

    /**
     * select real name mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<RealName>> selectRealNameByIds(List<Long> ids);

    /**
     * select real name info by ids
     *
     * @param ids
     * @return
     */
    Mono<List<RealNameInfo>> selectRealNameInfoByIds(List<Long> ids);

    /**
     * select real name mono by member ids
     *
     * @param memberIds
     * @return
     */
    Mono<List<RealName>> selectRealNameMonoByMemberIds(List<Long> memberIds);

    /**
     * select real name info by member ids
     *
     * @param memberIds
     * @return
     */
    Mono<List<RealNameInfo>> selectRealNameInfoByMemberIds(List<Long> memberIds);

    /**
     * select real name by page and condition
     *
     * @param limit
     * @param rows
     * @param realNameCondition
     * @return
     */
    Mono<List<RealName>> selectRealNameByLimitAndCondition(Long limit, Long rows, RealNameCondition realNameCondition);

    /**
     * count real name by condition
     *
     * @param realNameCondition
     * @return
     */
    Mono<Long> countRealNameByCondition(RealNameCondition realNameCondition);

    /**
     * select real name info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<RealNameInfo>> selectRealNameInfoPageByPageAndCondition(PageModelRequest<RealNameCondition> pageModelRequest);

}
