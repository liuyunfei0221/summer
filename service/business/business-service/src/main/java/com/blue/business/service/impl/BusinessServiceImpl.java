package com.blue.business.service.impl;

import com.blue.base.common.base.ConstantProcessor;
import com.blue.base.constant.business.SubjectType;
import com.blue.base.model.exps.BlueException;
import com.blue.business.api.model.*;
import com.blue.business.converter.BusinessModelConverters;
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

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;
import static com.blue.base.constant.base.Status.VALID;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

/**
 * 文章门面业务实现
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
     * 获取文章详情
     *
     * @param id
     * @return
     */
    @Override
    public Mono<ArticleInfo> getArticle(Long id) {
        if (id == null || id < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        //TODO 查询es
        CompletableFuture<Optional<Article>> articleOptCf = CompletableFuture
                .supplyAsync(() -> articleService.getByPrimaryKey(id), executorService);

        CompletableFuture<List<Link>> linksCf = CompletableFuture
                .supplyAsync(() -> linkService.listBySubIdAndSubType(id, SubjectType.ARTICLE.identity), executorService);

        return
                just(articleOptCf)
                        .flatMap(aoc ->
                                just(aoc.join()))
                        .flatMap(articleOpt ->
                                articleOpt
                                        .map(Mono::just)
                                        .orElseGet(() ->
                                                error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "文章不存在")))
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
     * 添加文章
     *
     * @param articleInsertParam
     * @param memberId
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 15)
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
                            link.setSubType(SubjectType.ARTICLE.identity);
                            link.setSubAuthorId(memberId);

                            return link;
                        }).collect(toList()))
                .ifPresent(linkService::insertBatch);

        articleService.insert(article);

        //TODO 同步es
    }

    /**
     * 修改文章
     *
     * @param articleUpdateParam
     */
    @Override
    public void updateArticle(ArticleUpdateParam articleUpdateParam) {

    }

}
