package com.blue.article.service.impl;

import com.blue.article.repository.entity.Article;
import com.blue.article.repository.template.ArticleRepository;
import com.blue.article.service.inter.ArticleService;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.base.common.base.BlueChecker.isValidIdentity;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

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

    private final RestClient restClient;

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(BlueIdentityProcessor blueIdentityProcessor, RestClient restClient, ArticleRepository articleRepository) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.restClient = restClient;
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
