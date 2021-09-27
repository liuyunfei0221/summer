package com.blue.database.api.conf;

import java.util.List;

/**
 * 分片参数封装
 *
 * @author DarkBlue
 */
public final class SingleDatabaseWithTablesAttr extends ShardingDatabaseAttr {

    private List<String> singleTables;

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
