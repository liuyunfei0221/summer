package com.blue.business.converter;

import com.blue.base.common.base.ConstantProcessor;
import com.blue.base.model.exps.BlueException;
import com.blue.business.api.model.ArticleInsertParam;
import com.blue.business.api.model.LinkInsertParam;
import com.blue.business.repository.entity.Article;
import com.blue.business.repository.entity.Link;

import java.time.Instant;
import java.util.function.Function;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.Status.VALID;
import static java.util.Optional.ofNullable;

/**
 * model converters in business project
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class BusinessModelConverters {

    public static final Function<ArticleInsertParam, Article> ARTICLE_INSERT_PARAM_2_ARTICLE = articleInsertParam -> {
        String title = articleInsertParam.getTitle();
        if (title == null || "".equals(title))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "title can't be blank");

        Integer type = articleInsertParam.getType();
        ConstantProcessor.assertArticleType(type);

        String content = articleInsertParam.getContent();
        if (content == null || "".equals(content))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "content can't be blank");

        long epochSecond = Instant.now().getEpochSecond();

        Article article = new Article();
        article.setTitle(title);
        article.setType(type);
        article.setContent(content);

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

        String linkUrl = linkInsertParam.getLinkUrl();
        if (linkUrl == null || "".equals(linkUrl))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "linkUrl can't be blank");

        long epochSecond = Instant.now().getEpochSecond();
        Link link = new Link();

        link.setLinkUrl(linkUrl);
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
