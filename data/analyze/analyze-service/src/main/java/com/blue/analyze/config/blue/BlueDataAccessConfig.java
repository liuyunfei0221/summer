package com.blue.analyze.config.blue;

import com.blue.database.api.conf.BaseDataAccessConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * data access config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "data")
public class BlueDataAccessConfig extends BaseDataAccessConfParams {

    @Override
    public List<UnaryOperator<DataSource>> getShardingProxiesChain() {
        return null;
    }

    @Override
    public List<UnaryOperator<DataSource>> getSingleProxiesChain() {
        return null;
    }

}
