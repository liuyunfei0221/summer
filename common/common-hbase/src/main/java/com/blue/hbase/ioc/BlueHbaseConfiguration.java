package com.blue.hbase.ioc;

import com.blue.hbase.api.conf.HbaseConf;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

/**
 * hbase configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {HbaseConf.class})
@AutoConfiguration
public class BlueHbaseConfiguration {


}