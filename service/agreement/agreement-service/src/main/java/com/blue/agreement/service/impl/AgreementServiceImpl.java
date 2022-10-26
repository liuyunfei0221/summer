package com.blue.agreement.service.impl;

import com.blue.agreement.api.model.AgreementInfo;
import com.blue.agreement.model.AgreementCondition;
import com.blue.agreement.model.AgreementInsertParam;
import com.blue.agreement.model.AgreementManagerInfo;
import com.blue.agreement.model.AgreementUpdateParam;
import com.blue.agreement.repository.entity.Agreement;
import com.blue.agreement.service.inter.AgreementService;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.agreement.converter.AgreementModelConverters.AGREEMENT_2_AGREEMENT_INFO;
import static reactor.util.Loggers.getLogger;

/**
 * agreement service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class AgreementServiceImpl implements AgreementService {

    private static final Logger LOGGER = getLogger(AgreementServiceImpl.class);

//    private final AgreementRepository agreementRepository;
//
//    public AgreementServiceImpl(AgreementRepository agreementRepository) {
//        this.agreementRepository = agreementRepository;
//    }

    @Autowired
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    /**
     * insert agreement
     *
     * @param agreementInsertParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<AgreementInfo> insertAgreement(AgreementInsertParam agreementInsertParam, Long operatorId) {


        return null;
    }

    /**
     * update agreement
     *
     * @param agreementUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<AgreementInfo> updateAgreement(AgreementUpdateParam agreementUpdateParam, Long operatorId) {
        return null;
    }

    /**
     * delete agreement
     *
     * @param id
     * @return
     */
    @Override
    public Mono<AgreementInfo> deleteAgreement(Long id) {
        return null;
    }

    /**
     * expire agreement info
     *
     * @return
     */
    @Override
    public void invalidAgreementInfosCache() {

    }

    /**
     * get agreement by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Agreement> getAgreement(Long id) {
        return Optional.empty();
    }

    /**
     * get agreement mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Agreement> getAgreementMono(Long id) {
        return null;
    }

    /**
     * get agreement by type
     *
     * @param agreementType
     * @return
     */
    @Override
    public Mono<Agreement> getAgreementByType(Integer agreementType) {
        return null;
    }

    /**
     * get agreement info
     *
     * @param agreementType
     * @return
     */
    @Override
    public Mono<AgreementInfo> getAgreementInfoMonoByTypeWithCache(Integer agreementType) {

        Criteria criteria = Criteria.empty();
        criteria.and("type").is(3);

        Query query = Query.query(criteria);

        Mono<Agreement> agreementMono = r2dbcEntityTemplate.selectOne(query, Agreement.class);

        return agreementMono.map(AGREEMENT_2_AGREEMENT_INFO);
    }

    /**
     * select agreement by page and condition
     *
     * @param limit
     * @param rows
     * @param agreementCondition
     * @return
     */
    @Override
    public Mono<List<Agreement>> selectAgreementMonoByLimitAndCondition(Long limit, Long rows, AgreementCondition agreementCondition) {
        return null;
    }

    /**
     * count agreement by condition
     *
     * @param agreementCondition
     * @return
     */
    @Override
    public Mono<Long> countAgreementMonoByCondition(AgreementCondition agreementCondition) {
        return null;
    }

    /**
     * select agreement info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<AgreementManagerInfo>> selectAgreementManagerInfoPageMonoByPageAndCondition(PageModelRequest<AgreementCondition> pageModelRequest) {
        return null;
    }

}
