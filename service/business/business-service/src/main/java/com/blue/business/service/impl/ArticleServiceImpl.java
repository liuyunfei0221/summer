package com.blue.business.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.business.repository.entity.Article;
import com.blue.business.repository.mapper.ArticleMapper;
import com.blue.business.repository.template.ArticleRepository;
import com.blue.business.service.inter.ArticleService;
import com.blue.identity.common.BlueIdentityProcessor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.base.common.base.Asserter.isValidIdentity;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * article service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class ArticleServiceImpl implements ArticleService {

    private static final Logger LOGGER = getLogger(ArticleServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final RestHighLevelClient restHighLevelClient;

    private final ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ArticleServiceImpl(BlueIdentityProcessor blueIdentityProcessor, RestHighLevelClient restHighLevelClient, ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.restHighLevelClient = restHighLevelClient;
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
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
            return ofNullable(articleMapper.selectByPrimaryKey(id));

        throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);
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
