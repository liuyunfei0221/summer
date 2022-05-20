package com.blue.portal.constant;


import com.blue.base.model.common.PageModelRequest;
import com.blue.portal.model.BulletinCondition;
import com.blue.portal.model.StyleCondition;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * type reference for portal module
 *
 * @author DarkBlue
 */
public final class PortalTypeReference {

    public static final ParameterizedTypeReference<PageModelRequest<BulletinCondition>> PAGE_MODEL_FOR_BULLETIN_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<StyleCondition>> PAGE_MODEL_FOR_STYLE_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

}
