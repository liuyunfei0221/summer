package com.blue.file.service.inter;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.file.api.model.AttachmentInfo;
import com.blue.file.repository.entity.Attachment;

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
    Attachment getAttachment(Long id);

    /**
     * select attachment by page and member id
     *
     * @param pageModelRequest
     * @param memberId
     * @return
     */
    PageModelResponse<AttachmentInfo> listAttachment(PageModelRequest<Void> pageModelRequest, Long memberId);

}
