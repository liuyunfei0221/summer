package com.blue.file.handler.api;

import com.blue.base.model.base.Access;
import com.blue.base.model.base.BlueResult;
import com.blue.base.model.base.IdentityWrapper;
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

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccess;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.BlueHeader.CONTENT_DISPOSITION;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.OK;
import static com.blue.base.constant.base.ResponseMessage.FILE_NOT_EXIST;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.web.reactive.function.BodyInserters.fromResource;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;


/**
 * 文件处理控制器
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "Duplicates", "AliControlFlowStatementWithoutBraces"})
@Component
public final class FileApiHandler {

    private final FileService fileService;

    private final FileDeploy fileDeploy;

    private static long ALL_FILE_SIZE_THRESHOLD;

    public FileApiHandler(FileService fileService, FileDeploy fileDeploy) {
        this.fileService = fileService;
        this.fileDeploy = fileDeploy;
    }

    private static final BlueException FILE_NOT_EXIST_EXP = new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, FILE_NOT_EXIST.message);

    @PostConstruct
    public void init() {
        ALL_FILE_SIZE_THRESHOLD = fileDeploy.getAllFileSizeThreshold();
    }

    /**
     * 文件上传
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> upload(ServerRequest serverRequest) {
        long allFileSize = serverRequest.headers().contentLength().orElse(0L);
        if (0L == allFileSize || allFileSize > ALL_FILE_SIZE_THRESHOLD)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "上传文件总大小不能为空或超过 " + ALL_FILE_SIZE_THRESHOLD);

        Access access = getAccess(serverRequest);
        return serverRequest.multipartData()
                .flatMap(mm ->
                        fileService.uploadAttachment(mm, access.getId()))
                .flatMap(rl ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(generate(OK.code, rl, OK.message), BlueResult.class));
    }

    /**
     * 文件下载
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> download(ServerRequest serverRequest) {
        Access access = getAccess(serverRequest);
        return serverRequest.bodyToMono(IdentityWrapper.class)
                .flatMap(dto ->
                        fileService.getAttachmentForDownload(dto.getId(), access.getId())
                                .switchIfEmpty(error(FILE_NOT_EXIST_EXP))
                                .flatMap(attachment -> {
                                    String link = attachment.getLink();
                                    if (link == null || "".equals(link))
                                        return error(FILE_NOT_EXIST_EXP);

                                    FileSystemResource resource = new FileSystemResource(new File(link));
                                    if (!resource.exists())
                                        return error(FILE_NOT_EXIST_EXP);

                                    return ok().contentType(APPLICATION_OCTET_STREAM)
                                            .header(CONTENT_DISPOSITION.name,
                                                    "attachment; filename=" + URLEncoder.encode(attachment.getName(), UTF_8))
                                            .body(fromResource(resource));
                                }));
    }

}
