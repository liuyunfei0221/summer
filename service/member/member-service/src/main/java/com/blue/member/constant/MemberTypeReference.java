package com.blue.member.constant;


import com.blue.base.model.common.PageModelRequest;
import com.blue.member.model.*;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * type reference for member module
 *
 * @author DarkBlue
 */
public final class MemberTypeReference {

    public static final ParameterizedTypeReference<PageModelRequest<MemberBasicCondition>> PAGE_MODEL_FOR_MEMBER_BASIC_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<MemberDetailCondition>> PAGE_MODEL_FOR_MEMBER_DETAIL_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<RealNameCondition>> PAGE_MODEL_FOR_REAL_NAME_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<AddressCondition>> PAGE_MODEL_FOR_ADDRESS_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<CardCondition>> PAGE_MODEL_FOR_CARD_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

}
