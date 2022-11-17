package com.blue.media.service.inter;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.media.api.model.MessageTemplateInfo;
import com.blue.media.model.MessageTemplateCondition;
import com.blue.media.model.MessageTemplateInsertParam;
import com.blue.media.model.MessageTemplateManagerInfo;
import com.blue.media.model.MessageTemplateUpdateParam;
import com.blue.media.repository.entity.MessageTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * message template service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue", "unused"})
public interface MessageTemplateService {

    /**
     * insert message template
     *
     * @param messageTemplateInsertParam
     * @param operatorId
     * @return
     */
    Mono<MessageTemplateInfo> insertMessageTemplate(MessageTemplateInsertParam messageTemplateInsertParam, Long operatorId);

    /**
     * update message template
     *
     * @param messageTemplateUpdateParam
     * @param operatorId
     * @return
     */
    Mono<MessageTemplateInfo> updateMessageTemplate(MessageTemplateUpdateParam messageTemplateUpdateParam, Long operatorId);

    /**
     * delete message template
     *
     * @param id
     * @return
     */
    Mono<MessageTemplateInfo> deleteMessageTemplate(Long id);

    /**
     * get message template mono by id
     *
     * @param id
     * @return
     */
    Mono<MessageTemplate> getMessageTemplate(Long id);

    /**
     * get message template mono by types
     *
     * @param type
     * @param businessType
     * @return
     */
    Mono<MessageTemplateInfo> getMessageTemplateInfoByTypes(Integer type, Integer businessType);

    /**
     * select message template by limit and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    Mono<List<MessageTemplate>> selectMessageTemplateByLimitAndCondition(Long limit, Long rows, Query query);

    /**
     * count message template by query
     *
     * @param query
     * @return
     */
    Mono<Long> countMessageTemplateByCondition(Query query);

    /**
     * select message template manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<MessageTemplateManagerInfo>> selectMessageTemplateManagerInfoPageByPageAndCondition(PageModelRequest<MessageTemplateCondition> pageModelRequest);

}
