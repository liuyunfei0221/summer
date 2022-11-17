package com.blue.risk.service.inter;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.risk.api.model.RiskStrategyInfo;
import com.blue.risk.model.RiskStrategyCondition;
import com.blue.risk.model.RiskStrategyInsertParam;
import com.blue.risk.model.RiskStrategyManagerInfo;
import com.blue.risk.model.RiskStrategyUpdateParam;
import com.blue.risk.repository.entity.RiskStrategy;
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
    RiskStrategyInfo insertRiskStrategy(RiskStrategyInsertParam riskStrategyInsertParam, Long operatorId);

    /**
     * update a exist risk strategy
     *
     * @param riskStrategyUpdateParam
     * @param operatorId
     * @return
     */
    RiskStrategyInfo updateRiskStrategy(RiskStrategyUpdateParam riskStrategyUpdateParam, Long operatorId);

    /**
     * delete risk strategy
     *
     * @param id
     * @return
     */
    RiskStrategyInfo deleteRiskStrategy(Long id);

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
     * @param riskType
     * @return
     */
    Mono<RiskStrategy> getRiskStrategyByType(Integer riskType);

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
     * @param riskStrategyCondition
     * @return
     */
    Mono<List<RiskStrategy>> selectRiskStrategyByLimitAndCondition(Long limit, Long rows, RiskStrategyCondition riskStrategyCondition);

    /**
     * count risk strategy by condition
     *
     * @param riskStrategyCondition
     * @return
     */
    Mono<Long> countRiskStrategyByCondition(RiskStrategyCondition riskStrategyCondition);

    /**
     * select risk strategy manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<RiskStrategyManagerInfo>> selectRiskStrategyManagerInfoPageByPageAndCondition(PageModelRequest<RiskStrategyCondition> pageModelRequest);

}
