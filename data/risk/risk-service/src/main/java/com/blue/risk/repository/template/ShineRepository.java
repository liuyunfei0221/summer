package com.blue.risk.repository.template;

import com.blue.risk.repository.entity.Shine;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * shine repository
 *
 * @author liuyunfei
 */
public interface ShineRepository extends ReactiveMongoRepository<Shine, Long> {
}
