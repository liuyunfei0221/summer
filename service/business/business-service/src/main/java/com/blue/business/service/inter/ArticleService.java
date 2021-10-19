package com.blue.business.service.inter;

import com.blue.business.repository.entity.Article;

import java.util.List;
import java.util.Optional;

/**
 * article service
 *
 * @author DarkBlue
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
