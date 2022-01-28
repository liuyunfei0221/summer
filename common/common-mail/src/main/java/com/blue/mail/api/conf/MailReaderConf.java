package com.blue.mail.api.conf;

import java.util.List;

/**
 * @author DarkBlue
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface MailReaderConf {

    String getImapHost();

    Integer getImapPort();

    Boolean getImapSslEnable();

    String getUser();

    String getPassword();

    String getFolderName();

    List<String> getThrowableForRetry();

    Integer getMaxWaitingMillisForRefresh();

    Boolean getDebug();

}
