package com.blue.mail.api.conf;

import com.sanctionco.jmail.EmailValidator;
import org.simplejavamail.api.mailer.config.LoadBalancingStrategy;
import org.simplejavamail.api.mailer.config.TransportStrategy;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author liuyunfei
 * @date 2022/1/4
 * @apiNote
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "AlibabaLowerCamelCaseVariableNaming"})
public interface MailConf {

    String getSmtpServerHost();

    Integer getSmtpServerPort();

    String getSmtpUsername();

    String getSmtpPassword();

    EmailValidator getEmailValidator();

    ExecutorService getExecutorService();

    Integer getConnectionPoolCoreSize();

    Integer getConnectionPoolMaxSize();

    Integer getConnectionPoolClaimTimeoutMillis();

    Integer getConnectionPoolExpireAfterMillis();

    Integer getSessionTimeout();

    LoadBalancingStrategy getConnectionPoolLoadBalancingStrategy();

    TransportStrategy getTransportStrategy();

    Boolean getAsync();

    Boolean getWithDKIM();

    String getDomainKeyFile();

    String getDomain();

    String getSelector();

    Boolean getDebugLogging();

    Map<String, Object> getProperties();

}
