package com.blue.database.api.conf;

/**
 * sharding database attributes
 *
 * @author liuyunfei
 */
public class ShardingTableAttr {

    /**
     * table name
     */
    protected transient String tableName;

    /**
     * column name for sharding
     */
    protected transient String shardingColumn;

    public ShardingTableAttr() {
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getShardingColumn() {
        return shardingColumn;
    }

    public void setShardingColumn(String shardingColumn) {
        this.shardingColumn = shardingColumn;
    }

    @Override
    public String toString() {
        return "ShardingTableAttr{" +
                "tableName='" + tableName + '\'' +
                ", shardingColumn='" + shardingColumn + '\'' +
                '}';
    }

}
