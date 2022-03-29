package com.blue.base.repository.template;

import com.blue.base.repository.entity.Country;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * country repository
 *
 * @author liuyunfei
 * @date 2021/9/16
 * @apiNote
 */
public interface CountryRepository extends ReactiveMongoRepository<Country, Long> {
}