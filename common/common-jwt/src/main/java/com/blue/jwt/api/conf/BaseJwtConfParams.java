package com.blue.jwt.api.conf;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;

/**
 * jwt params
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public abstract class BaseJwtConfParams<T> implements JwtConf<T> {

    protected transient Long maxExpiresMillis;
    protected transient Long minExpiresMillis;
    protected transient Long refreshExpiresMillis;

    protected transient String signKey;
    protected transient List<String> gammaSecrets;

    protected String issuer;
    protected String subject;
    protected String audience;

    public BaseJwtConfParams() {
    }

    public BaseJwtConfParams(Long maxExpiresMillis, Long minExpiresMillis, Long refreshExpiresMillis, String signKey, List<String> gammaSecrets) {
        this.maxExpiresMillis = maxExpiresMillis;
        this.minExpiresMillis = minExpiresMillis;
        this.refreshExpiresMillis = refreshExpiresMillis;
        this.signKey = signKey;
        this.gammaSecrets = gammaSecrets;
        this.issuer = EMPTY_DATA.value;
        this.subject = EMPTY_DATA.value;
        this.audience = EMPTY_DATA.value;
    }

    public BaseJwtConfParams(Long maxExpiresMillis, Long minExpiresMillis, Long refreshExpiresMillis, String signKey, List<String> gammaSecrets, String issuer, String subject, String audience) {
        this.maxExpiresMillis = maxExpiresMillis;
        this.minExpiresMillis = minExpiresMillis;
        this.refreshExpiresMillis = refreshExpiresMillis;
        this.signKey = signKey;
        this.gammaSecrets = gammaSecrets;
        this.issuer = issuer;
        this.subject = subject;
        this.audience = audience;
    }

    @Override
    public Long getMaxExpiresMillis() {
        return maxExpiresMillis;
    }

    @Override
    public Long getMinExpiresMillis() {
        return minExpiresMillis;
    }

    @Override
    public Long getRefreshExpiresMillis() {
        return refreshExpiresMillis;
    }

    @Override
    public String getSignKey() {
        return signKey;
    }

    @Override
    public List<String> getGammaSecrets() {
        return gammaSecrets;
    }

    @Override
    public abstract Function<T, Map<String, String>> getDataToClaimProcessor();

    @Override
    public abstract Function<Map<String, String>, T> getClaimToDataProcessor();

    @Override
    public String getIssuer() {
        return issuer;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getAudience() {
        return audience;
    }

    @Override
    public String toString() {
        return "BaseJwtConfParams{" +
                "maxExpiresMillis=" + maxExpiresMillis +
                ", minExpiresMillis=" + minExpiresMillis +
                ", refreshExpiresMillis=" + refreshExpiresMillis +
                ", signKey='" + signKey + '\'' +
                ", gammaSecrets=" + gammaSecrets +
                ", issuer='" + issuer + '\'' +
                ", subject='" + subject + '\'' +
                ", audience='" + audience + '\'' +
                '}';
    }

}
