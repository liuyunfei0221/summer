package com.blue.database.api.conf;

/**
 * force write table
 *
 * @author liuyunfei
 * @date 2021/10/19
 * @apiNote
 */
public final class ForceWriteTableAttr extends ShardingTableAttr {

    /**
     * data center id
     */
    private transient Integer dataCenter;

    /**
     * worker id
     */
    private transient Integer worker;

    public ForceWriteTableAttr() {
    }

    public Integer getDataCenter() {
        return dataCenter;
    }

    public void setDataCenter(Integer dataCenter) {
        this.dataCenter = dataCenter;
    }

    public Integer getWorker() {
        return worker;
    }

    public void setWorker(Integer worker) {
        this.worker = worker;
    }

    @Override
    public String toString() {
        return "ForceWriteTableAttr{" +
                "tableName='" + tableName + '\'' +
                ", shardingColumn='" + shardingColumn + '\'' +
                "dataCenter=" + dataCenter +
                ", worker=" + worker +
                '}';
    }
}
