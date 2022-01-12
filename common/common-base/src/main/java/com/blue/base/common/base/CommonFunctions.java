package com.blue.base.common.base;

import com.blue.base.component.exception.common.ExceptionProcessor;
import com.blue.base.constant.base.*;
import com.blue.base.model.base.*;
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

import static com.blue.base.common.base.RsaProcessor.*;
import static com.blue.base.constant.base.BlueDataAttrKey.*;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Symbol.PAIR_SEPARATOR;
import static java.lang.System.currentTimeMillis;
import static java.time.Instant.now;
import static java.util.Arrays.asList;
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
 * @author DarkBlue
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
            UNKNOWN = Symbol.UNKNOWN.identity;

    /**
     * auth header key
     */
    public static final String AUTHORIZATION = BlueHeader.AUTHORIZATION.name;

    /**
     * rate limiter key prefix
     */
    public static final String RATE_LIMIT_KEY_PREFIX = Symbol.RATE_LIMIT_KEY_PRE.identity;

    /**
     * random key length
     */
    public static final int RAN_KEY_STR_LEN = 8;

    /**
     * index of non element
     */
    public static final int NON_EXIST_INDEX = -1;

    /**
     * clock
     */
    public static final Clock CLOCK = SummerAttr.CLOCK;

    /**
     * limiter script keys prefix and suffix
     */
    public static final String KEY_PREFIX = "r_r_li_";
    public static final String TOKEN_SUFFIX = "_tks", STAMP_SUFFIX = "_tst";

    /**
     * valid freemarker /.html/.js
     */
    public static final Set<String> VALID_TAILS = of(ValidResourceFormatters.values())
            .map(vrf -> vrf.identity).collect(toSet());

    /**
     * uri parser - for request
     */
    public static final UnaryOperator<String> REST_URI_PROCESSOR = uri -> {
        if (uri == null || "".equals(uri))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "uri can't be null");

        int lastPartIdx = lastIndexOf(uri, PATH_SEPARATOR);
        if (lastPartIdx == -1 || lastPartIdx == length(uri))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "invalid uri, not contains / -> " + uri);

        String maybePathVariable = substring(uri, lastPartIdx + 1);
        if (isDigits(maybePathVariable))
            return (substring(uri, 0, lastPartIdx).intern() + PATH_SEPARATOR + WILDCARD).intern();

        int schemaIdx = lastIndexOf(maybePathVariable, SCHEME_SEPARATOR);
        String schema;
        if (schemaIdx != -1 && VALID_TAILS.contains(schema = substring(maybePathVariable, schemaIdx)))
            return (substring(uri, 0, lastPartIdx).intern() + PATH_SEPARATOR + WILDCARD + schema.intern()).intern();

        return uri.intern();
    };

    /**
     * uri asserter
     */
    public static final Consumer<String> REST_URI_ASSERTER = uri -> {
        if (uri == null || "".equals(uri))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "uri can't be null");

        String tar = trim(uri);
        int idx = indexOf(tar, PATH_SEPARATOR);

        if (idx == -1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "invalid uri, not contains / -> " + uri);
        if (idx != 0)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "invalid uri, not start with / -> " + uri);
        if (isBlank(substring(tar, idx)))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "invalid uri, non content but / -> " + uri);
    };

    /**
     * uri parser - for init
     */
    public static final UnaryOperator<String> REST_URI_CONVERTER = uri -> {
        REST_URI_ASSERTER.accept(uri);

        int lastPartIdx = lastIndexOf(uri, PATH_SEPARATOR);
        if (lastPartIdx == -1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "invalid uri, not contains / -> " + uri);

        String maybePathVariable = substring(uri, lastPartIdx);

        int left = indexOf(maybePathVariable, "{");
        int right = lastIndexOf(maybePathVariable, "}");

        if (left == -1 && right == -1)
            return uri.intern();

        int maybePathVariableLength = length(maybePathVariable);
        if (right == maybePathVariableLength - 1)
            return (substring(uri, 0, lastPartIdx).intern() + PATH_SEPARATOR + WILDCARD).intern();

        String schema = substring(maybePathVariable, right + 1).intern();
        if (VALID_TAILS.contains(schema))
            return (substring(uri, 0, lastPartIdx).intern() + PATH_SEPARATOR + WILDCARD + schema.intern()).intern();

        throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "invalid uri, freemarker unsupported -> " + uri);
    };

    /**
     * stamp getter/seconds
     */
    public static final Supplier<Long> TIME_STAMP_GETTER = () -> now(CLOCK).getEpochSecond();

    /**
     * limit key generator
     */
    public static final Function<String, List<String>> LIMIT_KEYS_GENERATOR = id -> {
        String prefix = KEY_PREFIX + id;
        return asList(prefix + TOKEN_SUFFIX, prefix + STAMP_SUFFIX);
    };

    /**
     * resource key generator for request
     */
    public static final BinaryOperator<String> REQ_RES_KEY_CONVERTER = (method, uri) ->
            ((upperCase(method).intern() + PAR_CONCATENATION + REST_URI_PROCESSOR.apply(uri).intern()).intern()).intern();

    /**
     * resource key generator for request
     */
    public static final BinaryOperator<String> REQ_RES_KEY_GENERATOR = (method, uri) ->
            (upperCase(method).intern() + PAR_CONCATENATION + uri).intern();

    /**
     * resource key generator for init
     */
    public static final BinaryOperator<String> INIT_RES_KEY_GENERATOR = (method, uri) ->
            (upperCase(method).intern() + PAR_CONCATENATION + REST_URI_CONVERTER.apply(uri).intern()).intern();

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

        throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_REQUEST_METHOD.message);
    };

    /**
     * method asserter
     */
    public static final Consumer<String> METHOD_VALUE_ASSERTER = method -> {
        if (VALID_METHODS.contains(upperCase(method)))
            return;

        throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_REQUEST_METHOD.message);
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

        throw new BlueException(RSA_FAILED);
    };

    private static final Set<String> UN_PACK_KEYS = Stream.of(REQUEST_BODY.key, RESPONSE_STATUS.key, RESPONSE_BODY.key)
            .collect(toSet());

    private static final List<String> ATTR_KEYS = Stream.of(BlueDataAttrKey.values())
            .map(ak -> ak.key).filter(k -> !UN_PACK_KEYS.contains(k)).collect(toList());

    /**
     * package info to event
     */
    public static final BiConsumer<Map<String, Object>, DataEvent> EVENT_PACKAGER = (attributes, dataEvent) -> {
        if (attributes != null && dataEvent != null)
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
        if (requestBody == null || "".equals(requestBody))
            throw new BlueException(BAD_REQUEST);

        if (secKey == null || "".equals(secKey))
            throw new BlueException(BAD_REQUEST);

        EncryptedRequest encryptedRequest = GSON.fromJson(requestBody, EncryptedRequest.class);
        String encrypted = encryptedRequest.getEncrypted();

        if (!verify(encrypted, encryptedRequest.getSignature(), secKey))
            throw new BlueException(RSA_FAILED);

        return DATA_CONVERTER.apply(GSON.fromJson(decryptByPublicKey(encrypted, secKey), DataWrapper.class), expire);
    }

    /**
     * encrypt response body
     *
     * @param responseBody
     * @param secKey
     * @return
     */
    public static String encryptResponseBody(String responseBody, String secKey) {
        if (responseBody == null || "".equals(responseBody))
            throw new BlueException(BAD_REQUEST);

        if (secKey == null || "".equals(secKey))
            throw new BlueException(BAD_REQUEST);

        return GSON.toJson(new EncryptedResponse(encryptByPublicKey(GSON.toJson(new DataWrapper(responseBody, TIME_STAMP_GETTER.get())), secKey)));
    }

}
