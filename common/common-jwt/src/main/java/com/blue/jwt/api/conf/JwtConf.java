package com.blue.jwt.api.conf;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * jwt conf
 *
 * @author liuyunfei
 * @date 2021/9/10
 * @apiNote
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface JwtConf<T> {

    Long getMaxExpireMillis();

    Long getMinExpireMillis();

    String getSignKey();

    List<String> getGammaSecrets();

    Function<T, Map<String, String>> getDataToClaimProcessor();

    Function<Map<String, String>, T> getClaimToDataProcessor();

}
