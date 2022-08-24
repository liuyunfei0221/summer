package com.blue.hbase.ioc;

import com.blue.hbase.api.conf.HbaseConf;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.AsyncConnection;
import org.apache.hadoop.hbase.client.Connection;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import static com.blue.hbase.api.generator.BlueHbaseGenerator.*;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * hbase configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {HbaseConf.class})
@AutoConfiguration
@Order(HIGHEST_PRECEDENCE)
public class BlueHbaseConfiguration {

    @Bean
    Configuration hbaseConfiguration(HbaseConf hbaseConf) {
        return generateConfiguration(hbaseConf);
    }

    @Bean
    Connection connection(Configuration hbaseConfiguration, HbaseConf hbaseConf) {
        return generateConnection(hbaseConfiguration, hbaseConf);
    }

    @Bean
    AsyncConnection asyncConnection(Configuration hbaseConfiguration) {
        return generateAsyncConnection(hbaseConfiguration);
    }

}