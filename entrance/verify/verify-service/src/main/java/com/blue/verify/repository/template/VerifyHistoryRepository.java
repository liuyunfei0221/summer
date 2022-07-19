package com.blue.verify.repository.template;

import com.blue.verify.repository.entity.VerifyHistory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * verify history repository
 *
 * @author liuyunfei
 */
public interface VerifyHistoryRepository extends ReactiveMongoRepository<VerifyHistory, Long> {
}