package com.blue.lake.service.impl;

import com.blue.base.model.common.DataEvent;
import com.blue.base.model.common.LimitModelRequest;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.lake.repository.entity.OptEvent;
import com.blue.lake.repository.mapper.OptEventMapper;
import com.blue.lake.service.inter.LakeService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;

import static com.blue.base.constant.common.BlueNumericalValue.LIMIT;
import static com.blue.base.constant.common.BlueNumericalValue.ROWS;
import static com.blue.lake.converter.LakeModelConverters.DATA_EVENT_2_OPT_EVENT;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * lake service impl
 *
 * @author liuyunfei
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
        LOGGER.info("void insertEvent(DataEvent dataEvent), dataEvent = {}", dataEvent);

        OptEvent optEvent = DATA_EVENT_2_OPT_EVENT.apply(dataEvent);
        optEvent.setId(blueIdentityProcessor.generate(OptEvent.class));

        //TODO delete
        LOGGER.warn("optEvent = {}", optEvent);
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