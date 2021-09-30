package com.blue.secure.config.blue;

import com.blue.database.api.conf.BaseDataAccessConfParams;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.function.UnaryOperator;

import static java.util.Collections.singletonList;

/**
 * data access config
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Component
@ConfigurationProperties(prefix = "data")
public class BlueDataAccessConfig extends BaseDataAccessConfParams {

    /**
     * seata proxy
     *
     * @return
     */
    @Override
    public List<UnaryOperator<DataSource>> getProxiesChain() {
        return singletonList(DataSourceProxy::new);
    }

}
