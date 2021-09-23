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
 * mysql配置参数类
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Component
@ConfigurationProperties(prefix = "data")
public class BlueDataAccessConfig extends BaseDataAccessConfParams {

    /**
     * 添加seata代理
     *
     * @return
     */
    @Override
    public List<UnaryOperator<DataSource>> getProxiesChain() {
        return singletonList(DataSourceProxy::new);
    }

}
