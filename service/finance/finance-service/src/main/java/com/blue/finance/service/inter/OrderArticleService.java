package com.blue.finance.service.inter;

import com.blue.finance.repository.entity.OrderArticle;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * order article service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue"})
public interface OrderArticleService {

    /**
     * insert order article
     *
     * @param orderArticle
     * @return
     */
    OrderArticle insertOrderArticle(OrderArticle orderArticle);

    /**
     * insert order article
     *
     * @param orderArticles
     * @return
     */
    List<OrderArticle> insertOrderArticles(List<OrderArticle> orderArticles);

    /**
     * update status
     *
     * @param id
     * @param originalStatus
     * @param destStatus
     * @return
     */
    Boolean updateOrderArticleStatus(Long id, Integer originalStatus, Integer destStatus);

    /**
     * get order article by id
     *
     * @param id
     * @return
     */
    Optional<OrderArticle> getOrderArticle(Long id);

    /**
     * get order article mono by role id
     *
     * @param id
     * @return
     */
    Mono<OrderArticle> getOrderArticleMono(Long id);

    /**
     * select order articles by ids
     *
     * @param ids
     * @return
     */
    List<OrderArticle> selectOrderArticleByIds(List<Long> ids);

    /**
     * select order articles mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<OrderArticle>> selectOrderArticleMonoByIds(List<Long> ids);

}