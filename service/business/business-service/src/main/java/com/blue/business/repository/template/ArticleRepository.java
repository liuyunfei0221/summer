package com.blue.business.repository.template;

import com.blue.business.repository.entity.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author liuyunfei
 * @date 2021/9/23
 * @apiNote
 */
public interface ArticleRepository extends ElasticsearchRepository<Article, Long> {
}
