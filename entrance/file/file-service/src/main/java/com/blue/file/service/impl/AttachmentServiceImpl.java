package com.blue.file.service.impl;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.file.api.model.AttachmentInfo;
import com.blue.file.repository.entity.Attachment;
import com.blue.file.repository.mapper.AttachmentMapper;
import com.blue.file.service.inter.AttachmentService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;

import static com.blue.base.common.base.Asserter.isInvalidIdentity;
import static com.blue.base.common.base.Asserter.isNull;
import static com.blue.base.constant.base.ResponseElement.*;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * attachment service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
@Service
public class AttachmentServiceImpl implements AttachmentService {

    private static final Logger LOGGER = getLogger(AttachmentServiceImpl.class);

    private final AttachmentMapper attachmentMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AttachmentServiceImpl(AttachmentMapper attachmentMapper) {
        this.attachmentMapper = attachmentMapper;
    }

    /**
     * insert attachment
     *
     * @param attachment
     * @return
     */
    @Override
    public void insert(Attachment attachment) {
        LOGGER.info("insert(Attachment attachment), attachment = {}", attachment);
        ofNullable(attachment)
                .ifPresent(attachmentMapper::insert);
    }

    /**
     * insert attachment batch
     *
     * @param attachments
     * @return
     */
    @Override
    public void insertBatch(List<Attachment> attachments) {
        LOGGER.info("insertBatch(List<Attachment> attachments), attachments = {}", attachments);
        ofNullable(attachments)
                .filter(as -> as.size() > 0)
                .ifPresent(attachmentMapper::insertBatch);
    }

    /**
     * get attachment by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Attachment> getAttachment(Long id) {
        LOGGER.info("getAttachment(Long id), id = {}", id);

        if (id == null || id < 1L)
            throw new BlueException(INVALID_IDENTITY);

        Attachment attachment = attachmentMapper.selectByPrimaryKey(id);
        if (attachment == null)
            throw new BlueException(DATA_NOT_EXIST);

        LOGGER.info("attachment = {}", attachment);
        return just(attachment);
    }

    /**
     * select attachment by page and member id
     *
     * @param pageModelRequest
     * @param memberId
     * @return
     */
    @Override
    public Mono<PageModelResponse<AttachmentInfo>> selectAttachmentByPageAndMemberId(PageModelRequest<Void> pageModelRequest, Long memberId) {
        LOGGER.info("listAttachment(PageModelParam<Void> pageModelParam, Long memberId), pageModelDTO = {},memberId = {}", pageModelRequest, memberId);

        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return just(ofNullable(attachmentMapper.countByMemberId(memberId)).orElse(0L))
                .flatMap(count -> {
                    PageModelResponse<AttachmentInfo> pageModelResponse = new PageModelResponse<>();
                    pageModelResponse.setCount(count);
                    pageModelResponse.setList(count > 0L ?
                            ofNullable(attachmentMapper.selectByLimitAndMemberId(memberId, pageModelRequest.getLimit(), pageModelRequest.getRows()))
                                    .orElse(emptyList()).stream().map(a ->
                                            new AttachmentInfo(a.getId(), a.getName(), a.getSize(), a.getCreateTime(), "")
                                    ).collect(toList())
                            : emptyList()
                    );

                    LOGGER.info("pageModelResponse = {}", pageModelResponse);
                    return just(pageModelResponse);
                });
    }
}
