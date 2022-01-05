package com.blue.media.service.inter;

import com.blue.media.api.model.FileUploadResult;
import com.blue.media.repository.entity.Attachment;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * media service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface FileService {

    /**
     * upload attachment
     *
     * @param valueMap
     * @param memberId
     * @return
     */
    Mono<List<FileUploadResult>> uploadAttachment(MultiValueMap<String, Part> valueMap, Long memberId);

    /**
     * download attachment
     *
     * @param attachmentId
     * @param memberId
     * @return
     */
    Mono<Attachment> getAttachmentForDownload(Long attachmentId, Long memberId);

}
