package com.blue.jwt.api.conf;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * jwt conf
 *
 * @author liuyunfei
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface JwtConf<T> {

    Long getMaxExpiresMillis();

    Long getMinExpiresMillis();

    Long getRefreshExpiresMillis();

    String getSignKey();

    List<String> getGammaSecrets();

    Function<T, Map<String, String>> getDataToClaimProcessor();

    Function<Map<String, String>, T> getClaimToDataProcessor();

    String getIssuer();

    String getSubject();

    String getAudience();

}
