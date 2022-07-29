package com.blue.finance.service.inter;

import com.blue.finance.repository.entity.Refund;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * refund service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue"})
public interface RefundService {

    /**
     * insert refund
     *
     * @param refund
     * @return
     */
    Refund insertRefund(Refund refund);

    /**
     * update a exist refund
     *
     * @param refund
     * @return
     */
    Boolean updateRefund(Refund refund);

    /**
     * delete refund
     *
     * @param id
     * @return
     */
    Boolean deleteRefund(Long id);

    /**
     * get refund by id
     *
     * @param id
     * @return
     */
    Optional<Refund> getRefund(Long id);

    /**
     * get refund mono by role id
     *
     * @param id
     * @return
     */
    Mono<Refund> getRefundMono(Long id);

    /**
     * select refunds by ids
     *
     * @param ids
     * @return
     */
    List<Refund> selectRefundByIds(List<Long> ids);

    /**
     * select refunds mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<Refund>> selectRefundMonoByIds(List<Long> ids);

}