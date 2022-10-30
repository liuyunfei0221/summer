package com.blue.marketing.repository.template;

import com.blue.marketing.repository.entity.EventRecord;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * event record repository
 *
 * @author liuyunfei
 */
public interface EventRecordRepository extends ReactiveMongoRepository<EventRecord, Long> {
}