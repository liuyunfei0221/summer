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
 * @author DarkBlue
 */
@SuppressWarnings({"WeakerAccess", "JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class BlueJwtProcessor<T> implements JwtProcessor<T> {

    private static final Logger LOGGER = getLogger(BlueJwtProcessor.class);

    //<editor-fold desc="jwt配置信息">
    /**
     * header信息
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
     * 常量配置
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
     * 最大过期时间/用于认证的最大有效期
     */
    private final long MAX_EXPIRE_MILLIS;

    /**
     * 最小过期时间/用于最后一次操作之后的认证过期时间
     */
    private final long MIN_EXPIRE_MILLIS;

    /**
     * 加密
     */
    private final Algorithm ALGORITHM;

    /**
     * 验证
     */
    private final JWTVerifier VERIFIER;

    /**
     * 抽象的用于将认证信息实体转换为payload的函数
     */
    private final Function<T, Map<String, String>> DATA_2_CLAIM_PROCESSOR;

    /**
     * 抽象的用于将payload转换为认证信息实体的函数
     */
    private final Function<Map<String, String>, T> CLAIM_2_DATA_PROCESSOR;

    /**
     * 签发者
     */
    private final String ISSUER;

    /**
     * 主题
     */
    private final String SUBJECT;

    /**
     * 受众
     */
    private final String AUDIENCE;

    /**
     * 混淆key
     */
    private String[] gammaSecretArr;

    /**
     * 混淆key最大角标
     */
    private int gammaSecretArrIndexMask;
    //</editor-fold>

    /**
     * 过期时间戳claim key
     */
    private static final String EXPIRES_AT_STAMP_KEY = "blueExpiresAtStamp";

    /**
     * 日期构建器
     */
    private static final Function<Long, Date> DATE_GEN = Date::new;

    /**
     * 时间戳生成器
     */
    private static final Supplier<Long> TIME_STAMP_SUP = System::currentTimeMillis;

    /**
     * hash离散器
     */
    private static final UnaryOperator<Integer> HASH_DISCRETE_PROCESSOR = hash ->
            hash ^ (hash >>> 16);

    /**
     * expiresAtStamp离散器
     */
    private static final Function<Long, Integer> EXPIRES_AT_STAMP_DISCRETE_PROCESSOR = expiresAtStamp ->
            HASH_DISCRETE_PROCESSOR.apply((int) ((expiresAtStamp << 31) >>> 31));

    /**
     * 混淆器
     */
    private final BiFunction<String, Long, String> MIX_UP_GENERATOR = (jwtId, expiresAtStamp) -> {
        int h = HASH_DISCRETE_PROCESSOR.apply(jwtId.hashCode());
        return gammaSecretArr[~h & gammaSecretArrIndexMask]
                + gammaSecretArr[EXPIRES_AT_STAMP_DISCRETE_PROCESSOR.apply(expiresAtStamp) & gammaSecretArrIndexMask]
                + jwtId
                + gammaSecretArr[h & gammaSecretArrIndexMask];
    };

    /**
     * jwt keyId 生成器
     */
    private final BiFunction<String, Long, String> KEY_ID_GENERATOR = (jwtId, expiresAtStamp) ->
            sha512Hex(MIX_UP_GENERATOR.apply(jwtId, expiresAtStamp));

    /**
     * jwt keyId 断言器
     */
    private final Consumer<DecodedJWT> JWT_ASSERT = jwt -> {
        long expiresAtStamp = ofNullable(jwt.getClaim(EXPIRES_AT_STAMP_KEY))
                .map(Claim::asLong).orElse(0L);

        if (jwt.getKeyId().equals(KEY_ID_GENERATOR.apply(jwt.getId(), expiresAtStamp)))
            return;
        throw new AuthenticationException(UNAUTHORIZED.status, UNAUTHORIZED.code, UNAUTHORIZED.message);
    };

    /**
     * 构造
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
     * 创建jwt
     *
     * @param t
     * @return
     */
    @Override
    public String create(T t) {
        if (t == null) {
            LOGGER.error("create(T t), t不能为空");
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message);
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
            LOGGER.error("create(T t), 创建jwt失败, t = {}, e = {}", t, e);
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message);
        }
    }

    /**
     * 解析jwt
     *
     * @param jwtToken
     * @return
     */
    @Override
    public T parse(String jwtToken) {
        if (isNotEmpty(jwtToken))
            try {
                DecodedJWT jwt = VERIFIER.verify(jwtToken);
                JWT_ASSERT.accept(jwt);

                return CLAIM_2_DATA_PROCESSOR.apply(
                        jwt.getClaims().entrySet().stream()
                                .collect(toMap(Map.Entry::getKey, e ->
                                        ofNullable(e.getValue()).map(Claim::asString).orElse(""), (a, b) -> a)));
            } catch (Exception e) {
                throw new AuthenticationException(UNAUTHORIZED.status, UNAUTHORIZED.code, UNAUTHORIZED.message);
            }

        throw new AuthenticationException(UNAUTHORIZED.status, UNAUTHORIZED.code, UNAUTHORIZED.message);
    }

    /**
     * 获取jwt过期时间上限值
     *
     * @return
     */
    @Override
    public long getMaxExpireMillis() {
        return MAX_EXPIRE_MILLIS;
    }

    /**
     * 获取jwt过期时间下限值
     *
     * @return
     */
    @Override
    public long getMinExpireMillis() {
        return MIN_EXPIRE_MILLIS;
    }

    /**
     * 参数校验
     *
     * @param jwtConf
     * @param <T>
     */
    private static <T> void assertConf(JwtConf<T> jwtConf) {
        Long minExpireMillis = jwtConf.getMinExpireMillis();
        if (minExpireMillis == null || minExpireMillis < 0L)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "minExpireMillis不能为空或小于0L");
        Long maxExpireMillis = jwtConf.getMaxExpireMillis();
        if (maxExpireMillis == null || maxExpireMillis < 0L)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "maxExpireMillis不能为空或小于0L");
        if (maxExpireMillis <= minExpireMillis)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "maxExpireMillis须大于minExpireMillis");

        String signKey = jwtConf.getSignKey();
        if (signKey == null || "".equals(signKey))
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "signKey不能为空或''");
        int secKeyLen = signKey.length();
        if (secKeyLen < SEC_KEY_STR_MIN_LEN || secKeyLen > SEC_KEY_STR_MAX_LEN)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "signKey的字符长度不能低于 " + SEC_KEY_STR_MIN_LEN + " 或高于 " + SEC_KEY_STR_MAX_LEN);

        List<String> gammaSecrets = jwtConf.getGammaSecrets();
        if (gammaSecrets == null || gammaSecrets.size() < 1)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "gammaSecrets不能为空");

        int gammaSecretSize = gammaSecrets.size();
        if (gammaSecretSize < GAMMA_SECRETS_MIN_LEN || gammaSecretSize > GAMMA_SECRETS_MAX_LEN)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "gammaSecret的元素数量不能低于 " + GAMMA_SECRETS_MIN_LEN + " 或高于 " + GAMMA_SECRETS_MAX_LEN);

        if (Integer.bitCount(gammaSecretSize) != 1)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "gammaSecrets的元素数量必须为2的幂数");
        gammaSecrets = gammaSecrets.stream()
                .peek(gs -> {
                    if (isBlank(gs))
                        throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "gammaSecrets不能存在空字符");
                    int gsLen = gs.length();
                    if (gsLen < GAMMA_KEY_STR_MIN_LEN || gsLen > GAMMA_KEY_STR_MAX_LEN)
                        throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "gammaSecrets元素的字符长度不能低于 " + GAMMA_KEY_STR_MIN_LEN + " 或高于 " + GAMMA_KEY_STR_MAX_LEN + ", gammaSecret -> " + gs);
                }).distinct().collect(toList());
        if (gammaSecrets.size() != gammaSecretSize)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "gammaSecrets不能存在重复元素");

        if (jwtConf.getDataToClaimProcessor() == null)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "dataToClaimProcessor不能为空");
        if (jwtConf.getClaimToDataProcessor() == null)
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "claimToDataProcessor不能为空");

        if (isBlank(jwtConf.getIssuer()))
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "issuer不能为空或''");
        if (isBlank(jwtConf.getSubject()))
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "subject不能为空或''");
        if (isBlank(jwtConf.getAudience()))
            throw new AuthenticationException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "audience不能为空或''");
    }

}
