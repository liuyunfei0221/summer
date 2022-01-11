package com.blue.lake.service.impl;

import com.blue.base.model.base.DataEvent;
import com.blue.base.model.base.LimitModelRequest;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.lake.converter.LakeModelConverters;
import com.blue.lake.repository.entity.OptEvent;
import com.blue.lake.repository.mapper.OptEventMapper;
import com.blue.lake.service.inter.LakeService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;

import static com.blue.base.constant.base.BlueNumericalValue.LIMIT;
import static com.blue.base.constant.base.BlueNumericalValue.ROWS;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * lake service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class LakeServiceImpl implements LakeService {

    private static final Logger LOGGER = getLogger(LakeServiceImpl.class);

    private final OptEventMapper optEventMapper;

    private final BlueIdentityProcessor blueIdentityProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public LakeServiceImpl(OptEventMapper optEventMapper, BlueIdentityProcessor blueIdentityProcessor) {
        this.optEventMapper = optEventMapper;
        this.blueIdentityProcessor = blueIdentityProcessor;
    }

    /**
     * insert event
     *
     * @param dataEvent
     */
    @Override
    public void insertEvent(DataEvent dataEvent) {
        OptEvent optEvent = LakeModelConverters.DATA_EVENT_2_OPT_EVENT.apply(dataEvent);
        optEvent.setId(blueIdentityProcessor.generate(OptEvent.class));
        optEventMapper.insert(optEvent);
    }

    /**
     * select by limit
     *
     * @param limitModelRequest
     * @return
     */
    @Override
    public Mono<List<OptEvent>> selectByLimitAndRows(LimitModelRequest<Void> limitModelRequest) {
        return limitModelRequest != null ?
                just(optEventMapper.selectByLimitAndRows(limitModelRequest.getLimit(), limitModelRequest.getRows()))
                :
                just(optEventMapper.selectByLimitAndRows(LIMIT.value, ROWS.value));
    }

}
