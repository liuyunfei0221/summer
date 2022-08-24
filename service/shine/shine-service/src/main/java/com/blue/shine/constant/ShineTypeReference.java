package com.blue.shine.constant;


import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.es.model.PitCursor;
import com.blue.es.model.SearchAfterCursor;
import com.blue.shine.model.ShineCondition;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * type reference for shine module
 *
 * @author DarkBlue
 */
public final class ShineTypeReference {

    public static final ParameterizedTypeReference<ScrollModelRequest<ShineCondition, SearchAfterCursor>> SCROLL_MODEL_FOR_SHINE_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<ScrollModelRequest<ShineCondition, PitCursor>> SCROLL_MODEL_WITH_SNAP_SHOT_FOR_SHINE_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<ShineCondition>> PAGE_MODEL_FOR_SHINE_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

}
