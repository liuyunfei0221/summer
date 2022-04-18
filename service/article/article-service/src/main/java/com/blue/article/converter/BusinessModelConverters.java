package com.blue.article.converter;

import com.blue.article.repository.entity.Article;
import com.blue.article.repository.entity.Link;
import com.blue.base.model.exps.BlueException;
import com.blue.article.model.ArticleInsertParam;
import com.blue.article.model.LinkInsertParam;

import java.time.Instant;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.Status.VALID;
import static java.util.Optional.ofNullable;

/**
 * model converters in business project
 *
 * @author liuyunfei
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class BusinessModelConverters {

    public static final Function<ArticleInsertParam, Article> ARTICLE_INSERT_PARAM_2_ARTICLE = articleInsertParam -> {
        if (isNull(articleInsertParam))
            throw new BlueException(EMPTY_PARAM);
        articleInsertParam.asserts();

        long epochSecond = Instant.now().getEpochSecond();

        Article article = new Article();
        article.setTitle(articleInsertParam.getTitle());
        article.setType(articleInsertParam.getType());
        article.setContent(articleInsertParam.getContent());

        article.setFavorites(0L);
        article.setReadings(0L);
        article.setComments(0L);
        article.setLikes(0L);
        article.setBoring(0L);
        article.setCreateTime(epochSecond);
        article.setUpdateTime(epochSecond);
        article.setStatus(VALID.status);

        return article;
    };

    public static final Function<LinkInsertParam, Link> LINK_INSERT_PARAM_2_LINK = linkInsertParam -> {
        if (isNull(linkInsertParam))
            throw new BlueException(EMPTY_PARAM);
        linkInsertParam.asserts();

        long epochSecond = Instant.now().getEpochSecond();
        Link link = new Link();

        link.setLinkUrl(linkInsertParam.getLinkUrl());
        link.setContent(ofNullable(linkInsertParam.getContent()).orElse(""));

        link.setFavorites(0L);
        link.setReadings(0L);
        link.setComments(0L);
        link.setLikes(0L);
        link.setBoring(0L);

        link.setStatus(VALID.status);
        link.setCreateTime(epochSecond);

        return link;
    };

}
