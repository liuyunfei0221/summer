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
 * db sharding algorithm
 *
 * @author liuyunfei
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class DatabaseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    private final Map<Long, String> mapping;

    public DatabaseShardingAlgorithm(String logicDataBaseName, List<IdentityToShardingMappingAttr> dataCenterToDatabaseMappings) {
        if (logicDataBaseName == null || "".equals(logicDataBaseName))
            throw new IdentityException("logicDataBaseName can't be blank");
        if (isEmpty(dataCenterToDatabaseMappings))
            throw new IdentityException("dataCenterToDatabaseMappings can't be empty");

        Map<Long, String> dataCenterIdAndDatabaseIndexMapping = new HashMap<>(dataCenterToDatabaseMappings.size());
        Integer id;
        Integer index;
        for (IdentityToShardingMappingAttr attr : dataCenterToDatabaseMappings) {
            id = attr.getId();
            index = attr.getIndex();
            if (id == null || id < 0)
                throw new IdentityException("id can't be less than 0");
            if (index == null || index < 0)
                throw new IdentityException("index can't be less than 0");

            dataCenterIdAndDatabaseIndexMapping.put(valueOf(id), (logicDataBaseName.intern() + PAR_CONCATENATION.identity + index).intern());
        }

        mapping = dataCenterIdAndDatabaseIndexMapping;
    }


    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        return mapping.get(parse(shardingValue.getValue()).getDataCenter()).intern();
    }

}
