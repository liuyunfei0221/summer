package com.blue.media.constant;


import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.media.model.*;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * type reference for media module
 *
 * @author DarkBlue
 */
public final class MediaTypeReference {

    public static final ParameterizedTypeReference<ScrollModelRequest<AttachmentCondition, Long>> SCROLL_MODEL_FOR_ATTACHMENT_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<AttachmentManagerCondition>> PAGE_MODEL_FOR_ATTACHMENT_MANAGER_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<ScrollModelRequest<Void, Long>> SCROLL_MODEL_FOR_DOWNLOAD_HISTORY_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<DownloadHistoryCondition>> PAGE_MODEL_FOR_DOWNLOAD_HISTORY_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<MessageTemplateCondition>> PAGE_MODEL_FOR_MESSAGE_TEMPLATE_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<QrCodeCondition>> PAGE_MODEL_FOR_QR_CODE_CONFIG_CONDITION_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

}