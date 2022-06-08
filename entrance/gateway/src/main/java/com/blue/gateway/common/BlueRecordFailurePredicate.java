package com.blue.gateway.common;

import com.blue.base.model.exps.BlueException;

import java.util.Set;
import java.util.function.Predicate;

import static com.blue.base.constant.common.ResponseElement.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;

/**
 * RecordFailurePredicate for resilience4j
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class BlueRecordFailurePredicate implements Predicate<Throwable> {

    private static final Set<Integer> RECORD_FAILURE_STATUS = of(INTERNAL_SERVER_ERROR, REQUEST_TIMEOUT, GATEWAY_TIMEOUT).map(re -> re.status).collect(toSet());

    @Override
    public boolean test(Throwable throwable) {
        return !(throwable instanceof BlueException) || RECORD_FAILURE_STATUS.contains(ofNullable(((BlueException) throwable).getStatus()).orElse(INTERNAL_SERVER_ERROR.status));
    }

}
