package com.blue.business.service.impl;

import com.blue.base.common.base.ConstantProcessor;
import com.blue.base.model.exps.BlueException;
import com.blue.business.api.model.ArticleInfo;
import com.blue.business.api.model.LinkInfo;
import com.blue.business.api.model.ReadingInfo;
import com.blue.business.model.ArticleInsertParam;
import com.blue.business.model.ArticleUpdateParam;
import com.blue.business.repository.entity.Article;
import com.blue.business.repository.entity.Link;
import com.blue.business.service.inter.*;
import com.blue.identity.common.BlueIdentityProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.blue.base.common.base.Asserter.isInvalidIdentity;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Status.VALID;
import static com.blue.base.constant.business.SubjectType.ARTICLE;
import static com.blue.business.converter.BusinessModelConverters.ARTICLE_INSERT_PARAM_2_ARTICLE;
import static com.blue.business.converter.BusinessModelConverters.LINK_INSERT_PARAM_2_LINK;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

/**
 * business service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class BusinessServiceImpl implements BusinessService {

    private final ArticleService articleService;

    private final LinkService linkService;

    private final CommonService commonService;

    private final ReplyService replyService;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final ExecutorService executorService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public BusinessServiceImpl(ArticleService articleService, LinkService linkService, CommonService commonService,
                               ReplyService replyService, BlueIdentityProcessor blueIdentityProcessor, ExecutorService executorService) {
        this.articleService = articleService;
        this.linkService = linkService;
        this.commonService = commonService;
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
                                                error(new BlueException(DATA_NOT_EXIST)))
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
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 15)
    public void insertArticle(ArticleInsertParam articleInsertParam, Long memberId) {
        Article article = ARTICLE_INSERT_PARAM_2_ARTICLE.apply(articleInsertParam);

        long articleId = blueIdentityProcessor.generate(Article.class);
        article.setId(articleId);
        article.setAuthorId(memberId);

        ofNullable(articleInsertParam.getLinks())
                .map(lks -> lks.stream()
                        .map(lk -> {
                            Link link = LINK_INSERT_PARAM_2_LINK.apply(lk);
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
