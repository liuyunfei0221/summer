package com.blue.agreement.service.inter;

import com.blue.agreement.api.model.AgreementInfo;
import com.blue.agreement.api.model.AgreementRecordInfo;
import com.blue.agreement.model.AgreementRecordInsertParam;
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
     * sign agreement
     *
     * @param agreementRecordInsertParam
     * @param memberId
     * @return
     */
    Mono<AgreementRecordInfo> insertAgreementRecord(AgreementRecordInsertParam agreementRecordInsertParam, Long memberId);

    /**
     * select agreement unsigned
     *
     * @param memberId
     * @return
     */
    Mono<List<AgreementInfo>> selectNewestAgreementInfosUnsigned(Long memberId);

}
