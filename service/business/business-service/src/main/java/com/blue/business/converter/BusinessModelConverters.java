package com.blue.business.converter;

import com.blue.business.model.ArticleInsertParam;
import com.blue.business.model.LinkInsertParam;
import com.blue.business.repository.entity.Article;
import com.blue.business.repository.entity.Link;

import java.time.Instant;
import java.util.function.Function;

import static com.blue.base.common.base.ConstantProcessor.assertArticleType;
import static com.blue.base.constant.base.CommonException.BAD_REQUEST_EXP;
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
            throw BAD_REQUEST_EXP.exp;

        Integer type = articleInsertParam.getType();
        assertArticleType(type, false);

        String content = articleInsertParam.getContent();
        if (content == null || "".equals(content))
            throw BAD_REQUEST_EXP.exp;

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
            throw BAD_REQUEST_EXP.exp;

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
