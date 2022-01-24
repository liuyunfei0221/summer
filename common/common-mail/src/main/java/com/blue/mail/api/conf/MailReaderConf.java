package com.blue.mail.api.conf;

public interface MailReaderConf {

    String getImapHost();

    Integer getImapPort();

    Boolean getImapSslEnable();

    String getUser();

    String getPassword();

    String getFolderName();

    Integer getMaxWaitingMillisForRefresh();

     Boolean getDebug();

}
