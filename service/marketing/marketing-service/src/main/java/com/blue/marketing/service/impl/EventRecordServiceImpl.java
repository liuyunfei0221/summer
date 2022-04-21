package com.blue.marketing.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.marketing.repository.entity.EventRecord;
import com.blue.marketing.repository.mapper.EventRecordMapper;
import com.blue.marketing.service.inter.EventRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.Logger;

import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.util.Loggers.getLogger;

/**
 * event record service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class EventRecordServiceImpl implements EventRecordService {

    private static final Logger LOGGER = getLogger(EventRecordServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final EventRecordMapper eventRecordMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public EventRecordServiceImpl(BlueIdentityProcessor blueIdentityProcessor, EventRecordMapper eventRecordMapper) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.eventRecordMapper = eventRecordMapper;
    }

    /**
     * insert event record
     *
     * @param eventRecord
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public EventRecord insertEvent(EventRecord eventRecord) {
        LOGGER.info("EventRecord insertEvent(EventRecord eventRecord), eventRecord = {}", eventRecord);
        if (isNull(eventRecord))
            throw new BlueException(EMPTY_PARAM);

        if (isInvalidIdentity(eventRecord.getId()))
            eventRecord.setId(blueIdentityProcessor.generate(EventRecord.class));

        eventRecordMapper.insert(eventRecord);

        return eventRecord;
    }

}
