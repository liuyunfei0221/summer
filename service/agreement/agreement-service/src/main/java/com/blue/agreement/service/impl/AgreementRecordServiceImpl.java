package com.blue.agreement.service.impl;

import com.blue.agreement.api.model.AgreementInfo;
import com.blue.agreement.api.model.AgreementRecordInfo;
import com.blue.agreement.model.AgreementRecordInsertParam;
import com.blue.agreement.service.inter.AgreementRecordService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;

import static reactor.util.Loggers.getLogger;

/**
 * agreement record service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class AgreementRecordServiceImpl implements AgreementRecordService {

    private static final Logger LOGGER = getLogger(AgreementRecordServiceImpl.class);

    /**
     * sign agreement
     *
     * @param agreementRecordInsertParam
     * @param memberId
     * @return
     */
    @Override
    public Mono<AgreementRecordInfo> insertAgreementRecord(AgreementRecordInsertParam agreementRecordInsertParam, Long memberId) {
        return null;
    }

    /**
     * select agreement unsigned
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<AgreementInfo>> selectNewestAgreementInfosUnsigned(Long memberId) {
        return null;
    }

}
