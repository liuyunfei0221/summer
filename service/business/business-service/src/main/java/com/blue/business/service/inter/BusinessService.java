package com.blue.business.service.inter;

import com.blue.business.api.model.ArticleInfo;
import com.blue.business.api.model.ArticleInsertParam;
import com.blue.business.api.model.ArticleUpdateParam;
import reactor.core.publisher.Mono;

/**
 * 文章门面业务接口
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "UnnecessaryInterfaceModifier", "unused"})
public interface BusinessService {

    /**
     * 获取文章详情
     *
     * @param id
     * @return
     */
    public Mono<ArticleInfo> getArticle(Long id);

    /**
     * 添加文章
     *
     * @param articleInsertParam
     * @param memberId
     */
    public void insertArticle(ArticleInsertParam articleInsertParam, Long memberId);

    /**
     * 添加文章
     *
     * @param articleUpdateParam
     */
    public void updateArticle(ArticleUpdateParam articleUpdateParam);

}
