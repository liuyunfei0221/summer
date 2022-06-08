package com.blue.media.service.impl;

import com.blue.base.common.base.CommonFunctions;
import com.blue.base.constant.common.BlueFileType;
import com.blue.base.model.common.BlueResponse;
import com.blue.base.model.common.IdentityParam;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.media.api.model.FileUploadResult;
import com.blue.media.component.file.ByteProcessor;
import com.blue.media.config.deploy.FileDeploy;
import com.blue.media.repository.entity.Attachment;
import com.blue.media.repository.entity.DownloadHistory;
import com.blue.media.service.inter.AttachmentService;
import com.blue.media.service.inter.ByteOperateService;
import com.blue.media.service.inter.DownloadHistoryService;
import org.springframework.core.io.buffer.DataBuffer;
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
import java.util.List;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.isNotNull;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.common.BlueHeader.CONTENT_DISPOSITION;
import static com.blue.base.constant.common.BlueNumericalValue.DB_WRITE;
import static com.blue.base.constant.common.ResponseElement.*;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.base.constant.common.Status.VALID;
import static com.blue.base.constant.common.Symbol.SCHEME_SEPARATOR;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static org.apache.commons.lang3.StringUtils.substring;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.web.reactive.function.BodyInserters.fromDataBuffers;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * byte operate service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class ByteOperateServiceImpl implements ByteOperateService {

    private static final Logger LOGGER = getLogger(ByteOperateServiceImpl.class);

    private ByteProcessor byteProcessor;

    private AttachmentService attachmentService;

    private DownloadHistoryService downloadHistoryService;

    private BlueIdentityProcessor blueIdentityProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ByteOperateServiceImpl(ByteProcessor byteProcessor, AttachmentService attachmentService, DownloadHistoryService downloadHistoryService,
                                  BlueIdentityProcessor blueIdentityProcessor, FileDeploy fileDeploy) {
        this.byteProcessor = byteProcessor;
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

        attachment.setId(blueIdentityProcessor.generate(Attachment.class));

        String destination = fur.getDestination();
        attachment.setLink(destination);
        attachment.setName(fur.getResource());
        attachment.setFileType(substring(destination, lastIndexOf(destination, SCHEME_SEPARATOR.identity) + 1));
        attachment.setSize(fur.getLength());
        attachment.setStatus(VALID.status);
        attachment.setCreateTime(TIME_STAMP_GETTER.get());
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

    private final Function<MultiValueMap<String, Part>, Mono<List<FileUploadResult>>> ATTACHMENTS_UPLOADER = valueMap -> {
        LOGGER.info("ATTACHMENT_UPLOADER, valueMap = {}", valueMap);

        List<Part> resources = valueMap.get(ATTR_NAME);
        if (isEmpty(resources))
            throw new BlueException(EMPTY_PARAM);

        int size = resources.size();
        if (size == 1 && EMPTY_DATA.value.equals(((FilePart) (resources.get(0))).filename()))
            throw new BlueException(EMPTY_PARAM);

        if (size > CURRENT_SIZE_THRESHOLD)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return fromIterable(resources)
                .flatMap(byteProcessor::write)
                .collectList();
    };

    private final Function<String, Flux<DataBuffer>> ATTACHMENTS_DOWNLOADER = link -> {
        LOGGER.info("ATTACHMENTS_DOWNLOADER, link = {}", link);

        return byteProcessor.read(new File(link).toPath());
    };

    private final BiConsumer<List<FileUploadResult>, Long> ATTACHMENTS_RECORDER = (rls, memberId) -> {
        LOGGER.info("ATTACHMENTS_RECORDER, rls = {}, memberId = {}", rls, memberId);

        allotByMax(rls, (int) DB_WRITE.value, false)
                .stream()
                .filter(l -> !isEmpty(l))
                .map(l -> l.stream()
                        .filter(FileUploadResult::getSuccess)
                        .map(fur -> ATTACHMENT_CONVERTER.apply(fur, memberId)
                        ).collect(toList()))
                .forEach(attachmentService::insertAttachments);
    };

    public static final UnaryOperator<String> FILE_TYPE_GETTER = CommonFunctions.FILE_TYPE_GETTER;

    private static final MediaType DEFAULT_MEDIA_TYPE = APPLICATION_OCTET_STREAM;
    private static final Map<String, MediaType> FILE_MEDIA_MAPPING = Stream.of(BlueFileType.values())
            .collect(Collectors.toMap(bft -> bft.identity, bft -> bft.mediaType, (a, b) -> a));

    private static final Function<String, MediaType> MEDIA_GETTER = suffix -> {
        MediaType mediaType = FILE_MEDIA_MAPPING.get(suffix);
        return isNotNull(mediaType) ? mediaType : DEFAULT_MEDIA_TYPE;
    };

    private final BiConsumer<Long, Long> DOWNLOAD_RECORDER = (attachmentId, memberId) -> {
        DownloadHistory downloadHistory = new DownloadHistory();
        downloadHistory.setId(blueIdentityProcessor.generate(DownloadHistory.class));
        downloadHistory.setAttachmentId(attachmentId);
        downloadHistory.setCreator(memberId);
        downloadHistory.setCreateTime(TIME_STAMP_GETTER.get());

        downloadHistoryService.insertDownloadHistory(downloadHistory)
                .doOnError(throwable -> LOGGER.info("downloadHistoryService.insert(downloadHistory) failed, downloadHistory = {}, throwable = {}", downloadHistory, throwable))
                .subscribe(dh -> LOGGER.info("DOWNLOAD_RECORDER -> insert(downloadHistory), dh = {}", dh));
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
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        zip(ATTACHMENTS_UPLOADER.apply(tuple2.getT1()), just(tuple2.getT2().getId())))
                .flatMap(tuple2 -> {
                    List<FileUploadResult> rl = tuple2.getT1();
                    return ok()
                            .contentType(APPLICATION_JSON)
                            .body(generate(OK.code, rl, serverRequest), BlueResponse.class)
                            .doOnSuccess(res -> ATTACHMENTS_RECORDER.accept(rl, tuple2.getT2()));
                });
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
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> {
                    Long attachmentId = tuple2.getT1().getId();
                    return attachmentService.getAttachmentMono(attachmentId)
                            .flatMap(attOpt ->
                                    just(attOpt.orElseThrow(() -> new BlueException(DATA_NOT_EXIST))))
                            .flatMap(attachment -> {
                                String link = attachment.getLink();

                                return ok().contentType(MEDIA_GETTER.apply(FILE_TYPE_GETTER.apply(link)))
                                        .header(CONTENT_DISPOSITION.name,
                                                "attachment; filename=" + URLEncoder.encode(attachment.getName(), UTF_8))
                                        .body(fromDataBuffers(ATTACHMENTS_DOWNLOADER.apply(link)))
                                        .doOnSuccess(res -> DOWNLOAD_RECORDER.accept(attachmentId, tuple2.getT2().getId()));
                            });
                });
    }

}
