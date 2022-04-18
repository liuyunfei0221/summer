package com.blue.article.service.inter;

import com.blue.article.api.model.ArticleInfo;
import com.blue.article.model.ArticleInsertParam;
import com.blue.article.model.ArticleUpdateParam;
import reactor.core.publisher.Mono;

/**
 * business service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnnecessaryInterfaceModifier", "unused"})
public interface ControlService {

    /**
     * get article by id
     *
     * @param id
     * @return
     */
    public Mono<ArticleInfo> getArticle(Long id);

    /**
     * insert article
     *
     * @param articleInsertParam
     * @param memberId
     */
    public void insertArticle(ArticleInsertParam articleInsertParam, Long memberId);

    /**
     * update article
     *
     * @param articleUpdateParam
     */
    public void updateArticle(ArticleUpdateParam articleUpdateParam);

}
