package com.blue.member.service.inter;

import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.base.model.common.StatusParam;
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
    RealNameInfo initRealName(Long memberId);

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
     * @param id
     * @param statusParam
     * @return
     */
    RealNameInfo updateRealNameStatus(Long id, StatusParam statusParam);

    /**
     * get by id
     *
     * @param id
     * @return
     */
    RealName getRealName(Long id);

    /**
     * get mono by id
     *
     * @param id
     * @return
     */
    Mono<RealName> getRealNameMono(Long id);

    /**
     * query real name by id with assert
     *
     * @param id
     * @return
     */
    Mono<RealNameInfo> getRealNameInfoMonoWithAssert(Long id);

    /**
     * get by member id
     *
     * @param memberId
     * @return
     */
    RealName getRealNameByMemberId(Long memberId);

    /**
     * get mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<RealName> getRealNameMonoByMemberId(Long memberId);

    /**
     * query real name info by member id with assert
     *
     * @param memberId
     * @return
     */
    Mono<RealNameInfo> getRealNameInfoMonoByMemberIdWithAssert(Long memberId);

    /**
     * select real name by ids
     *
     * @param ids
     * @return
     */
    List<RealName> selectRealNameByIds(List<Long> ids);

    /**
     * select real name mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<RealName>> selectRealNameMonoByIds(List<Long> ids);

    /**
     * select real name info by ids
     *
     * @param ids
     * @return
     */
    Mono<List<RealNameInfo>> selectRealNameInfoMonoByIds(List<Long> ids);

    /**
     * select real name by member ids
     *
     * @param memberIds
     * @return
     */
    List<RealName> selectRealNameByMemberIds(List<Long> memberIds);

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
    Mono<List<RealNameInfo>> selectRealNameInfoMonoByMemberIds(List<Long> memberIds);

    /**
     * select real name by page and condition
     *
     * @param limit
     * @param rows
     * @param realNameCondition
     * @return
     */
    Mono<List<RealName>> selectRealNameMonoByLimitAndCondition(Long limit, Long rows, RealNameCondition realNameCondition);

    /**
     * count real name by condition
     *
     * @param realNameCondition
     * @return
     */
    Mono<Long> countRealNameMonoByCondition(RealNameCondition realNameCondition);

    /**
     * select real name info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<RealNameInfo>> selectRealNameInfoPageMonoByPageAndCondition(PageModelRequest<RealNameCondition> pageModelRequest);

}
