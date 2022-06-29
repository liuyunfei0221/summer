package com.blue.jwt.api.conf;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

    public BaseJwtConfParams() {
    }

    public BaseJwtConfParams(Long maxExpiresMillis, Long minExpiresMillis, Long refreshExpiresMillis, String signKey, List<String> gammaSecrets) {
        this.maxExpiresMillis = maxExpiresMillis;
        this.minExpiresMillis = minExpiresMillis;
        this.refreshExpiresMillis = refreshExpiresMillis;
        this.signKey = signKey;
        this.gammaSecrets = gammaSecrets;
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

}
