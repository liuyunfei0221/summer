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
 * 分库
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class DatabaseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    private final Map<Long, String> mapping;

    public DatabaseShardingAlgorithm(String logicDataBaseName, int shardSize) {

        if (logicDataBaseName == null || "".equals(logicDataBaseName))
            throw new IdentityException("logicDataBaseName不能为空或''");
        if (shardSize < 1)
            throw new IdentityException("shardSize不能小于1");

        Map<Long, String> indexAndDataBaseMapping = new HashMap<>(shardSize);
        range(0L, shardSize)
                .forEach(index -> indexAndDataBaseMapping.put(index, (logicDataBaseName.intern() + PAR_CONCATENATION.identity + index).intern()));

        mapping = indexAndDataBaseMapping;
    }

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        return mapping.get(parse(shardingValue.getValue()).getDataCenter()).intern();
    }

}
