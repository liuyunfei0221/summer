package com.blue.jwt.api.conf;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * jwt params
 *
 * @author DarkBlue
 * @date 2021/8/12
 * @apiNote
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public abstract class BaseJwtConfParams<T> implements JwtConf<T> {

    protected Long maxExpireMillis;
    protected Long minExpireMillis;

    protected String signKey;
    protected List<String> gammaSecrets;

    protected String issuer;
    protected String subject;
    protected String audience;

    public BaseJwtConfParams() {
    }

    public BaseJwtConfParams(Long maxExpireMillis, Long minExpireMillis, String signKey, List<String> gammaSecrets,
                             String issuer, String subject, String audience) {
        this.maxExpireMillis = maxExpireMillis;
        this.minExpireMillis = minExpireMillis;
        this.signKey = signKey;
        this.gammaSecrets = gammaSecrets;
        this.issuer = issuer;
        this.subject = subject;
        this.audience = audience;
    }

    @Override
    public Long getMaxExpireMillis() {
        return maxExpireMillis;
    }

    @Override
    public Long getMinExpireMillis() {
        return minExpireMillis;
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
}
