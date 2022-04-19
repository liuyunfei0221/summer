package com.blue.article.repository.template;

import com.blue.article.repository.entity.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * comment repository
 *
 * @author liuyunfei
 */
public interface CommentRepository extends ReactiveMongoRepository<Comment, Long> {
}