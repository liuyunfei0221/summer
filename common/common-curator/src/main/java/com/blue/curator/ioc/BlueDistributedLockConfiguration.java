package com.blue.curator.ioc;

import com.blue.curator.api.conf.DistributedLockConf;
import com.blue.curator.api.generator.BlueDistributedLockGenerator;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * zk lock configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaRemoveCommentedCode", "JavaDoc", "AliControlFlowStatementWithoutBraces"})
@ConditionalOnBean(value = {DistributedLockConf.class})
@Configuration
public class BlueDistributedLockConfiguration {

    private static final Logger LOGGER = getLogger(BlueDistributedLockConfiguration.class);

    @Bean
    BlueDistributedLockGenerator distributedLockGenerator(DistributedLockConf distributedLockConf) {
        LOGGER.info("distributedLockGenerator(DistributedLockConf distributedLockConf), distributedLockConf = {}", distributedLockConf);
        assertConf(distributedLockConf);
        return new BlueDistributedLockGenerator(distributedLockConf);
    }

    /**
     * assert params
     *
     * @param conf
     */
    private static void assertConf(DistributedLockConf conf) {
        if (conf == null)
            throw new RuntimeException("distributedLockConf can't be null");

        String connectString = conf.getConnectString();
        if (connectString == null || "".equals(connectString))
            throw new RuntimeException("connectString can't be null");
    }

}
