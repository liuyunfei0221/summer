package com.blue.finance.service.inter;

import com.blue.finance.repository.entity.ReferenceAmount;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * reference amount service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue"})
public interface ReferenceAmountService {

    /**
     * insert reference amount
     *
     * @param referenceAmount
     * @return
     */
    ReferenceAmount insertReferenceAmount(ReferenceAmount referenceAmount);

    /**
     * insert reference amounts
     *
     * @param referenceAmounts
     * @return
     */
    List<ReferenceAmount> insertReferenceAmounts(List<ReferenceAmount> referenceAmounts);

    /**
     * update a exist reference amount
     *
     * @param referenceAmount
     * @return
     */
    Boolean updateReferenceAmount(ReferenceAmount referenceAmount);

    /**
     * delete reference amount
     *
     * @param id
     * @return
     */
    Boolean deleteReferenceAmount(Long id);

    /**
     * get reference amount by id
     *
     * @param id
     * @return
     */
    Optional<ReferenceAmount> getReferenceAmount(Long id);

    /**
     * get reference amount mono by role id
     *
     * @param id
     * @return
     */
    Mono<ReferenceAmount> getReferenceAmountMono(Long id);

    /**
     * select reference amounts by ids
     *
     * @param ids
     * @return
     */
    List<ReferenceAmount> selectReferenceAmountByIds(List<Long> ids);

    /**
     * select reference amounts mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<ReferenceAmount>> selectReferenceAmountMonoByIds(List<Long> ids);

}