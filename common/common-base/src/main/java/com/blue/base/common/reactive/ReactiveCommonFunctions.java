package com.blue.base.common.reactive;

import com.blue.base.common.base.CommonFunctions;
import com.blue.base.model.base.BlueResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.blue.base.constant.base.SummerAttr.LANGUAGE;
import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION_DATABASE_URL;
import static java.lang.Double.compare;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.just;

/**
 * common func for reactive
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
     * request identity getter func
     */
    public static final Function<ServerHttpRequest, Mono<String>> REQUEST_IDENTITY_GETTER = request ->
            just(RATE_LIMIT_KEY_PREFIX + ofNullable(request)
                    .map(ServerHttpRequest::getHeaders)
                    .map(h -> h.getFirst(AUTHORIZATION))
                    .filter(StringUtils::hasText)
                    .map(String::hashCode)
                    .map(String::valueOf)
                    .orElse(ofNullable(request)
                            .map(ServerHttpRequest::getRemoteAddress)
                            .map(InetSocketAddress::getHostString)
                            .orElse(UNKNOWN)).hashCode());


    private static final int MAX_LANGUAGE_COUNT = 16;

    private static final String DEFAULT_LANGUAGE = LANGUAGE.replace(PAR_CONCATENATION, PAR_CONCATENATION_DATABASE_URL.identity).toLowerCase();
    private static final List<String> DEFAULT_LANGUAGES = singletonList(DEFAULT_LANGUAGE);

    private static List<String> parseAcceptLanguages(List<Locale.LanguageRange> languageRanges) {
        if (languageRanges != null && languageRanges.size() <= MAX_LANGUAGE_COUNT)
            return languageRanges.stream()
                    .sorted((a, b) -> compare(b.getWeight(), a.getWeight()))
                    .map(Locale.LanguageRange::getRange)
                    .map(String::toLowerCase)
                    .collect(toList());

        return DEFAULT_LANGUAGES;
    }

    /**
     * package response result for reactive
     *
     * @param code
     * @param data
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Mono<BlueResponse<T>> generate(int code, T data, String message) {
        return just(new BlueResponse<>(code, data, message));
    }

    /**
     * get request ip
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
     * get request ip
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

    /**
     * get Accept-Language
     *
     * @param serverRequest
     * @return
     */
    @SuppressWarnings("DuplicatedCode")
    public static List<String> getAcceptLanguages(ServerRequest serverRequest) {
        return parseAcceptLanguages(serverRequest.headers().acceptLanguage());
    }

    /**
     * get Accept-Language
     *
     * @param serverHttpRequest
     * @return
     */
    @SuppressWarnings("DuplicatedCode")
    public static List<String> getAcceptLanguages(ServerHttpRequest serverHttpRequest) {
        return parseAcceptLanguages(serverHttpRequest.getHeaders().getAcceptLanguage());
    }

}
