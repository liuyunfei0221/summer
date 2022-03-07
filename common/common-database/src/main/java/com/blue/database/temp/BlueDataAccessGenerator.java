package com.blue.database.temp;

import com.blue.base.model.exps.BlueException;
import com.blue.database.api.conf.*;
import com.blue.database.common.DatabaseForceAlgorithm;
import com.blue.database.common.DatabaseShardingAlgorithm;
import com.blue.database.common.TableForceAlgorithm;
import com.blue.database.common.TableShardingAlgorithm;
import com.blue.identity.api.conf.IdentityConf;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.ExecutorType;
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
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.blue.base.common.base.MathProcessor.assertDisorderIntegerContinuous;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.Symbol.*;
import static com.blue.identity.constant.IdentitySchema.MAX_DATA_CENTER_ID;
import static com.blue.identity.constant.IdentitySchema.MAX_WORKER_ID;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * data access component generator
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "PlaceholderCountMatchesArgumentCount", "AliControlFlowStatementWithoutBraces", "SpellCheckingInspection", "DuplicatedCode"})
public final class BlueDataAccessGenerator {

    private static final Logger LOGGER = getLogger(BlueDataAccessGenerator.class);

    /**
     * generate datasource
     *
     * @param dataAccessConf
     * @param identityConf
     * @return
     */
    public static DataSource generateDataSource(DataAccessConf dataAccessConf, IdentityConf identityConf) {
        if (dataAccessConf == null || identityConf == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "shardingConf or identityConf can't be null");

        try {
            DataAccessConfElements dataAccessConfElements = generateDataConfAttr(dataAccessConf, identityConf, dataAccessConf.getProxiesChain());
            return ShardingDataSourceFactory.createDataSource(dataAccessConfElements.getDataSources(), dataAccessConfElements.getShardingRuleConfiguration(), dataAccessConfElements.getProps());
        } catch (Exception e) {
            LOGGER.error("createDataSource(ShardingConf shardingConf, ShardingIdentityConf shardingIdentityConf, List<UnaryOperator<DataSource>> proxiesChain) failed, e = {}", e);
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "createDataSource(ShardingConf shardingConf, ShardingIdentityConf shardingIdentityConf, List<UnaryOperator<DataSource>> proxiesChain) failed, e = " + e);
        }
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
     * generate sqlsession factory
     *
     * @param dataSource
     * @param dataAccessConf
     * @return
     */
    public static SqlSessionFactory generateSqlSessionFactory(DataSource dataSource, DataAccessConf dataAccessConf) {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();

        ofNullable(dataAccessConf.getCacheEnabled()).ifPresent(configuration::setCacheEnabled);
        ofNullable(dataAccessConf.getLazyLoadingEnabled()).ifPresent(configuration::setLazyLoadingEnabled);
        ofNullable(dataAccessConf.getAggressiveLazyLoading()).ifPresent(configuration::setAggressiveLazyLoading);
        ofNullable(dataAccessConf.getMultipleResultSetsEnabled()).ifPresent(configuration::setMultipleResultSetsEnabled);
        ofNullable(dataAccessConf.getUseColumnLabel()).ifPresent(configuration::setUseColumnLabel);
        ofNullable(dataAccessConf.getUseGeneratedKeys()).ifPresent(configuration::setUseGeneratedKeys);
        ofNullable(dataAccessConf.getConnectionTimeout()).ifPresent(configuration::setDefaultStatementTimeout);
        ofNullable(dataAccessConf.getAutoMappingBehavior()).ifPresent(configuration::setAutoMappingBehavior);
        ofNullable(dataAccessConf.getExecutorType()).ifPresent(configuration::setDefaultExecutorType);
        if (ofNullable(dataAccessConf.getDebugLogging()).orElse(false))
            configuration.setLogImpl(StdOutImpl.class);

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setDataSource(dataSource);

        try {
            ResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
            sqlSessionFactoryBean.setMapperLocations(resourceLoader.getResources(dataAccessConf.getMapperLocation()));
        } catch (IOException e) {
            LOGGER.error("invalid mapper.xml path, e = {}", e);
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "invalid mapper.xml path");
        }

        try {
            return sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            LOGGER.error("generate sqlSessionFactory failed, e = {}", e);
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "generate sqlSessionFactory failed, e = " + e);
        }

    }

    /**
     * generate sqlsession template
     *
     * @param sqlSessionFactory
     * @return
     */
    public static SqlSessionTemplate generateSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
        return new SqlSessionTemplate(sqlSessionFactory, executorType);
    }

    //<editor-fold desc="constants">
    /**
     * valid data element parts num
     */
    private static final int MIN_VALID_DB_URL_PARTS_LEN = 4;
    //</editor-fold>

    /**
     * datasource generator
     */
    private static final Function<ShardingDatabaseAttr, DataSource> DATA_SOURCE_GENERATOR = shardAttr -> {
        HikariConfig hikariConfig = new HikariConfig();

        String driverClassName = shardAttr.getDriverClassName();
        if (isBlank(driverClassName))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "driverClassName can't be blank");

        String url = shardAttr.getUrl();
        if (isBlank(url))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "url can't be blank");

        String username = shardAttr.getUsername();
        if (isBlank(username))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "username can't be blank");

        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(url + ofNullable(shardAttr.getDataBaseConf()).map(c -> PAR_CONCATENATION_DATABASE_CONF.identity + c).orElse(""));
        hikariConfig.setUsername(username);
        ofNullable(shardAttr.getPassword()).ifPresent(hikariConfig::setPassword);

        ofNullable(shardAttr.getReadOnly()).ifPresent(hikariConfig::setReadOnly);
        ofNullable(shardAttr.getConnectionTimeout()).ifPresent(hikariConfig::setConnectionTimeout);
        ofNullable(shardAttr.getMaxLifetime()).ifPresent(hikariConfig::setMaxLifetime);
        ofNullable(shardAttr.getMaximumPoolSize()).ifPresent(hikariConfig::setMaximumPoolSize);
        ofNullable(shardAttr.getMinimumIdle()).ifPresent(hikariConfig::setMinimumIdle);
        ofNullable(shardAttr.getTestQuery()).ifPresent(hikariConfig::setConnectionTestQuery);

        return new HikariDataSource(hikariConfig);
    };

    /**
     * generate elements for datasource creation
     *
     * @param dataAccessConf
     * @param identityConf
     * @return
     */
    @SuppressWarnings("AlibabaMethodTooLong")
    private static DataAccessConfElements generateDataConfAttr(DataAccessConf dataAccessConf, IdentityConf identityConf, List<UnaryOperator<DataSource>> proxiesChain) {
        //prepare
        if (dataAccessConf == null || identityConf == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "shardingConf or shardingIdentityConf can't be null");

        int shardingSize = ofNullable(dataAccessConf.getShardingDatabases()).map(List::size).orElse(0);
        if (shardingSize > MAX_DATA_CENTER_ID.max)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "shardingDatabases can't be more than max datacenter size -> " + MAX_DATA_CENTER_ID.max);

        int totalSize = shardingSize + ofNullable(dataAccessConf.getSingleDatabasesWithTables()).map(List::size).orElse(0);
        if (totalSize < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "shardingDatabase can't be null");

        Map<String, DataSource> dataSources = new HashMap<>(totalSize);
        Set<String> existDatabases = new HashSet<>(totalSize);
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();

        //process sharding db
        processShardingDb(shardingRuleConfiguration, dataSources, existDatabases, dataAccessConf, identityConf, proxiesChain);

        //process single db
        processSingleDb(shardingRuleConfiguration, dataSources, existDatabases, dataAccessConf, identityConf, proxiesChain);

        //additional attributes
        Properties props = new Properties();
        ofNullable(dataAccessConf.getProps()).ifPresent(props::putAll);

        return new DataAccessConfElements(dataSources, shardingRuleConfiguration, props);
    }

    /**
     * process sharding db
     *
     * @param shardingRuleConfiguration
     * @param dataSources
     * @param existDatabases
     * @param dataAccessConf
     * @param identityConf
     */
    @SuppressWarnings("AlibabaMethodTooLong")
    private static void processShardingDb(ShardingRuleConfiguration shardingRuleConfiguration, Map<String, DataSource> dataSources, Set<String> existDatabases, DataAccessConf dataAccessConf, IdentityConf identityConf, List<UnaryOperator<DataSource>> proxiesChain) {
        if (shardingRuleConfiguration == null || dataSources == null || existDatabases == null || dataAccessConf == null || identityConf == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "shardingRuleConfiguration or dataSources or existDatabases or dataAccessConf or identityConf can't be null");

        List<ShardingDatabaseAttr> shardingDatabases = dataAccessConf.getShardingDatabases();

        int shardingSize = ofNullable(shardingDatabases).map(List::size).orElse(0);
        if (shardingSize < 1)
            return;

        List<UnaryOperator<DataSource>> tarChain = ofNullable(proxiesChain).map(pc -> pc.stream().filter(Objects::nonNull).collect(toList())).orElseGet(Collections::emptyList);

        List<Integer> assertIndexList = new ArrayList<>(shardingSize);

        String tempLogicDataBaseName, dataBaseIndexStr;
        int dataBaseNameLen, nameAndIndexConcatenationIdx, dataBaseIndex;
        String logicDataBaseName = null;

        String dataBaseName;
        DataSource dataSource;
        for (ShardingDatabaseAttr shardingDatabase : shardingDatabases) {
            dataBaseName = parseDbName(shardingDatabase);

            if (!existDatabases.add(dataBaseName))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "database name dupicates, dataBaseName = " + dataBaseName);

            dataBaseNameLen = dataBaseName.length();
            nameAndIndexConcatenationIdx = lastIndexOf(dataBaseName, PAR_CONCATENATION.identity);
            if (nameAndIndexConcatenationIdx < 0 || nameAndIndexConcatenationIdx == dataBaseNameLen)
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "database name must consist of logical name and index number, for example -> member_0");

            if (isBlank(tempLogicDataBaseName = dataBaseName.substring(0, nameAndIndexConcatenationIdx)))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "database logical name can't be blank, for example -> member_0");

            if (isBlank(dataBaseIndexStr = dataBaseName.substring(nameAndIndexConcatenationIdx + 1, dataBaseNameLen)))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "database index number can't be null, for example -> member_0");

            try {
                dataBaseIndex = Integer.parseInt(dataBaseIndexStr);
            } catch (NumberFormatException e) {
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "database index number only number, for example -> member_0");
            }

            if (dataBaseIndex < 0)
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "database index number can't be less than 0");

            assertIndexList.add(dataBaseIndex);

            if (logicDataBaseName != null) {
                if (!tempLogicDataBaseName.equals(logicDataBaseName))
                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "sharding db in same micro service must has the same prefix, " + tempLogicDataBaseName + "/" + logicDataBaseName);
            } else {
                logicDataBaseName = tempLogicDataBaseName;
            }

            dataSource = DATA_SOURCE_GENERATOR.apply(shardingDatabase);

            if (!isEmpty(tarChain))
                for (UnaryOperator<DataSource> proxy : tarChain)
                    dataSource = proxy.apply(dataSource);

            dataSources.put(dataBaseName, dataSource);
        }

        if (0 != assertIndexList.stream().min(Integer::compare).orElse(-1))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "db index number must start at 0");

        if (!assertDisorderIntegerContinuous(assertIndexList))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "The database index set should be a continuous number starting from 0");

        Integer shardingTableSizePerDataBase = dataAccessConf.getShardingTableSizePerDataBase();
        if (shardingTableSizePerDataBase == null || shardingTableSizePerDataBase < 1 || shardingTableSizePerDataBase > MAX_WORKER_ID.max)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "the number of table splits in each database cannot be less than 1 or greater than the maximum number of machines in each data center -> " + MAX_WORKER_ID.max);

        int maxDataBaseIndex = shardingSize - 1;
        int maxTableIndex = shardingTableSizePerDataBase - 1;

        Integer dataCenter = identityConf.getDataCenter();
        if (dataCenter == null || dataCenter < 0)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "dataCenter can't be null or less than 0");

        List<IdentityToShardingMappingAttr> dataCenterToDatabaseMappings = dataAccessConf.getDataCenterToDatabaseMappings();
        if (isEmpty(dataCenterToDatabaseMappings))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "dataCenterToDatabaseMappings can't be empty");

        Set<Integer> dbMappingIds = dataCenterToDatabaseMappings.stream().map(IdentityToShardingMappingAttr::getId).collect(toSet());
        if (dbMappingIds.size() < dataCenterToDatabaseMappings.size())
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "dataCenterToDatabaseMappings contains duplicate id, dataCenterToDatabaseMappings = " + dataCenterToDatabaseMappings);
        if (!dbMappingIds.contains(dataCenter))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "dbMappingIds not contains dataCenter, dbMappingIds = " + dbMappingIds + ", dataCenter = " + dataCenter);

        Integer maxDbMappingIndex = dataCenterToDatabaseMappings.stream().map(IdentityToShardingMappingAttr::getIndex).max(Integer::compareTo).orElseThrow(() ->
                new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "dbMapping's index can't be empty"));
        if (maxDbMappingIndex > maxDataBaseIndex)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "maxDbMappingIndex can't be greater than maxDataBaseIndex, maxDbMappingIndex = " + maxDbMappingIndex + ", maxDataBaseIndex = " + maxDataBaseIndex);

        Integer worker = identityConf.getWorker();
        if (worker == null || worker < 0)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "worker can't be null or less than 0");

        List<IdentityToShardingMappingAttr> workerToTableMappings = dataAccessConf.getWorkerToTableMappings();
        if (isEmpty(workerToTableMappings))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "workerToTableMappings can't be empty");

        Set<Integer> wokerMappingIds = workerToTableMappings.stream().map(IdentityToShardingMappingAttr::getId).collect(toSet());
        if (wokerMappingIds.size() < workerToTableMappings.size())
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "workerToTableMappings contains duplicate id, workerToTableMappings = " + workerToTableMappings);
        if (!wokerMappingIds.contains(worker))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "wokerMappingIds not contains dataCenter, wokerMappingIds = " + wokerMappingIds + ", worker = " + worker);

        Integer maxWorkerMappingIndex = workerToTableMappings.stream().map(IdentityToShardingMappingAttr::getIndex).max(Integer::compareTo).orElseThrow(() ->
                new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "workerMapping's index can't be empty"));
        if (maxWorkerMappingIndex > maxTableIndex)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "maxWorkerMappingIndex can't be greater than maxTableIndex, maxWorkerMappingIndex = " + maxWorkerMappingIndex + ", maxTableIndex = " + maxTableIndex);

        List<ShardingTableAttr> shardingTables = dataAccessConf.getShardingTables();
        if (isEmpty(shardingTables))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "shardingTables cannot be null, and should contains all tables that need to be fragmented, otherwise data fragmentation cannot be defined");

        String shardingLogicDataBaseName = logicDataBaseName;

        shardingRuleConfiguration.getTableRuleConfigs().addAll(
                shardingTables.stream().distinct().map(tableAttr -> {
                    String logicTableName = tableAttr.getTableName();
                    if (logicTableName == null || "".equals(logicTableName))
                        throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "logicTableName can't be blank");

                    String shardingColumn = tableAttr.getShardingColumn();
                    if (shardingColumn == null || "".equals(shardingColumn))
                        throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "shardingColumn can't be blank");

                    String expression = shardingLogicDataBaseName + "_$->{0.." + maxDataBaseIndex + "}." + logicTableName + "_$->{0.." + maxTableIndex + "}";

                    TableRuleConfiguration conf = new TableRuleConfiguration(logicTableName, expression);
                    conf.setDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration(shardingColumn, new DatabaseShardingAlgorithm(shardingLogicDataBaseName, dataCenterToDatabaseMappings)));
                    conf.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration(shardingColumn, new TableShardingAlgorithm(logicTableName, workerToTableMappings)));

                    return conf;
                }).collect(toList()));

        ofNullable(dataAccessConf.getForceWriteTables())
                .filter(fwts -> fwts.size() > 0)
                .ifPresent(fwts ->
                        shardingRuleConfiguration.getTableRuleConfigs().addAll(
                                fwts.stream().distinct().map(tableAttr -> {
                                    String logicTableName = tableAttr.getTableName();
                                    if (logicTableName == null || "".equals(logicTableName))
                                        throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "logicTableName can't be blank");

                                    String shardingColumn = tableAttr.getShardingColumn();
                                    if (shardingColumn == null || "".equals(shardingColumn))
                                        throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "shardingColumn can't be blank");

                                    String expression = shardingLogicDataBaseName + "_$->{0.." + maxDataBaseIndex + "}." + logicTableName + "_$->{0.." + maxTableIndex + "}";

                                    TableRuleConfiguration conf = new TableRuleConfiguration(logicTableName, expression);
                                    conf.setDatabaseShardingStrategyConfig(
                                            new StandardShardingStrategyConfiguration(shardingColumn,
                                                    new DatabaseForceAlgorithm(shardingLogicDataBaseName, dataCenterToDatabaseMappings, dataCenter)));
                                    conf.setTableShardingStrategyConfig(
                                            new StandardShardingStrategyConfiguration(shardingColumn,
                                                    new TableForceAlgorithm(logicTableName, workerToTableMappings, worker)));

                                    return conf;
                                }).collect(toList()))
                );

        ofNullable(dataAccessConf.getBroadcastTables())
                .filter(sbts -> sbts.size() > 0)
                .ifPresent(sbts ->
                        shardingRuleConfiguration.getBroadcastTables().addAll(
                                sbts.stream()
                                        .filter(tb -> tb != null && !"".equals(tb))
                                        .distinct()
                                        .collect(toList())
                        ));
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
    private static void processSingleDb(ShardingRuleConfiguration shardingRuleConfiguration, Map<String, DataSource> dataSources, Set<String> existDatabases, DataAccessConf dataAccessConf, IdentityConf identityConf, List<UnaryOperator<DataSource>> proxiesChain) {
        if (shardingRuleConfiguration == null || dataSources == null || existDatabases == null || dataAccessConf == null || identityConf == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "shardingRuleConfiguration or dataSources or existDatabases or dataAccessConf or identityConf can't be null");

        List<SingleDatabaseWithTablesAttr> singleDatabasesWithTables = dataAccessConf.getSingleDatabasesWithTables();
        int singleSize = ofNullable(singleDatabasesWithTables).map(List::size).orElse(0);

        if (singleSize < 1)
            return;

        List<UnaryOperator<DataSource>> tarChain = ofNullable(proxiesChain).map(pc -> pc.stream().filter(Objects::nonNull).collect(toList())).orElseGet(Collections::emptyList);

        String dataBaseName;
        DataSource dataSource;
        for (SingleDatabaseWithTablesAttr single : singleDatabasesWithTables) {
            dataBaseName = parseDbName(single);

            if (!existDatabases.add(dataBaseName))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "database name dupicates, dataBaseName = " + dataBaseName);

            List<String> singleTables = single.getSingleTables();
            if (isEmpty(singleTables))
                continue;

            dataSource = DATA_SOURCE_GENERATOR.apply(single);

            if (!isEmpty(tarChain))
                for (UnaryOperator<DataSource> proxy : tarChain)
                    dataSource = proxy.apply(dataSource);

            dataSources.put(dataBaseName, dataSource);

            String singleDataBaseName = dataBaseName;

            ShardingStrategyConfiguration noneShardingStrategyConfiguration = new NoneShardingStrategyConfiguration();
            shardingRuleConfiguration.getTableRuleConfigs().addAll(
                    singleTables.stream().distinct().map(tableName -> {
                        TableRuleConfiguration conf = new TableRuleConfiguration(tableName, singleDataBaseName + "." + tableName);
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
        if (attr == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "attr can't be null");

        String[] urlParts;
        String url, dataBaseName;

        if (isBlank(url = attr.getUrl()))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "url can't be blank");

        if ((urlParts = url.split(PATH_SEPARATOR.identity)).length < MIN_VALID_DB_URL_PARTS_LEN)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "invalid url, url = " + url);

        if (isBlank(dataBaseName = urlParts[urlParts.length - 1]))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "database name can't be blank, dataBaseName = " + dataBaseName);

        return dataBaseName;
    }

    /**
     * config elements
     */
    static final class DataAccessConfElements {

        private final Map<String, DataSource> dataSources;

        private final ShardingRuleConfiguration shardingRuleConfiguration;

        private final Properties props;

        DataAccessConfElements(Map<String, DataSource> dataSources, ShardingRuleConfiguration shardingRuleConfiguration, Properties props) {
            if (dataSources == null || shardingRuleConfiguration == null || props == null)
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "dataSources or shardingRuleConfiguration or props can't be null");

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