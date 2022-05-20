package com.blue.base.common.reactive;

import com.blue.base.model.common.TuringData;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import static com.blue.base.common.turing.TuringDataProcessor.jsonToTuringData;
import static com.blue.base.constant.base.BlueHeader.TURING_DATA;
import static reactor.core.publisher.Mono.just;

/**
 * turing data getter for reactive
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class TuringDataGetterForReactive {

    /**
     * get turing data from request
     *
     * @param serverRequest
     * @return
     */
    public static TuringData getTuringData(ServerRequest serverRequest) {
        return jsonToTuringData(serverRequest.headers().firstHeader(TURING_DATA.name));
    }

    /**
     * get turing data from serverHttpRequest
     *
     * @param serverHttpRequest
     * @return
     */
    public static TuringData getTuringData(ServerHttpRequest serverHttpRequest) {
        return jsonToTuringData(serverHttpRequest.getHeaders().getFirst(TURING_DATA.name));
    }

    /**
     * get turing data mono from serverRequest
     *
     * @param serverRequest
     * @return
     */
    public static Mono<TuringData> getTuringDataReact(ServerRequest serverRequest) {
        return just(jsonToTuringData(serverRequest.headers().firstHeader(TURING_DATA.name)));
    }

    /**
     * get turing data mono from serverHttpRequest
     *
     * @param serverHttpRequest
     * @return
     */
    public static Mono<TuringData> getTuringDataReact(ServerHttpRequest serverHttpRequest) {
        return just(jsonToTuringData(serverHttpRequest.getHeaders().getFirst(TURING_DATA.name)));
    }


}
