package com.blue.media.constant;


import com.blue.base.model.base.PageModelRequest;
import com.blue.media.model.AttachmentCondition;
import com.blue.media.model.DownloadHistoryCondition;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;

/**
 * type reference for media module
 *
 * @author DarkBlue
 */
public final class MediaTypeReference {

    public static final ParameterizedTypeReference<PageModelRequest<DownloadHistoryCondition>> PAGE_MODEL_FOR_DOWNLOAD_HISTORY_CONDITION_TYPE = new ParameterizedTypeReference<>() {
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

}
