package com.blue.media.service.inter;

import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
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
     * select attachments mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<AttachmentInfo>> selectAttachmentMonoByIds(List<Long> ids);

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
     * select attachment info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<AttachmentInfo>> selectAttachmentInfoPageMonoByPageAndCondition(PageModelRequest<AttachmentCondition> pageModelRequest);

}
