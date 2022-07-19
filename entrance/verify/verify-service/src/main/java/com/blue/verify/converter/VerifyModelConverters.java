package com.blue.verify.converter;

import com.blue.basic.model.exps.BlueException;
import com.blue.verify.api.model.VerifyHistoryInfo;
import com.blue.verify.repository.entity.VerifyHistory;

import java.util.List;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * model converters in verify project
 *
 * @author liuyunfei
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class VerifyModelConverters {

    /**
     * verify history -> verify history info
     */
    public static final Function<VerifyHistory, VerifyHistoryInfo> VERIFY_HISTORY_2_VERIFY_HISTORY_INFO_CONVERTER = verifyHistory -> {
        if (isNull(verifyHistory))
            throw new BlueException(EMPTY_PARAM);

        return new VerifyHistoryInfo(verifyHistory.getId(), verifyHistory.getVerifyType(), verifyHistory.getBusinessType(),
                verifyHistory.getDestination(), verifyHistory.getVerify(), verifyHistory.getRequestIp(), verifyHistory.getCreateTime());
    };

    /**
     * verify histories -> verify history infos
     */
    public static final Function<List<VerifyHistory>, List<VerifyHistoryInfo>> VERIFY_HISTORIES_2_VERIFY_HISTORY_INFOS_CONVERTER = verifyHistories ->
            verifyHistories != null && verifyHistories.size() > 0 ? verifyHistories.stream()
                    .map(VERIFY_HISTORY_2_VERIFY_HISTORY_INFO_CONVERTER)
                    .collect(toList()) : emptyList();

}
