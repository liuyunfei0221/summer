package com.blue.database.api.conf;

/**
 * sharding database attributes
 *
 * @author DarkBlue
 */
public class ShardingDatabaseAttr {

    /**
     * db url, for example -> jdbc:mysql://localhost:3306/portal_0
     */
    protected String url;

    /**
     * db conf, for example -> useSSL=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
     */
    protected String dataBaseConf;

    /**
     * username
     */
    protected String username;

    /**
     * password
     */
    protected String password;

    /**
     * driver class name, for example -> com.mysql.cj.jdbc.Driver
     */
    protected String driverClassName;

    /**
     * connection timeout
     */
    protected Integer connectionTimeout;

    /**
     * max lifetime
     */
    protected Integer maxLifetime;

    /**
     * max connection pool size
     */
    protected Integer maximumPoolSize;

    /**
     * min idle
     */
    protected Integer minimumIdle;

    /**
     * idle timeout
     */
    protected Integer idleTimeout;

    /**
     * sql for test
     */
    protected String testQuery;

    /**
     * read only, true means that db is a slave node
     */
    protected Boolean readOnly;

    /**
     * auto commit
     */
    protected Boolean autoCommit;

    public ShardingDatabaseAttr() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDataBaseConf() {
        return dataBaseConf;
    }

    public void setDataBaseConf(String dataBaseConf) {
        this.dataBaseConf = dataBaseConf;
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

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getMaxLifetime() {
        return maxLifetime;
    }

    public void setMaxLifetime(Integer maxLifetime) {
        this.maxLifetime = maxLifetime;
    }

    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public Integer getMinimumIdle() {
        return minimumIdle;
    }

    public void setMinimumIdle(Integer minimumIdle) {
        this.minimumIdle = minimumIdle;
    }

    public Integer getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(Integer idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public String getTestQuery() {
        return testQuery;
    }

    public void setTestQuery(String testQuery) {
        this.testQuery = testQuery;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Boolean getAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(Boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    @Override
    public String toString() {
        return "ShardingDatabaseAttr{" +
                "url='" + url + '\'' +
                ", dataBaseConf='" + dataBaseConf + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", driverClassName='" + driverClassName + '\'' +
                ", connectionTimeout=" + connectionTimeout +
                ", maxLifetime=" + maxLifetime +
                ", maximumPoolSize=" + maximumPoolSize +
                ", minimumIdle=" + minimumIdle +
                ", idleTimeout=" + idleTimeout +
                ", testQuery='" + testQuery + '\'' +
                ", readOnly=" + readOnly +
                ", autoCommit=" + autoCommit +
                '}';
    }

}
