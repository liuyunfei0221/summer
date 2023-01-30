package com.blue.finance.service.inter;


import com.blue.finance.api.model.FinanceAccountInfo;
import reactor.core.publisher.Mono;

/**
 * finance control service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue"})
public interface FinanceControlService {

    /**
     * init finance account for a new member
     *
     * @param memberId
     */
    Mono<FinanceAccountInfo> initFinanceAccount(Long memberId);

    /**
     * query finance account mono by id
     *
     * @param id
     * @return
     */
    Mono<FinanceAccountInfo> getFinanceAccountInfo(Long id);

    /**
     * get finance account mono by member id with auto init
     *
     * @param memberId
     * @return
     */
    Mono<FinanceAccountInfo> getFinanceAccountInfoByMemberId(Long memberId);

}
