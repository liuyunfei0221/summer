package com.blue.risk.service.inter;

import com.blue.basic.model.common.ConditionCountResponse;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.risk.model.RiskHitRecordCondition;
import com.blue.risk.repository.entity.RiskHitRecord;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * risk hit record service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface RiskHitRecordService {

    /**
     * insert records
     *
     * @param riskHitRecords
     * @return
     */
    Mono<Boolean> insertRiskHitRecords(List<RiskHitRecord> riskHitRecords);

    /**
     * select by search after
     *
     * @param scrollModelRequest
     * @return
     */
    Mono<ScrollModelResponse<RiskHitRecord, Long>> selectRiskHitRecordScrollByConditionAndCursor(ScrollModelRequest<RiskHitRecordCondition, Long> scrollModelRequest);

    /**
     * count by condition
     *
     * @param riskHitRecordCondition
     * @return
     */
    Mono<ConditionCountResponse<RiskHitRecordCondition>> countRiskHitRecordByCondition(RiskHitRecordCondition riskHitRecordCondition);

}