package com.blue.verify.service.inter;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.verify.api.model.VerifyTemplateInfo;
import com.blue.verify.model.VerifyTemplateCondition;
import com.blue.verify.model.VerifyTemplateInsertParam;
import com.blue.verify.model.VerifyTemplateManagerInfo;
import com.blue.verify.model.VerifyTemplateUpdateParam;
import com.blue.verify.repository.entity.VerifyTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * verify template service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface VerifyTemplateService {

    /**
     * insert verify template
     *
     * @param verifyTemplateInsertParam
     * @param operatorId
     * @return
     */
    Mono<VerifyTemplateInfo> insertVerifyTemplate(VerifyTemplateInsertParam verifyTemplateInsertParam, Long operatorId);

    /**
     * update verify template
     *
     * @param verifyTemplateUpdateParam
     * @param operatorId
     * @return
     */
    Mono<VerifyTemplateInfo> updateVerifyTemplate(VerifyTemplateUpdateParam verifyTemplateUpdateParam, Long operatorId);

    /**
     * delete verify template
     *
     * @param id
     * @return
     */
    Mono<VerifyTemplateInfo> deleteVerifyTemplate(Long id);

    /**
     * get verify template mono by id
     *
     * @param id
     * @return
     */
    Mono<VerifyTemplate> getVerifyTemplateMono(Long id);

    /**
     * get verify template mono by type and business type and languages
     *
     * @param type
     * @param businessType
     * @param languages
     * @return
     */
    Mono<VerifyTemplateInfo> getVerifyTemplateInfoMonoByTypesAndLanguages(String type, String businessType, List<String> languages);

    /**
     * select verify template by limit and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    Mono<List<VerifyTemplate>> selectVerifyTemplateMonoByLimitAndCondition(Long limit, Long rows, Query query);

    /**
     * count verify template by query
     *
     * @param query
     * @return
     */
    Mono<Long> countVerifyTemplateMonoByCondition(Query query);

    /**
     * select verify template manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<VerifyTemplateManagerInfo>> selectVerifyTemplateManagerInfoPageMonoByPageAndCondition(PageModelRequest<VerifyTemplateCondition> pageModelRequest);

}
