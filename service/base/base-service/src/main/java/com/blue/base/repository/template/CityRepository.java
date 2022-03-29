package com.blue.base.repository.template;

import com.blue.base.repository.entity.City;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * city repository
 *
 * @author liuyunfei
 * @date 2021/9/16
 * @apiNote
 */
public interface CityRepository extends ReactiveMongoRepository<City, Long> {
}