package com.blue.risk.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.risk.constant.RiskHitRecordSortAttribute;
import com.blue.risk.repository.entity.RiskHitRecord;
import com.blue.risk.repository.mapper.RiskHitRecordMapper;
import com.blue.risk.service.inter.RiskHitRecordService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNotEmpty;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.database.common.SearchAfterProcessor.parseSearchAfter;
import static java.util.Collections.emptyList;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * risk hit record service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class RiskHitRecordServiceImpl implements RiskHitRecordService {

    private static final Logger LOGGER = getLogger(RiskHitRecordServiceImpl.class);

    private BlueIdentityProcessor blueIdentityProcessor;

    private RiskHitRecordMapper riskHitRecordMapper;

    public RiskHitRecordServiceImpl(BlueIdentityProcessor blueIdentityProcessor, RiskHitRecordMapper riskHitRecordMapper) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.riskHitRecordMapper = riskHitRecordMapper;
    }

    private final Function<List<RiskHitRecord>, Mono<Boolean>> RECORDS_INSERTER = records ->
            justOrEmpty(records)
                    .filter(BlueChecker::isNotEmpty)
                    .map(rs -> {
                        try {
                            for (RiskHitRecord record : records)
                                if (BlueChecker.isInvalidIdentity(record.getId()))
                                    record.setId(blueIdentityProcessor.generate(RiskHitRecord.class));

                            riskHitRecordMapper.insertBatch(rs);
                            return true;
                        } catch (Exception e) {
                            LOGGER.info("riskHitRecordMapper.insertBatch() failed, records = {}, e = {}", records, e);
                            return false;
                        }
                    }).switchIfEmpty(defer(() -> just(false)));

    @Override
    public Mono<Boolean> insertRiskHitRecords(List<RiskHitRecord> riskHitRecords) {
        LOGGER.info("riskHitRecords = {}", riskHitRecords);

        return RECORDS_INSERTER.apply(riskHitRecords);
    }

    @Override
    public Mono<ScrollModelResponse<RiskHitRecord, Long>> selectRiskHitRecordScrollByScrollAndCursor(ScrollModelRequest<Void, Long> scrollModelRequest) {
        LOGGER.info("scrollModelRequest = {}", scrollModelRequest);
        if (isNull(scrollModelRequest))
            throw new BlueException(EMPTY_PARAM);

        List<RiskHitRecord> hitRecords = riskHitRecordMapper.selectByRowsAndSearchAfter(scrollModelRequest.getRows(), scrollModelRequest.getCursor());

        return isNotEmpty(hitRecords) ?
                just(new ScrollModelResponse<>(hitRecords, parseSearchAfter(hitRecords, RiskHitRecordSortAttribute.ID.column, RiskHitRecord::getId)))
                :
                just(new ScrollModelResponse<>(emptyList(), null));
    }
}
