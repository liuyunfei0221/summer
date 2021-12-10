package com.blue.base.common.base;

import com.blue.base.component.exception.common.ExceptionProcessor;
import com.blue.base.constant.base.BlueHeader;
import com.blue.base.constant.base.SummerAttr;
import com.blue.base.constant.base.Symbol;
import com.blue.base.constant.base.ValidResourceFormatters;
import com.blue.base.model.base.DataWrapper;
import com.blue.base.model.base.EncryptedRequest;
import com.blue.base.model.base.EncryptedResponse;
import com.blue.base.model.base.ExceptionResponse;
import com.blue.base.model.exps.BlueException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import reactor.util.Logger;

import java.time.Clock;
import java.util.List;
import java.util.Set;
import java.util.function.*;

import static com.blue.base.common.base.RsaProcessor.*;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.ResponseMessage.INVALID_REQUEST_METHOD;
import static com.blue.base.constant.base.ResponseMessage.RSA_FAILED;
import static com.blue.base.constant.base.Symbol.PAIR_SEPARATOR;
import static java.lang.System.currentTimeMillis;
import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.math.NumberUtils.isDigits;
import static reactor.util.Loggers.getLogger;

/**
 * Common function set
 *
 * @author DarkBlue
 */
@SuppressWarnings({"WeakerAccess", "JavaDoc", "AliControlFlowStatementWithoutBraces"})
public class CommonFunctions {

    public static final Logger LOGGER = getLogger(CommonFunctions.class);

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
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "uri can't be null", null);

        int lastPartIdx = lastIndexOf(uri, PATH_SEPARATOR);
        if (lastPartIdx == -1 || lastPartIdx == length(uri))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "invalid uri, not contains / -> " + uri, null);

        String maybePathVariable = uri.substring(lastPartIdx + 1);
        if (isDigits(maybePathVariable))
            return (uri.substring(0, lastPartIdx).intern() + PATH_SEPARATOR + WILDCARD).intern();

        int schemaIdx = lastIndexOf(maybePathVariable, SCHEME_SEPARATOR);
        String schema;
        if (schemaIdx != -1 && VALID_TAILS.contains(schema = maybePathVariable.substring(schemaIdx)))
            return (uri.substring(0, lastPartIdx).intern() + PATH_SEPARATOR + WILDCARD + schema.intern()).intern();

        return uri.intern();
    };

    /**
     * uri asserter
     */
    public static final Consumer<String> REST_URI_ASSERTER = uri -> {
        if (uri == null || "".equals(uri))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "uri can't be null", null);

        String tar = uri.trim();

        int idx = indexOf(tar, PATH_SEPARATOR);

        if (idx == -1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "invalid uri, not contains / -> " + uri, null);
        if (idx != 0)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "invalid uri, not start with / -> " + uri, null);
        if (isBlank(substring(tar, idx)))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "invalid uri, non content but / -> " + uri, null);
    };

    /**
     * uri parser - for init
     */
    public static final UnaryOperator<String> REST_URI_CONVERTER = uri -> {
        REST_URI_ASSERTER.accept(uri);

        int lastPartIdx = lastIndexOf(uri, PATH_SEPARATOR);
        if (lastPartIdx == -1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "invalid uri, not contains / -> " + uri, null);

        String maybePathVariable = uri.substring(lastPartIdx);
        int left = indexOf(maybePathVariable, "{");
        int right = lastIndexOf(maybePathVariable, "}");

        if (left == -1 && right == -1)
            return uri.intern();

        int maybePathVariableLength = length(maybePathVariable);
        if (right == maybePathVariableLength - 1)
            return (uri.substring(0, lastPartIdx).intern() + PATH_SEPARATOR + WILDCARD).intern();

        String schema = maybePathVariable.substring(right + 1).intern();
        if (VALID_TAILS.contains(schema))
            return (uri.substring(0, lastPartIdx).intern() + PATH_SEPARATOR + WILDCARD + schema.intern()).intern();

        throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "invalid uri, freemarker unsupported -> " + uri, null);
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
    public static final BinaryOperator<String> REQ_RES_KEY_GENERATOR = (method, uri) ->
            ((method.toUpperCase().intern() + PAR_CONCATENATION + REST_URI_PROCESSOR.apply(uri).intern()).intern()).intern();

    /**
     * resource key generator for init
     */
    public static final BinaryOperator<String> INIT_RES_KEY_GENERATOR = (method, uri) ->
            (method.toUpperCase().intern() + PAR_CONCATENATION + REST_URI_CONVERTER.apply(uri).intern()).intern();

    /**
     * header value getter
     */
    public static final UnaryOperator<String> HEADER_VALUE_CONVERTER = contentType -> {
        int idx = indexOf(contentType, PAIR_SEPARATOR.identity);
        return idx == NON_EXIST_INDEX ? contentType : contentType.substring(0, idx);
    };

    /**
     * valid schema
     */
    public static final Set<String> VALID_SCHEMAS = of("https", "http")
            .map(String::toLowerCase)
            .collect(toSet());

    /**
     * valid method
     */
    public static final Set<String> VALID_METHODS = of(HttpMethod.values())
            .map(Enum::name)
            .map(String::toUpperCase)
            .collect(toSet());

    /**
     * schema asserter
     */
    public static final Consumer<String> SCHEMA_ASSERTER = schema -> {
        if (VALID_SCHEMAS.contains(schema))
            return;

        throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_REQUEST_METHOD.message, null);
    };

    /**
     * method asserter
     */
    public static final Consumer<String> METHOD_VALUE_ASSERTER = method -> {
        if (VALID_METHODS.contains(method.toUpperCase()))
            return;

        throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_REQUEST_METHOD.message, null);
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

        throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, RSA_FAILED.message, null);
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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message, null);

        if (secKey == null || "".equals(secKey))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message, null);

        EncryptedRequest encryptedRequest = GSON.fromJson(requestBody, EncryptedRequest.class);
        String encrypted = encryptedRequest.getEncrypted();

        if (!verify(encrypted, encryptedRequest.getSignature(), secKey))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, RSA_FAILED.message, null);

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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message, null);

        if (secKey == null || "".equals(secKey))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message, null);

        return GSON.toJson(new EncryptedResponse(encryptByPublicKey(GSON.toJson(new DataWrapper(responseBody, TIME_STAMP_GETTER.get())), secKey)));
    }

}
