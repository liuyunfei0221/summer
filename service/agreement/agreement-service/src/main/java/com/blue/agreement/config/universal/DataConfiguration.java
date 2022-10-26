//package com.blue.agreement.config.universal;
//
//import io.r2dbc.spi.ConnectionFactory;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
//import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
//
//@Configuration
//@EnableR2dbcRepositories
//public class DataConfiguration extends AbstractR2dbcConfiguration {
//
//    private final ConnectionFactory connectionFactory;
//
//    public DataConfiguration(ConnectionFactory connectionFactory) {
//        this.connectionFactory = connectionFactory;
//    }
//
//    @Override
//    public ConnectionFactory connectionFactory() {
//        return connectionFactory;
//    }
//}
