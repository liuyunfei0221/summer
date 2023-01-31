package com.blue.basic.common.base;

import com.blue.basic.model.common.TuringData;
import com.blue.basic.model.exps.BlueException;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.turing.TuringDataProcessor.jsonToTuringData;
import static com.blue.basic.constant.common.BlueHeader.TURING_DATA;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static reactor.core.publisher.Mono.just;

/**
 * turing data getter for reactive
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class TuringDataGetter {

    /**
     * get turing data from request
     *
     * @param serverRequest
     * @return
     */
    public static TuringData getTuringData(ServerRequest serverRequest) {
        if (isNull(serverRequest))
            throw new BlueException(EMPTY_PARAM);

        return jsonToTuringData(serverRequest.headers().firstHeader(TURING_DATA.name));
    }

    /**
     * get turing data from serverHttpRequest
     *
     * @param serverHttpRequest
     * @return
     */
    public static TuringData getTuringData(ServerHttpRequest serverHttpRequest) {
        if (isNull(serverHttpRequest))
            throw new BlueException(EMPTY_PARAM);

        return jsonToTuringData(serverHttpRequest.getHeaders().getFirst(TURING_DATA.name));
    }

    /**
     * get turing data mono from serverRequest
     *
     * @param serverRequest
     * @return
     */
    public static Mono<TuringData> getTuringDataReact(ServerRequest serverRequest) {
        return just(getTuringData(serverRequest));
    }

    /**
     * get turing data mono from serverHttpRequest
     *
     * @param serverHttpRequest
     * @return
     */
    public static Mono<TuringData> getTuringDataReact(ServerHttpRequest serverHttpRequest) {
        return just(getTuringData(serverHttpRequest));
    }

}