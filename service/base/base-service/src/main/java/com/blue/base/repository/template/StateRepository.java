package com.blue.base.repository.template;

import com.blue.base.repository.entity.State;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * state repository
 *
 * @author liuyunfei
 */
public interface StateRepository extends ReactiveMongoRepository<State, Long> {
}