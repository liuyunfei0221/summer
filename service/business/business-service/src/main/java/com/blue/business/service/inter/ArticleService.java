package com.blue.business.service.inter;

import com.blue.business.repository.entity.Article;

import java.util.List;
import java.util.Optional;

/**
 * 文章业务接口
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "UnnecessaryInterfaceModifier", "unused"})
public interface ArticleService {

    /**
     * 根据主键查询文章信息
     *
     * @param id
     * @return
     */
    public Optional<Article> getByPrimaryKey(Long id);

    /**
     * 根据主键集批量查询文章信息
     *
     * @param ids
     * @return
     */
    public List<Article> listByIds(List<Long> ids);

    /**
     * 添加文章
     *
     * @param article
     */
    public void insert(Article article);

    /**
     * 修改文章
     *
     * @param article
     */
    public void update(Article article);

}
