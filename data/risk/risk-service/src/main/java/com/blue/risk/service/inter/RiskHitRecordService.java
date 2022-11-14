package com.blue.risk.service.inter;

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
    Mono<ScrollModelResponse<RiskHitRecord, Long>> selectRiskHitRecordScrollByScrollAndCursor(ScrollModelRequest<RiskHitRecordCondition, Long> scrollModelRequest);

}
