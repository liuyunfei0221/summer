package com.blue.base.common.reactive;


import com.blue.base.model.exps.BlueException;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import static com.blue.base.common.base.Check.isBlank;
import static com.blue.base.constant.base.ResponseElement.*;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static reactor.core.publisher.Mono.just;

/**
 * get data from uri placeholder
 *
 * @author liuyunfei
 * @date 2021/11/9
 * @apiNote
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
        if (isBlank(placeHolder))
            throw new BlueException(EMPTY_PATH_VARIABLE);

        String pathVariable = serverRequest.pathVariable(placeHolder);
        if (isBlank(pathVariable))
            throw new BlueException(EMPTY_PATH_VARIABLE);

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
        if (isBlank(placeHolder))
            throw new BlueException(EMPTY_PATH_VARIABLE);

        String pathVariable = serverRequest.pathVariable(placeHolder);
        if (isBlank(pathVariable))
            throw new BlueException(EMPTY_PATH_VARIABLE);

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
        if (isBlank(placeHolder))
            throw new BlueException(EMPTY_PATH_VARIABLE);

        String pathVariable = serverRequest.pathVariable(placeHolder);
        if (isBlank(pathVariable))
            throw new BlueException(EMPTY_PATH_VARIABLE);

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

}
