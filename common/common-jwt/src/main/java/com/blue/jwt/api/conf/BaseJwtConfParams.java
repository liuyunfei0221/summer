package com.blue.jwt.api.conf;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * jwt params
 *
 * @author liuyunfei
 * @date 2021/8/12
 * @apiNote
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public abstract class BaseJwtConfParams<T> implements JwtConf<T> {

    protected transient Long maxExpireMillis;
    protected transient Long minExpireMillis;

    protected transient String signKey;
    protected transient List<String> gammaSecrets;

    public BaseJwtConfParams() {
    }

    public BaseJwtConfParams(Long maxExpireMillis, Long minExpireMillis, String signKey, List<String> gammaSecrets) {
        this.maxExpireMillis = maxExpireMillis;
        this.minExpireMillis = minExpireMillis;
        this.signKey = signKey;
        this.gammaSecrets = gammaSecrets;
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

}
