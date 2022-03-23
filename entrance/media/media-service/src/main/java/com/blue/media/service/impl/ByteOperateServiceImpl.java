package com.blue.media.service.impl;

import com.blue.base.common.base.CommonFunctions;
import com.blue.base.constant.base.BlueFileType;
import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.base.IdentityParam;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.media.api.model.FileUploadResult;
import com.blue.media.component.file.inter.FileUploader;
import com.blue.media.config.deploy.FileDeploy;
import com.blue.media.repository.entity.Attachment;
import com.blue.media.repository.entity.DownloadHistory;
import com.blue.media.service.inter.AttachmentService;
import com.blue.media.service.inter.ByteOperateService;
import com.blue.media.service.inter.DownloadHistoryService;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.io.File;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.BlueHeader.CONTENT_DISPOSITION;
import static com.blue.base.constant.base.BlueNumericalValue.DB_WRITE;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Status.VALID;
import static com.blue.base.constant.base.Symbol.SCHEME_SEPARATOR;
import static com.blue.media.common.MediaCommonFunctions.BUFFER_SIZE;
import static com.blue.media.common.MediaCommonFunctions.DATA_BUFFER_FACTORY;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.READ;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static org.apache.commons.lang3.StringUtils.substring;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.web.reactive.function.BodyInserters.fromDataBuffers;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * byte operate service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class ByteOperateServiceImpl implements ByteOperateService {

    private static final Logger LOGGER = getLogger(ByteOperateServiceImpl.class);

    private FileUploader localDiskFileUploader;

    private AttachmentService attachmentService;

    private DownloadHistoryService downloadHistoryService;

    private BlueIdentityProcessor blueIdentityProcessor;

    public ByteOperateServiceImpl(FileUploader localDiskFileUploader, AttachmentService attachmentService, DownloadHistoryService downloadHistoryService,
                                  BlueIdentityProcessor blueIdentityProcessor, FileDeploy fileDeploy) {
        this.localDiskFileUploader = localDiskFileUploader;
        this.attachmentService = attachmentService;
        this.downloadHistoryService = downloadHistoryService;
        this.blueIdentityProcessor = blueIdentityProcessor;

        allFileSizeThreshold = fileDeploy.getAllFileSizeThreshold();
        ATTR_NAME = fileDeploy.getAttrName();
        CURRENT_SIZE_THRESHOLD = fileDeploy.getCurrentSizeThreshold();
    }

    private long allFileSizeThreshold;

    private static long CURRENT_SIZE_THRESHOLD;

    private static String ATTR_NAME;

    private final BiFunction<FileUploadResult, Long, Attachment> ATTACHMENT_CONVERTER = (fur, memberId) -> {
        Attachment attachment = new Attachment();

        String destination = fur.getDestination();

        attachment.setLink(destination);
        attachment.setName(fur.getResource());
        attachment.setFileType(substring(destination, lastIndexOf(destination, SCHEME_SEPARATOR.identity) + 1));
        attachment.setSize(fur.getLength());
        attachment.setStatus(VALID.status);
        attachment.setCreateTime(Instant.now().getEpochSecond());
        attachment.setCreator(memberId);
        return attachment;
    };

    private final Consumer<ServerRequest> CONTENT_LENGTH_ASSERTER = serverRequest -> {
        long allFileSize = ofNullable(serverRequest)
                .map(sr -> sr.headers().contentLength().orElse(0L)).orElse(0L);

        if (allFileSize < 1L)
            throw new BlueException(FILE_NOT_EXIST);
        if (allFileSize > allFileSizeThreshold)
            throw new BlueException(PAYLOAD_TOO_LARGE);
    };

    private final BiFunction<MultiValueMap<String, Part>, Long, Mono<List<FileUploadResult>>> ATTACHMENT_UPLOADER = (valueMap, memberId) -> {
        LOGGER.info("ATTACHMENT_UPLOADER, valueMap = {}, memberId = {}", valueMap, memberId);

        List<Part> resources = valueMap.get(ATTR_NAME);
        if (isEmpty(resources))
            throw new BlueException(EMPTY_PARAM);

        int size = resources.size();
        if (size == 1 && "".equals(((FilePart) (resources.get(0))).filename()))
            throw new BlueException(EMPTY_PARAM);

        if (size > CURRENT_SIZE_THRESHOLD)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return fromIterable(resources)
                .flatMap(localDiskFileUploader::upload)
                .collectList()
                .flatMap(uploadedFiles -> {
                    allotByMax(uploadedFiles, (int) DB_WRITE.value, false)
                            .stream()
                            .filter(l -> !isEmpty(l))
                            .forEach(l ->
                                    attachmentService.insertBatch(
                                            l.stream()
                                                    .filter(FileUploadResult::getSuccess)
                                                    .map(fur -> {
                                                        Attachment attachment = ATTACHMENT_CONVERTER.apply(fur, memberId);
                                                        attachment.setId(blueIdentityProcessor.generate(Attachment.class));
                                                        return attachment;
                                                    }).collect(toList()))
                            );
                    LOGGER.info("uploadedFiles -> " + uploadedFiles);
                    return just(uploadedFiles);
                });
    };

    public static final UnaryOperator<String> FILE_TYPE_GETTER = CommonFunctions.FILE_TYPE_GETTER;

    private static final MediaType DEFAULT_MEDIA_TYPE = MediaType.APPLICATION_OCTET_STREAM;
    private static final Map<String, MediaType> FILE_MEDIA_MAPPING = Stream.of(BlueFileType.values())
            .collect(Collectors.toMap(bft -> bft.identity, bft -> bft.mediaType, (a, b) -> a));

    private static final Function<String, MediaType> MEDIA_GETTER = suffix -> {
        MediaType mediaType = FILE_MEDIA_MAPPING.get(suffix);
        return mediaType != null ? mediaType : DEFAULT_MEDIA_TYPE;
    };

    private final Function<String, Flux<DataBuffer>> DATA_BUFFERS_READER = pathname -> {
        try {
            return DataBufferUtils.readByteChannel(() -> FileChannel.open(new File(pathname).toPath(), READ), DATA_BUFFER_FACTORY, BUFFER_SIZE);
        } catch (Exception e) {
            throw new BlueException(INTERNAL_SERVER_ERROR);
        }
    };

    private final BiConsumer<Long, Long> HISTORY_RECORDER = (attachmentId, memberId) -> {
        DownloadHistory downloadHistory = new DownloadHistory();
        downloadHistory.setId(blueIdentityProcessor.generate(DownloadHistory.class));
        downloadHistory.setAttachmentId(attachmentId);
        downloadHistory.setCreator(memberId);
        downloadHistory.setCreateTime(TIME_STAMP_GETTER.get());

        downloadHistoryService.insert(downloadHistory);
    };

    /**
     * upload
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> upload(ServerRequest serverRequest) {
        CONTENT_LENGTH_ASSERTER.accept(serverRequest);
        return zip(serverRequest.multipartData()
                        .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        ATTACHMENT_UPLOADER.apply(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(rl ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(generate(OK.code, rl, serverRequest), BlueResponse.class));
    }

    /**
     * download
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> download(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(IdentityParam.class)
                        .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> {
                    Long attachmentId = tuple2.getT1().getId();
                    return attachmentService.getAttachment(attachmentId)
                            .switchIfEmpty(error(() -> new BlueException(DATA_NOT_EXIST)))
                            .flatMap(attachment -> {
                                        String link = attachment.getLink();
                                        MediaType mediaType = MEDIA_GETTER.apply(FILE_TYPE_GETTER.apply(link));

                                        return ok().contentType(mediaType)
                                                .header(CONTENT_DISPOSITION.name,
                                                        "attachment; filename=" + URLEncoder.encode(attachment.getName(), UTF_8))
                                                .body(fromDataBuffers(DATA_BUFFERS_READER.apply(link)))
                                                .doOnSuccess(res -> HISTORY_RECORDER.accept(attachmentId, tuple2.getT2().getId()));
                                    }
                            );
                });
    }

}
