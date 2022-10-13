package com.blue.media.repository.template;

import com.blue.media.repository.entity.MessageTemplate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * message template repository
 *
 * @author liuyunfei
 */
public interface MessageTemplateRepository extends ReactiveMongoRepository<MessageTemplate, Long> {
}