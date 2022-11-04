package com.blue.database.api.conf;

import com.zaxxer.hikari.metrics.MetricsTrackerFactory;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;

/**
 * sharding database attributes
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public class ShardingDatabaseAttr {

    /**
     * db url, for example -> jdbc:mysql://localhost:3306/portal_0
     */
    protected transient String url;

    /**
     * db conf, for example -> useAffectedRows=true&useSSL=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
     */
    protected transient String databaseConf;

    /**
     * username
     */
    protected transient String username;

    /**
     * password
     */
    protected transient String password;

    /**
     * driver class name, for example -> com.mysql.cj.jdbc.Driver
     */
    protected transient String driverClassName;

    protected String catalog;

    protected Integer connectionTimeout;

    protected Integer validationTimeout;

    protected Integer idleTimeout;

    protected Long leakDetectionThreshold;

    protected Integer maxLifetime;

    protected Integer maxPoolSize;

    protected Integer minIdle;

    protected Long initializationFailTimeout;

    protected String connectionInitSql;

    protected String connectionTestQuery;

    protected String dataSourceClassName;

    protected String dataSourceJndiName;

    protected String exceptionOverrideClassName;

    protected String poolName;

    protected String schema;

    protected String transactionIsolationName;

    protected Boolean isAutoCommit;

    protected Boolean isReadOnly;

    protected Boolean isIsolateInternalQueries;

    protected Boolean isRegisterMbeans;

    protected Boolean isAllowPoolSuspension;

    protected DataSource dataSource;

    protected Properties dataSourceProperties;

    protected ScheduledExecutorService scheduledExecutor;

    protected MetricsTrackerFactory metricsTrackerFactory;

    protected Object metricRegistry;

    protected Object healthCheckRegistry;

    protected Properties healthCheckProperties;

    public ShardingDatabaseAttr() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDatabaseConf() {
        return databaseConf;
    }

    public void setDatabaseConf(String databaseConf) {
        this.databaseConf = databaseConf;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getValidationTimeout() {
        return validationTimeout;
    }

    public void setValidationTimeout(Integer validationTimeout) {
        this.validationTimeout = validationTimeout;
    }

    public Integer getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(Integer idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public Long getLeakDetectionThreshold() {
        return leakDetectionThreshold;
    }

    public void setLeakDetectionThreshold(Long leakDetectionThreshold) {
        this.leakDetectionThreshold = leakDetectionThreshold;
    }

    public Integer getMaxLifetime() {
        return maxLifetime;
    }

    public void setMaxLifetime(Integer maxLifetime) {
        this.maxLifetime = maxLifetime;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Long getInitializationFailTimeout() {
        return initializationFailTimeout;
    }

    public void setInitializationFailTimeout(Long initializationFailTimeout) {
        this.initializationFailTimeout = initializationFailTimeout;
    }

    public String getConnectionInitSql() {
        return connectionInitSql;
    }

    public void setConnectionInitSql(String connectionInitSql) {
        this.connectionInitSql = connectionInitSql;
    }

    public String getConnectionTestQuery() {
        return connectionTestQuery;
    }

    public void setConnectionTestQuery(String connectionTestQuery) {
        this.connectionTestQuery = connectionTestQuery;
    }

    public String getDataSourceClassName() {
        return dataSourceClassName;
    }

    public void setDataSourceClassName(String dataSourceClassName) {
        this.dataSourceClassName = dataSourceClassName;
    }

    public String getDataSourceJndiName() {
        return dataSourceJndiName;
    }

    public void setDataSourceJndiName(String dataSourceJndiName) {
        this.dataSourceJndiName = dataSourceJndiName;
    }

    public String getExceptionOverrideClassName() {
        return exceptionOverrideClassName;
    }

    public void setExceptionOverrideClassName(String exceptionOverrideClassName) {
        this.exceptionOverrideClassName = exceptionOverrideClassName;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTransactionIsolationName() {
        return transactionIsolationName;
    }

    public void setTransactionIsolationName(String transactionIsolationName) {
        this.transactionIsolationName = transactionIsolationName;
    }

    public Boolean getIsAutoCommit() {
        return isAutoCommit;
    }

    public void setIsAutoCommit(Boolean autoCommit) {
        isAutoCommit = autoCommit;
    }

    public Boolean getIsReadOnly() {
        return isReadOnly;
    }

    public void setIsReadOnly(Boolean readOnly) {
        isReadOnly = readOnly;
    }

    public Boolean getIsolateInternalQueries() {
        return isIsolateInternalQueries;
    }

    public void setIsolateInternalQueries(Boolean isolateInternalQueries) {
        isIsolateInternalQueries = isolateInternalQueries;
    }

    public Boolean getIsRegisterMbeans() {
        return isRegisterMbeans;
    }

    public void setIsRegisterMbeans(Boolean registerMbeans) {
        isRegisterMbeans = registerMbeans;
    }

    public Boolean getAllowPoolSuspension() {
        return isAllowPoolSuspension;
    }

    public void setAllowPoolSuspension(Boolean allowPoolSuspension) {
        isAllowPoolSuspension = allowPoolSuspension;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Properties getDataSourceProperties() {
        return dataSourceProperties;
    }

    public void setDataSourceProperties(Properties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    public ScheduledExecutorService getScheduledExecutor() {
        return scheduledExecutor;
    }

    public void setScheduledExecutor(ScheduledExecutorService scheduledExecutor) {
        this.scheduledExecutor = scheduledExecutor;
    }

    public MetricsTrackerFactory getMetricsTrackerFactory() {
        return metricsTrackerFactory;
    }

    public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
        this.metricsTrackerFactory = metricsTrackerFactory;
    }

    public Object getMetricRegistry() {
        return metricRegistry;
    }

    public void setMetricRegistry(Object metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    public Object getHealthCheckRegistry() {
        return healthCheckRegistry;
    }

    public void setHealthCheckRegistry(Object healthCheckRegistry) {
        this.healthCheckRegistry = healthCheckRegistry;
    }

    public Properties getHealthCheckProperties() {
        return healthCheckProperties;
    }

    public void setHealthCheckProperties(Properties healthCheckProperties) {
        this.healthCheckProperties = healthCheckProperties;
    }

    @Override
    public String toString() {
        return "ShardingDatabaseAttr{" +
                "url='" + url + '\'' +
                ", databaseConf='" + databaseConf + '\'' +
                ", username='" + ":)" + '\'' +
                ", password='" + ":)" + '\'' +
                ", driverClassName='" + driverClassName + '\'' +
                ", catalog='" + catalog + '\'' +
                ", connectionTimeout=" + connectionTimeout +
                ", validationTimeout=" + validationTimeout +
                ", idleTimeout=" + idleTimeout +
                ", leakDetectionThreshold=" + leakDetectionThreshold +
                ", maxLifetime=" + maxLifetime +
                ", maxPoolSize=" + maxPoolSize +
                ", minIdle=" + minIdle +
                ", initializationFailTimeout=" + initializationFailTimeout +
                ", connectionInitSql='" + connectionInitSql + '\'' +
                ", connectionTestQuery='" + connectionTestQuery + '\'' +
                ", dataSourceClassName='" + dataSourceClassName + '\'' +
                ", dataSourceJndiName='" + dataSourceJndiName + '\'' +
                ", exceptionOverrideClassName='" + exceptionOverrideClassName + '\'' +
                ", poolName='" + poolName + '\'' +
                ", schema='" + schema + '\'' +
                ", transactionIsolationName='" + transactionIsolationName + '\'' +
                ", isAutoCommit=" + isAutoCommit +
                ", isReadOnly=" + isReadOnly +
                ", isIsolateInternalQueries=" + isIsolateInternalQueries +
                ", isRegisterMbeans=" + isRegisterMbeans +
                ", isAllowPoolSuspension=" + isAllowPoolSuspension +
                ", dataSource=" + dataSource +
                ", dataSourceProperties=" + dataSourceProperties +
                ", scheduledExecutor=" + scheduledExecutor +
                ", metricsTrackerFactory=" + metricsTrackerFactory +
                ", metricRegistry=" + metricRegistry +
                ", healthCheckRegistry=" + healthCheckRegistry +
                ", healthCheckProperties=" + healthCheckProperties +
                '}';
    }

}
