package com.blue.lake.repository.template;

import com.blue.lake.repository.entity.Shine;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * shine repository
 *
 * @author liuyunfei
 * @date 2021/9/16
 * @apiNote
 */
public interface ShineRepository extends ReactiveMongoRepository<Shine, Long> {
}
