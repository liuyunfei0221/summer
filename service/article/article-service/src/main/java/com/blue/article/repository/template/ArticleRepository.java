package com.blue.article.repository.template;

import com.blue.article.repository.entity.Article;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * article repository
 *
 * @author liuyunfei
 */
public interface ArticleRepository extends ReactiveMongoRepository<Article, Long> {
}