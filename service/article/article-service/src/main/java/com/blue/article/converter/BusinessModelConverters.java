package com.blue.article.converter;

import com.blue.article.model.ArticleInsertParam;
import com.blue.article.model.LinkInsertParam;
import com.blue.article.repository.entity.Article;
import com.blue.article.repository.entity.Link;
import com.blue.base.model.exps.BlueException;

import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.base.constant.common.Status.VALID;
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

        Long stamp = TIME_STAMP_GETTER.get();

        Article article = new Article();
        article.setTitle(articleInsertParam.getTitle());
        article.setType(articleInsertParam.getType());
        article.setContent(articleInsertParam.getContent());

        article.setFavorites(0L);
        article.setReadings(0L);
        article.setComments(0L);
        article.setLikes(0L);
        article.setBoring(0L);
        article.setCreateTime(stamp);
        article.setUpdateTime(stamp);
        article.setStatus(VALID.status);

        return article;
    };

    public static final Function<LinkInsertParam, Link> LINK_INSERT_PARAM_2_LINK = linkInsertParam -> {
        if (isNull(linkInsertParam))
            throw new BlueException(EMPTY_PARAM);
        linkInsertParam.asserts();

        Long stamp = TIME_STAMP_GETTER.get();
        Link link = new Link();

        link.setLinkUrl(linkInsertParam.getLinkUrl());
        link.setContent(ofNullable(linkInsertParam.getContent()).orElse(EMPTY_DATA.value));

        link.setFavorites(0L);
        link.setReadings(0L);
        link.setComments(0L);
        link.setLikes(0L);
        link.setBoring(0L);

        link.setStatus(VALID.status);
        link.setCreateTime(stamp);

        return link;
    };

}
