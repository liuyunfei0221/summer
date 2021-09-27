package com.blue.database.api.conf;

import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.ExecutorType;

import java.util.List;
import java.util.Map;

/**
 * 数据源配置信息封装
 *
 * @author liuyunfei
 * @date 2021/9/10
 * @apiNote
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused"})
public abstract class BaseDataAccessConfParams implements DataAccessConf {

    /**
     * 当前可用数据库分片
     */
    protected List<ShardingDatabaseAttr> shardingDatabases;

    /**
     * 需要分片的表,建议初期就完善配置
     */
    protected List<String> shardingTables;

    /**
     * 当前每库分表数量
     */
    protected Integer shardingTableSizePerDataBase;

    /**
     * 分片属性名
     */
    protected String shardingColumn;

    /**
     * 广播/当前主要用于seata 会滚日志,因为当前无法定义seata回滚数据主键策略
     */
    protected List<String> shardingBroadcastTables;

    /**
     * 当前数据库分片
     */
    protected List<SingleDatabaseWithTablesAttr> singleDatabasesWithTables;

    protected Boolean cacheEnabled;

    protected Boolean lazyLoadingEnabled;

    protected Boolean aggressiveLazyLoading;

    protected Boolean multipleResultSetsEnabled;

    protected Boolean useColumnLabel;

    protected Boolean useGeneratedKeys;

    protected Integer connectionTimeout;

    protected AutoMappingBehavior autoMappingBehavior;

    protected ExecutorType executorType;

    protected String mapperLocation;

    protected Map<String, String> props;

    public BaseDataAccessConfParams() {
    }

    @Override
    public List<ShardingDatabaseAttr> getShardingDatabases() {
        return shardingDatabases;
    }

    @Override
    public List<String> getShardingTables() {
        return shardingTables;
    }

    @Override
    public Integer getShardingTableSizePerDataBase() {
        return shardingTableSizePerDataBase;
    }

    @Override
    public String getShardingColumn() {
        return shardingColumn;
    }

    @Override
    public List<String> getShardingBroadcastTables() {
        return shardingBroadcastTables;
    }

    @Override
    public List<SingleDatabaseWithTablesAttr> getSingleDatabasesWithTables() {
        return singleDatabasesWithTables;
    }

    @Override
    public Boolean getCacheEnabled() {
        return cacheEnabled;
    }

    @Override
    public Boolean getLazyLoadingEnabled() {
        return lazyLoadingEnabled;
    }

    @Override
    public Boolean getAggressiveLazyLoading() {
        return aggressiveLazyLoading;
    }

    @Override
    public Boolean getMultipleResultSetsEnabled() {
        return multipleResultSetsEnabled;
    }

    @Override
    public Boolean getUseColumnLabel() {
        return useColumnLabel;
    }

    @Override
    public Boolean getUseGeneratedKeys() {
        return useGeneratedKeys;
    }

    @Override
    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    @Override
    public AutoMappingBehavior getAutoMappingBehavior() {
        return autoMappingBehavior;
    }

    @Override
    public ExecutorType getExecutorType() {
        return executorType;
    }

    @Override
    public String getMapperLocation() {
        return mapperLocation;
    }

    @Override
    public Map<String, String> getProps() {
        return props;
    }

    public void setShardingDatabases(List<ShardingDatabaseAttr> shardingDatabases) {
        this.shardingDatabases = shardingDatabases;
    }

    public void setShardingTables(List<String> shardingTables) {
        this.shardingTables = shardingTables;
    }

    public void setShardingTableSizePerDataBase(Integer shardingTableSizePerDataBase) {
        this.shardingTableSizePerDataBase = shardingTableSizePerDataBase;
    }

    public void setShardingColumn(String shardingColumn) {
        this.shardingColumn = shardingColumn;
    }

    public void setShardingBroadcastTables(List<String> shardingBroadcastTables) {
        this.shardingBroadcastTables = shardingBroadcastTables;
    }

    public void setSingleDatabasesWithTables(List<SingleDatabaseWithTablesAttr> singleDatabasesWithTables) {
        this.singleDatabasesWithTables = singleDatabasesWithTables;
    }

    public void setCacheEnabled(Boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public void setLazyLoadingEnabled(Boolean lazyLoadingEnabled) {
        this.lazyLoadingEnabled = lazyLoadingEnabled;
    }

    public void setAggressiveLazyLoading(Boolean aggressiveLazyLoading) {
        this.aggressiveLazyLoading = aggressiveLazyLoading;
    }

    public void setMultipleResultSetsEnabled(Boolean multipleResultSetsEnabled) {
        this.multipleResultSetsEnabled = multipleResultSetsEnabled;
    }

    public void setUseColumnLabel(Boolean useColumnLabel) {
        this.useColumnLabel = useColumnLabel;
    }

    public void setUseGeneratedKeys(Boolean useGeneratedKeys) {
        this.useGeneratedKeys = useGeneratedKeys;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setAutoMappingBehavior(AutoMappingBehavior autoMappingBehavior) {
        this.autoMappingBehavior = autoMappingBehavior;
    }

    public void setExecutorType(ExecutorType executorType) {
        this.executorType = executorType;
    }

    public void setMapperLocation(String mapperLocation) {
        this.mapperLocation = mapperLocation;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }

    @Override
    public String toString() {
        return "BaseDataAccessConfParams{" +
                "shardingDatabases=" + shardingDatabases +
                ", shardingTables=" + shardingTables +
                ", shardingTableSizePerDataBase=" + shardingTableSizePerDataBase +
                ", shardingColumn='" + shardingColumn + '\'' +
                ", shardingBroadcastTables=" + shardingBroadcastTables +
                ", singleDatabasesWithTables=" + singleDatabasesWithTables +
                ", cacheEnabled=" + cacheEnabled +
                ", lazyLoadingEnabled=" + lazyLoadingEnabled +
                ", aggressiveLazyLoading=" + aggressiveLazyLoading +
                ", multipleResultSetsEnabled=" + multipleResultSetsEnabled +
                ", useColumnLabel=" + useColumnLabel +
                ", useGeneratedKeys=" + useGeneratedKeys +
                ", connectionTimeout=" + connectionTimeout +
                ", autoMappingBehavior=" + autoMappingBehavior +
                ", executorType=" + executorType +
                ", mapperLocation='" + mapperLocation + '\'' +
                ", props=" + props +
                '}';
    }

}
