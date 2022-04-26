package com.blue.article.service.impl;

import com.blue.article.api.model.ArticleInfo;
import com.blue.article.api.model.LinkInfo;
import com.blue.article.api.model.ReadingInfo;
import com.blue.article.converter.BusinessModelConverters;
import com.blue.article.model.ArticleInsertParam;
import com.blue.article.model.ArticleUpdateParam;
import com.blue.article.repository.entity.Article;
import com.blue.article.repository.entity.Link;
import com.blue.article.service.inter.*;
import com.blue.base.common.base.ConstantProcessor;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.constant.article.SubjectType.ARTICLE;
import static com.blue.base.constant.base.ResponseElement.DATA_NOT_EXIST;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static com.blue.base.constant.base.Status.VALID;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

/**
 * business service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "FieldCanBeLocal"})
@Service
public class ControlServiceImpl implements ControlService {

    private final ArticleService articleService;

    private final LinkService linkService;

    private final CommentService commentService;

    private final ReplyService replyService;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final ExecutorService executorService;

    public ControlServiceImpl(ArticleService articleService, LinkService linkService, CommentService commentService,
                              ReplyService replyService, BlueIdentityProcessor blueIdentityProcessor, ExecutorService executorService) {
        this.articleService = articleService;
        this.linkService = linkService;
        this.commentService = commentService;
        this.replyService = replyService;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.executorService = executorService;
    }

    /**
     * get article by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<ArticleInfo> getArticle(Long id) {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        //TODO es
        CompletableFuture<Optional<Article>> articleOptCf = CompletableFuture
                .supplyAsync(() -> articleService.getByPrimaryKey(id), executorService);

        CompletableFuture<List<Link>> linksCf = CompletableFuture
                .supplyAsync(() -> linkService.selectBySubIdAndSubType(id, ARTICLE.identity), executorService);

        return
                just(articleOptCf)
                        .flatMap(aoc ->
                                just(aoc.join()))
                        .flatMap(articleOpt ->
                                articleOpt
                                        .map(Mono::just)
                                        .orElseGet(() ->
                                                error(() -> new BlueException(DATA_NOT_EXIST)))
                        )
                        .flatMap(article ->
                                {
                                    Integer type = article.getType();
                                    return just(new ArticleInfo(article.getId(), article.getTitle(), article.getAuthorId(),
                                            article.getAuthor(), type, ConstantProcessor.getArticleTypeByIdentity(type).disc,
                                            article.getCreateTime(), article.getUpdateTime(), article.getContent(),
                                            new ReadingInfo(article.getFavorites(), article.getReadings(),
                                                    article.getComments(), article.getLikes(), article.getBoring()),
                                            linksCf.join().stream()
                                                    .filter(lk -> VALID.status == lk.getStatus())
                                                    .map(lk ->
                                                            new LinkInfo(lk.getId(), lk.getLinkUrl(), lk.getContent(), lk.getFavorites(),
                                                                    lk.getReadings(), lk.getComments(), lk.getLikes(), lk.getBoring())
                                                    ).collect(toList())
                                    ));
                                }
                        );
    }

    /**
     * insert article
     *
     * @param articleInsertParam
     * @param memberId
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public void insertArticle(ArticleInsertParam articleInsertParam, Long memberId) {
        Article article = BusinessModelConverters.ARTICLE_INSERT_PARAM_2_ARTICLE.apply(articleInsertParam);

        long articleId = blueIdentityProcessor.generate(Article.class);
        article.setId(articleId);
        article.setAuthorId(memberId);

        ofNullable(articleInsertParam.getLinks())
                .map(lks -> lks.stream()
                        .map(lk -> {
                            Link link = BusinessModelConverters.LINK_INSERT_PARAM_2_LINK.apply(lk);
                            link.setId(blueIdentityProcessor.generate(Link.class));
                            link.setSubId(articleId);
                            link.setSubType(ARTICLE.identity);
                            link.setSubAuthorId(memberId);

                            return link;
                        }).collect(toList()))
                .ifPresent(linkService::insertBatch);

        articleService.insert(article);

        //TODO es
    }

    /**
     * update article
     *
     * @param articleUpdateParam
     */
    @Override
    public void updateArticle(ArticleUpdateParam articleUpdateParam) {

    }

}
