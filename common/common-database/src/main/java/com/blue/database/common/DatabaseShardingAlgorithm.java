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
 * db sharding algorithm
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class DatabaseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    private final Map<Long, String> mapping;

    public DatabaseShardingAlgorithm(String logicDataBaseName, int shardSize) {
        if (logicDataBaseName == null || "".equals(logicDataBaseName))
            throw new IdentityException("logicDataBaseName can't be blank");
        if (shardSize < 1)
            throw new IdentityException("shardSize can't be less than 1");

        Map<Long, String> indexAndDatabaseMapping = new HashMap<>(shardSize);
        range(0L, shardSize)
                .forEach(index -> indexAndDatabaseMapping.put(index, (logicDataBaseName.intern() + PAR_CONCATENATION.identity + index).intern()));

        mapping = indexAndDatabaseMapping;
    }

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        return mapping.get(parse(shardingValue.getValue()).getDataCenter()).intern();
    }

}
