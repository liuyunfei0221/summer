package com.blue.verify.constant;


import com.blue.basic.model.common.PageModelRequest;
import com.blue.verify.model.VerifyHistoryCondition;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * type reference for verify module
 *
 * @author DarkBlue
 */
public final class VerifyTypeReference {

    public static final ParameterizedTypeReference<PageModelRequest<VerifyHistoryCondition>> PAGE_MODEL_FOR_VERIFY_HISTORY_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

}
