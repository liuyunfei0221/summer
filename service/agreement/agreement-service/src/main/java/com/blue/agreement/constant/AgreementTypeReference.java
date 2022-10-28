package com.blue.agreement.constant;


import com.blue.agreement.model.AgreementCondition;
import com.blue.agreement.model.AgreementRecordCondition;
import com.blue.basic.model.common.PageModelRequest;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * type reference for agreement module
 *
 * @author DarkBlue
 */
public final class AgreementTypeReference {

    public static final ParameterizedTypeReference<PageModelRequest<AgreementCondition>> PAGE_MODEL_FOR_AGREEMENT_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<AgreementRecordCondition>> PAGE_MODEL_FOR_AGREEMENT_RECORD_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

}
