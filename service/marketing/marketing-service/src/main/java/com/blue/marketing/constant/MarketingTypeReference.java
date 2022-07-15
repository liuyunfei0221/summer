package com.blue.marketing.constant;


import com.blue.basic.model.common.PageModelRequest;
import com.blue.marketing.model.EventRecordCondition;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * type reference for marketing module
 *
 * @author DarkBlue
 */
public final class MarketingTypeReference {

    public static final ParameterizedTypeReference<PageModelRequest<EventRecordCondition>> PAGE_MODEL_FOR_EVENT_RECORD_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

}
