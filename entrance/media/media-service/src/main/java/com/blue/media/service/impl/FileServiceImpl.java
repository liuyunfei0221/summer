package com.blue.media.service.impl;

import com.blue.base.constant.base.Status;
import com.blue.base.model.exps.BlueException;
import com.blue.media.api.model.FileUploadResult;
import com.blue.media.component.file.inter.FileUploader;
import com.blue.media.config.deploy.FileDeploy;
import com.blue.media.repository.entity.Attachment;
import com.blue.media.repository.entity.DownloadHistory;
import com.blue.media.service.inter.AttachmentService;
import com.blue.media.service.inter.DownloadHistoryService;
import com.blue.media.service.inter.FileService;
import com.blue.identity.common.BlueIdentityProcessor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.Instant;
import java.util.List;
import java.util.function.BiFunction;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.constant.base.BlueNumericalValue.DB_WRITE;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.PAYLOAD_TOO_LARGE;
import static com.blue.base.constant.base.Symbol.SCHEME_SEPARATOR;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static org.apache.commons.lang3.StringUtils.substring;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * media service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = getLogger(FileServiceImpl.class);

    private final FileUploader localDiskFileUploader;

    private final AttachmentService attachmentService;

    private final DownloadHistoryService downloadHistoryService;

    private final BlueIdentityProcessor blueIdentityProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public FileServiceImpl(FileUploader localDiskFileUploader, AttachmentService attachmentService, DownloadHistoryService downloadHistoryService, BlueIdentityProcessor blueIdentityProcessor, FileDeploy fileDeploy) {
        this.localDiskFileUploader = localDiskFileUploader;
        this.attachmentService = attachmentService;
        this.downloadHistoryService = downloadHistoryService;
        this.blueIdentityProcessor = blueIdentityProcessor;

        ATTR_NAME = fileDeploy.getAttrName();
        CURRENT_SIZE_THRESHOLD = fileDeploy.getCurrentSizeThreshold();
    }

    private static long CURRENT_SIZE_THRESHOLD;

    private static String ATTR_NAME;

    private final BiFunction<FileUploadResult, Long, Attachment> ATTACHMENT_CONVERTER = (fur, memberId) -> {
        Attachment attachment = new Attachment();

        String destination = fur.getDestination();

        attachment.setLink(destination);
        attachment.setName(fur.getResource());
        attachment.setFileType(substring(destination, lastIndexOf(destination, SCHEME_SEPARATOR.identity) + 1));
        attachment.setSize(fur.getLength());
        attachment.setStatus(Status.VALID.status);
        attachment.setCreateTime(Instant.now().getEpochSecond());
        attachment.setCreator(memberId);
        return attachment;
    };

    /**
     * upload attachment
     *
     * @param valueMap
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<FileUploadResult>> uploadAttachment(MultiValueMap<String, Part> valueMap, Long memberId) {
        LOGGER.info("uploadAttachment(MultiValueMap<String, Part> valueMap, Long memberId), valueMap = {}, memberId = {}", valueMap, memberId);

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
                                            l
                                                    .stream()
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

    }


    /**
     * download attachment
     *
     * @param attachmentId
     * @param memberId
     * @return
     */
    @Override
    public Mono<Attachment> getAttachmentForDownload(Long attachmentId, Long memberId) {
        LOGGER.info("getAttachmentForDownload(Long attachmentId, Long memberId), attachmentId = {}, memberId = {}", attachmentId, memberId);
        return attachmentService.getAttachment(attachmentId)
                .flatMap(attachment -> {

                    DownloadHistory downloadHistory = new DownloadHistory();
                    downloadHistory.setId(blueIdentityProcessor.generate(DownloadHistory.class));
                    downloadHistory.setAttachmentId(attachmentId);
                    downloadHistory.setCreator(memberId);
                    downloadHistory.setCreateTime(Instant.now().getEpochSecond());
                    downloadHistoryService.insert(downloadHistory);

                    return just(attachment);
                });
    }

}
