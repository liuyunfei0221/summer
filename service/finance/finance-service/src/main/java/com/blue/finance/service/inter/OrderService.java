package com.blue.finance.service.inter;

import com.blue.finance.repository.entity.Order;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * order service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue"})
public interface OrderService {

    /**
     * insert order
     *
     * @param order
     * @return
     */
    Order insertOrder(Order order);

    /**
     * update a exist order
     *
     * @param order
     * @return
     */
    Boolean updateOrder(Order order);

    /**
     * delete order
     *
     * @param id
     * @return
     */
    Boolean deleteOrder(Long id);

    /**
     * get order by id
     *
     * @param id
     * @return
     */
    Optional<Order> getOrder(Long id);

    /**
     * get order mono by role id
     *
     * @param id
     * @return
     */
    Mono<Order> getOrderMono(Long id);

    /**
     * select orders by ids
     *
     * @param ids
     * @return
     */
    List<Order> selectOrderByIds(List<Long> ids);

    /**
     * select orders mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<Order>> selectOrderMonoByIds(List<Long> ids);

}