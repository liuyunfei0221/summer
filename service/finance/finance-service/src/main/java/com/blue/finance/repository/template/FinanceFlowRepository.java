package com.blue.finance.repository.template;

import com.blue.finance.repository.entity.FinanceFlow;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * finance flow repository
 *
 * @author liuyunfei
 */
public interface FinanceFlowRepository extends ReactiveMongoRepository<FinanceFlow, Long> {
}