package com.blue.file.service.inter;

import com.blue.file.api.model.FileUploadResult;
import com.blue.file.repository.entity.Attachment;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 文件业务接口
 *
 * @author DarkBlue
 */

@SuppressWarnings("JavaDoc")
public interface FileService {

    /**
     * 文件上传
     *
     * @param valueMap
     * @param memberId
     * @return
     */
    Mono<List<FileUploadResult>> uploadAttachment(MultiValueMap<String, Part> valueMap, Long memberId);

    /**
     * 下载文件
     *
     * @param attachmentId
     * @param memberId
     * @return
     */
    Mono<Attachment> getAttachmentForDownload(Long attachmentId, Long memberId);

}
