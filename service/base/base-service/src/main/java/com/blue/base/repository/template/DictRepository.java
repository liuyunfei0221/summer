package com.blue.base.repository.template;

import com.blue.base.repository.entity.Dict;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * dict repository
 *
 * @author liuyunfei
 */
public interface DictRepository extends ReactiveMongoRepository<Dict, Long> {
}