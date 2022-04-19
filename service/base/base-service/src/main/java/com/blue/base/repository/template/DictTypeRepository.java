package com.blue.base.repository.template;

import com.blue.base.repository.entity.DictType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * dict type repository
 *
 * @author liuyunfei
 */
public interface DictTypeRepository extends ReactiveMongoRepository<DictType, Long> {
}