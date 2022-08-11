package com.blue.finance.service.inter;

import com.blue.basic.model.common.Pit;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.finance.repository.entity.OrderSummary;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * order summary service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue"})
public interface OrderSummaryService {

    /**
     * insert order summary
     *
     * @param orderSummary
     * @return
     */
    OrderSummary insertOrderSummary(OrderSummary orderSummary);

    /**
     * insert order summary async
     *
     * @param orderSummary
     * @return
     */
    OrderSummary insertOrderSummaryAsync(OrderSummary orderSummary);

    /**
     * update order summary
     *
     * @param orderSummary
     * @return
     */
    OrderSummary updateOrderSummary(OrderSummary orderSummary);

    /**
     * update order summary async
     *
     * @param orderSummary
     * @return
     */
    OrderSummary updateOrderSummaryAsync(OrderSummary orderSummary);

    /**
     * get order summary mono by id
     *
     * @param id
     * @return
     */
    Mono<OrderSummary> getOrderSummaryMono(Long id);

    /**
     * get order summary flow by id
     *
     * @param id
     * @return
     */
    Optional<OrderSummary> getOrderSummary(Long id);

    /**
     * select order summary by page and memberId
     *
     * @param rows
     * @param pit
     * @param memberId
     * @return
     */
    Mono<List<OrderSummary>> selectOrderSummaryMonoByPitAndMemberId(Long rows, Pit pit, Long memberId);

    /**
     * select order summary by scroll and member id
     *
     * @param pageModelRequest
     * @param memberId
     * @return
     */
    Mono<ScrollModelResponse<OrderSummary, Pit>> selectOrderSummaryByScrollAndMemberId(ScrollModelRequest<Pit> pageModelRequest, Long memberId);

}