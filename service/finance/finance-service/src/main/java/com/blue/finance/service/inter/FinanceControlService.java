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
    FinanceAccountInfo initFinanceAccount(Long memberId);

    /**
     * get finance account mono by member id with auto init
     *
     * @param memberId
     * @return
     */
    Mono<FinanceAccountInfo> getFinanceAccountInfoByMemberIdWithAutoInit(Long memberId);

}
