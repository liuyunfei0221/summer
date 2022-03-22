package com.blue.media.handler.api;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.base.IdentityParam;
import com.blue.base.model.exps.BlueException;
import com.blue.media.config.deploy.FileDeploy;
import com.blue.media.service.inter.FileService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.BlueHeader.CONTENT_DISPOSITION;
import static com.blue.base.constant.base.ResponseElement.*;
import static io.netty.buffer.ByteBufAllocator.DEFAULT;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.READ;
import static java.util.Optional.ofNullable;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.web.reactive.function.BodyInserters.fromDataBuffers;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.zip;


/**
 * media api handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "Duplicates", "AliControlFlowStatementWithoutBraces"})
@Component
public final class FileApiHandler {

    private final FileService fileService;

    private final long allFileSizeThreshold;

    public FileApiHandler(FileService fileService, FileDeploy fileDeploy) {
        this.fileService = fileService;
        allFileSizeThreshold = fileDeploy.getAllFileSizeThreshold();
    }

    private void assertContentLength(ServerRequest serverRequest) {
        long allFileSize = ofNullable(serverRequest)
                .map(sr -> sr.headers().contentLength().orElse(0L)).orElse(0L);

        if (allFileSize < 1L)
            throw new BlueException(FILE_NOT_EXIST);
        if (allFileSize > allFileSizeThreshold)
            throw new BlueException(PAYLOAD_TOO_LARGE);
    }

    /**
     * upload
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> upload(ServerRequest serverRequest) {
        assertContentLength(serverRequest);
        return zip(serverRequest.multipartData()
                        .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        fileService.uploadAttachment(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(rl ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(generate(OK.code, rl, serverRequest), BlueResponse.class));
    }

    public static final DataBufferFactory DATA_BUFFER_FACTORY = new NettyDataBufferFactory(DEFAULT);

    /**
     * download
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> download(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(IdentityParam.class)
                        .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        fileService.getAttachmentForDownload(tuple2.getT1().getId(), tuple2.getT2().getId())
                                .switchIfEmpty(error(() -> new BlueException(DATA_NOT_EXIST)))
                                .flatMap(attachment -> {
                                    String link = attachment.getLink();
                                    if (link == null || "".equals(link))
                                        return error(() -> new BlueException(DATA_NOT_EXIST));

//                                    FileSystemResource resource = new FileSystemResource(new File(link));
//                                    if (!resource.exists())
//                                        return error(() -> new BlueException(DATA_NOT_EXIST));

                                    Flux<DataBuffer> dataBufferFlux = null;

                                    try {
                                        FileChannel channel = FileChannel.open(new File(link).toPath(), READ);

                                        dataBufferFlux = DataBufferUtils.readByteChannel(() -> channel, DATA_BUFFER_FACTORY, 8192);

                                        //TODO content - type

                                        FileSystemResource resource = new FileSystemResource(new File(link));

                                    } catch (Exception e) {
                                        return Mono.error(() -> new RuntimeException(""));
                                    }

                                    return ok().contentType(IMAGE_JPEG)
                                            .header(CONTENT_DISPOSITION.name,
                                                    "attachment; filename=" + URLEncoder.encode(attachment.getName(), UTF_8))
                                            .body(fromDataBuffers(dataBufferFlux));
                                }));
    }

}
