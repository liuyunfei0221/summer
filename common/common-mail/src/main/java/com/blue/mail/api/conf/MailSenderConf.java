package com.blue.mail.api.conf;

import java.util.List;

/**
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "AlibabaLowerCamelCaseVariableNaming"})
public interface MailSenderConf {

    List<SenderAttr> getSmtpAttrs();

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

}
