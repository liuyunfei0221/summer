package com.blue.rdatabase.api.conf;

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
    Integer getShardingTableSizePerDatabase();

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
    List<UnaryOperator<DataSource>> getProxiesChain();

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

    Integer getDefaultStatementTimeout();

    Integer getDefaultFetchSize();

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
