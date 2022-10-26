package com.blue.agreement.repository.template;

import com.blue.agreement.repository.entity.Agreement;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * agreement repository
 *
 * @author liuyunfei
 */
@Repository
public interface AgreementRepository extends ReactiveCrudRepository<Agreement, Long> {
}