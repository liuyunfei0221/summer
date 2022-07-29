package com.blue.finance.constant;


import com.blue.basic.model.common.PageModelRequest;
import com.blue.finance.model.FinanceFlowCondition;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * type reference for finance module
 *
 * @author liuyunfei
 */
public final class FinanceTypeReference {

    public static final ParameterizedTypeReference<PageModelRequest<FinanceFlowCondition>> PAGE_MODEL_FOR_FINANCE_FLOW_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

}
