package com.blue.jwt.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.blue.jwt.api.conf.JwtConf;
import com.blue.jwt.exception.AuthenticationException;
import org.slf4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.*;

import static com.auth0.jwt.JWT.require;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.blue.jwt.constant.JwtConfSchema.*;
import static com.blue.jwt.constant.JwtResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.jwt.constant.JwtResponseElement.UNAUTHORIZED;
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
 * @author DarkBlue
 */
@SuppressWarnings({"WeakerAccess", "JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class BlueJwtProcessor<T> implements JwtProcessor<T> {

    private static final Logger LOGGER = getLogger(BlueJwtProcessor.class);

    //<editor-fold desc="jwt configs">
    /**
     * header info
     */
    private static final String HEADER_TYPE_NAME = "Type", HEADER_TYPE_VALUE = "Jwt";
    private static final String HEADER_ALG_NAME = "alg", HEADER_ALG_VALUE = "HS512";

    /**
     * header
     */
    private static final Map<String, Object> JWT_HEADER = new HashMap<>(4, 1.0f);

    static {
        JWT_HEADER.put(HEADER_TYPE_NAME, HEADER_TYPE_VALUE);
        JWT_HEADER.put(HEADER_ALG_NAME, HEADER_ALG_VALUE);
    }

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
     * maximum expiration time/Maximum validity period for certification
     */
    private final long MAX_EXPIRE_MILLIS;

    /**
     * minimum expiration time/certification expiration time after the last operation
     */
    private final long MIN_EXPIRE_MILLIS;

    /**
     * encrypt algorithm
     */
    private final Algorithm ALGORITHM;

    /**
     * verifier
     */
    private final JWTVerifier VERIFIER;

    /**
     * abstract function used to convert authentication information entity into payload
     */
    private final Function<T, Map<String, String>> DATA_2_CLAIM_PROCESSOR;

    /**
     * abstract function used to convert payload into authentication information entity
     */
    private final Function<Map<String, String>, T> CLAIM_2_DATA_PROCESSOR;

    /**
     * issuer
     */
    private final String ISSUER;

    /**
     * subject
     */
    private final String SUBJECT;

    /**
     * audience
     */
    private final String AUDIENCE;

    /**
     * gamma secret keys
     */
    private String[] gammaSecretArr;

    /**
     * gamma secret arr max index
     */
    private int gammaSecretArrIndexMask;
    //</editor-fold>

    /**
     * expire key
     */
    private static final String EXPIRES_AT_STAMP_KEY = "blueExpiresAtStamp";

    /**
     * date generator
     */
    private static final Function<Long, Date> DATE_GEN = Date::new;

    /**
     * timestamp generator
     */
    private static final Supplier<Long> TIME_STAMP_SUP = System::currentTimeMillis;

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
        return gammaSecretArr[~h & gammaSecretArrIndexMask]
                + gammaSecretArr[EXPIRES_AT_STAMP_DISCRETE_PROCESSOR.apply(expiresAtStamp) & gammaSecretArrIndexMask]
                + jwtId
                + gammaSecretArr[h & gammaSecretArrIndexMask];
    };

    /**
     * jwt keyId generator
     */
    private final BiFunction<String, Long, String> KEY_ID_GENERATOR = (jwtId, expiresAtStamp) ->
            sha512Hex(MIX_UP_PROCESSOR.apply(jwtId, expiresAtStamp));

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

        this.MAX_EXPIRE_MILLIS = jwtConf.getMaxExpireMillis();
        this.MIN_EXPIRE_MILLIS = jwtConf.getMinExpireMillis();

        this.ALGORITHM = HMAC512(jwtConf.getSignKey());
        this.VERIFIER = require(ALGORITHM).build();

        this.gammaSecretArr = jwtConf.getGammaSecrets().toArray(String[]::new);
        this.gammaSecretArrIndexMask = gammaSecretArr.length - 1;

        this.DATA_2_CLAIM_PROCESSOR = jwtConf.getDataToClaimProcessor();
        this.CLAIM_2_DATA_PROCESSOR = jwtConf.getClaimToDataProcessor();

        this.ISSUER = jwtConf.getIssuer();
        this.SUBJECT = jwtConf.getSubject();
        this.AUDIENCE = jwtConf.getAudience();
    }

    /**
     * create jwt
     *
     * @param t
     * @return
     */
    @Override
    public String create(T t) {
        if (t == null) {
            LOGGER.error("String create(T t), t can't be null");
            throw new AuthenticationException(INTERNAL_SERVER_ERROR);
        }

        long currentStamp = TIME_STAMP_SUP.get();
        long expiresAtStamp = currentStamp + MAX_EXPIRE_MILLIS;
        try {
            JWTCreator.Builder builder = JWT.create().withHeader(JWT_HEADER);

            String jwtId = randomAlphanumeric(RANDOM_JWT_ID_LEN);
            builder.withJWTId(jwtId).withKeyId(KEY_ID_GENERATOR.apply(jwtId, expiresAtStamp));
            builder.withClaim(EXPIRES_AT_STAMP_KEY, expiresAtStamp);

            ofNullable(DATA_2_CLAIM_PROCESSOR.apply(t))
                    .ifPresent(cm -> cm.forEach(builder::withClaim));

            builder.withIssuer(ISSUER).withSubject(SUBJECT).withAudience(AUDIENCE);

            Date date = DATE_GEN.apply(currentStamp);
            builder.withIssuedAt(date).withNotBefore(date).withExpiresAt(DATE_GEN.apply(expiresAtStamp));

            return builder.sign(ALGORITHM);
        } catch (Exception e) {
            LOGGER.error("String create(T t), failed, t = {}, e = {}", t, e);
            throw new AuthenticationException(INTERNAL_SERVER_ERROR);
        }
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
                                        ofNullable(e.getValue()).map(Claim::asString).orElse(""), (a, b) -> a)));
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
    public long getMaxExpireMillis() {
        return MAX_EXPIRE_MILLIS;
    }

    /**
     * get the lower limit of the expiration time of jwt
     *
     * @return
     */
    @Override
    public long getMinExpireMillis() {
        return MIN_EXPIRE_MILLIS;
    }

    /**
     * assert params
     *
     * @param conf
     * @param <T>
     */
    private static <T> void assertConf(JwtConf<T> conf) {
        Long minExpireMillis = conf.getMinExpireMillis();
        if (minExpireMillis == null || minExpireMillis < 0L)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "minExpireMillis can't be null or less than 0L");
        Long maxExpireMillis = conf.getMaxExpireMillis();
        if (maxExpireMillis == null || maxExpireMillis < 0L)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "maxExpireMillis can't be null or less than 0L");
        if (maxExpireMillis <= minExpireMillis)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "maxExpireMillis can't less than minExpireMillis");

        String signKey = conf.getSignKey();
        if (signKey == null || "".equals(signKey))
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "signKey can't be blank");
        int secKeyLen = signKey.length();
        if (secKeyLen < SEC_KEY_STR_MIN_LEN || secKeyLen > SEC_KEY_STR_MAX_LEN)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "signKey len can't less than " + SEC_KEY_STR_MIN_LEN + " or greater than " + SEC_KEY_STR_MAX_LEN);

        List<String> gammaSecrets = conf.getGammaSecrets();
        if (gammaSecrets == null || gammaSecrets.size() < 1)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "gammaSecrets can't be empty");

        int gammaSecretSize = gammaSecrets.size();
        if (gammaSecretSize < GAMMA_SECRETS_MIN_LEN || gammaSecretSize > GAMMA_SECRETS_MAX_LEN)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "gammaSecret's element size can't be less than " + GAMMA_SECRETS_MIN_LEN + " or greater than " + GAMMA_SECRETS_MAX_LEN);

        if (Integer.bitCount(gammaSecretSize) != 1)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "gammaSecret's size must be power of 2");
        gammaSecrets = gammaSecrets.stream()
                .peek(gs -> {
                    if (isBlank(gs))
                        throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "gammaSecrets can't contains null element");
                    int gsLen = gs.length();
                    if (gsLen < GAMMA_KEY_STR_MIN_LEN || gsLen > GAMMA_KEY_STR_MAX_LEN)
                        throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "gammaSecret's element length can't less than " + GAMMA_KEY_STR_MIN_LEN + " or greater than " + GAMMA_KEY_STR_MAX_LEN + ", gammaSecret -> " + gs);
                }).distinct().collect(toList());
        if (gammaSecrets.size() != gammaSecretSize)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "gammaSecrets elements can't be same");

        if (conf.getDataToClaimProcessor() == null)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "dataToClaimProcessor can't be null");
        if (conf.getClaimToDataProcessor() == null)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "claimToDataProcessor can't be null");

        if (isBlank(conf.getIssuer()))
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "issuer can't be blank");
        if (isBlank(conf.getSubject()))
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "subject can't be blank");
        if (isBlank(conf.getAudience()))
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "audience can't be blank");
    }

}
