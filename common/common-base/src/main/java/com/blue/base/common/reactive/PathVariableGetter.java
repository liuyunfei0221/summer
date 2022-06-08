package com.blue.base.common.reactive;


import com.blue.base.model.exps.BlueException;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.common.ResponseElement.*;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static reactor.core.publisher.Mono.just;

/**
 * get data from uri placeholder
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc", "unused"})
public class PathVariableGetter {

    /**
     * get path variable
     *
     * @param serverRequest
     * @param placeHolder
     * @return
     */
    public static Long getLongVariable(ServerRequest serverRequest, String placeHolder) {
        assertParam(serverRequest, placeHolder);

        String pathVariable = serverRequest.pathVariable(placeHolder);
        assertPathVariable(pathVariable);

        try {
            return parseLong(pathVariable);
        } catch (NumberFormatException e) {
            throw new BlueException(INVALID_PATH_VARIABLE);
        }
    }

    /**
     * get path variable
     *
     * @param serverRequest
     * @param placeHolder
     * @return
     */
    public static Mono<Long> getLongVariableReact(ServerRequest serverRequest, String placeHolder) {
        return just(getLongVariable(serverRequest, placeHolder));
    }

    /**
     * get path variable
     *
     * @param serverRequest
     * @param placeHolder
     * @return
     */
    public static Integer getIntegerVariable(ServerRequest serverRequest, String placeHolder) {
        assertParam(serverRequest, placeHolder);

        String pathVariable = serverRequest.pathVariable(placeHolder);
        assertPathVariable(pathVariable);

        try {
            return parseInt(pathVariable);
        } catch (NumberFormatException e) {
            throw new BlueException(INVALID_PATH_VARIABLE);
        }
    }

    /**
     * get path variable
     *
     * @param serverRequest
     * @param placeHolder
     * @return
     */
    public static Mono<Integer> getIntegerVariableReact(ServerRequest serverRequest, String placeHolder) {
        return just(getIntegerVariable(serverRequest, placeHolder));
    }

    /**
     * get path variable
     *
     * @param serverRequest
     * @param placeHolder
     * @return
     */
    public static String getStringVariable(ServerRequest serverRequest, String placeHolder) {
        assertParam(serverRequest, placeHolder);

        String pathVariable = serverRequest.pathVariable(placeHolder);
        assertPathVariable(pathVariable);

        return pathVariable;
    }

    /**
     * get path variable
     *
     * @param serverRequest
     * @param placeHolder
     * @return
     */
    public static Mono<String> getStringVariableReact(ServerRequest serverRequest, String placeHolder) {
        return just(getStringVariable(serverRequest, placeHolder));
    }


    /**
     * asserter
     *
     * @param serverRequest
     * @param placeHolder
     */
    private static void assertParam(ServerRequest serverRequest, String placeHolder) {
        if (isBlank(placeHolder))
            throw new BlueException(EMPTY_PATH_VARIABLE);

        if (isNull(serverRequest))
            throw new BlueException(INTERNAL_SERVER_ERROR);
    }

    /**
     * asserter
     *
     * @param pathVariable
     */
    private static void assertPathVariable(String pathVariable) {
        if (isBlank(pathVariable))
            throw new BlueException(EMPTY_PATH_VARIABLE);
    }

}
