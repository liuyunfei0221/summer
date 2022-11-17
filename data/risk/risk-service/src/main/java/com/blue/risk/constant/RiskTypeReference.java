package com.blue.risk.constant;


import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.risk.model.RiskHitRecordCondition;
import com.blue.risk.model.RiskStrategyCondition;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * type reference for risk module
 *
 * @author DarkBlue
 */
public final class RiskTypeReference {

    public static final ParameterizedTypeReference<ScrollModelRequest<RiskHitRecordCondition, Long>> SCROLL_MODEL_FOR_RISK_HIT_RECORD_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<RiskStrategyCondition>> PAGE_MODEL_FOR_RISK_STRATEGY_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

}
