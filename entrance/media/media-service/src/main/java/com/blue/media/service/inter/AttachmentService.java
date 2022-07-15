package com.blue.media.service.inter;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.media.api.model.AttachmentDetailInfo;
import com.blue.media.api.model.AttachmentInfo;
import com.blue.media.model.AttachmentCondition;
import com.blue.media.repository.entity.Attachment;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;

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
    Mono<Attachment> insertAttachment(Attachment attachment);

    /**
     * insert attachment batch
     *
     * @param attachments
     * @return
     */
    Mono<List<Attachment>> insertAttachments(List<Attachment> attachments);

    /**
     * get attachment mono by id
     *
     * @param id
     * @return
     */
    Mono<Attachment> getAttachmentMono(Long id);

    /**
     * get attachment info mono by id
     *
     * @param id
     * @return
     */
    Mono<AttachmentInfo> getAttachmentInfoMono(Long id);

    /**
     * select attachment info mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<AttachmentInfo>> selectAttachmentInfoMonoByIds(List<Long> ids);

    /**
     * select attachments detail info mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<AttachmentDetailInfo>> selectAttachmentDetailInfoMonoByIds(List<Long> ids);

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
     * select attachment detail info by page and member id
     *
     * @param pageModelRequest
     * @param memberId
     * @return
     */
    Mono<PageModelResponse<AttachmentDetailInfo>> selectAttachmentDetailInfoByPageAndMemberId(PageModelRequest<Void> pageModelRequest, Long memberId);

    /**
     * select attachment by page and condition
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    Mono<List<Attachment>> selectAttachmentMonoByLimitAndQuery(Long limit, Long rows, Query query);

    /**
     * count attachment by condition
     *
     * @param query
     * @return
     */
    Mono<Long> countAttachmentMonoByQuery(Query query);

    /**
     * select attachment detail info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<AttachmentDetailInfo>> selectAttachmentDetailInfoPageMonoByPageAndCondition(PageModelRequest<AttachmentCondition> pageModelRequest);

}
