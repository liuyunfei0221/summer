package com.blue.base.repository.template;

import com.blue.base.repository.entity.DictType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * dict type repository
 *
 * @author liuyunfei
 * @date 2021/9/16
 * @apiNote
 */
public interface DictTypeRepository extends ReactiveMongoRepository<DictType, Long> {
}