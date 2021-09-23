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
@SuppressWarnings("JavaDoc")
public interface DataAccessConf {

    /**
     * 当前可用数据库分片
     *
     * @return
     */
    List<ShardYmlAttr> getShards();

    /**
     * 需要分片的表,建议初期就完善配置
     *
     * @return
     */
    List<String> getTables();

    /**
     * 当前每库分表数量
     *
     * @return
     */
    Integer getTableSizePerDataBase();

    /**
     * 分片属性名
     *
     * @return
     */
    String getShardingColumn();

    /**
     * 广播表
     *
     * @return
     */
    List<String> getBroadcastTables();

    /**
     * 是否开启缓存
     *
     * @return
     */
    Boolean getCacheEnabled();

    /**
     * 是否开启懒加载
     *
     * @return
     */
    Boolean getLazyLoadingEnabled();

    /**
     * 是否开启激进懒加载
     *
     * @return
     */
    Boolean getAggressiveLazyLoading();

    /**
     * 是否开发多res set
     *
     * @return
     */
    Boolean getMultipleResultSetsEnabled();

    /**
     * 是否使用列标签
     *
     * @return
     */
    Boolean getUseColumnLabel();

    /**
     * 是否生成key
     *
     * @return
     */
    Boolean getUseGeneratedKeys();

    /**
     * 连接超时时间
     *
     * @return
     */
    Integer getConnectionTimeout();

    /**
     * 映射处理
     *
     * @return
     */
    AutoMappingBehavior getAutoMappingBehavior();

    /**
     * 执行类型
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
     * 附加属性
     *
     * @return
     */
    Map<String, String> getProps();

    /**
     * 代理/当前用于seata
     *
     * @return
     */
    List<UnaryOperator<DataSource>> getProxiesChain();

}
