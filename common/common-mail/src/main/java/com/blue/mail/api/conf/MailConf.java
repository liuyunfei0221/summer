package com.blue.mail.api.conf;

import com.sanctionco.jmail.EmailValidator;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author liuyunfei
 * @date 2022/1/4
 * @apiNote
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface MailConf {

    String getSmtpServerHost();

    Integer getSmtpServerPort();

    Integer getSmtpUsername();

    Integer getSmtpPassword();

    Boolean getClearEmailValidator();

    EmailValidator getEmailValidator();

    ExecutorService getExecutorService();

    Integer getConnectionPoolCoreSize();

    Integer getConnectionPoolMaxSize();

    Integer getConnectionPoolExpireAfterMillis();

    Integer getSessionTimeout();

    String getConnectionPoolLoadBalancingStrategy();

    String getTransportStrategy();

    Boolean getAsync();

    String getDomainKeyFile();

    String getDomain();

    String getSelector();

    Boolean getDebugLogging();

    Map<String, Object> getProperties();

}
