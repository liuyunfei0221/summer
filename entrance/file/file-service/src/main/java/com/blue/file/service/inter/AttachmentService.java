package com.blue.file.service.inter;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.file.api.model.AttachmentInfo;
import com.blue.file.repository.entity.Attachment;

import java.util.List;

/**
 * 附件业务接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface AttachmentService {

    /**
     * 新增附件
     *
     * @param attachment
     * @return
     */
    void insert(Attachment attachment);

    /**
     * 批量新增附件
     *
     * @param attachments
     * @return
     */
    void insertBatch(List<Attachment> attachments);

    /**
     * 根据文件id获取文件
     *
     * @param id
     * @return
     */
    Attachment getAttachment(Long id);

    /**
     * 分页查询附件对应用户的附件列表
     *
     * @param pageModelRequest
     * @param memberId
     * @return
     */
    PageModelResponse<AttachmentInfo> listAttachment(PageModelRequest<Void> pageModelRequest, Long memberId);

}
