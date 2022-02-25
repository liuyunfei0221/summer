package com.blue.database.common;

import com.blue.database.api.conf.IdentityToShardingMappingAttr;
import com.blue.identity.core.exp.IdentityException;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION;
import static com.blue.identity.core.SnowflakeIdentityParser.parse;
import static java.lang.Long.valueOf;

/**
 * table sharding algorithm
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class TableShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    private final Map<Long, String> mapping;

    public TableShardingAlgorithm(String logicTableName, List<IdentityToShardingMappingAttr> workerToTableMappings) {
        if (logicTableName == null || "".equals(logicTableName))
            throw new IdentityException("logicTableName can't be blank");
        if (isEmpty(workerToTableMappings))
            throw new IdentityException("workerToTableMappings can't be empty");

        Map<Long, String> workerIdAndDatabaseIndexMapping = new HashMap<>(workerToTableMappings.size());
        Integer id;
        Integer index;
        for (IdentityToShardingMappingAttr attr : workerToTableMappings) {
            id = attr.getId();
            index = attr.getIndex();
            if (id == null || id < 0)
                throw new IdentityException("id can't be less than 0");
            if (index == null || index < 0)
                throw new IdentityException("index can't be less than 0");

            workerIdAndDatabaseIndexMapping.put(valueOf(id), (logicTableName.intern() + PAR_CONCATENATION.identity + index).intern());
        }

        mapping = workerIdAndDatabaseIndexMapping;
    }

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        return mapping.get(parse(shardingValue.getValue()).getWorker()).intern();
    }

    public String doSharding1(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {

        long worker = parse(shardingValue.getValue()).getWorker();


        return mapping.get(parse(shardingValue.getValue()).getWorker()).intern();
    }

}
