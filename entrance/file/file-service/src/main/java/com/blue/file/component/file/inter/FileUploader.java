package com.blue.file.component.file.inter;

import com.blue.file.api.model.FileUploadResult;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Mono;

/**
 * file upload interface
 *
 * @author liuyunfei
 * @date 2021/9/23
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public interface FileUploader {

    /**
     * upload
     *
     * @param part
     * @return
     */
    Mono<FileUploadResult> upload(Part part);

}
