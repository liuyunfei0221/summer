package com.blue.database.api.generator;

import com.blue.base.common.base.MathProcessor;
import com.blue.database.api.conf.DataAccessConf;
import com.blue.database.api.conf.ShardYmlAttr;
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

import static com.blue.base.constant.base.Symbol.*;
import static com.blue.identity.constant.IdentitySchema.MAX_DATA_CENTER_ID;
import static com.blue.identity.constant.IdentitySchema.MAX_WORKER_ID;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * sharding数据源创建工厂
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "PlaceholderCountMatchesArgumentCount", "AliControlFlowStatementWithoutBraces"})
public final class BlueDataAccessGenerator {

    private static final Logger LOGGER = getLogger(BlueDataAccessGenerator.class);

    /**
     * 创建数据源
     *
     * @param dataAccessConf
     * @param identityConf
     * @return
     */
    public static DataSource generateDataSource(DataAccessConf dataAccessConf, IdentityConf identityConf) {
        if (dataAccessConf == null || identityConf == null)
            throw new RuntimeException("shardingConf or identityConf can't be null");

        List<UnaryOperator<DataSource>> proxiesChain = ofNullable(dataAccessConf.getProxiesChain())
                .map(pc -> pc.stream().filter(Objects::nonNull)
                        .collect(toList()))
                .filter(pcs -> pcs.size() > 0)
                .orElse(singletonList(ds -> ds));

        try {
            DataAccessConfElements dataAccessConfElements = generateDataConfAttr(dataAccessConf, identityConf, proxiesChain);
            return ShardingDataSourceFactory.createDataSource(dataAccessConfElements.getDataSources(), dataAccessConfElements.getShardingRuleConfiguration(), dataAccessConfElements.getProps());
        } catch (Exception e) {
            LOGGER.error("createDataSource(ShardingConf shardingConf, ShardingIdentityConf shardingIdentityConf, List<UnaryOperator<DataSource>> proxiesChain) failed, e = {}", e);
            throw new RuntimeException("createDataSource(ShardingConf shardingConf, ShardingIdentityConf shardingIdentityConf, List<UnaryOperator<DataSource>> proxiesChain) failed, e = " + e);
        }
    }

    /**
     * 创建事务管理器
     *
     * @param dataSource
     * @return
     */
    public static TransactionManager generateTxManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * SqlSessionFactory创建器
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

        //TODO 开发阶段打印sql日志
        configuration.setLogImpl(StdOutImpl.class);

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setDataSource(dataSource);

        try {
            ResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
            sqlSessionFactoryBean.setMapperLocations(resourceLoader.getResources(dataAccessConf.getMapperLocation()));
        } catch (IOException e) {
            throw new RuntimeException("mapper.xml文件路径异常");
        }

        try {
            return sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            throw new RuntimeException("获取sqlSessionFactory异常");
        }

    }

    /**
     * SqlSessionTemplate创建器
     *
     * @param sqlSessionFactory
     * @return
     */
    public static SqlSessionTemplate generateSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
        return new SqlSessionTemplate(sqlSessionFactory, executorType);
    }

    //<editor-fold desc="常量定义">
    /**
     * 合法数据元素组成部分
     */
    private static final int VALID_DB_URL_PARTS_LEN = 4, VALID_DB_NAME_PARTS_LEN = 2;
    private static final int DATA_BASE_NAME_PAR = 3;
    private static final int DATA_BASE_LOGIC_NAME_PAR = 0, DATA_BASE_INDEX_PAR = 1;
    //</editor-fold>

    /**
     * 连接池构建器
     */
    private static final Function<ShardYmlAttr, DataSource> DATA_SOURCE_GENERATOR = shardAttr -> {
        HikariConfig hikariConfig = new HikariConfig();

        String driverClassName = shardAttr.getDriverClassName();
        if (isBlank(driverClassName))
            throw new RuntimeException("driverClassName不能为空");

        String url = shardAttr.getUrl();
        if (isBlank(url))
            throw new RuntimeException("url不能为空");

        String username = shardAttr.getUsername();
        if (isBlank(username))
            throw new RuntimeException("username不能为空");

        String password = shardAttr.getPassword();
        if (isBlank(password))
            throw new RuntimeException("password不能为空");

        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(url + ofNullable(shardAttr.getDataBaseConf()).map(c -> PAR_CONCATENATION_DATABASE_CONF.identity + c).orElse(""));
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        ofNullable(shardAttr.getReadOnly()).ifPresent(hikariConfig::setReadOnly);
        ofNullable(shardAttr.getConnectionTimeout()).ifPresent(hikariConfig::setConnectionTimeout);
        ofNullable(shardAttr.getMaxLifetime()).ifPresent(hikariConfig::setMaxLifetime);
        ofNullable(shardAttr.getMaximumPoolSize()).ifPresent(hikariConfig::setMaximumPoolSize);
        ofNullable(shardAttr.getMinimumIdle()).ifPresent(hikariConfig::setMinimumIdle);
        ofNullable(shardAttr.getTestQuery()).ifPresent(hikariConfig::setConnectionTestQuery);

        return new HikariDataSource(hikariConfig);
    };

    /**
     * 构建数据源属性
     *
     * @param dataAccessConf
     * @param identityConf
     * @param proxiesChain
     * @return
     */
    @SuppressWarnings("AlibabaMethodTooLong")
    private static DataAccessConfElements generateDataConfAttr(DataAccessConf dataAccessConf, IdentityConf identityConf, List<UnaryOperator<DataSource>> proxiesChain) {
        if (dataAccessConf == null || identityConf == null)
            throw new RuntimeException("shardingConf或shardingIdentityConf不能为空");

        //<editor-fold desc="构建数据源">
        List<ShardYmlAttr> shards = dataAccessConf.getShards();

        int shardSize;
        if (shards == null || (shardSize = shards.size()) < 1 || shardSize > MAX_DATA_CENTER_ID.max)
            throw new RuntimeException("dataBases配置不能为空,且不能小于1或大于最大数据中心数量 -> " + MAX_DATA_CENTER_ID.max);

        Map<String, DataSource> dataSources = new HashMap<>(shardSize);
        List<Integer> assertIndexList = new ArrayList<>(shardSize);

        String logicDataBaseName = null;
        String[] urlParts, dataBaseNameParts;
        String url, dataBaseName, tempLogicDataBaseName, dataBaseIndexStr;

        int dataBaseIndex;
        DataSource dataSource;
        for (ShardYmlAttr shardAttr : shards) {
            if (shardAttr == null)
                throw new RuntimeException("shardAttr不能为空");

            if (isBlank(url = shardAttr.getUrl()))
                throw new RuntimeException("url不能为空");

            if ((urlParts = url.split(PATH_SEPARATOR.identity)).length != VALID_DB_URL_PARTS_LEN)
                throw new RuntimeException("url错误,url = " + url);

            if (isBlank(dataBaseName = urlParts[DATA_BASE_NAME_PAR]))
                throw new RuntimeException("数据库名称不能为空,url = " + url);

            if ((dataBaseNameParts = dataBaseName.split(PAR_CONCATENATION.identity)).length != VALID_DB_NAME_PARTS_LEN)
                throw new RuntimeException("数据库名称必须由逻辑名称 + " + "_" + " + index组成,例如 member_0");

            if (isBlank(tempLogicDataBaseName = dataBaseNameParts[DATA_BASE_LOGIC_NAME_PAR]))
                throw new RuntimeException("数据库逻辑名称为空,数据库名称必须由逻辑名称 + " + "_" + " + index组成,例如 member_0");

            if (isBlank(dataBaseIndexStr = dataBaseNameParts[DATA_BASE_INDEX_PAR]))
                throw new RuntimeException("数据库索引为空,数据库名称必须由逻辑名称 + " + "_" + " + index组成,例如 member_0");

            try {
                dataBaseIndex = Integer.parseInt(dataBaseIndexStr);
            } catch (NumberFormatException e) {
                throw new RuntimeException("数据库索引只能为数字,数据库名称必须由逻辑名称 + " + "_" + " + index组成,例如 member_0");
            }

            if (dataBaseIndex < 0)
                throw new RuntimeException("数据库索引不能小于0,数据库名称必须由逻辑名称 + " + "_" + " + index组成,例如 member_0");

            assertIndexList.add(dataBaseIndex);

            if (logicDataBaseName == null) {
                logicDataBaseName = tempLogicDataBaseName;
            } else {
                if (!tempLogicDataBaseName.equals(logicDataBaseName))
                    throw new RuntimeException("数据库的逻辑名称/前缀必须相同, " + tempLogicDataBaseName + "/" + logicDataBaseName);
            }

            dataSource = DATA_SOURCE_GENERATOR.apply(shardAttr);

            for (UnaryOperator<DataSource> proxy : proxiesChain)
                dataSource = proxy.apply(dataSource);

            dataSources.put(dataBaseName, dataSource);
        }

        if (0 != assertIndexList.stream().min(Integer::compare).orElse(-1))
            throw new RuntimeException("数据库索引应由0开始");

        if (!MathProcessor.assertDisorderIntegerContinuous(assertIndexList))
            throw new RuntimeException("数据库索引集应为由0开始的连续数字");
        //</editor-fold>

        //<editor-fold desc="构建数据拆分策略">
        Integer tableSizePerDataBase = dataAccessConf.getTableSizePerDataBase();
        if (tableSizePerDataBase == null || tableSizePerDataBase < 1 || tableSizePerDataBase > MAX_WORKER_ID.max)
            throw new RuntimeException("每个库的表拆分数量不能小于1或高于每数据中心内最大机器数量 -> " + MAX_WORKER_ID.max);

        int maxDataBaseIndex = shards.size() - 1;
        int maxTableIndex = tableSizePerDataBase - 1;

        Integer dataCenter = identityConf.getDataCenter();
        if (dataCenter == null || dataCenter < 0 || dataCenter > maxDataBaseIndex)
            throw new RuntimeException("dataCenter不能小于0或高于maxDataBaseIndex");

        Integer worker = identityConf.getWorker();
        if (worker == null || worker < 0 || worker > maxTableIndex)
            throw new RuntimeException("worker不能小于0或高于maxTableIndex");

        List<String> tables = dataAccessConf.getTables();
        if (isEmpty(tables))
            throw new RuntimeException("tables不能为空,且应包含所有需要分片的表,否则无法定义数据分片");

        String shardingColumn = dataAccessConf.getShardingColumn();
        if (shardingColumn == null || "".equals(shardingColumn))
            throw new RuntimeException("shardingColumn配置不能为空");

        StandardShardingStrategyConfiguration dataBaseShardingStrategyConfiguration = new StandardShardingStrategyConfiguration(shardingColumn, new DatabaseShardingAlgorithm(logicDataBaseName, shardSize));

        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        String finalLogicDataBaseName = logicDataBaseName;

        shardingRuleConfiguration.getTableRuleConfigs().addAll(
                tables.stream().distinct().map(logicTableName -> {
                    String expression = finalLogicDataBaseName + "_$->{0.." + maxDataBaseIndex + "}." + logicTableName + "_$->{0.." + maxTableIndex + "}";

                    TableRuleConfiguration conf = new TableRuleConfiguration(logicTableName, expression);
                    conf.setDatabaseShardingStrategyConfig(dataBaseShardingStrategyConfiguration);
                    conf.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration(shardingColumn, new TableShardingAlgorithm(logicTableName, tableSizePerDataBase)));

                    return conf;
                }).collect(toList()));

        ofNullable(dataAccessConf.getBroadcastTables())
                .filter(bts -> bts.size() > 0)
                .ifPresent(bts ->
                        shardingRuleConfiguration.getBroadcastTables().addAll(
                                bts.stream()
                                        .filter(tb -> tb != null && !"".equals(tb))
                                        .distinct()
                                        .collect(toList())
                        ));
        //</editor-fold>

        //<editor-fold desc="附加属性">
        Properties props = new Properties();
        props.putAll(dataAccessConf.getProps());
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
