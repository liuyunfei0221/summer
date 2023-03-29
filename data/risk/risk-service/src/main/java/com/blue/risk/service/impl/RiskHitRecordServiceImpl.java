package com.blue.risk.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.ConditionCountResponse;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.risk.constant.RiskHitRecordSortAttribute;
import com.blue.risk.model.RiskHitRecordCondition;
import com.blue.risk.repository.entity.RiskHitRecord;
import com.blue.risk.repository.mapper.RiskHitRecordMapper;
import com.blue.risk.service.inter.RiskHitRecordService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ComparisonType.GREATER_THAN;
import static com.blue.basic.constant.common.ComparisonType.LESS_THAN;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.SortType.DESC;
import static com.blue.database.common.ConditionSortProcessor.process;
import static com.blue.database.common.SearchAfterProcessor.parseSearchAfter;
import static com.blue.risk.constant.RiskHitRecordSortAttribute.CURSOR;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.*;

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
                        riskHitRecordMapper.insertBatch(
                                rs.stream().filter(BlueChecker::isNotNull)
                                        .peek(record -> {
                                            if (isInvalidIdentity(record.getId()))
                                                record.setId(blueIdentityProcessor.generate(RiskHitRecord.class));
                                        }).collect(toList()));
                        return true;
                    })
                    .onErrorResume(t -> {
                        LOGGER.error("RECORDS_INSERTER failed, t = {}", t.getMessage());
                        return just(false);
                    })
                    .switchIfEmpty(defer(() -> just(false)));

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(RiskHitRecordSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<RiskHitRecordCondition> CONDITION_PROCESSOR = c -> {
        RiskHitRecordCondition rc = isNotNull(c) ? c : new RiskHitRecordCondition();

        process(rc, SORT_ATTRIBUTE_MAPPING, RiskHitRecordSortAttribute.STAMP.column);

        return rc;
    };

    /**
     * insert records
     *
     * @param riskHitRecords
     * @return
     */
    @Override
    public Mono<Boolean> insertRiskHitRecords(List<RiskHitRecord> riskHitRecords) {
        LOGGER.info("riskHitRecords = {}", riskHitRecords);

        return RECORDS_INSERTER.apply(riskHitRecords);
    }

    /**
     * select by search after
     *
     * @param scrollModelRequest
     * @return
     */
    @Override
    public Mono<ScrollModelResponse<RiskHitRecord, Long>> selectRiskHitRecordScrollByConditionAndCursor(ScrollModelRequest<RiskHitRecordCondition, Long> scrollModelRequest) {
        LOGGER.info("scrollModelRequest = {}", scrollModelRequest);
        if (isNull(scrollModelRequest))
            throw new BlueException(EMPTY_PARAM);

        RiskHitRecordCondition riskHitRecordCondition = CONDITION_PROCESSOR.apply(scrollModelRequest.getCondition());

        String comparison = DESC.identity.equals(riskHitRecordCondition.getSortType()) ? LESS_THAN.identity : GREATER_THAN.identity;

        List<RiskHitRecord> hitRecords = riskHitRecordMapper.selectBySearchAfterAndCondition(scrollModelRequest.getRows(), riskHitRecordCondition, CURSOR.column, comparison, scrollModelRequest.getCursor());

        return isNotEmpty(hitRecords) ?
                just(new ScrollModelResponse<>(hitRecords, parseSearchAfter(hitRecords, DESC.identity, RiskHitRecord::getId)))
                :
                just(new ScrollModelResponse<>(emptyList()));
    }

    /**
     * count by condition
     *
     * @param riskHitRecordCondition
     * @return
     */
    @Override
    public Mono<ConditionCountResponse<RiskHitRecordCondition>> countRiskHitRecordByCondition(RiskHitRecordCondition riskHitRecordCondition) {
        LOGGER.info("riskHitRecordCondition = {}", riskHitRecordCondition);
        if (isNull(riskHitRecordCondition))
            throw new BlueException(EMPTY_PARAM);

        RiskHitRecordCondition countCondition = CONDITION_PROCESSOR.apply(riskHitRecordCondition);

        return justOrEmpty(riskHitRecordMapper.countByCondition(countCondition))
                .switchIfEmpty(defer(() -> just(0L)))
                .map(count -> new ConditionCountResponse<>(riskHitRecordCondition, count));
    }

}