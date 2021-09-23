package com.blue.database.api.conf;

/**
 * 分片参数封装
 *
 * @author DarkBlue
 */
public final class ShardYmlAttr {

    private String url;

    private String dataBaseConf;

    private String username;

    private String password;

    private String driverClassName;

    private Integer connectionTimeout;

    private Integer maxLifetime;

    private Integer maximumPoolSize;

    private Integer minimumIdle;

    private Integer idleTimeout;

    private String testQuery;

    private Boolean readOnly;

    private Boolean autoCommit;

    public ShardYmlAttr() {
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
        return "ShardYmlAttr{" +
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
