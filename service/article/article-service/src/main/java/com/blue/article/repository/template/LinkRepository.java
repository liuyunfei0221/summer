package com.blue.article.repository.template;

import com.blue.article.repository.entity.Link;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * link repository
 *
 * @author liuyunfei
 */
public interface LinkRepository extends ReactiveMongoRepository<Link, Long> {
}