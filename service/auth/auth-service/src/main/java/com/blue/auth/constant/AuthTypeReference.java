package com.blue.auth.constant;


import com.blue.auth.model.ResourceCondition;
import com.blue.auth.model.RoleCondition;
import com.blue.auth.model.SecurityQuestionInsertParam;
import com.blue.base.model.base.ListParam;
import com.blue.base.model.base.PageModelRequest;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * type reference for auth module
 *
 * @author DarkBlue
 */
public final class AuthTypeReference {

    public static final ParameterizedTypeReference<PageModelRequest<ResourceCondition>> PAGE_MODEL_FOR_RESOURCE_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<RoleCondition>> PAGE_MODEL_FOR_ROLE_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<ListParam<SecurityQuestionInsertParam>> LIST_PARAM_FOR_QUESTION_INSERT_PARAM_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

}
