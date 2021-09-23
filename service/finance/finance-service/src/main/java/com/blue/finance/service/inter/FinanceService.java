package com.blue.finance.service.inter;

import com.blue.finance.api.model.FinanceInfo;
import reactor.core.publisher.Mono;

/**
 * 财务业务接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface FinanceService {

    /**
     * 根据成员主键获取资金账户余额信息
     *
     * @param memberId
     * @return
     */
    Mono<FinanceInfo> getBalanceByMemberId(Long memberId);

}
