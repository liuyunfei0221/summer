package com.blue.shine.constant;


import com.blue.basic.model.common.PageModelRequest;
import com.blue.shine.model.ShineCondition;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * type reference for shine module
 *
 * @author DarkBlue
 */
public final class MemberTypeReference {

    public static final ParameterizedTypeReference<PageModelRequest<ShineCondition>> PAGE_MODEL_FOR_SHINE_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

}
