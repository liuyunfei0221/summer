package com.blue.jwt.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.blue.jwt.api.conf.JwtConf;
import com.blue.jwt.exception.AuthenticationException;
import org.slf4j.Logger;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.*;

import static com.auth0.jwt.HeaderParams.CONTENT_TYPE;
import static com.auth0.jwt.JWT.require;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.blue.jwt.constant.JwtConfSchema.*;
import static com.blue.jwt.constant.JwtResponseElement.UNAUTHORIZED;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.codec.digest.DigestUtils.sha512Hex;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * default impl of JwtProcessor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"WeakerAccess", "JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class BlueJwtProcessor<T> implements JwtProcessor<T> {

    private static final Logger LOGGER = getLogger(BlueJwtProcessor.class);

    //<editor-fold desc="jwt configs">

    /**
     * content type
     */
    private static final String HEADER_CONTENT_TYPE_NAME = CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE = "application/jwt";

    /**
     * header
     */
    private static final Map<String, Object> COMMON_HEADER = new HashMap<>(1, 2.0f);

    static {
        COMMON_HEADER.put(HEADER_CONTENT_TYPE_NAME, HEADER_CONTENT_TYPE_VALUE);
    }

    private static final String ISSUER = "Blue", SUBJECT = "Hello", AUDIENCE = "Bluer";

    private static final String EMPTY_DATA = "";

    /**
     * constants
     */
    private static final int
            RANDOM_JWT_ID_LEN = RANDOM_JWT_ID.len,
            SEC_KEY_STR_MIN_LEN = SEC_KEY_STR_MIN.len,
            SEC_KEY_STR_MAX_LEN = SEC_KEY_STR_MAX.len,
            GAMMA_KEY_STR_MIN_LEN = GAMMA_KEY_STR_MIN.len,
            GAMMA_KEY_STR_MAX_LEN = GAMMA_KEY_STR_MAX.len,
            GAMMA_SECRETS_MIN_LEN = GAMMA_SECRETS_MIN.len,
            GAMMA_SECRETS_MAX_LEN = GAMMA_SECRETS_MAX.len;

    /**
     * randoms
     */
    private static final int
            RANDOM_NUM_1 = 2,
            RANDOM_NUM_2 = 7,
            RANDOM_NUM_3 = 5,
            RANDOM_NUM_4 = 3,
            RANDOM_NUM_5 = 2;

    /**
     * maximum expiration time/Maximum validity period for certification
     */
    private transient long maxExpiresMillis;

    /**
     * minimum expiration time/certification expiration time after the last operation
     */
    private final transient long minExpiresMillis;

    /**
     * refresh token expiration time after the last operation
     */
    private final transient long refreshExpiresMillis;

    /**
     * encrypt algorithm
     */
    private final transient Algorithm ALGORITHM;

    /**
     * verifier
     */
    private final transient JWTVerifier VERIFIER;

    /**
     * abstract function used to convert authentication information entity into payload
     */
    private final transient Function<T, Map<String, String>> DATA_2_CLAIM_PROCESSOR;

    /**
     * abstract function used to convert payload into authentication information entity
     */
    private final transient Function<Map<String, String>, T> CLAIM_2_DATA_PROCESSOR;

    /**
     * randoms
     */
    private transient String
            randomSalt1,
            randomSalt2,
            randomSalt3,
            randomSalt4,
            randomSalt5;

    /**
     * gamma secret keys
     */
    private transient String[] gammaSecretArr;

    /**
     * gamma secret arr max index
     */
    private transient int gammaSecretArrIndexMask;
    //</editor-fold>

    /**
     * expire key
     */
    private static final String EXPIRES_AT_STAMP_KEY = "eas";

    /**
     * instant generator
     */
    private static final Function<Long, Instant> INSTANT_GEN = Instant::ofEpochMilli;

    /**
     * timestamp generator
     */
    private static final Supplier<Long> MILLIS_STAMP_SUP = System::currentTimeMillis;

    /**
     * hash discrete processor
     */
    private static final UnaryOperator<Integer> HASH_DISCRETE_PROCESSOR = hash ->
            hash ^ (hash >>> 16);

    /**
     * expiresAtStamp discrete processor
     */
    private static final Function<Long, Integer> EXPIRES_AT_STAMP_DISCRETE_PROCESSOR = expiresAtStamp ->
            HASH_DISCRETE_PROCESSOR.apply((int) ((expiresAtStamp << 31) >>> 31));

    /**
     * mix up processor
     */
    private final BiFunction<String, Long, String> MIX_UP_PROCESSOR = (jwtId, expiresAtStamp) -> {
        int h = HASH_DISCRETE_PROCESSOR.apply(jwtId.hashCode());
        return randomSalt1
                + gammaSecretArr[~h & gammaSecretArrIndexMask]
                + randomSalt2
                + gammaSecretArr[EXPIRES_AT_STAMP_DISCRETE_PROCESSOR.apply(expiresAtStamp) & gammaSecretArrIndexMask]
                + randomSalt3
                + jwtId
                + randomSalt4
                + gammaSecretArr[h & gammaSecretArrIndexMask]
                + randomSalt5;
    };

    /**
     * jwt keyId generator
     */
    private final BiFunction<String, Long, String> KEY_ID_GENERATOR = (jwtId, expiresAtStamp) ->
            sha512Hex(MIX_UP_PROCESSOR.apply(jwtId, expiresAtStamp));

    /**
     * commons packager
     */
    private final Consumer<JWTCreator.Builder> JWT_COMMON_PACKAGER = builder -> {
        long currentStamp = MILLIS_STAMP_SUP.get();
        long expiresAtStamp = currentStamp + maxExpiresMillis;

        String jwtId = randomAlphanumeric(RANDOM_JWT_ID_LEN);
        builder.withJWTId(jwtId).withKeyId(KEY_ID_GENERATOR.apply(jwtId, expiresAtStamp));
        builder.withClaim(EXPIRES_AT_STAMP_KEY, expiresAtStamp);

        Instant instant = INSTANT_GEN.apply(currentStamp);
        builder.withIssuedAt(instant).withNotBefore(instant).withExpiresAt(INSTANT_GEN.apply(expiresAtStamp));

        builder.withIssuer(ISSUER).withSubject(SUBJECT).withAudience(AUDIENCE);
    };

    /**
     * jwt keyId asserter
     */
    private final Consumer<DecodedJWT> JWT_ASSERTER = jwt -> {
        long expiresAtStamp = ofNullable(jwt.getClaim(EXPIRES_AT_STAMP_KEY))
                .map(Claim::asLong).orElse(0L);

        if (jwt.getKeyId().equals(KEY_ID_GENERATOR.apply(jwt.getId(), expiresAtStamp)))
            return;
        throw new AuthenticationException(UNAUTHORIZED);
    };

    /**
     * construct
     *
     * @param jwtConf
     */
    public BlueJwtProcessor(JwtConf<T> jwtConf) {
        assertConf(jwtConf);

        this.maxExpiresMillis = jwtConf.getMaxExpiresMillis();
        this.minExpiresMillis = jwtConf.getMinExpiresMillis();
        this.refreshExpiresMillis = jwtConf.getRefreshExpiresMillis();

        String signKey = jwtConf.getSignKey();
        int signKeyLen = signKey.length();

        this.randomSalt1 = String.valueOf(signKey.charAt(signKeyLen - RANDOM_NUM_1));
        this.randomSalt2 = String.valueOf(signKey.charAt(signKeyLen / RANDOM_NUM_2));
        this.randomSalt3 = String.valueOf(signKey.charAt(signKeyLen / RANDOM_NUM_3));
        this.randomSalt4 = String.valueOf(signKey.charAt(signKeyLen / RANDOM_NUM_4));
        this.randomSalt5 = String.valueOf(signKey.charAt(signKeyLen / RANDOM_NUM_5));

        this.ALGORITHM = HMAC512(signKey);
        this.VERIFIER = require(ALGORITHM).build();

        this.gammaSecretArr = jwtConf.getGammaSecrets().toArray(String[]::new);
        this.gammaSecretArrIndexMask = gammaSecretArr.length - 1;

        this.DATA_2_CLAIM_PROCESSOR = jwtConf.getDataToClaimProcessor();
        this.CLAIM_2_DATA_PROCESSOR = jwtConf.getClaimToDataProcessor();
    }

    /**
     * create jwt
     *
     * @param t
     * @return
     */
    @Override
    public String create(T t) {
        if (nonNull(t)) {
            try {
                JWTCreator.Builder builder = JWT.create()
                        .withHeader(COMMON_HEADER);

                ofNullable(DATA_2_CLAIM_PROCESSOR.apply(t))
                        .ifPresent(cm -> cm.forEach(builder::withClaim));

                JWT_COMMON_PACKAGER.accept(builder);

                return builder.sign(ALGORITHM);
            } catch (Exception e) {
                LOGGER.error("String create(T t), failed, t = {}, e = {}", t, e);
                throw new RuntimeException("String create(T t), failed, t = " + t + ", e = " + e);
            }
        }

        LOGGER.error("String create(T t), t can't be null");
        throw new AuthenticationException(UNAUTHORIZED);
    }

    /**
     * parse jwt
     *
     * @param jwtToken
     * @return
     */
    @Override
    public T parse(String jwtToken) {
        if (isNotEmpty(jwtToken))
            try {
                DecodedJWT jwt = VERIFIER.verify(jwtToken);
                JWT_ASSERTER.accept(jwt);

                return CLAIM_2_DATA_PROCESSOR.apply(
                        jwt.getClaims().entrySet().stream()
                                .collect(toMap(Map.Entry::getKey, e ->
                                        ofNullable(e.getValue()).map(Claim::asString).orElse(EMPTY_DATA), (a, b) -> a)));
            } catch (Exception e) {
                throw new AuthenticationException(UNAUTHORIZED);
            }

        throw new AuthenticationException(UNAUTHORIZED);
    }

    /**
     * get the upper limit of the expiration time of jwt
     *
     * @return
     */
    @Override
    public long getMaxExpiresMillis() {
        return maxExpiresMillis;
    }

    /**
     * get the lower limit of the expiration time of jwt
     *
     * @return
     */
    @Override
    public long getMinExpiresMillis() {
        return minExpiresMillis;
    }

    /**
     * get expire millis of the expiration time of refresh token
     *
     * @return
     */
    @Override
    public long getRefreshExpiresMillis() {
        return refreshExpiresMillis;
    }

    /**
     * assert params
     *
     * @param conf
     * @param <T>
     */
    private static <T> void assertConf(JwtConf<T> conf) {
        Long minExpiresMillis = conf.getMinExpiresMillis();
        if (isNull(minExpiresMillis) || minExpiresMillis < 0L)
            throw new RuntimeException("minExpiresMillis can't be null or less than 0L");
        Long maxExpiresMillis = conf.getMaxExpiresMillis();
        if (isNull(maxExpiresMillis) || maxExpiresMillis < 0L)
            throw new RuntimeException("maxExpiresMillis can't be null or less than 0L");
        if (maxExpiresMillis <= minExpiresMillis)
            throw new RuntimeException("maxExpiresMillis can't less than minExpiresMillis");

        String signKey = conf.getSignKey();
        if (isBlank(signKey))
            throw new RuntimeException("signKey can't be blank");
        int secKeyLen = signKey.length();
        if (secKeyLen < SEC_KEY_STR_MIN_LEN || secKeyLen > SEC_KEY_STR_MAX_LEN)
            throw new RuntimeException("signKey len can't less than " + SEC_KEY_STR_MIN_LEN + " or greater than " + SEC_KEY_STR_MAX_LEN);

        List<String> gammaSecrets = conf.getGammaSecrets();
        if (isNull(gammaSecrets) || gammaSecrets.size() < 1)
            throw new RuntimeException("gammaSecrets can't be empty");

        int gammaSecretSize = gammaSecrets.size();
        if (gammaSecretSize < GAMMA_SECRETS_MIN_LEN || gammaSecretSize > GAMMA_SECRETS_MAX_LEN)
            throw new RuntimeException("gammaSecret's element size can't be less than " + GAMMA_SECRETS_MIN_LEN + " or greater than " + GAMMA_SECRETS_MAX_LEN);
        if (Integer.bitCount(gammaSecretSize) != 1)
            throw new RuntimeException("gammaSecret's size must be power of 2");

        gammaSecrets = gammaSecrets.stream()
                .peek(gs -> {
                    if (isBlank(gs))
                        throw new RuntimeException("gammaSecrets can't contains null element");
                    int gsLen = gs.length();
                    if (gsLen < GAMMA_KEY_STR_MIN_LEN || gsLen > GAMMA_KEY_STR_MAX_LEN)
                        throw new RuntimeException("gammaSecret's element length can't less than " + GAMMA_KEY_STR_MIN_LEN + " or greater than " + GAMMA_KEY_STR_MAX_LEN + ", gammaSecret -> " + gs);
                }).distinct().collect(toList());
        if (gammaSecrets.size() != gammaSecretSize)
            throw new RuntimeException("gammaSecrets elements can't be same");

        if (isNull(conf.getDataToClaimProcessor()))
            throw new RuntimeException("dataToClaimProcessor can't be null");
        if (isNull(conf.getClaimToDataProcessor()))
            throw new RuntimeException("claimToDataProcessor can't be null");
    }

}
