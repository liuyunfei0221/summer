package com.blue.article.service.inter;

import com.blue.article.repository.entity.Article;

import java.util.List;
import java.util.Optional;

/**
 * article service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnnecessaryInterfaceModifier", "unused"})
public interface ArticleService {

    /**
     * get article by id
     *
     * @param id
     * @return
     */
    public Optional<Article> getByPrimaryKey(Long id);

    /**
     * list article by ids
     *
     * @param ids
     * @return
     */
    public List<Article> selectByIds(List<Long> ids);

    /**
     * insert article
     *
     * @param article
     */
    public void insert(Article article);

    /**
     * update article
     *
     * @param article
     */
    public void update(Article article);

}
