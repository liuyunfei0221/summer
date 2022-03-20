package com.blue.auth.api.component.jwt.api.conf;

import java.util.List;

/**
 * member jwt conf
 *
 * @author liuyunfei
 * @date 2021/9/10
 * @apiNote
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface MemberJwtConf {

    Long getGlobalMaxExpireMillis();

    Long getGlobalMinExpireMillis();

    String getSignKey();

    List<String> getGammaSecrets();

}
