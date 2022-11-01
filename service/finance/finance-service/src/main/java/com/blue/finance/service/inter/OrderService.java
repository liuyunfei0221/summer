package com.blue.finance.service.inter;

import com.blue.finance.model.db.OrderUpdateModel;
import com.blue.finance.model.db.OrderVersionUpdateModel;
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
     * update target columns selective by id and status and version
     *
     * @param orderUpdateModel
     * @return
     */
    Boolean updateTargetColumnByPrimaryKeySelectiveWithStamps(OrderUpdateModel orderUpdateModel);

    /**
     * update order version by id and version
     *
     * @param orderVersionUpdateModel
     * @return
     */
    Boolean updateTargetColumnByPrimaryKeySelectiveWithStamps(OrderVersionUpdateModel orderVersionUpdateModel);

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