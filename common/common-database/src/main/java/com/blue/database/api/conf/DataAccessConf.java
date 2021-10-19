package com.blue.database.api.conf;

import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.ExecutorType;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * 数据分片配置类
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "SpellCheckingInspection"})
public interface DataAccessConf {

    /**
     * database shard
     *
     * @return
     */
    List<ShardingDatabaseAttr> getShardingDatabases();

    /**
     * tables to be sharding
     *
     * @return
     */
    List<ShardingTableAttr> getShardingTables();

    /**
     * table sharding size per db
     *
     * @return
     */
    Integer getShardingTableSizePerDataBase();

    /**
     * broadcast tables in shard
     *
     * @return
     */
    List<String> getShardingBroadcastTables();

    /**
     * db and tables for not sharding
     *
     * @return
     */
    List<SingleDatabaseWithTablesAttr> getSingleDatabasesWithTables();

    /**
     * enable cache
     *
     * @return
     */
    Boolean getCacheEnabled();

    /**
     * enable lazy loading
     *
     * @return
     */
    Boolean getLazyLoadingEnabled();

    /**
     * enable aggress lazy loading
     *
     * @return
     */
    Boolean getAggressiveLazyLoading();

    /**
     * enable multi result set
     *
     * @return
     */
    Boolean getMultipleResultSetsEnabled();

    /**
     * use column label
     *
     * @return
     */
    Boolean getUseColumnLabel();

    /**
     * use generated keys, false in summer, all id are generated by snowfalke
     *
     * @return
     */
    Boolean getUseGeneratedKeys();

    /**
     * connection timeout
     *
     * @return
     */
    Integer getConnectionTimeout();

    /**
     * auto mapping behavior
     *
     * @return
     */
    AutoMappingBehavior getAutoMappingBehavior();

    /**
     * executor type
     *
     * @return
     */
    ExecutorType getExecutorType();

    /**
     * mybatis mapper location
     *
     * @return
     */
    String getMapperLocation();

    /**
     * additional attributes
     *
     * @return
     */
    Map<String, String> getProps();

    /**
     * datasource proxy, for example : seata proxy
     *
     * @return
     */
    List<UnaryOperator<DataSource>> getProxiesChain();

}
