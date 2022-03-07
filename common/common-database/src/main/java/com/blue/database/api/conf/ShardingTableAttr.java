package com.blue.database.api.conf;

/**
 * sharding database attributes
 *
 * @author liuyunfei
 * @date 2021/10/19
 * @apiNote
 */
public class ShardingTableAttr {

    /**
     * table name
     */
    protected String tableName;

    /**
     * column name for sharding
     */
    protected String shardingColumn;

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
