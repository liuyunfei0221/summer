package com.blue.base.common.reactive;

import com.blue.base.common.base.CommonFunctions;
import com.blue.base.model.base.BlueResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.create;
import static reactor.core.publisher.Mono.just;

/**
 * 响应式组件集
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc", "UastIncorrectHttpHeaderInspection", "AliControlFlowStatementWithoutBraces"})
public class ReactiveCommonFunctions extends CommonFunctions {

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";

    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    private static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
    private static final String X_REAL_IP = "X-Real-IP";

    private static final Predicate<String> VALID_HEADER_ASSERTER = h ->
            h != null && !"".equals(h) && !UNKNOWN.equalsIgnoreCase(h);

    /**
     * 请求特征获取器
     */
    public static final Function<ServerHttpRequest, String> REQUEST_IDENTITY_GETTER = request ->
            RATE_LIMIT_KEY_PRE + of(request)
                    .map(ServerHttpRequest::getHeaders)
                    .map(h -> h.getFirst(AUTHORIZATION))
                    .filter(StringUtils::hasText)
                    .map(String::hashCode)
                    .map(String::valueOf)
                    .orElse(of(request)
                            .map(ServerHttpRequest::getRemoteAddress)
                            .map(InetSocketAddress::getHostString)
                            .orElse(UNKNOWN)).hashCode();


    /**
     * 封装reactive响应
     *
     * @param code
     * @param data
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Mono<BlueResult<T>> generate(int code, T data, String message) {
        return just(new BlueResult<>(code, data, message));
    }

    /**
     * 转换
     *
     * @param future
     * @param executorService
     * @param <T>
     * @return
     */
    public static <T> Mono<T> converterFutureToMono(CompletableFuture<T> future, ExecutorService executorService) {
        return create(monoSink ->
                future.whenCompleteAsync((data, throwable) -> {
                    if (throwable == null) {
                        monoSink.success(data);
                    } else {
                        monoSink.error(throwable);
                    }
                }, executorService));
    }

    /**
     * 转换
     *
     * @param mono
     * @param executorService
     * @param <T>
     * @return
     */
    public static <T> CompletableFuture<T> converterMonoToFuture(Mono<T> mono, ExecutorService executorService) {
        CompletableFuture<T> future = new CompletableFuture<>();
        mono.doOnError(future::completeExceptionally)
                .subscribe(data -> future.completeAsync(() -> data, executorService));
        return future;
    }

    /**
     * 转换
     *
     * @param future
     * @param <T>
     * @return
     */
    public static <T> Mono<T> converterFutureToMono(CompletableFuture<T> future) {
        return create(monoSink ->
                future.whenCompleteAsync((data, throwable) -> {
                    if (throwable == null) {
                        monoSink.success(data);
                    } else {
                        monoSink.error(throwable);
                    }
                }));
    }

    /**
     * 转换
     *
     * @param mono
     * @param <T>
     * @return
     */
    public static <T> CompletableFuture<T> converterMonoToFuture(Mono<T> mono) {
        CompletableFuture<T> future = new CompletableFuture<>();
        mono.doOnError(future::completeExceptionally)
                .subscribe(data -> future.completeAsync(() -> data));
        return future;
    }

    /**
     * 获取请求IP
     *
     * @param serverRequest
     * @return
     */
    @SuppressWarnings("DuplicatedCode")
    public static String getIp(ServerRequest serverRequest) {
        ServerRequest.Headers headers = serverRequest.headers();

        String ip = headers.firstHeader(X_FORWARDED_FOR);
        if (VALID_HEADER_ASSERTER.test(ip))
            return ip;

        ip = headers.firstHeader(PROXY_CLIENT_IP);
        if (VALID_HEADER_ASSERTER.test(ip))
            return ip;

        ip = headers.firstHeader(WL_PROXY_CLIENT_IP);
        if (VALID_HEADER_ASSERTER.test(ip))
            return ip;

        ip = headers.firstHeader(HTTP_CLIENT_IP);
        if (VALID_HEADER_ASSERTER.test(ip))
            return ip;

        ip = headers.firstHeader(HTTP_X_FORWARDED_FOR);
        if (VALID_HEADER_ASSERTER.test(ip))
            return ip;

        ip = headers.firstHeader(X_REAL_IP);
        if (VALID_HEADER_ASSERTER.test(ip))
            return ip;

        return serverRequest.remoteAddress().map(InetSocketAddress::getAddress)
                .map(InetAddress::getHostAddress).orElse(UNKNOWN);
    }


    /**
     * 获取请求IP
     *
     * @param serverHttpRequest
     * @return
     */
    @SuppressWarnings("DuplicatedCode")
    public static String getIp(ServerHttpRequest serverHttpRequest) {
        HttpHeaders headers = serverHttpRequest.getHeaders();

        String ip = headers.getFirst(X_FORWARDED_FOR);
        if (VALID_HEADER_ASSERTER.test(ip))
            return ip;

        ip = headers.getFirst(PROXY_CLIENT_IP);
        if (VALID_HEADER_ASSERTER.test(ip))
            return ip;

        ip = headers.getFirst(WL_PROXY_CLIENT_IP);
        if (VALID_HEADER_ASSERTER.test(ip))
            return ip;

        ip = headers.getFirst(HTTP_CLIENT_IP);
        if (VALID_HEADER_ASSERTER.test(ip))
            return ip;

        ip = headers.getFirst(HTTP_X_FORWARDED_FOR);
        if (VALID_HEADER_ASSERTER.test(ip))
            return ip;

        ip = headers.getFirst(X_REAL_IP);
        if (VALID_HEADER_ASSERTER.test(ip))
            return ip;

        return ofNullable(serverHttpRequest.getRemoteAddress())
                .map(InetSocketAddress::getAddress)
                .map(InetAddress::getHostAddress).orElse(UNKNOWN);
    }

}
