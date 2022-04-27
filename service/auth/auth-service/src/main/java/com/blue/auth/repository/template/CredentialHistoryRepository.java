package com.blue.auth.repository.template;

import com.blue.auth.repository.entity.CredentialHistory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * credential history repository
 *
 * @author liuyunfei
 */
public interface CredentialHistoryRepository extends ReactiveMongoRepository<CredentialHistory, Long> {
}