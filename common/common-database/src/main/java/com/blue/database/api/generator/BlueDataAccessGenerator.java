package com.blue.database.api.generator;

import com.blue.base.common.base.MathProcessor;
import com.blue.base.model.exps.BlueException;
import com.blue.database.api.conf.*;
import com.blue.database.common.DatabaseShardingAlgorithm;
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

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.Symbol.*;
import static com.blue.identity.constant.IdentitySchema.MAX_DATA_CENTER_ID;
import static com.blue.identity.constant.IdentitySchema.MAX_WORKER_ID;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isBlank;
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

        //TODO Print SQL log during development phase
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
    private static final int VALID_DB_URL_PARTS_LEN = 4, VALID_DB_NAME_PARTS_LEN = 2;
    private static final int DATA_BASE_NAME_PAR = 3;
    private static final int DATA_BASE_LOGIC_NAME_PAR = 0, DATA_BASE_INDEX_PAR = 1;
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
     * @param proxiesChain
     * @return
     */
    @SuppressWarnings("AlibabaMethodTooLong")
    private static DataAccessConfElements generateDataConfAttr(DataAccessConf dataAccessConf, IdentityConf identityConf, List<UnaryOperator<DataSource>> proxiesChain) {
        if (dataAccessConf == null || identityConf == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "shardingConf or shardingIdentityConf can't be null");

        List<UnaryOperator<DataSource>> tarChain = ofNullable(proxiesChain).map(pc -> pc.stream().filter(Objects::nonNull).collect(toList())).orElse(emptyList());
        boolean enableProxy = !isEmpty(tarChain);

        //<editor-fold desc="generate sharding datasource">
        List<ShardingDatabaseAttr> shardingDatabases = dataAccessConf.getShardingDatabases();

        int shardingSize;
        if (shardingDatabases == null || (shardingSize = shardingDatabases.size()) < 1 || shardingSize > MAX_DATA_CENTER_ID.max)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "shardingDatabases can't be empty or more than max datacenter size -> " + MAX_DATA_CENTER_ID.max);

        List<SingleDatabaseWithTablesAttr> singleDatabasesWithTables = dataAccessConf.getSingleDatabasesWithTables();

        int totalSize = shardingSize + ofNullable(singleDatabasesWithTables).map(List::size).orElse(0);
        Map<String, DataSource> dataSources = new HashMap<>(totalSize);
        List<Integer> assertIndexList = new ArrayList<>(shardingSize);

        Set<String> existDatabases = new HashSet<>(totalSize);
        String logicDataBaseName = null;
        String[] urlParts, dataBaseNameParts;
        String url, dataBaseName, tempLogicDataBaseName, dataBaseIndexStr;

        int dataBaseIndex;
        DataSource dataSource;
        for (ShardingDatabaseAttr shardingDatabase : shardingDatabases) {
            if (shardingDatabase == null)
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "shardingDatabase can't be null");

            if (isBlank(url = shardingDatabase.getUrl()))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "url can't be blank");

            if ((urlParts = url.split(PATH_SEPARATOR.identity)).length != VALID_DB_URL_PARTS_LEN)
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "invalid url, url = " + url);

            if (isBlank(dataBaseName = urlParts[DATA_BASE_NAME_PAR]))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "database name can't be blank, dataBaseName = " + dataBaseName);

            if (!existDatabases.add(dataBaseName))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "database name dupicates, dataBaseName = " + dataBaseName);

            if ((dataBaseNameParts = dataBaseName.split(PAR_CONCATENATION.identity)).length != VALID_DB_NAME_PARTS_LEN)
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "database name must consist of logical name and index number, for example -> member_0");

            if (isBlank(tempLogicDataBaseName = dataBaseNameParts[DATA_BASE_LOGIC_NAME_PAR]))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "database logical name can't be blank, for example -> member_0");

            if (isBlank(dataBaseIndexStr = dataBaseNameParts[DATA_BASE_INDEX_PAR]))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "database index number can't be null, for example -> member_0");

            try {
                dataBaseIndex = Integer.parseInt(dataBaseIndexStr);
            } catch (NumberFormatException e) {
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "database index number only number, for example -> member_0");
            }

            if (dataBaseIndex < 0)
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "database index number can't be less than 0");

            assertIndexList.add(dataBaseIndex);

            if (logicDataBaseName == null) {
                logicDataBaseName = tempLogicDataBaseName;
            } else {
                if (!tempLogicDataBaseName.equals(logicDataBaseName))
                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "sharding db in same micro service must has the same prefix, " + tempLogicDataBaseName + "/" + logicDataBaseName);
            }

            dataSource = DATA_SOURCE_GENERATOR.apply(shardingDatabase);

            if (enableProxy)
                for (UnaryOperator<DataSource> proxy : tarChain)
                    dataSource = proxy.apply(dataSource);

            dataSources.put(dataBaseName, dataSource);
        }

        if (0 != assertIndexList.stream().min(Integer::compare).orElse(-1))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "db index number must start at 0");

        if (!MathProcessor.assertDisorderIntegerContinuous(assertIndexList))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "The database index set should be a continuous number starting from 0");
        //</editor-fold>

        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();

        //<editor-fold desc="db shards">
        Integer shardingTableSizePerDataBase = dataAccessConf.getShardingTableSizePerDataBase();
        if (shardingTableSizePerDataBase == null || shardingTableSizePerDataBase < 1 || shardingTableSizePerDataBase > MAX_WORKER_ID.max)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "the number of table splits in each database cannot be less than 1 or greater than the maximum number of machines in each data center -> " + MAX_WORKER_ID.max);

        int maxDataBaseIndex = shardingSize - 1;
        int maxTableIndex = shardingTableSizePerDataBase - 1;

        Integer dataCenter = identityConf.getDataCenter();
        if (dataCenter == null || dataCenter < 0)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "dataCenter can't be null or less than 0");

        List<IdentityToShardingMappingAttr> dataCenterToDatabaseMappings = dataAccessConf.getDataCenterToDatabaseMappings();
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
        //</editor-fold>

        //<editor-fold desc="broadcast">
        ofNullable(dataAccessConf.getShardingBroadcastTables())
                .filter(sbts -> sbts.size() > 0)
                .ifPresent(sbts ->
                        shardingRuleConfiguration.getBroadcastTables().addAll(
                                sbts.stream()
                                        .filter(tb -> tb != null && !"".equals(tb))
                                        .distinct()
                                        .collect(toList())
                        ));
        //</editor-fold>

        //<editor-fold desc="single db and tables">
        if (!isEmpty(singleDatabasesWithTables)) {
            List<String> singleTables;
            for (SingleDatabaseWithTablesAttr single : singleDatabasesWithTables) {
                if (single == null)
                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "single db can't be null");

                if (isBlank(url = single.getUrl()))
                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "url can't be blank");

                if ((urlParts = url.split(PATH_SEPARATOR.identity)).length != VALID_DB_URL_PARTS_LEN)
                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "invalid url, url = " + url);

                if (isBlank(dataBaseName = urlParts[DATA_BASE_NAME_PAR]))
                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "database name can't be blank, dataBaseName = " + dataBaseName);

                if (!existDatabases.add(dataBaseName))
                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "database name dupicates, dataBaseName = " + dataBaseName);

                singleTables = single.getSingleTables();
                if (isEmpty(shardingTables))
                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "singleTables can't be empty");

                dataSource = DATA_SOURCE_GENERATOR.apply(single);

                if (enableProxy)
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
        //</editor-fold>

        //<editor-fold desc="additional attributes">
        Properties props = new Properties();
        ofNullable(dataAccessConf.getProps()).ifPresent(props::putAll);
        //</editor-fold>

        return new DataAccessConfElements(dataSources, shardingRuleConfiguration, props);
    }

    static final class DataAccessConfElements {

        private final Map<String, DataSource> dataSources;

        private final ShardingRuleConfiguration shardingRuleConfiguration;

        private final Properties props;

        public DataAccessConfElements(Map<String, DataSource> dataSources, ShardingRuleConfiguration shardingRuleConfiguration, Properties props) {
            this.dataSources = dataSources;
            this.shardingRuleConfiguration = shardingRuleConfiguration;
            this.props = props;
        }

        public Map<String, DataSource> getDataSources() {
            return dataSources;
        }

        public ShardingRuleConfiguration getShardingRuleConfiguration() {
            return shardingRuleConfiguration;
        }

        public Properties getProps() {
            return props;
        }
    }

}
