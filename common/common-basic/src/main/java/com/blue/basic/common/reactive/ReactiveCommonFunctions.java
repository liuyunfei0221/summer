package com.blue.basic.common.reactive;

import com.blue.basic.common.base.CommonFunctions;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.common.ExceptionElement;
import com.blue.basic.model.common.ExceptionResponse;
import com.blue.basic.model.exps.BlueException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.message.MessageProcessor.resolveToMessage;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Symbol.LIST_ELEMENT_SEPARATOR;
import static java.lang.Double.compare;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.split;
import static reactor.core.publisher.Mono.just;

/**
 * common func for reactive
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "UastIncorrectHttpHeaderInspection", "AliControlFlowStatementWithoutBraces"})
public class ReactiveCommonFunctions extends CommonFunctions {

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";

    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    private static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
    private static final String X_REAL_IP = "X-Real-IP";

    private static final Predicate<String> VALID_IP_PRE = h ->
            isNotBlank(h) && !UNKNOWN.equalsIgnoreCase(h);

    /**
     * request identity getter func
     */
    public static final Function<ServerHttpRequest, Mono<String>> SERVER_HTTP_REQUEST_IDENTITY_SYNC_KEY_GETTER = request ->
            just(RATE_LIMIT_KEY_PRE + ofNullable(request)
                    .map(ServerHttpRequest::getHeaders)
                    .map(h -> h.getFirst(AUTHORIZATION))
                    .filter(StringUtils::isNotEmpty)
                    .map(String::hashCode)
                    .map(String::valueOf)
                    .orElseGet(() ->
                            getIp(request)).hashCode());

    /**
     * request identity getter func
     */
    public static final Function<ServerRequest, Mono<String>> SERVER_REQUEST_IDENTITY_SYNC_KEY_GETTER = request ->
            just(RATE_LIMIT_KEY_PRE + ofNullable(request)
                    .map(ServerRequest::headers)
                    .map(h -> h.firstHeader(AUTHORIZATION))
                    .filter(StringUtils::isNotEmpty)
                    .map(String::hashCode)
                    .map(String::valueOf)
                    .orElseGet(() ->
                            getIp(request)).hashCode());

    /**
     * request ip getter func
     */
    public static final Function<ServerHttpRequest, Mono<String>> SERVER_HTTP_REQUEST_IP_SYNC_KEY_GETTER = request ->
            just(RATE_LIMIT_KEY_PRE + ofNullable(request)
                    .map(req -> just(getIp(req)))
                    .orElseThrow(() -> new BlueException(BAD_REQUEST)));

    /**
     * request ip getter func
     */
    public static final Function<ServerRequest, Mono<String>> SERVER_REQUEST_IP_SYNC_KEY_GETTER = request ->
            just(RATE_LIMIT_KEY_PRE + ofNullable(request)
                    .map(ReactiveCommonFunctions::getIp)
                    .orElseThrow(() -> new BlueException(BAD_REQUEST)));


    private static final int MAX_LANGUAGE_COUNT = 32;

    private static List<String> parseAcceptLanguages(List<Locale.LanguageRange> languageRanges) {
        if (isNotNull(languageRanges) && languageRanges.size() <= MAX_LANGUAGE_COUNT)
            return languageRanges.stream()
                    .sorted((a, b) -> compare(b.getWeight(), a.getWeight()))
                    .map(Locale.LanguageRange::getRange)
                    .collect(toList());

        return DEFAULT_LANGUAGES;
    }

    public static final Function<ExceptionElement, ExceptionResponse> EXP_ELE_2_RESP = exceptionElement ->
            isNotNull(exceptionElement) ?
                    new ExceptionResponse(exceptionElement.getCode(), exceptionElement.getMessage())
                    :
                    new ExceptionResponse(INTERNAL_SERVER_ERROR.code, resolveToMessage(INTERNAL_SERVER_ERROR.code));


    /**
     * package response result for reactive
     *
     * @return
     */
    public static Mono<BlueResponse<String>> success() {
        return just(new BlueResponse<>(resolveToMessage(OK.code).intern()));
    }

    /**
     * package response result for reactive
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Mono<BlueResponse<T>> success(T data) {
        return just(new BlueResponse<>(data));
    }

    /**
     * package response result for reactive
     *
     * @param code
     * @return
     */
    public static Mono<BlueResponse<String>> success(int code) {
        return just(new BlueResponse<>(resolveToMessage(code).intern()));
    }

    /**
     * package response result for reactive
     *
     * @param serverRequest
     * @return
     */
    public static Mono<BlueResponse<String>> success(ServerRequest serverRequest) {
        return just(new BlueResponse<>(resolveToMessage(OK.code, serverRequest).intern()));
    }

    /**
     * package response result for reactive
     *
     * @param code
     * @param serverRequest
     * @return
     */
    public static Mono<BlueResponse<String>> success(int code, ServerRequest serverRequest) {
        return just(new BlueResponse<>(resolveToMessage(code, serverRequest).intern()));
    }


    /**
     * package response result for reactive
     *
     * @param code
     * @return
     */
    public static Mono<BlueResponse<String>> success(int code, String[] replacements) {
        return just(new BlueResponse<>(resolveToMessage(code, replacements)));
    }

    /**
     * package response result for reactive
     *
     * @param code
     * @param serverRequest
     * @return
     */
    public static Mono<BlueResponse<String>> success(int code, ServerRequest serverRequest, String[] replacements) {
        return just(new BlueResponse<>(resolveToMessage(code, serverRequest, replacements)));
    }

    /**
     * get request ip
     *
     * @param serverRequest
     * @return
     */
    public static String getIp(ServerRequest serverRequest) {
        if (isNull(serverRequest))
            return UNKNOWN;

        ServerRequest.Headers headers = serverRequest.headers();

        String ip = headers.firstHeader(X_FORWARDED_FOR);
        if (VALID_IP_PRE.test(ip))
            //noinspection ConstantConditions
            return split(ip, LIST_ELEMENT_SEPARATOR.identity)[0];

        ip = headers.firstHeader(PROXY_CLIENT_IP);
        if (VALID_IP_PRE.test(ip))
            return ip;

        ip = headers.firstHeader(WL_PROXY_CLIENT_IP);
        if (VALID_IP_PRE.test(ip))
            return ip;

        ip = headers.firstHeader(HTTP_CLIENT_IP);
        if (VALID_IP_PRE.test(ip))
            return ip;

        ip = headers.firstHeader(HTTP_X_FORWARDED_FOR);
        if (VALID_IP_PRE.test(ip))
            return ip;

        ip = headers.firstHeader(X_REAL_IP);
        if (VALID_IP_PRE.test(ip))
            return ip;

        return serverRequest.remoteAddress().map(InetSocketAddress::getAddress)
                .map(InetAddress::getHostAddress).orElse(UNKNOWN);
    }

    /**
     * get request ip
     *
     * @param serverHttpRequest
     * @return
     */
    public static String getIp(ServerHttpRequest serverHttpRequest) {
        if (isNull(serverHttpRequest))
            return UNKNOWN;

        HttpHeaders headers = serverHttpRequest.getHeaders();

        String ip = headers.getFirst(X_FORWARDED_FOR);
        if (VALID_IP_PRE.test(ip))
            //noinspection ConstantConditions
            return split(ip, LIST_ELEMENT_SEPARATOR.identity)[0];

        ip = headers.getFirst(PROXY_CLIENT_IP);
        if (VALID_IP_PRE.test(ip))
            return ip;

        ip = headers.getFirst(WL_PROXY_CLIENT_IP);
        if (VALID_IP_PRE.test(ip))
            return ip;

        ip = headers.getFirst(HTTP_CLIENT_IP);
        if (VALID_IP_PRE.test(ip))
            return ip;

        ip = headers.getFirst(HTTP_X_FORWARDED_FOR);
        if (VALID_IP_PRE.test(ip))
            return ip;

        ip = headers.getFirst(X_REAL_IP);
        if (VALID_IP_PRE.test(ip))
            return ip;

        return ofNullable(serverHttpRequest.getRemoteAddress())
                .map(InetSocketAddress::getAddress)
                .map(InetAddress::getHostAddress).orElse(UNKNOWN);
    }

    /**
     * get Accept-Language
     *
     * @param serverRequest
     * @return
     */
    public static List<String> getAcceptLanguages(ServerRequest serverRequest) {
        try {
            return parseAcceptLanguages(serverRequest.headers().acceptLanguage());
        } catch (Exception e) {
            return DEFAULT_LANGUAGES;
        }
    }

    /**
     * get request ip react
     *
     * @param serverRequest
     * @return
     */
    public static Mono<String> getIpReact(ServerRequest serverRequest) {
        return just(getIp(serverRequest));
    }

    /**
     * get request ip react
     *
     * @param serverHttpRequest
     * @return
     */
    public static Mono<String> getIpReact(ServerHttpRequest serverHttpRequest) {
        return just(getIp(serverHttpRequest));
    }

    /**
     * get Accept-Language
     *
     * @param serverHttpRequest
     * @return
     */
    public static List<String> getAcceptLanguages(ServerHttpRequest serverHttpRequest) {
        try {
            return parseAcceptLanguages(serverHttpRequest.getHeaders().getAcceptLanguage());
        } catch (Exception e) {
            return DEFAULT_LANGUAGES;
        }
    }

}
