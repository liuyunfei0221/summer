package com.blue.mail.api.conf;

import java.util.List;
import java.util.Map;

/**
 * @author liuyunfei
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface MailReaderConf {

    String getUser();

    String getPassword();

    Map<String, String> getProps();

    String getFolderName();

    List<String> getThrowableForRetry();

    Integer getMaxWaitingMillisForRefresh();

}
