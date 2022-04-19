package com.blue.article.repository.template;

import com.blue.article.repository.entity.Reply;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * reply repository
 *
 * @author liuyunfei
 */
public interface ReplyRepository extends ReactiveMongoRepository<Reply, Long> {
}