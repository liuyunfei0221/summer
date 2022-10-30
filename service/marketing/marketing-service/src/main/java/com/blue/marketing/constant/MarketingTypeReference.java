package com.blue.marketing.constant;


import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.marketing.model.EventRecordCondition;
import com.blue.marketing.model.RewardCondition;
import com.blue.marketing.model.RewardDateRelationCondition;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * type reference for marketing module
 *
 * @author DarkBlue
 */
public final class MarketingTypeReference {

    public static final ParameterizedTypeReference<ScrollModelRequest<Void, Long>> SCROLL_MODEL_FOR_EVENT_RECORD_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<RewardCondition>> PAGE_MODEL_FOR_REWARD_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<RewardDateRelationCondition>> PAGE_MODEL_FOR_REWARD_DATE_RELATION_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<EventRecordCondition>> PAGE_MODEL_FOR_EVENT_RECORD_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

}
