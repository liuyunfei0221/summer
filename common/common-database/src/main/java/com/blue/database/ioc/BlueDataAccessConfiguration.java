package com.blue.database.ioc;

import com.blue.database.api.conf.DataAccessConf;
import com.blue.identity.api.conf.IdentityConf;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static com.blue.database.api.generator.BlueDataAccessGenerator.*;
import static org.apache.ibatis.session.ExecutorType.BATCH;
import static org.springframework.context.annotation.AdviceMode.PROXY;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;


/**
 * data access components configuration
 *
 * @author DarkBlue
 */
@SuppressWarnings({"DefaultAnnotationParam"})
@Configuration
@EnableTransactionManagement(proxyTargetClass = true, mode = PROXY, order = LOWEST_PRECEDENCE)
public class BlueDataAccessConfiguration {

    private final DataAccessConf dataAccessConf;

    private final IdentityConf identityConf;

    public BlueDataAccessConfiguration(DataAccessConf dataAccessConf, IdentityConf identityConf) {
        this.dataAccessConf = dataAccessConf;
        this.identityConf = identityConf;
    }

    @Bean
    public DataSource dataSource() {
        return generateDataSource(dataAccessConf, identityConf);
    }

    @Bean
    public TransactionManager txManager(DataSource dataSource) {
        return generateTxManager(dataSource);
    }

    @Bean
    SqlSessionFactory sqlSessionFactory(DataSource dataSource) {
        return generateSqlSessionFactory(dataSource, dataAccessConf);
    }

    @Bean
    SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return generateSqlSessionTemplate(sqlSessionFactory, BATCH);
    }

}
