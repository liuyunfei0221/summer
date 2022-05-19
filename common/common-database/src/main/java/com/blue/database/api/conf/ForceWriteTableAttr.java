package com.blue.database.api.conf;

/**
 * force write table
 *
 * @author liuyunfei
 */
public final class ForceWriteTableAttr {

    /**
     * table name
     */
    private transient String tableName;

    /**
     * data center
     */
    private transient Integer dataCenter;

    /**
     * worker
     */
    private transient Integer worker;

    public ForceWriteTableAttr() {
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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
                ", dataCenter=" + dataCenter +
                ", worker=" + worker +
                '}';
    }

}
