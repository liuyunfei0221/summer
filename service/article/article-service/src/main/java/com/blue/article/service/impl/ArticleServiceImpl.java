package com.blue.article.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import com.blue.article.repository.entity.Article;
import com.blue.article.repository.template.ArticleRepository;
import com.blue.article.service.inter.ArticleService;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.blue.basic.common.base.BlueChecker.isValidIdentity;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * article service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "FieldCanBeLocal"})
@Service
public class ArticleServiceImpl implements ArticleService {

    private static final Logger LOGGER = getLogger(ArticleServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final ElasticsearchAsyncClient elasticsearchAsyncClient;

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ElasticsearchAsyncClient elasticsearchAsyncClient, ArticleRepository articleRepository) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.elasticsearchAsyncClient = elasticsearchAsyncClient;
        this.articleRepository = articleRepository;
    }

    /**
     * get article by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Article> getByPrimaryKey(Long id) {
        LOGGER.info("id = {}", id);
        if (isValidIdentity(id))
            return ofNullable(articleRepository.findById(id).toFuture().join());

        throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * list article by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<Article> selectByIds(List<Long> ids) {
        return null;
    }

    /**
     * insert article
     *
     * @param article
     */
    @Override
    public void insert(Article article) {

    }

    /**
     * update article
     *
     * @param article
     */
    @Override
    public void update(Article article) {

    }

}
