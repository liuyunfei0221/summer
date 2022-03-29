package com.blue.base.repository.template;

import com.blue.base.repository.entity.State;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * state repository
 *
 * @author liuyunfei
 * @date 2021/9/16
 * @apiNote
 */
public interface StateRepository extends ReactiveMongoRepository<State, Long> {
}