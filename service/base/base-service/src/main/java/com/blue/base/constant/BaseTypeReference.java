package com.blue.base.constant;


import com.blue.base.model.AreaCondition;
import com.blue.base.model.CityCondition;
import com.blue.base.model.CountryCondition;
import com.blue.base.model.StateCondition;
import com.blue.base.model.common.PageModelRequest;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * type reference for base module
 *
 * @author DarkBlue
 */
public final class BaseTypeReference {

    public static final ParameterizedTypeReference<PageModelRequest<CountryCondition>> PAGE_MODEL_FOR_COUNTRY_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<StateCondition>> PAGE_MODEL_FOR_STATE_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<CityCondition>> PAGE_MODEL_FOR_CITY_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<AreaCondition>> PAGE_MODEL_FOR_AREA_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

}
