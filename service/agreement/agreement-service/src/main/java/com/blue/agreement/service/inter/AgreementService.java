package com.blue.agreement.service.inter;

import com.blue.agreement.api.model.AgreementInfo;
import com.blue.agreement.model.AgreementCondition;
import com.blue.agreement.model.AgreementInsertParam;
import com.blue.agreement.model.AgreementManagerInfo;
import com.blue.agreement.repository.entity.Agreement;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * agreement service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface AgreementService {

    /**
     * insert agreement
     *
     * @param agreementInsertParam
     * @param operatorId
     * @return
     */
    AgreementInfo insertAgreement(AgreementInsertParam agreementInsertParam, Long operatorId);

    /**
     * expire agreement info
     *
     * @return
     */
    void invalidAgreementInfosCache();

    /**
     * get agreement mono by id
     *
     * @param id
     * @return
     */
    Mono<Agreement> getAgreement(Long id);

    /**
     * get newest agreement by type
     *
     * @param agreementType
     * @return
     */
    Mono<Agreement> getNewestAgreementByType(Integer agreementType);

    /**
     * get newest agreement info
     *
     * @param agreementType
     * @return
     */
    Mono<AgreementInfo> getNewestAgreementInfoByTypeWithCache(Integer agreementType);

    /**
     * select all newest agreement info
     *
     * @return
     */
    Mono<List<AgreementInfo>> selectNewestAgreementInfosByAllTypeWithCache();

    /**
     * select agreements by ids
     *
     * @param ids
     * @return
     */
    Mono<List<AgreementInfo>> selectAgreementInfoByIds(List<Long> ids);

    /**
     * select agreement by page and condition
     *
     * @param limit
     * @param rows
     * @param agreementCondition
     * @return
     */
    Mono<List<Agreement>> selectAgreementByLimitAndCondition(Long limit, Long rows, AgreementCondition agreementCondition);

    /**
     * count agreement by condition
     *
     * @param agreementCondition
     * @return
     */
    Mono<Long> countAgreementByCondition(AgreementCondition agreementCondition);

    /**
     * select agreement info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<AgreementManagerInfo>> selectAgreementManagerInfoPageByPageAndCondition(PageModelRequest<AgreementCondition> pageModelRequest);

}
