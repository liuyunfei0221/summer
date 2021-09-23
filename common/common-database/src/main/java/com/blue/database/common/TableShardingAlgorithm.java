package com.blue.database.common;

import com.blue.identity.core.exp.IdentityException;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION;
import static com.blue.identity.core.SnowflakeIdentityParser.parse;
import static java.util.stream.LongStream.range;

/**
 * 分表
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class TableShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    private final Map<Long, String> mapping;

    public TableShardingAlgorithm(String logicTableName, int tableSizePerDataBase) {

        if (logicTableName == null || "".equals(logicTableName))
            throw new IdentityException("logicTableName不能为空或''");
        if (tableSizePerDataBase < 1)
            throw new IdentityException("tableSizePerDataBase不能小于1");

        Map<Long, String> indexAndTableMapping = new HashMap<>(tableSizePerDataBase);
        range(0L, tableSizePerDataBase)
                .forEach(index -> indexAndTableMapping.put(index, (logicTableName.intern() + PAR_CONCATENATION.identity + index).intern()));

        mapping = indexAndTableMapping;
    }

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        return mapping.get(parse(shardingValue.getValue()).getWorker()).intern();
    }

}
