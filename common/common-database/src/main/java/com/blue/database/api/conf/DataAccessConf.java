package com.blue.database.api.conf;

import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.AutoMappingUnknownColumnBehavior;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.type.JdbcType;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * data access conf
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "SpellCheckingInspection", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
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
     * data center id to db index mappings
     *
     * @return
     */
    List<IdentityToShardingMappingAttr> getDataCenterToDatabaseMappings();

    /**
     * worker id to db index mappings
     *
     * @return
     */
    List<IdentityToShardingMappingAttr> getWorkerToTableMappings();

    /**
     * force write tables
     *
     * @return
     */
    List<ForceWriteTableAttr> getForceWriteTables();

    /**
     * broadcast tables
     *
     * @return
     */
    List<String> getBroadcastTables();

    /**
     * db and tables for not sharding
     *
     * @return
     */
    List<SingleDatabaseWithTablesAttr> getSingleDatabasesWithTables();

    /**
     * datasource proxy, for example : seata proxy
     *
     * @return
     */
    List<UnaryOperator<DataSource>> getShardingProxiesChain();

    /**
     * datasource proxy, for example : seata proxy
     *
     * @return
     */
    List<UnaryOperator<DataSource>> getSingleProxiesChain();

    Boolean getSafeRowBoundsEnabled();

    Boolean getSafeResultHandlerEnabled();

    Boolean getMapUnderscoreToCamelCase();

    Boolean getAggressiveLazyLoading();

    Boolean getMultipleResultSetsEnabled();

    Boolean getUseGeneratedKeys();

    Boolean getUseColumnLabel();

    Boolean getCacheEnabled();

    Boolean getCallSettersOnNulls();

    Boolean getUseActualParamName();

    Boolean getReturnInstanceForEmptyRow();

    Boolean getShrinkWhitespacesInSql();

    Boolean getNullableOnForEach();

    LocalCacheScope getLocalCacheScope();

    JdbcType getJdbcTypeForNull();

    Integer getDefaultStatementTimeout();

    Integer getDefaultFetchSize();

    ResultSetType getDefaultResultSetType();

    ExecutorType getDefaultExecutorType();

    AutoMappingBehavior getAutoMappingBehavior();

    AutoMappingUnknownColumnBehavior getAutoMappingUnknownColumnBehavior();

    Boolean getLazyLoadingEnabled();

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
     * show sql log?
     *
     * @return
     */
    Boolean getDebugLogging();

}
