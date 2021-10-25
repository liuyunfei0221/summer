package com.blue.business.service.inter;

import com.blue.business.api.model.ArticleInfo;
import com.blue.business.model.ArticleInsertParam;
import com.blue.business.model.ArticleUpdateParam;
import reactor.core.publisher.Mono;

/**
 * business service
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "UnnecessaryInterfaceModifier", "unused"})
public interface BusinessService {

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
