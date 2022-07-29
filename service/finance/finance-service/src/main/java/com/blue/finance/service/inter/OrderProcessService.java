package com.blue.finance.service.inter;

import com.blue.finance.api.model.OrderInfo;
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
public interface OrderProcessService {

    /**
     * insert order
     *
     * @param order
     * @return
     */
    OrderInfo insertOrder(Order order);

    /**
     * update a exist order
     *
     * @param order
     * @return
     */
    OrderInfo updateOrder(Order order);

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
    Optional<OrderInfo> getOrderInfo(Long id);

    /**
     * get order mono by role id
     *
     * @param id
     * @return
     */
    Mono<OrderInfo> getOrderInfoMono(Long id);

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