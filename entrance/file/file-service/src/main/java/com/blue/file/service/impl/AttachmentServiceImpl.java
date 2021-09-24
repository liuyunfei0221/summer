package com.blue.file.service.impl;

import com.blue.base.model.base.PageModelParam;
import com.blue.base.model.base.PageModelResult;
import com.blue.base.model.exps.BlueException;
import com.blue.file.api.model.AttachmentInfo;
import com.blue.file.repository.entity.Attachment;
import com.blue.file.repository.mapper.AttachmentMapper;
import com.blue.file.service.inter.AttachmentService;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import java.util.List;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.FILE_NOT_EXIST;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static reactor.util.Loggers.getLogger;

/**
 * 附件业务实现
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
     * 新增附件
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
     * 批量新增附件
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
     * 根据文件id获取文件
     *
     * @param id
     * @return
     */
    @Override
    public Attachment getAttachment(Long id) {
        LOGGER.info("getAttachment(Long id), id = {}", id);

        if (id == null || id < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        Attachment attachment = attachmentMapper.selectByPrimaryKey(id);
        if (attachment == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, FILE_NOT_EXIST.message);

        LOGGER.info("attachment = {}", attachment);
        return attachment;
    }

    /**
     * 分页查询附件对应用户的附件列表
     *
     * @param pageModelParam
     * @param memberId
     * @return
     */
    @Override
    public PageModelResult<AttachmentInfo> listAttachment(PageModelParam<Void> pageModelParam, Long memberId) {
        LOGGER.info("listAttachment(PageModelParam<Void> pageModelParam, Long memberId), pageModelDTO = {},memberId = {}", pageModelParam, memberId);

        if (memberId == null || memberId < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        Long page = pageModelParam.getPage();
        Long rows = pageModelParam.getRows();

        Long count = ofNullable(attachmentMapper.countAttachment(memberId)).orElse(0L);

        PageModelResult<AttachmentInfo> pageModelResult = new PageModelResult<>();
        pageModelResult.setCount(count);
        pageModelResult.setList(0L < count ?
                ofNullable(attachmentMapper.listAttachmentByLimit(memberId, (page - 1L) * rows, rows))
                        .orElse(emptyList()).stream().map(a ->
                                new AttachmentInfo(a.getId(), a.getName(), a.getSize(), a.getCreateTime(), "")
                        ).collect(toList())
                : emptyList()
        );

        LOGGER.info("pageModelVO = {}", pageModelResult);
        return pageModelResult;
    }
}
