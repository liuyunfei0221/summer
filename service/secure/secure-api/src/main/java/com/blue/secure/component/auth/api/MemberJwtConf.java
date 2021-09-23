package com.blue.secure.component.auth.api;

import java.util.List;

/**
 * 成员jwt处理器配置
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

    String getIssuer();

    String getSubject();

    String getAudience();

}
