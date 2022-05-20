package com.blue.base.common.base;

import com.blue.base.component.exception.handler.ExceptionProcessor;
import com.blue.base.constant.base.*;
import com.blue.base.model.common.*;
import com.blue.base.model.exps.BlueException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Stream;

import static com.blue.base.common.base.BlueChecker.isNotNull;
import static com.blue.base.common.base.RsaProcessor.*;
import static com.blue.base.constant.base.BlueDataAttrKey.*;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.SummerAttr.LANGUAGE;
import static com.blue.base.constant.base.Symbol.PAIR_SEPARATOR;
import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION_DATABASE_URL;
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

/**
 * Common function set
 *
 * @author liuyunfei
 */
@SuppressWarnings({"WeakerAccess", "JavaDoc", "AliControlFlowStatementWithoutBraces", "unused"})
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
            throw new BlueException(BAD_REQUEST.status, INTERNAL_SERVER_ERROR.code, "invalid uri, not contains / -> " + uri);

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
                    .orElse("");

    /**
     * simple header value getter
     */
    public static final BiFunction<HttpHeaders, String, String> SIMPLE_HEADER_VALUE_GETTER = (headers, key) ->
            ofNullable(headers.getFirst(key))
                    .orElse("");

    /**
     * get a random str
     */
    public static final Supplier<String> RANDOM_KEY_GETTER = () ->
            randomAlphanumeric(RAN_KEY_STR_LEN) + PAR_CONCATENATION + currentTimeMillis();

    /**
     * throwable converter
     */
    public static final BiFunction<Throwable, List<String>, ExceptionResponse> THROWABLE_CONVERTER = ExceptionProcessor::handle;

    /**
     * assert decrypted data and assert
     */
    public static final BiFunction<DataWrapper, Long, String> DATA_CONVERTER = (dataWrapper, expire) -> {
        if (TIME_STAMP_GETTER.get() - ofNullable(dataWrapper.getTimeStamp()).orElse(0L) <= expire)
            return ofNullable(dataWrapper.getOriginal()).orElse("");

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

    public static final UnaryOperator<String> FILE_TYPE_GETTER = fileName -> {
        if (isBlank(fileName))
            throw new BlueException(INVALID_PARAM);

        int lastIndex = lastIndexOf(fileName, SCHEME_SEPARATOR);
        if (lastIndex < 0 || lastIndex == fileName.length() - 1)
            throw new BlueException(INVALID_PARAM);

        return substring(fileName, lastIndex + 1);
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

}
