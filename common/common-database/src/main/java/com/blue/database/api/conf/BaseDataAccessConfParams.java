package com.blue.database.api.conf;

import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.ExecutorType;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

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
    protected List<ShardYmlAttr> shards;

    /**
     * 需要分片的表,建议初期就完善配置
     */
    protected List<String> tables;

    /**
     * 当前每库分表数量
     */
    protected Integer tableSizePerDataBase;

    /**
     * 分片属性名
     */
    protected String shardingColumn;

    /**
     * 广播/当前主要用于seata undolog,因为当前无法定义seata回滚数据主键策略
     */
    protected List<String> broadcastTables;

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

    public BaseDataAccessConfParams(List<ShardYmlAttr> shards, List<String> tables, Integer tableSizePerDataBase,
                                    String shardingColumn, List<String> broadcastTables, Boolean cacheEnabled,
                                    Boolean lazyLoadingEnabled, Boolean aggressiveLazyLoading,
                                    Boolean multipleResultSetsEnabled, Boolean useColumnLabel, Boolean useGeneratedKeys,
                                    Integer connectionTimeout, ExecutorType executorType, AutoMappingBehavior autoMappingBehavior,
                                    String mapperLocation, Map<String, String> props) {
        this.shards = shards;
        this.tables = tables;
        this.tableSizePerDataBase = tableSizePerDataBase;
        this.shardingColumn = shardingColumn;
        this.broadcastTables = broadcastTables;
        this.cacheEnabled = cacheEnabled;
        this.lazyLoadingEnabled = lazyLoadingEnabled;
        this.aggressiveLazyLoading = aggressiveLazyLoading;
        this.multipleResultSetsEnabled = multipleResultSetsEnabled;
        this.useColumnLabel = useColumnLabel;
        this.useGeneratedKeys = useGeneratedKeys;
        this.connectionTimeout = connectionTimeout;
        this.autoMappingBehavior = autoMappingBehavior;
        this.executorType = executorType;
        this.mapperLocation = mapperLocation;
        this.props = props;
    }

    @Override
    public List<ShardYmlAttr> getShards() {
        return shards;
    }

    @Override
    public List<String> getTables() {
        return tables;
    }

    @Override
    public Integer getTableSizePerDataBase() {
        return tableSizePerDataBase;
    }

    @Override
    public String getShardingColumn() {
        return shardingColumn;
    }

    @Override
    public List<String> getBroadcastTables() {
        return broadcastTables;
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

    @Override
    public abstract List<UnaryOperator<DataSource>> getProxiesChain();

    public void setShards(List<ShardYmlAttr> shards) {
        this.shards = shards;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public void setTableSizePerDataBase(Integer tableSizePerDataBase) {
        this.tableSizePerDataBase = tableSizePerDataBase;
    }

    public void setShardingColumn(String shardingColumn) {
        this.shardingColumn = shardingColumn;
    }

    public void setBroadcastTables(List<String> broadcastTables) {
        this.broadcastTables = broadcastTables;
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
                "shards=" + shards +
                ", tables=" + tables +
                ", tableSizePerDataBase=" + tableSizePerDataBase +
                ", shardingColumn='" + shardingColumn + '\'' +
                ", broadcastTables=" + broadcastTables +
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
