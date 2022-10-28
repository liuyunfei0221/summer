package com.blue.agreement.service.inter;

import com.blue.agreement.api.model.AgreementInfo;
import com.blue.agreement.api.model.AgreementRecordInfo;
import com.blue.agreement.model.AgreementRecordCondition;
import com.blue.agreement.model.AgreementRecordInsertParam;
import com.blue.agreement.model.AgreementRecordManagerInfo;
import com.blue.agreement.repository.entity.AgreementRecord;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * agreement record service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface AgreementRecordService {

    /**
     * select agreement unsigned
     *
     * @param memberId
     * @return
     */
    Mono<List<AgreementInfo>> selectNewestAgreementInfosUnsigned(Long memberId);

    /**
     * sign agreement record
     *
     * @param agreementRecordInsertParam
     * @param memberId
     * @return
     */
    Mono<AgreementRecordInfo> insertAgreementRecord(AgreementRecordInsertParam agreementRecordInsertParam, Long memberId);

    /**
     * select agreement record by limit and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    Mono<List<AgreementRecord>> selectAgreementRecordMonoByLimitAndCondition(Long limit, Long rows, Query query);

    /**
     * count agreement record by query
     *
     * @param query
     * @return
     */
    Mono<Long> countAgreementRecordMonoByCondition(Query query);

    /**
     * select agreement record manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<AgreementRecordManagerInfo>> selectAgreementRecordManagerInfoPageMonoByPageAndCondition(PageModelRequest<AgreementRecordCondition> pageModelRequest);

}
