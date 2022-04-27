package com.blue.media.service.inter;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.media.api.model.AttachmentInfo;
import com.blue.media.model.AttachmentCondition;
import com.blue.media.repository.entity.Attachment;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * attachment service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue", "unused"})
public interface AttachmentService {

    /**
     * insert attachment
     *
     * @param attachment
     * @return
     */
    int insert(Attachment attachment);

    /**
     * insert attachment batch
     *
     * @param attachments
     * @return
     */
    int insertBatch(List<Attachment> attachments);

    /**
     * get attachment by id
     *
     * @param id
     * @return
     */
    Optional<Attachment> getAttachment(Long id);

    /**
     * get attachment mono by id
     *
     * @param id
     * @return
     */
    Mono<Optional<Attachment>> getAttachmentMono(Long id);

    /**
     * select attachments by ids
     *
     * @param ids
     * @return
     */
    List<Attachment> selectAttachmentByIds(List<Long> ids);

    /**
     * select attachments mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<Attachment>> selectAttachmentMonoByIds(List<Long> ids);

    /**
     * select attachment by limit and member id
     *
     * @param limit
     * @param rows
     * @param memberId
     * @return
     */
    Mono<List<Attachment>> selectAttachmentMonoByLimitAndMemberId(Long limit, Long rows, Long memberId);

    /**
     * count attachment by member id
     *
     * @param memberId
     * @return
     */
    Mono<Long> countAttachmentMonoByMemberId(Long memberId);

    /**
     * select attachment info by page and member id
     *
     * @param pageModelRequest
     * @param memberId
     * @return
     */
    Mono<PageModelResponse<AttachmentInfo>> selectAttachmentInfoByPageAndMemberId(PageModelRequest<Void> pageModelRequest, Long memberId);

    /**
     * select attachment by page and condition
     *
     * @param limit
     * @param rows
     * @param attachmentCondition
     * @return
     */
    Mono<List<Attachment>> selectAttachmentMonoByLimitAndCondition(Long limit, Long rows, AttachmentCondition attachmentCondition);

    /**
     * count attachment by condition
     *
     * @param attachmentCondition
     * @return
     */
    Mono<Long> countAttachmentMonoByCondition(AttachmentCondition attachmentCondition);

    /**
     * select attachment info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<AttachmentInfo>> selectAttachmentInfoPageMonoByPageAndCondition(PageModelRequest<AttachmentCondition> pageModelRequest);

}
