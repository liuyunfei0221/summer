package com.blue.mail.api.conf;

import java.util.List;

/**
 * @author liuyunfei
 * @date 2022/1/4
 * @apiNote
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "AlibabaLowerCamelCaseVariableNaming"})
public interface MailSenderConf {

    List<SmtpAttr> getSmtpAttrs();

    Integer getCorePoolSize();

    Integer getMaximumPoolSize();

    Long getKeepAliveSeconds();

    Integer getBlockingQueueCapacity();

    String getThreadNamePre();

    Integer getBufferSize();

    List<String> getThrowableForRetry();

    Integer getRetryTimes();

    Boolean getWithDKIM();

    String getDomainKeyFile();

    String getDomain();

    String getSelector();

    Boolean getDebug();

}
