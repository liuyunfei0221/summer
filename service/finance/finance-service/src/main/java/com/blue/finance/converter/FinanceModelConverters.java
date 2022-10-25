package com.blue.finance.converter;

import com.blue.basic.model.exps.BlueException;
import com.blue.finance.api.model.FinanceAccountInfo;
import com.blue.finance.api.model.FinanceFlowInfo;
import com.blue.finance.api.model.FinanceFlowManagerInfo;
import com.blue.finance.api.model.OrderInfo;
import com.blue.finance.repository.entity.FinanceAccount;
import com.blue.finance.repository.entity.FinanceFlow;
import com.blue.finance.repository.entity.Order;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static java.util.Optional.ofNullable;

/**
 * model converters in finance project
 *
 * @author liuyunfei
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class FinanceModelConverters {

    /**
     * finance account -> finance account info
     */
    public static final Function<FinanceAccount, FinanceAccountInfo> FINANCE_ACCOUNT_2_FINANCE_ACCOUNT_INFO_CONVERTER = financeAccount -> {
        if (isNull(financeAccount))
            throw new BlueException(EMPTY_PARAM);

        return new FinanceAccountInfo(financeAccount.getId(), financeAccount.getMemberId(),
                financeAccount.getBalance(), financeAccount.getFrozen(), financeAccount.getIncome(), financeAccount.getOutlay(), financeAccount.getStatus());
    };

    /**
     * finance flow -> finance flow info
     */
    public static final Function<FinanceFlow, FinanceFlowInfo> FINANCE_FLOW_2_FINANCE_FLOW_INFO_CONVERTER = financeFlow -> {
        if (isNull(financeFlow))
            throw new BlueException(EMPTY_PARAM);

        return new FinanceFlowInfo(financeFlow.getId(), financeFlow.getMemberId(), financeFlow.getOrderId(), financeFlow.getOrderNo(), financeFlow.getFlowNo(), financeFlow.getType(),
                financeFlow.getChangeType(), financeFlow.getAmountChanged(), financeFlow.getAmountBeforeChanged(), financeFlow.getAmountAfterChanged(), financeFlow.getCreateTime());
    };

    /**
     * finance flow -> finance flow manager info
     */
    public static final BiFunction<FinanceFlow, Map<Long, String>, FinanceFlowManagerInfo> FINANCE_FLOW_2_FINANCE_FLOW_MANAGER_INFO_CONVERTER = (financeFlow, idAndMemberNameMapping) -> {
        if (isNull(financeFlow))
            throw new BlueException(EMPTY_PARAM);

        return new FinanceFlowManagerInfo(financeFlow.getId(), financeFlow.getMemberId(), ofNullable(idAndMemberNameMapping.get(financeFlow.getMemberId())).orElse(EMPTY_VALUE.value),
                financeFlow.getOrderId(), financeFlow.getOrderNo(), financeFlow.getFlowNo(), financeFlow.getType(),
                financeFlow.getChangeType(), financeFlow.getAmountChanged(), financeFlow.getAmountBeforeChanged(), financeFlow.getAmountAfterChanged(), financeFlow.getCreateTime());
    };

    /**
     * order -> order info
     */
    public static final Function<Order, OrderInfo> ORDER_2_ORDER_INFO_CONVERTER = order -> {
        if (isNull(order))
            throw new BlueException(EMPTY_PARAM);

        return new OrderInfo(order.getId(), order.getMemberId(), order.getOrderNo(), order.getFlowNo(), order.getType(), order.getPaymentType(), order.getAmount(), order.getPayAmount(),
                order.getExtra(), order.getPaymentExtra(), order.getDetail(), order.getStatus());
    };

}
