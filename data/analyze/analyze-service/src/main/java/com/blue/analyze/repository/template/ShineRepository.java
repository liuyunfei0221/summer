package com.blue.analyze.repository.template;

import com.blue.analyze.repository.entity.Shine;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * shine repository
 *
 * @author liuyunfei
 */
public interface ShineRepository extends ReactiveMongoRepository<Shine, Long> {
}
