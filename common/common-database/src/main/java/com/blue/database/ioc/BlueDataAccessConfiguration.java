package com.blue.database.ioc;

import com.blue.database.api.conf.DataAccessConf;
import com.blue.identity.api.conf.IdentityConf;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;

import static com.blue.database.api.generator.BlueDataAccessGenerator.*;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;


/**
 * data access components configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@AutoConfiguration
@Order(HIGHEST_PRECEDENCE)
public class BlueDataAccessConfiguration {

    private final DataAccessConf dataAccessConf;

    private final IdentityConf identityConf;

    public BlueDataAccessConfiguration(DataAccessConf dataAccessConf, IdentityConf identityConf) {
        this.dataAccessConf = dataAccessConf;
        this.identityConf = identityConf;
    }

    @Bean
    DataSource dataSource() {
        return generateDataSource(dataAccessConf, identityConf);
    }

    @Bean
    SqlSessionFactory sqlSessionFactory(DataSource dataSource) {
        return generateSqlSessionFactory(dataSource, dataAccessConf);
    }

    @Bean
    SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return generateSqlSessionTemplate(sqlSessionFactory, dataAccessConf);
    }

    @Bean
    TransactionManager txManager(DataSource dataSource) {
        return generateTxManager(dataSource);
    }

}
