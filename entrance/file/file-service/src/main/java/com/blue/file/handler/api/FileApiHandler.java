package com.blue.file.handler.api;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.base.IdentityParam;
import com.blue.base.model.exps.BlueException;
import com.blue.file.config.deploy.FileDeploy;
import com.blue.file.service.inter.FileService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URLEncoder;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.BlueHeader.CONTENT_DISPOSITION;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.ResponseMessage.DATA_NOT_EXIST;
import static com.blue.base.constant.base.ResponseMessage.EMPTY_PARAM;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.web.reactive.function.BodyInserters.fromResource;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.zip;


/**
 * file api handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "Duplicates", "AliControlFlowStatementWithoutBraces"})
@Component
public final class FileApiHandler {

    private final FileService fileService;

    private final FileDeploy fileDeploy;

    private long allFileSizeThreshold;

    public FileApiHandler(FileService fileService, FileDeploy fileDeploy) {
        this.fileService = fileService;
        this.fileDeploy = fileDeploy;
    }

    @PostConstruct
    public void init() {
        allFileSizeThreshold = fileDeploy.getAllFileSizeThreshold();
    }

    /**
     * upload
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> upload(ServerRequest serverRequest) {
        long allFileSize = serverRequest.headers().contentLength().orElse(0L);
        if (0L == allFileSize || allFileSize > allFileSizeThreshold)
            throw new BlueException(PAYLOAD_TOO_LARGE.status, PAYLOAD_TOO_LARGE.code, PAYLOAD_TOO_LARGE.message, null);

        return zip(serverRequest.multipartData()
                        .switchIfEmpty(error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message, null))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        fileService.uploadAttachment(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(rl ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(generate(OK.code, rl, OK.message), BlueResponse.class));
    }

    /**
     * download
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> download(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(IdentityParam.class)
                        .switchIfEmpty(error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message, null))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        fileService.getAttachmentForDownload(tuple2.getT1().getId(), tuple2.getT2().getId())
                                .switchIfEmpty(error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, DATA_NOT_EXIST.message, null)))
                                .flatMap(attachment -> {
                                    String link = attachment.getLink();
                                    if (link == null || "".equals(link))
                                        return error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, DATA_NOT_EXIST.message, null));

                                    FileSystemResource resource = new FileSystemResource(new File(link));
                                    if (!resource.exists())
                                        return error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, DATA_NOT_EXIST.message, null));

                                    return ok().contentType(APPLICATION_OCTET_STREAM)
                                            .header(CONTENT_DISPOSITION.name,
                                                    "attachment; filename=" + URLEncoder.encode(attachment.getName(), UTF_8))
                                            .body(fromResource(resource));
                                }));
    }

}
