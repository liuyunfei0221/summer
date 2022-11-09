package com.blue.finance.service.impl;

import com.blue.basic.model.exps.BlueException;
import com.blue.finance.model.db.OrderArticleUpdateModel;
import com.blue.finance.repository.entity.OrderArticle;
import com.blue.finance.repository.mapper.OrderArticleMapper;
import com.blue.finance.service.inter.OrderArticleService;
import com.blue.identity.component.BlueIdentityProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.justOrEmpty;
import static reactor.util.Loggers.getLogger;

/**
 * order article service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class OrderArticleServiceImpl implements OrderArticleService {

    private static final Logger LOGGER = getLogger(OrderArticleServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final OrderArticleMapper orderArticleMapper;

    public OrderArticleServiceImpl(BlueIdentityProcessor blueIdentityProcessor, OrderArticleMapper orderArticleMapper) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.orderArticleMapper = orderArticleMapper;
    }

    /**
     * insert order article
     *
     * @param orderArticle
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public OrderArticle insertOrderArticle(OrderArticle orderArticle) {
        LOGGER.info("orderArticle = {}", orderArticle);
        if (isNull(orderArticle))
            throw new BlueException(EMPTY_PARAM);

        if (isInvalidIdentity(orderArticle.getId()))
            orderArticle.setId(blueIdentityProcessor.generate(OrderArticle.class));

        orderArticleMapper.insert(orderArticle);

        return orderArticle;
    }

    /**
     * insert order article
     *
     * @param orderArticles
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public List<OrderArticle> insertOrderArticles(List<OrderArticle> orderArticles) {
        LOGGER.info("orderArticles = {}", orderArticles);
        if (isEmpty(orderArticles))
            return emptyList();

        for (OrderArticle orderArticle : orderArticles)
            if (isInvalidIdentity(orderArticle.getId()))
                orderArticle.setId(blueIdentityProcessor.generate(OrderArticle.class));

        int inserted = orderArticleMapper.insertBatch(orderArticles);
        LOGGER.info("inserted = {}", inserted);

        return orderArticles;
    }

    /**
     * update target columns selective by id and status
     *
     * @param orderArticleUpdateModel
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public Boolean updateTargetColumnByPrimaryKeySelectiveWithStatusStamp(OrderArticleUpdateModel orderArticleUpdateModel) {
        LOGGER.info("Boolean updateTargetColumnByPrimaryKeySelectiveWithStatusStamp(OrderArticleUpdateModel orderArticleUpdateModel), orderArticleUpdateModel = {}", orderArticleUpdateModel);
        if (isNull(orderArticleUpdateModel))
            throw new BlueException(EMPTY_PARAM);
        orderArticleUpdateModel.asserts();

        return orderArticleMapper.updateTargetColumnByPrimaryKeySelectiveWithStatusStamp(orderArticleUpdateModel) >= 1;
    }

    /**
     * get order article by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<OrderArticle> getOrderArticle(Long id) {
        LOGGER.info("Optional<OrderArticle> getOrderArticle(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(orderArticleMapper.selectByPrimaryKey(id));
    }

    /**
     * get order article mono by role id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<OrderArticle> getOrderArticleMono(Long id) {
        LOGGER.info("Mono<OrderArticle> getOrderArticleMono(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return justOrEmpty(orderArticleMapper.selectByPrimaryKey(id));
    }

    /**
     * select order articles by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<OrderArticle> selectOrderArticleByIds(List<Long> ids) {
        LOGGER.info("List<OrderArticle> selectOrderArticleByIds(List<Long> ids), ids = {}", ids);
        if (isEmpty(ids))
            return emptyList();

        return orderArticleMapper.selectByIds(ids);
    }

    /**
     * select order articles mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<OrderArticle>> selectOrderArticleMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<OrderArticle>> selectOrderArticleMonoByIds(List<Long> ids), ids = {}", ids);

        return just(selectOrderArticleByIds(ids));
    }

}
