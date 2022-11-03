package com.blue.finance.service.inter;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.finance.api.model.FinanceFlowInfo;
import com.blue.finance.api.model.FinanceFlowManagerInfo;
import com.blue.finance.model.FinanceFlowCondition;
import com.blue.finance.repository.entity.FinanceFlow;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * finance flow service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue"})
public interface FinanceFlowService {

    /**
     * insert finance flow async
     *
     * @param financeFlow
     * @return
     */
    Mono<FinanceFlow> insertFinanceFlowAsync(FinanceFlow financeFlow);

    /**
     * insert finance flow
     *
     * @param financeFlow
     * @return
     */
    Mono<FinanceFlow> insertFinanceFlow(FinanceFlow financeFlow);

    /**
     * get finance flow mono by id
     *
     * @param id
     * @return
     */
    Mono<FinanceFlow> getFinanceFlow(Long id);

    /**
     * get finance flow by id
     *
     * @param id
     * @return
     */
    Optional<FinanceFlow> getFinanceFlowOpt(Long id);

    /**
     * select finance flow by page and memberId
     *
     * @param limit
     * @param rows
     * @param memberId
     * @return
     */
    Mono<List<FinanceFlow>> selectFinanceFlowByLimitAndMemberId(Long limit, Long rows, Long memberId);

    /**
     * count finance flow by memberId
     *
     * @param memberId
     * @return
     */
    Mono<Long> countFinanceFlowByMemberId(Long memberId);

    /**
     * select finance flow info by page and member id
     *
     * @param pageModelRequest
     * @param memberId
     * @return
     */
    Mono<PageModelResponse<FinanceFlowInfo>> selectFinanceFlowInfoByPageAndMemberId(PageModelRequest<Void> pageModelRequest, Long memberId);

    /**
     * select finance flow by page and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    Mono<List<FinanceFlow>> selectFinanceFlowByLimitAndQuery(Long limit, Long rows, Query query);

    /**
     * count finance flow by query
     *
     * @param query
     * @return
     */
    Mono<Long> countFinanceFlowByQuery(Query query);

    /**
     * select finance flow info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<FinanceFlowManagerInfo>> selectFinanceFlowManagerInfoPageByPageAndCondition(PageModelRequest<FinanceFlowCondition> pageModelRequest);

}