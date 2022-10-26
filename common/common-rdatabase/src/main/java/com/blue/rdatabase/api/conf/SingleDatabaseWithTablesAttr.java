package com.blue.rdatabase.api.conf;

import java.util.List;

/**
 * single db shard with tables
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public final class SingleDatabaseWithTablesAttr extends ShardingDatabaseAttr {

    /**
     * tables not sharding
     */
    private transient List<String> singleTables;

    public SingleDatabaseWithTablesAttr() {
    }

    public List<String> getSingleTables() {
        return singleTables;
    }

    public void setSingleTables(List<String> singleTables) {
        this.singleTables = singleTables;
    }

    @Override
    public String toString() {
        return "SingleDatabaseWithTablesAttr{" +
                "singleTables=" + singleTables +
                ", url='" + url + '\'' +
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
                ", metricRegistry=" + metricRegistry +
                ", healthCheckRegistry=" + healthCheckRegistry +
                ", healthCheckProperties=" + healthCheckProperties +
                '}';
    }

}
