package com.blue.basic.common.base;

import com.blue.basic.component.exception.handler.ExceptionProcessor;
import com.blue.basic.constant.common.*;
import com.blue.basic.model.common.*;
import com.blue.basic.model.exps.BlueException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.Clock;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.RsaProcessor.*;
import static com.blue.basic.common.message.MessageProcessor.resolveToMessage;
import static com.blue.basic.constant.common.BlueDataAttrKey.*;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.basic.constant.common.SummerAttr.LANGUAGE;
import static com.blue.basic.constant.common.Symbol.*;
import static com.blue.basic.constant.common.Symbol.LIST_ELEMENT_SEPARATOR;
import static java.lang.Double.compare;
import static java.lang.System.currentTimeMillis;
import static java.time.Instant.now;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.math.NumberUtils.isDigits;
import static reactor.core.publisher.Mono.just;

/**
 * Common function set
 *
 * @author liuyunfei
 */
@SuppressWarnings({"WeakerAccess", "JavaDoc", "AliControlFlowStatementWithoutBraces", "unused", "UastIncorrectHttpHeaderInspection"})
public class CommonFunctions {

    public static final Gson GSON = new GsonBuilder().serializeNulls().create();

    /**
     * symbols
     */
    public static final String
            PATH_SEPARATOR = Symbol.PATH_SEPARATOR.identity,
            WILDCARD = Symbol.WILDCARD.identity,
            PAR_CONCATENATION = Symbol.PAR_CONCATENATION.identity,
            SCHEME_SEPARATOR = Symbol.SCHEME_SEPARATOR.identity,
            URL_PAR_SEPARATOR = Symbol.URL_PAR_SEPARATOR.identity,
            UNKNOWN = Symbol.UNKNOWN.identity;

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    private static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
    private static final String X_REAL_IP = "X-Real-IP";

    public static final String DEFAULT_LANGUAGE = lowerCase(LANGUAGE.replace(PAR_CONCATENATION, PAR_CONCATENATION_DATABASE_URL.identity));
    public static final List<String> DEFAULT_LANGUAGES = singletonList(DEFAULT_LANGUAGE);

    /**
     * auth header key
     */
    public static final String AUTHORIZATION = BlueHeader.AUTHORIZATION.name;

    /**
     * rate limiter key prefix
     */
    public static final String RATE_LIMIT_KEY_PRE = Symbol.RATE_LIMIT_KEY_PRE.identity;

    /**
     * random key length
     */
    public static final int RAN_KEY_STR_LEN = 8;

    /**
     * index of non element
     */
    public static final int NON_EXIST_INDEX = -1, START_IDX = 0;

    /**
     * clock
     */
    public static final Clock CLOCK = SummerAttr.CLOCK;

    private static final int MAX_LANGUAGE_COUNT = 32;

    /**
     * valid freemarker /.html/.js
     */
    public static final Set<String> VALID_TAILS = of(ValidResourceFormatters.values())
            .map(vrf -> vrf.identity).collect(toSet());

    /**
     * uri parser - for request
     */
    public static final UnaryOperator<String> REQUEST_REST_URI_PROCESSOR = uri -> {
        if (isBlank(uri))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "uri can't be null");

        int lastPartIdx = lastIndexOf(uri, PATH_SEPARATOR);
        if (lastPartIdx == -1 || lastPartIdx == length(uri))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid uri, not contains / -> " + uri);

        String maybePathVariable = substring(uri, lastPartIdx + 1);
        int upSeparatorIdx = indexOf(maybePathVariable, URL_PAR_SEPARATOR);
        if (upSeparatorIdx != -1)
            maybePathVariable = substring(maybePathVariable, 0, upSeparatorIdx);

        if (isDigits(maybePathVariable))
            return (substring(uri, 0, lastPartIdx).intern() + PATH_SEPARATOR + WILDCARD).intern();

        int scSeparatorIdx = lastIndexOf(maybePathVariable, SCHEME_SEPARATOR);
        String schema;
        if (scSeparatorIdx != -1 && VALID_TAILS.contains(schema = substring(maybePathVariable, scSeparatorIdx)))
            return (substring(uri, 0, lastPartIdx).intern() + PATH_SEPARATOR + WILDCARD + schema.intern()).intern();

        return uri.intern();
    };

    /**
     * uri asserter
     */
    public static final Consumer<String> REST_URI_ASSERTER = uri -> {
        if (isBlank(uri))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "uri can't be null");

        int idx = indexOf(uri, PATH_SEPARATOR);

        if (idx == NON_EXIST_INDEX)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid uri, not contains / -> " + uri);
        if (idx != START_IDX)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid uri, not start with / -> " + uri);
        if (isBlank(substring(uri, idx)))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid uri, non content but / -> " + uri);
    };

    /**
     * uri parser - for init
     */
    public static final UnaryOperator<String> INIT_REST_URI_PROCESSOR = uri -> {
        REST_URI_ASSERTER.accept(uri);

        int lastPartIdx = lastIndexOf(uri, PATH_SEPARATOR);
        if (lastPartIdx == NON_EXIST_INDEX)
            throw new RuntimeException("invalid uri, not contains / -> " + uri);

        String maybePathVariable = substring(uri, lastPartIdx);

        int left = indexOf(maybePathVariable, "{");
        int right = lastIndexOf(maybePathVariable, "}");

        if (left == NON_EXIST_INDEX && right == NON_EXIST_INDEX)
            return uri.intern();

        int maybePathVariableLength = length(maybePathVariable);
        if (right == maybePathVariableLength - 1)
            return (substring(uri, 0, lastPartIdx).intern() + PATH_SEPARATOR + WILDCARD).intern();

        String schema = substring(maybePathVariable, right + 1).intern();
        if (VALID_TAILS.contains(schema))
            return (substring(uri, 0, lastPartIdx).intern() + PATH_SEPARATOR + WILDCARD + schema.intern()).intern();

        throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid uri, freemarker unsupported -> " + uri);
    };

    /**
     * stamp getter/seconds
     */
    public static final Supplier<Long> TIME_STAMP_GETTER = () -> now(CLOCK).getEpochSecond();

    /**
     * stamp getter/millis
     */
    public static final Supplier<Long> MILLIS_STAMP_SUP = () -> now(CLOCK).toEpochMilli();

    /**
     * resource key generator for request
     */
    public static final BinaryOperator<String> REQ_RES_KEY_GENERATOR = (method, uri) ->
            (upperCase(method).intern() + PAR_CONCATENATION + uri).intern();

    /**
     * resource key generator
     */
    public static final BinaryOperator<String> RES_KEY_GENERATOR = (method, uri) ->
            ((upperCase(method).intern() + PAR_CONCATENATION + INIT_REST_URI_PROCESSOR.apply(uri).intern()).intern()).intern();

    /**
     * header value getter
     */
    public static final UnaryOperator<String> HEADER_VALUE_CONVERTER = headerValue -> {
        int idx = indexOf(headerValue, PAIR_SEPARATOR.identity);
        return idx == NON_EXIST_INDEX ? headerValue : substring(headerValue, 0, idx);
    };

    /**
     * valid schema
     */
    public static final Set<String> VALID_SCHEMAS = of("https", "http")
            .map(StringUtils::lowerCase)
            .collect(toSet());

    /**
     * valid method
     */
    public static final Set<String> VALID_METHODS = of(HttpMethod.values())
            .map(Enum::name)
            .map(StringUtils::upperCase)
            .collect(toSet());

    /**
     * schema asserter
     */
    public static final Consumer<String> SCHEMA_ASSERTER = schema -> {
        if (VALID_SCHEMAS.contains(lowerCase(schema)))
            return;

        throw new BlueException(BAD_REQUEST);
    };

    /**
     * method asserter
     */
    public static final Consumer<String> METHOD_VALUE_ASSERTER = method -> {
        if (VALID_METHODS.contains(upperCase(method)))
            return;

        throw new BlueException(BAD_REQUEST);
    };

    /**
     * header value getter
     */
    public static final BiFunction<HttpHeaders, String, String> HEADER_VALUE_GETTER = (headers, key) ->
            ofNullable(headers.get(key))
                    .filter(cts -> cts.size() > 0)
                    .map(cts -> cts.get(0))
                    .map(HEADER_VALUE_CONVERTER)
                    .orElse(EMPTY_DATA.value);

    /**
     * simple header value getter
     */
    public static final BiFunction<HttpHeaders, String, String> SIMPLE_HEADER_VALUE_GETTER = (headers, key) ->
            ofNullable(headers.getFirst(key))
                    .orElse(EMPTY_DATA.value);

    /**
     * get a random str
     */
    public static final Supplier<String> RANDOM_KEY_GETTER = () ->
            randomAlphanumeric(RAN_KEY_STR_LEN) + PAR_CONCATENATION + currentTimeMillis();

    /**
     * throwable converter
     */
    public static final BiFunction<Throwable, List<String>, ExceptionElement> THROWABLE_CONVERTER = ExceptionProcessor::handle;

    /**
     * assert decrypted data and assert
     */
    public static final BiFunction<DataWrapper, Long, String> DATA_CONVERTER = (dataWrapper, expire) -> {
        if (TIME_STAMP_GETTER.get() - ofNullable(dataWrapper.getTimeStamp()).orElse(0L) <= expire)
            return ofNullable(dataWrapper.getOriginal()).orElse(EMPTY_DATA.value);

        throw new BlueException(DECRYPTION_FAILED);
    };

    private static final Set<String> UN_PACK_KEYS = Stream.of(REQUEST_BODY.key, RESPONSE_STATUS.key, RESPONSE_BODY.key)
            .collect(toSet());

    private static final List<String> ATTR_KEYS = Stream.of(BlueDataAttrKey.values())
            .map(ak -> ak.key).filter(k -> !UN_PACK_KEYS.contains(k)).collect(toList());

    /**
     * package info to event
     */
    public static final BiConsumer<Map<String, Object>, DataEvent> EVENT_PACKAGER = (attributes, dataEvent) -> {
        if (isNotNull(attributes) && isNotNull(dataEvent))
            ATTR_KEYS.forEach(key -> ofNullable(attributes.get(key)).map(String::valueOf)
                    .ifPresent(metadata -> dataEvent.addData(key, metadata)));
    };

    /**
     * decrypt request body
     *
     * @param requestBody
     * @param secKey
     * @param expire
     * @return
     */
    public static String decryptRequestBody(String requestBody, String secKey, long expire) {
        if (isBlank(requestBody) || isBlank(secKey))
            throw new BlueException(BAD_REQUEST);

        EncryptedRequest encryptedRequest = GSON.fromJson(requestBody, EncryptedRequest.class);
        String encrypted = encryptedRequest.getEncrypted();

        if (verify(encrypted, encryptedRequest.getSignature(), secKey))
            return DATA_CONVERTER.apply(GSON.fromJson(decryptByPublicKey(encrypted, secKey), DataWrapper.class), expire);

        throw new BlueException(DECRYPTION_FAILED);
    }

    /**
     * encrypt response body
     *
     * @param responseBody
     * @param secKey
     * @return
     */
    public static String encryptResponseBody(String responseBody, String secKey) {
        if (isBlank(responseBody) || isBlank(secKey))
            throw new BlueException(BAD_REQUEST);

        return GSON.toJson(new EncryptedResponse(encryptByPublicKey(GSON.toJson(new DataWrapper(responseBody, TIME_STAMP_GETTER.get())), secKey)));
    }

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
                    .map(CommonFunctions::getIp)
                    .orElseThrow(() -> new BlueException(BAD_REQUEST)));

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
        String message = resolveToMessage(OK.code).intern();
        return just(new BlueResponse<>(OK.code, message, message));
    }

    /**
     * package response result for reactive
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Mono<BlueResponse<T>> success(T data) {
        return just(new BlueResponse<>(OK.code, data, resolveToMessage(OK.code).intern()));
    }

    /**
     * package response result for reactive
     *
     * @param code
     * @return
     */
    public static Mono<BlueResponse<String>> success(int code) {
        String message = resolveToMessage(code).intern();
        return just(new BlueResponse<>(code, message, message));
    }

    /**
     * package response result for reactive
     *
     * @param serverRequest
     * @return
     */
    public static Mono<BlueResponse<String>> success(ServerRequest serverRequest) {
        String message = resolveToMessage(OK.code, serverRequest).intern();
        return just(new BlueResponse<>(OK.code, message, message));
    }

    /**
     * package response result for reactive
     *
     * @param data
     * @param serverRequest
     * @return
     */
    public static <T> Mono<BlueResponse<T>> success(T data, ServerRequest serverRequest) {
        return just(new BlueResponse<>(OK.code, data, resolveToMessage(OK.code, serverRequest)));
    }

    /**
     * package response result for reactive
     *
     * @param code
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Mono<BlueResponse<T>> success(int code, T data) {
        return just(new BlueResponse<>(code, data, resolveToMessage(code).intern()));
    }

    /**
     * package response result for reactive
     *
     * @param code
     * @param serverRequest
     * @return
     */
    public static Mono<BlueResponse<String>> success(int code, ServerRequest serverRequest) {
        String message = resolveToMessage(code, serverRequest).intern();
        return just(new BlueResponse<>(code, message, message));
    }

    /**
     * package response result for reactive
     *
     * @param code
     * @param replacements
     * @return
     */
    public static Mono<BlueResponse<String>> success(int code, String[] replacements) {
        String message = resolveToMessage(code, replacements);
        return just(new BlueResponse<>(code, message, message));
    }

    /**
     * package response result for reactive
     *
     * @param code
     * @param data
     * @param serverRequest
     * @return
     */
    public static <T> Mono<BlueResponse<T>> success(int code, T data, ServerRequest serverRequest) {
        return just(new BlueResponse<>(code, data, resolveToMessage(code, serverRequest)));
    }

    /**
     * package response result for reactive
     *
     * @param code
     * @param data
     * @param replacements
     * @return
     */
    public static <T> Mono<BlueResponse<T>> success(int code, T data, String[] replacements) {
        return just(new BlueResponse<>(code, data, resolveToMessage(code, replacements)));
    }

    /**
     * package response result for reactive
     *
     * @param code
     * @param serverRequest
     * @param replacements
     * @return
     */
    public static Mono<BlueResponse<String>> success(int code, ServerRequest serverRequest, String[] replacements) {
        String message = resolveToMessage(code, serverRequest, replacements);
        return just(new BlueResponse<>(code, message, message));
    }

    /**
     * package response result for reactive
     *
     * @param code
     * @param data
     * @param serverRequest
     * @param replacements
     * @return
     */
    public static <T> Mono<BlueResponse<T>> success(int code, T data, ServerRequest serverRequest, String[] replacements) {
        return just(new BlueResponse<>(code, data, resolveToMessage(code, serverRequest, replacements)));
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

}
