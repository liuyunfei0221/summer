package com.blue.event.constant;


import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * type reference for event module
 *
 * @author DarkBlue
 */
public final class EventTypeReference {

    public static final ParameterizedTypeReference<Map<String,Object>> EVENT_MODEL_FOR_RESOURCE_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

}