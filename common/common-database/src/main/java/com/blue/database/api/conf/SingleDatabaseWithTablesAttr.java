package com.blue.database.api.conf;

import java.util.List;

/**
 * single db shard with tables
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
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
                ", singleTables=" + singleTables +
                '}';
    }
}
