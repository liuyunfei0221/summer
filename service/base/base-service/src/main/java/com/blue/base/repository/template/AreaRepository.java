package com.blue.base.repository.template;

import com.blue.base.repository.entity.Area;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * area repository
 *
 * @author liuyunfei
 */
public interface AreaRepository extends ReactiveMongoRepository<Area, Long> {
}