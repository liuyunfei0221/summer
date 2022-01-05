package com.blue.media.service.inter;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.media.api.model.AttachmentInfo;
import com.blue.media.repository.entity.Attachment;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * attachment service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface AttachmentService {

    /**
     * insert attachment
     *
     * @param attachment
     * @return
     */
    void insert(Attachment attachment);

    /**
     * insert attachment batch
     *
     * @param attachments
     * @return
     */
    void insertBatch(List<Attachment> attachments);

    /**
     * get attachment by id
     *
     * @param id
     * @return
     */
    Mono<Attachment> getAttachment(Long id);

    /**
     * select attachment by page and member id
     *
     * @param pageModelRequest
     * @param memberId
     * @return
     */
    Mono<PageModelResponse<AttachmentInfo>> selectAttachmentByPageAndMemberId(PageModelRequest<Void> pageModelRequest, Long memberId);

}
