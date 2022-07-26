package com.blue.hbase.api.conf;

import java.util.List;
import java.util.Map;

/**
 * hbase conf
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface HbaseConf {

    Map<String, String> getConfig();

    List<String> getResourceLocations();

    Boolean getQuietMode();

    Boolean getRestrictSystemProps();

    Boolean getAllowNullValueProperties();

    Integer getCorePoolSize();

    Integer getMaximumPoolSize();

    Long getKeepAliveSeconds();

    Integer getBlockingQueueCapacity();

    String getThreadNamePre();

}
