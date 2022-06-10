package com.blue.member.repository.template;

import com.blue.member.repository.entity.Card;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * card repository
 *
 * @author liuyunfei
 */
public interface CardRepository extends ReactiveMongoRepository<Card, Long> {
}