package com.blue.lake.constant;


import com.blue.basic.model.common.ScrollModelRequest;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * type reference for lake module
 *
 * @author DarkBlue
 */
public final class LakeTypeReference {

    public static final ParameterizedTypeReference<ScrollModelRequest<Void, Long>> SCROLL_MODEL_FOR_OPT_EVENT_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

}
