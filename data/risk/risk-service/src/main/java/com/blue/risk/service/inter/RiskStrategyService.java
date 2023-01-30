package com.blue.risk.service.inter;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.risk.api.model.RiskStrategyInfo;
import com.blue.risk.model.RiskStrategyCondition;
import com.blue.risk.model.RiskStrategyInsertParam;
import com.blue.risk.model.RiskStrategyManagerInfo;
import com.blue.risk.model.RiskStrategyUpdateParam;
import com.blue.risk.repository.entity.RiskStrategy;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * risk analyse service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RiskStrategyService {

    /**
     * insert risk strategy
     *
     * @param riskStrategyInsertParam
     * @param operatorId
     * @return
     */
    Mono<RiskStrategyInfo> insertRiskStrategy(RiskStrategyInsertParam riskStrategyInsertParam, Long operatorId);

    /**
     * update a exist risk strategy
     *
     * @param riskStrategyUpdateParam
     * @param operatorId
     * @return
     */
    Mono<RiskStrategyInfo> updateRiskStrategy(RiskStrategyUpdateParam riskStrategyUpdateParam, Long operatorId);

    /**
     * delete risk strategy
     *
     * @param id
     * @return
     */
    Mono<RiskStrategyInfo> deleteRiskStrategy(Long id);

    /**
     * get risk strategy mono by id
     *
     * @param id
     * @return
     */
    Mono<RiskStrategy> getRiskStrategy(Long id);

    /**
     * get risk strategy by type
     *
     * @param type
     * @return
     */
    Mono<RiskStrategy> getRiskStrategyByType(Integer type);

    /**
     * select all risk strategies
     *
     * @return
     */
    Mono<List<RiskStrategy>> selectRiskStrategy();

    /**
     * select risk strategy by page and condition
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    Mono<List<RiskStrategy>> selectRiskStrategyByLimitAndCondition(Long limit, Long rows, Query query);

    /**
     * count risk strategy by condition
     *
     * @param query
     * @return
     */
    Mono<Long> countRiskStrategyByCondition(Query query);

    /**
     * select risk strategy manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<RiskStrategyManagerInfo>> selectRiskStrategyManagerInfoPageByPageAndCondition(PageModelRequest<RiskStrategyCondition> pageModelRequest);

}