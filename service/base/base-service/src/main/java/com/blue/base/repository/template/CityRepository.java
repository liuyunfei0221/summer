package com.blue.base.repository.template;

import com.blue.base.repository.entity.City;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * city repository
 *
 * @author liuyunfei
 */
public interface CityRepository extends ReactiveMongoRepository<City, Long> {
}