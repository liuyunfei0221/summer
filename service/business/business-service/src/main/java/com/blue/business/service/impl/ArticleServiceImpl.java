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

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * 文章业务实现
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
     * 根据主键查询文章信息
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Article> getByPrimaryKey(Long id) {
        if (id == null || id < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        LOGGER.info("id = {}", id);
        return ofNullable(articleMapper.selectByPrimaryKey(id));
    }

    /**
     * 根据主键集批量查询文章信息
     *
     * @param ids
     * @return
     */
    @Override
    public List<Article> listByIds(List<Long> ids) {
        return null;
    }

    /**
     * 添加文章
     *
     * @param article
     */
    @Override
    public void insert(Article article) {

    }

    /**
     * 修改文章
     *
     * @param article
     */
    @Override
    public void update(Article article) {

    }

}
