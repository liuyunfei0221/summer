package com.blue.agreement.repository.template;

import com.blue.agreement.repository.entity.AgreementRecord;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * agreement record repository
 *
 * @author liuyunfei
 */
public interface AgreementRecordRepository extends ReactiveMongoRepository<AgreementRecord, Long> {
}