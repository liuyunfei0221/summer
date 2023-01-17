package com.blue.media.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.common.BlueFileType;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.media.api.model.AttachmentUploadInfo;
import com.blue.media.api.model.FileUploadResult;
import com.blue.media.api.model.UploadResultSummary;
import com.blue.media.common.MediaCommonFunctions;
import com.blue.media.component.file.ByteProcessor;
import com.blue.media.config.deploy.LocalDiskFileDeploy;
import com.blue.media.repository.entity.Attachment;
import com.blue.media.repository.entity.DownloadHistory;
import com.blue.media.service.inter.AttachmentService;
import com.blue.media.service.inter.ByteOperateService;
import com.blue.media.service.inter.DownloadHistoryService;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.blue.basic.common.base.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.common.base.ConstantProcessor.assertAttachmentType;
import static com.blue.basic.common.base.PathVariableGetter.getLongVariableReact;
import static com.blue.basic.constant.common.BlueBoolean.FALSE;
import static com.blue.basic.constant.common.BlueBoolean.TRUE;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_WRITE;
import static com.blue.basic.constant.common.BluePrefix.CONTENT_DISPOSITION_FILE_NAME_PREFIX;
import static com.blue.basic.constant.common.PathVariable.ID;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Status.VALID;
import static com.blue.basic.constant.common.Symbol.PERIOD;
import static com.blue.media.converter.MediaModelConverters.ATTACHMENTS_2_ATTACHMENT_UPLOAD_INFOS_CONVERTER;
import static com.blue.media.converter.MediaModelConverters.ATTACHMENT_2_ATTACHMENT_UPLOAD_INFO_CONVERTER;
import static java.lang.Integer.parseInt;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static org.apache.commons.lang3.StringUtils.substring;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
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

    public ByteOperateServiceImpl(ByteProcessor byteProcessor, AttachmentService attachmentService, DownloadHistoryService downloadHistoryService,
                                  BlueIdentityProcessor blueIdentityProcessor, LocalDiskFileDeploy localDiskFileDeploy) {
        this.byteProcessor = byteProcessor;
        this.attachmentService = attachmentService;
        this.downloadHistoryService = downloadHistoryService;
        this.blueIdentityProcessor = blueIdentityProcessor;

        allFileSizeThreshold = localDiskFileDeploy.getAllFileSizeThreshold();

        String attrName = localDiskFileDeploy.getAttrName();
        String typeName = localDiskFileDeploy.getTypeName();
        if (isBlank(attrName) || isBlank(typeName))
            throw new RuntimeException("attrName or typeName can't be blank");

        ATTR_NAME = attrName;
        TYPE_NAME = typeName;
        CURRENT_SIZE_THRESHOLD = localDiskFileDeploy.getCurrentSizeThreshold();
    }

    private long allFileSizeThreshold;

    private static long CURRENT_SIZE_THRESHOLD;

    private static String ATTR_NAME;
    private static String TYPE_NAME;

    private final BiFunction<FileUploadResult, Long, Attachment> ATTACHMENT_CONVERTER = (fur, mid) -> {
        Attachment attachment = new Attachment();

        attachment.setId(blueIdentityProcessor.generate(Attachment.class));

        String destination = fur.getDestination();
        attachment.setType(fur.getType());
        attachment.setLink(destination);
        attachment.setName(fur.getResource());
        attachment.setFileType(substring(destination, lastIndexOf(destination, PERIOD.identity) + 1));
        attachment.setSize(fur.getLength());
        attachment.setStatus(VALID.status);
        attachment.setCreateTime(TIME_STAMP_GETTER.get());
        attachment.setCreator(mid);

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

    private static final Function<List<Part>, Integer> TYPE_PARSER = parts -> {
        if (isEmpty(parts))
            throw new BlueException(EMPTY_PARAM);

        try {
            FormFieldPart formFieldPart = (FormFieldPart) parts.get(0);
            return parseInt(formFieldPart.value());
        } catch (Exception e) {
            throw new BlueException(BAD_REQUEST);
        }
    };

    private final BiFunction<MultiValueMap<String, Part>, Long, Mono<List<FileUploadResult>>> ATTACHMENTS_UPLOADER = (valueMap, memberId) -> {
        LOGGER.info("valueMap = {}", valueMap);

        Integer type = TYPE_PARSER.apply(valueMap.get(TYPE_NAME));
        assertAttachmentType(type, false);

        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        List<Part> resources = valueMap.get(ATTR_NAME);
        if (isEmpty(resources))
            throw new BlueException(EMPTY_PARAM);
        if (resources.size() > CURRENT_SIZE_THRESHOLD)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return fromIterable(resources)
                .flatMap(part -> byteProcessor.write(part, type, memberId))
                .collectList();
    };

    private final BiFunction<String, Long, Flux<DataBuffer>> ATTACHMENTS_DOWNLOADER = (link, memberId) -> {
        LOGGER.info("link = {}", link);

        return byteProcessor.read(link, memberId);
    };

    private final BiFunction<List<FileUploadResult>, Long, Mono<UploadResultSummary>> ATTACHMENTS_RECORDER = (fileUploadResults, memberId) -> {
        LOGGER.info("fileUploadResults = {}, memberId = {}", fileUploadResults, memberId);

        UploadResultSummary uploadResultSummary = new UploadResultSummary();

        List<AttachmentUploadInfo> success = uploadResultSummary.getSuccess();
        List<FileUploadResult> fail = uploadResultSummary.getFail();

        Map<Boolean, List<FileUploadResult>> booleanListMap = fileUploadResults.stream().collect(Collectors.groupingBy(FileUploadResult::getSuccess));
        ofNullable(booleanListMap.get(FALSE.bool)).filter(BlueChecker::isNotEmpty).ifPresent(fail::addAll);

        ofNullable(booleanListMap.get(TRUE.bool)).filter(BlueChecker::isNotEmpty).ifPresent(results ->
                success.addAll(allotByMax(results, (int) DB_WRITE.value, false)
                        .parallelStream()
                        .filter(l -> !isEmpty(l))
                        .map(l -> l.stream()
                                .filter(FileUploadResult::getSuccess)
                                .map(fur -> ATTACHMENT_CONVERTER.apply(fur, memberId)).collect(toList()))
                        .map(attachments ->
                                ATTACHMENTS_2_ATTACHMENT_UPLOAD_INFOS_CONVERTER.apply(attachmentService.insertAttachments(attachments).toFuture().join())
                        )
                        .flatMap(List::stream)
                        .collect(toList())));

        return just(uploadResultSummary);
    };

    private final BiFunction<FileUploadResult, Long, Mono<AttachmentUploadInfo>> ATTACHMENT_RECORDER = (fileUploadResult, memberId) -> {
        LOGGER.info("fileUploadResult = {}, memberId = {}", fileUploadResult, memberId);

        if (isNull(fileUploadResult) || isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        return fileUploadResult.getSuccess()
                ?
                attachmentService.insertAttachment(ATTACHMENT_CONVERTER.apply(fileUploadResult, memberId))
                        .map(ATTACHMENT_2_ATTACHMENT_UPLOAD_INFO_CONVERTER)
                :
                error(() -> new BlueException(BAD_REQUEST));
    };

    public static final UnaryOperator<String> FILE_TYPE_GETTER = MediaCommonFunctions.FILE_TYPE_GETTER;

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
                .doOnError(throwable -> LOGGER.info("downloadHistory = {}, throwable = {}", downloadHistory, throwable))
                .subscribe(dh -> LOGGER.info("DOWNLOAD_RECORDER -> insert(downloadHistory), dh = {}", dh));
    };

    private static final Function<Attachment, String> CONTENT_DISPOSITION_GEN = attachment ->
            ofNullable(attachment)
                    .map(Attachment::getName)
                    .filter(BlueChecker::isNotBlank)
                    .map(name -> URLEncoder.encode(name, UTF_8))
                    .map(encodedName -> CONTENT_DISPOSITION_FILE_NAME_PREFIX.prefix + encodedName)
                    .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));

    /**
     * upload
     *
     * @param bytes
     * @param type
     * @param memberId
     * @param originalName
     * @param descName
     * @return
     */
    @Override
    public Mono<AttachmentUploadInfo> upload(byte[] bytes, Integer type, Long memberId, String originalName, String descName) {
        return byteProcessor.write(bytes, type, memberId, originalName, descName)
                .flatMap(fur -> ATTACHMENT_RECORDER.apply(fur, memberId));
    }

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
                .flatMap(tuple2 -> {
                    long memberId = tuple2.getT2().getId();
                    return zip(ATTACHMENTS_UPLOADER.apply(tuple2.getT1(), memberId), just(memberId));
                })
                .flatMap(tuple2 ->
                        ATTACHMENTS_RECORDER.apply(tuple2.getT1(), tuple2.getT2()))
                .flatMap(summary ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(summary), BlueResponse.class)
                );
    }

    /**
     * download
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> download(ServerRequest serverRequest) {
        return zip(getLongVariableReact(serverRequest, ID.key)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> {
                    Long attachmentId = tuple2.getT1();
                    long memberId = tuple2.getT2().getId();
                    return attachmentService.getAttachment(attachmentId)
                            .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                            .flatMap(attachment -> {
                                String link = attachment.getLink();

                                return ok().contentType(MEDIA_GETTER.apply(FILE_TYPE_GETTER.apply(link)))
                                        .header(CONTENT_DISPOSITION, CONTENT_DISPOSITION_GEN.apply(attachment))
                                        .body(fromDataBuffers(ATTACHMENTS_DOWNLOADER.apply(link, memberId)))
                                        .doOnSuccess(res -> DOWNLOAD_RECORDER.accept(attachmentId, memberId));
                            });
                });
    }

}
