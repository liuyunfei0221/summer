package com.blue.marketing.config.blue;

import com.blue.database.api.conf.BaseDataAccessConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * mysql配置参数类
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "data")
public class BlueDataAccessConfig extends BaseDataAccessConfParams {

    @Override
    public List<UnaryOperator<DataSource>> getProxiesChain() {
        return null;
    }

}