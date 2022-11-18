package com.blue.risk.repository.template;

import com.blue.risk.repository.entity.RiskStrategy;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * risk strategy repository
 *
 * @author liuyunfei
 */
public interface RiskStrategyRepository extends ReactiveMongoRepository<RiskStrategy, Long> {
}