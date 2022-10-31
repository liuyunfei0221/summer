package com.blue.finance.api.common;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.finance.ReferenceAmountStatus;
import com.blue.basic.model.exps.BlueException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static com.blue.basic.common.base.ConstantProcessor.assertOrderStatus;
import static com.blue.basic.constant.common.ResponseElement.UNSUPPORTED_OPERATE;
import static com.blue.basic.constant.finance.ReferenceAmountStatus.*;
import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

/**
 * reference amount status change asserter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class ReferenceAmountStatusChangeAsserter {

    private static final Map<Integer, Set<Integer>> VALID_STATUS_CHANGE = new HashMap<>(ReferenceAmountStatus.values().length);

    static {
        VALID_STATUS_CHANGE.put(USING.identity, Stream.of(USED.identity, CANCEL.identity).collect(toSet()));
        VALID_STATUS_CHANGE.put(USED.identity, emptySet());
        VALID_STATUS_CHANGE.put(CANCEL.identity, emptySet());
    }

    public static final BiConsumer<Integer, Integer> STATUS_CHANGE_ASSERTER = (originalStatus, destStatus) -> {
        assertOrderStatus(originalStatus, false);
        assertOrderStatus(destStatus, false);

        if (!ofNullable(VALID_STATUS_CHANGE.get(originalStatus)).filter(BlueChecker::isNotEmpty)
                .map(validTargets -> validTargets.contains(destStatus)).orElse(false))
            throw new BlueException(UNSUPPORTED_OPERATE);
    };

    /**
     * assert order status change
     *
     * @param originalStatus
     * @param destStatus
     */
    public static void assertStatusChange(Integer originalStatus, Integer destStatus) {
        STATUS_CHANGE_ASSERTER.accept(originalStatus, destStatus);
    }

}
