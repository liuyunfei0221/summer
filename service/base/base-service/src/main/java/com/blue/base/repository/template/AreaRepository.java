package com.blue.base.repository.template;

import com.blue.base.repository.entity.Area;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * area repository
 *
 * @author liuyunfei
 * @date 2021/9/16
 * @apiNote
 */
public interface AreaRepository extends ReactiveMongoRepository<Area, Long> {
}