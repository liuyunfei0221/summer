package com.blue.marketing.service.inter;

import com.blue.marketing.repository.entity.EventRecord;

/**
 * event record service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface EventRecordService {

    /**
     * insert event record
     *
     * @param eventRecord
     * @return
     */
    EventRecord insertEvent(EventRecord eventRecord);

}
