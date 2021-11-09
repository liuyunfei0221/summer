package com.blue.base.common.reactive;


import com.blue.base.model.exps.BlueException;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import static com.blue.base.common.base.Asserter.isBlank;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.EMPTY_PATH_VARIABLE;
import static com.blue.base.constant.base.ResponseMessage.INVALID_PATH_VARIABLE;
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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PATH_VARIABLE.message);

        String pathVariable = serverRequest.pathVariable(placeHolder);
        if (isBlank(pathVariable))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PATH_VARIABLE.message);

        try {
            return parseLong(pathVariable);
        } catch (NumberFormatException e) {
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_PATH_VARIABLE.message);
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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PATH_VARIABLE.message);

        String pathVariable = serverRequest.pathVariable(placeHolder);
        if (isBlank(pathVariable))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PATH_VARIABLE.message);

        try {
            return parseInt(pathVariable);
        } catch (NumberFormatException e) {
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_PATH_VARIABLE.message);
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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PATH_VARIABLE.message);

        String pathVariable = serverRequest.pathVariable(placeHolder);
        if (isBlank(pathVariable))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PATH_VARIABLE.message);

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
