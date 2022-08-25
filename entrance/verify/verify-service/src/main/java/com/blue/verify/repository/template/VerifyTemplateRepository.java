package com.blue.verify.repository.template;

import com.blue.verify.repository.entity.VerifyTemplate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * qr code config repository
 *
 * @author liuyunfei
 */
public interface VerifyTemplateRepository extends ReactiveMongoRepository<VerifyTemplate, Long> {
}