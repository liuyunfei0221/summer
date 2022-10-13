package com.blue.media.constant;


import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.media.model.AttachmentCondition;
import com.blue.media.model.DownloadHistoryCondition;
import com.blue.media.model.MessageTemplateCondition;
import com.blue.media.model.QrCodeCondition;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * type reference for media module
 *
 * @author DarkBlue
 */
public final class MediaTypeReference {

    public static final ParameterizedTypeReference<ScrollModelRequest<Void, Long>> SCROLL_MODEL_FOR_ATTACHMENT_TYPE = new ParameterizedTypeReference<>() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Type getType() {
            return super.getType();
        }
    };

    public static final ParameterizedTypeReference<PageModelRequest<AttachmentCondition>> PAGE_MODEL_FOR_ATTACHMENT_CONDITION_TYPE = new ParameterizedTypeReference<>() {
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
