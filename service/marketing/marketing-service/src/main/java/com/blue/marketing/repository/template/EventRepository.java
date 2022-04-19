package com.blue.marketing.repository.template;

import com.blue.marketing.repository.entity.Event;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * event repository
 *
 * @author liuyunfei
 */
public interface EventRepository extends ReactiveMongoRepository<Event, Long> {
}