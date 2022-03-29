package com.blue.base.repository.template;

import com.blue.base.repository.entity.Dict;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * dict repository
 *
 * @author liuyunfei
 * @date 2021/9/16
 * @apiNote
 */
public interface DictRepository extends ReactiveMongoRepository<Dict, Long> {
}