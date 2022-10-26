package com.blue.agreement.service.inter;

import com.blue.agreement.api.model.AgreementInfo;
import com.blue.agreement.model.AgreementCondition;
import com.blue.agreement.model.AgreementInsertParam;
import com.blue.agreement.model.AgreementManagerInfo;
import com.blue.agreement.model.AgreementUpdateParam;
import com.blue.agreement.repository.entity.Agreement;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

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
    Mono<AgreementInfo> insertAgreement(AgreementInsertParam agreementInsertParam, Long operatorId);

    /**
     * update agreement
     *
     * @param agreementUpdateParam
     * @param operatorId
     * @return
     */
    Mono<AgreementInfo> updateAgreement(AgreementUpdateParam agreementUpdateParam, Long operatorId);

    /**
     * delete agreement
     *
     * @param id
     * @return
     */
    Mono<AgreementInfo> deleteAgreement(Long id);

    /**
     * expire agreement info
     *
     * @return
     */
    void invalidAgreementInfosCache();

    /**
     * get agreement by id
     *
     * @param id
     * @return
     */
    Optional<Agreement> getAgreement(Long id);

    /**
     * get agreement mono by id
     *
     * @param id
     * @return
     */
    Mono<Agreement> getAgreementMono(Long id);

    /**
     * get agreement by type
     *
     * @param agreementType
     * @return
     */
    Mono<Agreement> getAgreementByType(Integer agreementType);

    /**
     * get agreement info
     *
     * @param agreementType
     * @return
     */
    Mono<AgreementInfo> getAgreementInfoMonoByTypeWithCache(Integer agreementType);

    /**
     * select agreement by page and condition
     *
     * @param limit
     * @param rows
     * @param agreementCondition
     * @return
     */
    Mono<List<Agreement>> selectAgreementMonoByLimitAndCondition(Long limit, Long rows, AgreementCondition agreementCondition);

    /**
     * count agreement by condition
     *
     * @param agreementCondition
     * @return
     */
    Mono<Long> countAgreementMonoByCondition(AgreementCondition agreementCondition);

    /**
     * select agreement info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<AgreementManagerInfo>> selectAgreementManagerInfoPageMonoByPageAndCondition(PageModelRequest<AgreementCondition> pageModelRequest);

}
