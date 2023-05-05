package com.blue.database.api.generator;

import com.blue.basic.common.base.BlueChecker;
import com.blue.database.api.conf.*;
import com.blue.database.algorithm.SnowflakeDatabaseShardingAlgorithm;
import com.blue.database.algorithm.SnowflakeTableShardingAlgorithm;
import com.blue.identity.api.conf.IdentityConf;
import com.blue.identity.core.exp.IdentityException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.openhft.affinity.AffinityThreadFactory;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.NoneShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.MathProcessor.assertDisorderIntegerContinuous;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.basic.constant.common.Symbol.*;
import static com.blue.identity.constant.IdentitySchema.MAX_DATA_CENTER_ID;
import static com.blue.identity.constant.IdentitySchema.MAX_WORKER_ID;
import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static net.openhft.affinity.AffinityStrategies.DIFFERENT_CORE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static org.apache.ibatis.session.ExecutorType.BATCH;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * data access component generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "PlaceholderCountMatchesArgumentCount", "AliControlFlowStatementWithoutBraces", "SpellCheckingInspection", "DuplicatedCode"})
public final class BlueDataAccessGenerator {

    private static final Logger LOGGER = getLogger(BlueDataAccessGenerator.class);

    /**
     * valid data element parts num
     */
    private static final int MIN_VALID_DB_URL_PARTS_LEN = 4;

    private static final String THREAD_NAME_PRE = "blue-connect-thread" + HYPHEN.identity;
    private static final int RANDOM_LEN = 6;

    /**
     * datasource generator
     */
    private static final Function<ShardingDatabaseAttr, DataSource> DATA_SOURCE_GENERATOR = shardAttr -> {
        HikariConfig hikariConfig = new HikariConfig();

        String driverClassName = shardAttr.getDriverClassName();
        if (isBlank(driverClassName))
            throw new RuntimeException("driverClassName can't be blank");

        String url = shardAttr.getUrl();
        if (isBlank(url))
            throw new RuntimeException("url can't be blank");

        String username = shardAttr.getUsername();
        if (isBlank(username))
            throw new RuntimeException("username can't be blank");

        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(url + ofNullable(shardAttr.getDatabaseConf()).map(c -> QUESTION_MARK.identity + c).orElse(EMPTY_VALUE.value));
        hikariConfig.setUsername(username);

        ofNullable(shardAttr.getPassword()).filter(BlueChecker::isNotBlank).ifPresent(hikariConfig::setPassword);
        ofNullable(shardAttr.getCatalog()).filter(BlueChecker::isNotBlank).ifPresent(hikariConfig::setCatalog);
        ofNullable(shardAttr.getConnectionTimeout()).ifPresent(hikariConfig::setConnectionTimeout);
        ofNullable(shardAttr.getValidationTimeout()).ifPresent(hikariConfig::setValidationTimeout);
        ofNullable(shardAttr.getIdleTimeout()).ifPresent(hikariConfig::setIdleTimeout);
        ofNullable(shardAttr.getLeakDetectionThreshold()).ifPresent(hikariConfig::setLeakDetectionThreshold);
        ofNullable(shardAttr.getMaxLifetime()).ifPresent(hikariConfig::setMaxLifetime);
        ofNullable(shardAttr.getMaxPoolSize()).ifPresent(hikariConfig::setMaximumPoolSize);
        ofNullable(shardAttr.getMinIdle()).ifPresent(hikariConfig::setMinimumIdle);
        ofNullable(shardAttr.getInitializationFailTimeout()).ifPresent(hikariConfig::setInitializationFailTimeout);
        ofNullable(shardAttr.getConnectionInitSql()).filter(BlueChecker::isNotBlank).ifPresent(hikariConfig::setConnectionInitSql);
        ofNullable(shardAttr.getConnectionTestQuery()).filter(BlueChecker::isNotBlank).ifPresent(hikariConfig::setConnectionTestQuery);
        ofNullable(shardAttr.getDataSourceClassName()).filter(BlueChecker::isNotBlank).ifPresent(hikariConfig::setDataSourceClassName);
        ofNullable(shardAttr.getDataSourceJndiName()).filter(BlueChecker::isNotBlank).ifPresent(hikariConfig::setDataSourceJNDI);
        ofNullable(shardAttr.getExceptionOverrideClassName()).filter(BlueChecker::isNotBlank).ifPresent(hikariConfig::setExceptionOverrideClassName);
        ofNullable(shardAttr.getPoolName()).filter(BlueChecker::isNotBlank).ifPresent(hikariConfig::setPoolName);
        ofNullable(shardAttr.getSchema()).filter(BlueChecker::isNotBlank).ifPresent(hikariConfig::setSchema);
        ofNullable(shardAttr.getTransactionIsolationName()).filter(BlueChecker::isNotBlank).ifPresent(hikariConfig::setTransactionIsolation);
        ofNullable(shardAttr.getIsAutoCommit()).ifPresent(hikariConfig::setAutoCommit);
        ofNullable(shardAttr.getIsReadOnly()).ifPresent(hikariConfig::setReadOnly);
        ofNullable(shardAttr.getIsolateInternalQueries()).ifPresent(hikariConfig::setIsolateInternalQueries);
        ofNullable(shardAttr.getIsRegisterMbeans()).ifPresent(hikariConfig::setRegisterMbeans);
        ofNullable(shardAttr.getAllowPoolSuspension()).ifPresent(hikariConfig::setAllowPoolSuspension);
        ofNullable(shardAttr.getDataSource()).ifPresent(hikariConfig::setDataSource);
        ofNullable(shardAttr.getDataSourceProperties()).ifPresent(hikariConfig::setDataSourceProperties);
        ofNullable(shardAttr.getScheduledExecutor()).ifPresent(hikariConfig::setScheduledExecutor);
        ofNullable(shardAttr.getMetricsTrackerFactory()).ifPresent(hikariConfig::setMetricsTrackerFactory);
        ofNullable(shardAttr.getMetricRegistry()).ifPresent(hikariConfig::setMetricRegistry);
        ofNullable(shardAttr.getHealthCheckRegistry()).ifPresent(hikariConfig::setHealthCheckRegistry);
        ofNullable(shardAttr.getHealthCheckProperties()).ifPresent(hikariConfig::setHealthCheckProperties);

        hikariConfig.setThreadFactory(new AffinityThreadFactory(THREAD_NAME_PRE + randomAlphabetic(RANDOM_LEN), DIFFERENT_CORE));

        return new HikariDataSource(hikariConfig);
    };

    /**
     * generate datasource
     *
     * @param dataAccessConf
     * @param identityConf
     * @return
     */
    public static DataSource generateDataSource(DataAccessConf dataAccessConf, IdentityConf identityConf) {
        LOGGER.info("dataAccessConf = {}, identityConf = {}", dataAccessConf, identityConf);

        if (isNull(dataAccessConf) || isNull(identityConf))
            throw new RuntimeException("shardingConf or identityConf can't be null");

        try {
            DataAccessConfElements dataAccessConfElements = generateDataConfAttr(dataAccessConf, identityConf);

            DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataAccessConfElements.getDataSources(),
                    dataAccessConfElements.getShardingRuleConfiguration(), dataAccessConfElements.getProps());

            List<UnaryOperator<DataSource>> proxiesChain = dataAccessConf.getProxiesChain();
            if (isNotEmpty(proxiesChain))
                for (UnaryOperator<DataSource> proxy : proxiesChain)
                    dataSource = proxy.apply(dataSource);

            return dataSource;
        } catch (Exception e) {
            LOGGER.error("dataAccessConf = {}, identityConf = {}, e = {}", dataAccessConf, identityConf, e);
            throw new RuntimeException("generateDataSource failed, e = " + e);
        }
    }

    /**
     * generate sqlsession factory
     *
     * @param dataSource
     * @param dataAccessConf
     * @return
     */
    public static SqlSessionFactory generateSqlSessionFactory(DataSource dataSource, DataAccessConf dataAccessConf) {
        LOGGER.info("dataSource = {}, dataAccessConf = {}", dataSource, dataAccessConf);

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();

        ofNullable(dataAccessConf.getSafeRowBoundsEnabled()).ifPresent(configuration::setSafeRowBoundsEnabled);
        ofNullable(dataAccessConf.getSafeResultHandlerEnabled()).ifPresent(configuration::setSafeResultHandlerEnabled);
        ofNullable(dataAccessConf.getMapUnderscoreToCamelCase()).ifPresent(configuration::setMapUnderscoreToCamelCase);
        ofNullable(dataAccessConf.getAggressiveLazyLoading()).ifPresent(configuration::setAggressiveLazyLoading);
        ofNullable(dataAccessConf.getMultipleResultSetsEnabled()).ifPresent(configuration::setMultipleResultSetsEnabled);
        ofNullable(dataAccessConf.getUseGeneratedKeys()).ifPresent(configuration::setUseGeneratedKeys);
        ofNullable(dataAccessConf.getUseColumnLabel()).ifPresent(configuration::setUseColumnLabel);
        ofNullable(dataAccessConf.getCacheEnabled()).ifPresent(configuration::setCacheEnabled);
        ofNullable(dataAccessConf.getCallSettersOnNulls()).ifPresent(configuration::setCallSettersOnNulls);
        ofNullable(dataAccessConf.getUseActualParamName()).ifPresent(configuration::setUseActualParamName);
        ofNullable(dataAccessConf.getReturnInstanceForEmptyRow()).ifPresent(configuration::setReturnInstanceForEmptyRow);
        ofNullable(dataAccessConf.getShrinkWhitespacesInSql()).ifPresent(configuration::setShrinkWhitespacesInSql);
        ofNullable(dataAccessConf.getNullableOnForEach()).ifPresent(configuration::setNullableOnForEach);
        ofNullable(dataAccessConf.getLocalCacheScope()).ifPresent(configuration::setLocalCacheScope);
        ofNullable(dataAccessConf.getJdbcTypeForNull()).ifPresent(configuration::setJdbcTypeForNull);
        ofNullable(dataAccessConf.getDefaultStatementTimeout()).ifPresent(configuration::setDefaultStatementTimeout);
        ofNullable(dataAccessConf.getDefaultFetchSize()).ifPresent(configuration::setDefaultFetchSize);
        ofNullable(dataAccessConf.getDefaultResultSetType()).ifPresent(configuration::setDefaultResultSetType);
        ofNullable(dataAccessConf.getDefaultExecutorType()).ifPresent(configuration::setDefaultExecutorType);
        ofNullable(dataAccessConf.getAutoMappingBehavior()).ifPresent(configuration::setAutoMappingBehavior);
        ofNullable(dataAccessConf.getAutoMappingUnknownColumnBehavior()).ifPresent(configuration::setAutoMappingUnknownColumnBehavior);
        ofNullable(dataAccessConf.getLazyLoadingEnabled()).ifPresent(configuration::setLazyLoadingEnabled);

        if (ofNullable(dataAccessConf.getDebugLogging()).orElse(false))
            configuration.setLogImpl(StdOutImpl.class);

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setDataSource(dataSource);

        try {
            sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(dataAccessConf.getMapperLocation()));
            return sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            LOGGER.error("generate sqlSessionFactory failed, e = {}", e);
            throw new RuntimeException("generate sqlSessionFactory failed, e = " + e);
        }

    }

    /**
     * generate sqlsession template
     *
     * @param sqlSessionFactory
     * @return
     */
    public static SqlSessionTemplate generateSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, DataAccessConf dataAccessConf) {
        return new SqlSessionTemplate(sqlSessionFactory, ofNullable(dataAccessConf).map(DataAccessConf::getDefaultExecutorType).orElse(BATCH));
    }

    /**
     * generate transaction manager
     *
     * @param dataSource
     * @return
     */
    public static TransactionManager generateTxManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * generate elements for datasource creation
     *
     * @param dataAccessConf
     * @param identityConf
     * @return
     */
    @SuppressWarnings("AlibabaMethodTooLong")
    private static DataAccessConfElements generateDataConfAttr(DataAccessConf dataAccessConf, IdentityConf identityConf) {
        //prepare
        if (isNull(dataAccessConf) || isNull(identityConf))
            throw new RuntimeException("shardingConf or shardingIdentityConf can't be null");

        int shardingSize = ofNullable(dataAccessConf.getShardingDatabases()).map(List::size).orElse(0);
        if (shardingSize > MAX_DATA_CENTER_ID.max)
            throw new RuntimeException("shardingDatabases can't be more than max datacenter size -> " + MAX_DATA_CENTER_ID.max);

        int totalSize = shardingSize + ofNullable(dataAccessConf.getSingleDatabasesWithTables()).map(List::size).orElse(0);
        if (totalSize < 1)
            throw new RuntimeException("shardingDatabase can't be null");

        Map<String, DataSource> dataSources = new HashMap<>(totalSize, 2.0f);
        Set<String> existDatabases = new HashSet<>(totalSize);
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();

        //process sharding db
        processShardingAndForceDb(shardingRuleConfiguration, dataSources, existDatabases, dataAccessConf, identityConf);
        //process single db
        processSingleDb(shardingRuleConfiguration, dataSources, existDatabases, dataAccessConf, identityConf);

        //additional attributes
        Properties props = new Properties();
        ofNullable(dataAccessConf.getProps()).ifPresent(props::putAll);

        return new DataAccessConfElements(dataSources, shardingRuleConfiguration, props);
    }

    /**
     * process sharding and force db
     *
     * @param shardingRuleConfiguration
     * @param dataSources
     * @param existDatabases
     * @param dataAccessConf
     * @param identityConf
     */
    @SuppressWarnings("AlibabaMethodTooLong")
    private static void processShardingAndForceDb(ShardingRuleConfiguration shardingRuleConfiguration, Map<String, DataSource> dataSources, Set<String> existDatabases, DataAccessConf dataAccessConf, IdentityConf identityConf) {
        if (isNull(shardingRuleConfiguration) || isNull(dataSources) || isNull(existDatabases) || isNull(dataAccessConf) || isNull(identityConf))
            throw new RuntimeException("shardingRuleConfiguration or dataSources or existDatabases or dataAccessConf or identityConf can't be null");

        List<ShardingDatabaseAttr> shardingDatabases = dataAccessConf.getShardingDatabases();

        int shardingSize = ofNullable(shardingDatabases).map(List::size).orElse(0);
        if (shardingSize < 1)
            return;

        List<Integer> assertIndexList = new ArrayList<>(shardingSize);

        String tempLogicDataBaseName, dataBaseIndexStr;
        int dataBaseNameLen, nameAndIndexConcatenationIdx, dataBaseIndex;
        String logicDataBaseName = null;

        String dataBaseName;
        for (ShardingDatabaseAttr shardingDatabase : shardingDatabases) {
            dataBaseName = parseDbName(shardingDatabase);

            if (!existDatabases.add(dataBaseName))
                throw new RuntimeException("database name dupicates, dataBaseName = " + dataBaseName);

            dataBaseNameLen = dataBaseName.length();
            nameAndIndexConcatenationIdx = lastIndexOf(dataBaseName, PAR_CONCATENATION.identity);
            if (nameAndIndexConcatenationIdx < 0 || nameAndIndexConcatenationIdx == dataBaseNameLen)
                throw new RuntimeException("database name must consist of logical name and index number, for example -> member_0");

            if (isBlank(tempLogicDataBaseName = dataBaseName.substring(0, nameAndIndexConcatenationIdx)))
                throw new RuntimeException("database logical name can't be blank, for example -> member_0");

            if (isBlank(dataBaseIndexStr = dataBaseName.substring(nameAndIndexConcatenationIdx + 1, dataBaseNameLen)))
                throw new RuntimeException("database index number can't be null, for example -> member_0");

            try {
                dataBaseIndex = parseInt(dataBaseIndexStr);
            } catch (NumberFormatException e) {
                throw new RuntimeException("database index number only number, for example -> member_0");
            }

            if (dataBaseIndex < 0)
                throw new RuntimeException("database index number can't be less than 0");

            assertIndexList.add(dataBaseIndex);

            if (isNotNull(logicDataBaseName)) {
                if (!tempLogicDataBaseName.equals(logicDataBaseName))
                    throw new RuntimeException("sharding db in same micro service must has the same prefix, " + tempLogicDataBaseName + "/" + logicDataBaseName);
            } else {
                logicDataBaseName = tempLogicDataBaseName;
            }

            dataSources.put(dataBaseName, DATA_SOURCE_GENERATOR.apply(shardingDatabase));
        }

        if (0 != assertIndexList.stream().min(Integer::compare).orElse(-1))
            throw new RuntimeException("db index number must start at 0");

        if (!assertDisorderIntegerContinuous(assertIndexList))
            throw new RuntimeException("The database index set should be a continuous number starting from 0");

        Integer shardingTableSizePerDataBase = dataAccessConf.getShardingTableSizePerDatabase();
        if (isNull(shardingTableSizePerDataBase) || shardingTableSizePerDataBase < 1 || shardingTableSizePerDataBase > MAX_WORKER_ID.max)
            throw new RuntimeException("the number of table splits in each database cannot be less than 1 or greater than the maximum number of machines in each data center -> " + MAX_WORKER_ID.max);

        int maxDataBaseIndex = shardingSize - 1;
        int maxTableIndex = shardingTableSizePerDataBase - 1;

        Integer dataCenter = identityConf.getDataCenter();
        if (isNull(dataCenter) || dataCenter < 0)
            throw new RuntimeException("dataCenter can't be null or less than 0");

        List<IdentityToShardingMappingAttr> dataCenterToDatabaseMappings = dataAccessConf.getDataCenterToDatabaseMappings();
        if (isEmpty(dataCenterToDatabaseMappings))
            throw new RuntimeException("dataCenterToDatabaseMappings can't be empty");

        Set<Integer> dbMappingIds = dataCenterToDatabaseMappings.stream().map(IdentityToShardingMappingAttr::getId).collect(toSet());
        if (dbMappingIds.size() < dataCenterToDatabaseMappings.size())
            throw new RuntimeException("dataCenterToDatabaseMappings contains duplicate id, dataCenterToDatabaseMappings = " + dataCenterToDatabaseMappings);
        if (!dbMappingIds.contains(dataCenter))
            throw new RuntimeException("dbMappingIds not contains dataCenter, dbMappingIds = " + dbMappingIds + ", dataCenter = " + dataCenter);

        Integer maxDbMappingIndex = dataCenterToDatabaseMappings.stream().map(IdentityToShardingMappingAttr::getIndex).max(Integer::compareTo).orElseThrow(() ->
                new RuntimeException("dbMapping's index can't be empty"));
        if (maxDbMappingIndex > maxDataBaseIndex)
            throw new RuntimeException("maxDbMappingIndex can't be greater than maxDataBaseIndex, maxDbMappingIndex = " + maxDbMappingIndex + ", maxDataBaseIndex = " + maxDataBaseIndex);

        Integer worker = identityConf.getWorker();
        if (isNull(worker) || worker < 0)
            throw new RuntimeException("worker can't be null or less than 0");

        List<IdentityToShardingMappingAttr> workerToTableMappings = dataAccessConf.getWorkerToTableMappings();
        if (isEmpty(workerToTableMappings))
            throw new RuntimeException("workerToTableMappings can't be empty");

        Set<Integer> wokerMappingIds = workerToTableMappings.stream().map(IdentityToShardingMappingAttr::getId).collect(toSet());
        if (wokerMappingIds.size() < workerToTableMappings.size())
            throw new RuntimeException("workerToTableMappings contains duplicate id, workerToTableMappings = " + workerToTableMappings);
        if (!wokerMappingIds.contains(worker))
            throw new RuntimeException("wokerMappingIds not contains dataCenter, wokerMappingIds = " + wokerMappingIds + ", worker = " + worker);

        Integer maxWorkerMappingIndex = workerToTableMappings.stream().map(IdentityToShardingMappingAttr::getIndex).max(Integer::compareTo).orElseThrow(() ->
                new RuntimeException("workerMapping's index can't be empty"));
        if (maxWorkerMappingIndex > maxTableIndex)
            throw new RuntimeException("maxWorkerMappingIndex can't be greater than maxTableIndex, maxWorkerMappingIndex = " + maxWorkerMappingIndex + ", maxTableIndex = " + maxTableIndex);

        List<ShardingTableAttr> shardingTables = dataAccessConf.getShardingTables();
        if (isEmpty(shardingTables))
            throw new RuntimeException("shardingTables cannot be null, and should contains all tables that need to be fragmented, otherwise data fragmentation cannot be defined");

        String shardingLogicDataBaseName = logicDataBaseName;

        shardingRuleConfiguration.getTableRuleConfigs().addAll(
                shardingTables.stream().distinct().map(tableAttr -> {
                    String logicTableName = tableAttr.getTableName();
                    if (isBlank(logicTableName))
                        throw new RuntimeException("logicTableName can't be blank");

                    String shardingColumn = tableAttr.getShardingColumn();
                    if (isBlank(shardingColumn))
                        throw new RuntimeException("shardingColumn can't be blank");

                    String expression = shardingLogicDataBaseName + "_$->{0.." + maxDataBaseIndex + "}." + logicTableName + "_$->{0.." + maxTableIndex + "}";

                    TableRuleConfiguration conf = new TableRuleConfiguration(logicTableName, expression);
                    conf.setDatabaseShardingStrategyConfig(
                            new StandardShardingStrategyConfiguration(shardingColumn,
                                    new SnowflakeDatabaseShardingAlgorithm(shardingLogicDataBaseName, dataCenterToDatabaseMappings)));
                    conf.setTableShardingStrategyConfig(
                            new StandardShardingStrategyConfiguration(shardingColumn,
                                    new SnowflakeTableShardingAlgorithm(logicTableName, workerToTableMappings)));

                    return conf;
                }).collect(toList()));

        ofNullable(dataAccessConf.getForceWriteTables())
                .filter(fwts -> fwts.size() > 0)
                .ifPresent(fwts -> {
                            ShardingStrategyConfiguration noneShardingStrategyConfiguration = new NoneShardingStrategyConfiguration();
                            shardingRuleConfiguration.getTableRuleConfigs().addAll(
                                    fwts.stream().distinct().map(tableAttr -> {
                                        String logicTableName = tableAttr.getTableName();
                                        if (isBlank(logicTableName))
                                            throw new RuntimeException("logicTableName can't be blank");

                                        TableRuleConfiguration conf = new TableRuleConfiguration(logicTableName,
                                                parseForceDbName(shardingLogicDataBaseName, dataCenterToDatabaseMappings, tableAttr.getDataCenter()) +
                                                        PERIOD.identity +
                                                        parseForceTableName(logicTableName, workerToTableMappings, tableAttr.getWorker()));
                                        conf.setDatabaseShardingStrategyConfig(noneShardingStrategyConfiguration);
                                        conf.setTableShardingStrategyConfig(noneShardingStrategyConfiguration);

                                        return conf;
                                    }).collect(toList()));
                        }
                );

        ofNullable(dataAccessConf.getBroadcastTables())
                .filter(sbts -> sbts.size() > 0)
                .ifPresent(sbts ->
                        shardingRuleConfiguration.getBroadcastTables().addAll(
                                sbts.stream().filter(BlueChecker::isNotBlank).distinct().collect(toList())));
    }

    /**
     * process single db
     *
     * @param shardingRuleConfiguration
     * @param dataSources
     * @param existDatabases
     * @param dataAccessConf
     * @param identityConf
     */
    private static void processSingleDb(ShardingRuleConfiguration
                                                shardingRuleConfiguration, Map<String, DataSource> dataSources, Set<String> existDatabases, DataAccessConf
                                                dataAccessConf, IdentityConf identityConf) {
        if (isNull(shardingRuleConfiguration) || isNull(dataSources) || isNull(existDatabases) || isNull(dataAccessConf) || isNull(identityConf))
            throw new RuntimeException("shardingRuleConfiguration or dataSources or existDatabases or dataAccessConf or identityConf can't be null");

        List<SingleDatabaseWithTablesAttr> singleDatabasesWithTables = dataAccessConf.getSingleDatabasesWithTables();
        int singleSize = ofNullable(singleDatabasesWithTables).map(List::size).orElse(0);

        if (singleSize < 1)
            return;

        String dataBaseName;
        for (SingleDatabaseWithTablesAttr single : singleDatabasesWithTables) {
            dataBaseName = parseDbName(single);

            if (!existDatabases.add(dataBaseName))
                throw new RuntimeException("database name dupicates, dataBaseName = " + dataBaseName);

            List<String> singleTables = single.getSingleTables();
            if (isEmpty(singleTables))
                continue;

            dataSources.put(dataBaseName, DATA_SOURCE_GENERATOR.apply(single));

            String singleDataBaseName = dataBaseName;

            ShardingStrategyConfiguration noneShardingStrategyConfiguration = new NoneShardingStrategyConfiguration();
            shardingRuleConfiguration.getTableRuleConfigs().addAll(
                    singleTables.stream().distinct().map(tableName -> {
                        TableRuleConfiguration conf = new TableRuleConfiguration(tableName, singleDataBaseName + PERIOD.identity + tableName);
                        conf.setDatabaseShardingStrategyConfig(noneShardingStrategyConfiguration);
                        conf.setTableShardingStrategyConfig(noneShardingStrategyConfiguration);

                        return conf;
                    }).collect(toList()));
        }
    }

    /**
     * parse database name
     *
     * @param attr
     * @return
     */
    private static String parseDbName(ShardingDatabaseAttr attr) {
        if (isNull(attr))
            throw new RuntimeException("attr can't be null");

        String[] urlParts;
        String url, dataBaseName;

        if (isBlank(url = attr.getUrl()))
            throw new RuntimeException("url can't be blank");

        if ((urlParts = url.split(SLASH.identity)).length < MIN_VALID_DB_URL_PARTS_LEN)
            throw new RuntimeException("invalid url, url = " + url);

        if (isBlank(dataBaseName = urlParts[urlParts.length - 1]))
            throw new RuntimeException("database name can't be blank, dataBaseName = " + dataBaseName);

        return dataBaseName;
    }

    /**
     * parst force write db name
     *
     * @param logicDataBaseName
     * @param dataCenterToDatabaseMappings
     * @param dataCenter
     * @return
     */
    private static String parseForceDbName(String logicDataBaseName, List<IdentityToShardingMappingAttr> dataCenterToDatabaseMappings, Integer dataCenter) {
        if (isBlank(logicDataBaseName))
            throw new IdentityException("logicDataBaseName can't be blank");
        if (isNull(dataCenter) || dataCenter < 0)
            return logicDataBaseName;

        if (isEmpty(dataCenterToDatabaseMappings))
            throw new RuntimeException("dataCenterToDatabaseMappings can't be empty");

        Integer id;
        Integer index;
        for (IdentityToShardingMappingAttr attr : dataCenterToDatabaseMappings) {
            id = attr.getId();
            index = attr.getIndex();
            if (isNull(id) || id < 0)
                throw new RuntimeException("id can't be less than 0");
            if (isNull(index) || index < 0)
                throw new RuntimeException("index can't be less than 0");

            if (dataCenter.equals(id))
                return logicDataBaseName + PAR_CONCATENATION.identity + index;
        }

        throw new RuntimeException("mappings has no data center id " + dataCenter);
    }

    /**
     * parst force write table name
     *
     * @param logicTableName
     * @param workerToTableMappings
     * @param worker
     * @return
     */
    private static String parseForceTableName(String logicTableName, List<IdentityToShardingMappingAttr> workerToTableMappings, Integer worker) {
        if (isBlank(logicTableName))
            throw new IdentityException("logicTableName can't be blank");
        if (isNull(worker) || worker < 0)
            return logicTableName;

        if (isEmpty(workerToTableMappings))
            throw new RuntimeException("workerToTableMappings can't be empty");

        Integer id;
        Integer index;
        for (IdentityToShardingMappingAttr attr : workerToTableMappings) {
            id = attr.getId();
            index = attr.getIndex();
            if (isNull(id) || id < 0)
                throw new RuntimeException("id can't be less than 0");
            if (isNull(index) || index < 0)
                throw new RuntimeException("index can't be less than 0");

            if (worker.equals(id))
                return logicTableName + PAR_CONCATENATION.identity + index;
        }

        throw new RuntimeException("mappings has no worker id " + worker);
    }

    /**
     * config elements
     */
    private static final class DataAccessConfElements {

        private final Map<String, DataSource> dataSources;

        private final ShardingRuleConfiguration shardingRuleConfiguration;

        private final Properties props;

        DataAccessConfElements(Map<String, DataSource> dataSources, ShardingRuleConfiguration shardingRuleConfiguration, Properties props) {
            if (isNull(dataSources) || isNull(shardingRuleConfiguration) || isNull(props))
                throw new RuntimeException("dataSources or shardingRuleConfiguration or props can't be null");

            this.dataSources = dataSources;
            this.shardingRuleConfiguration = shardingRuleConfiguration;
            this.props = props;
        }

        Map<String, DataSource> getDataSources() {
            return dataSources;
        }

        ShardingRuleConfiguration getShardingRuleConfiguration() {
            return shardingRuleConfiguration;
        }

        Properties getProps() {
            return props;
        }
    }

}