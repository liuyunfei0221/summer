package com.blue.database.common;

import com.blue.base.model.exps.BlueException;
import com.blue.database.api.conf.IdentityToShardingMappingAttr;
import com.blue.identity.core.exp.IdentityException;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;
import java.util.List;

import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION;

/**
 * table force algorithm
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class TableForceAlgorithm implements PreciseShardingAlgorithm<Long> {

    public final String tableName;

    public TableForceAlgorithm(String logicTableName,
                               List<IdentityToShardingMappingAttr> workerToTableMappings, Integer workerId) {
        if (logicTableName == null || "".equals(logicTableName))
            throw new IdentityException("logicTableName can't be blank");
        if (isEmpty(workerToTableMappings))
            throw new IdentityException("workerToTableMappings can't be empty");
        if (workerId == null || workerId < 0)
            throw new IdentityException("workerId can't be null or less than 0");

        Integer id;
        Integer index;
        for (IdentityToShardingMappingAttr attr : workerToTableMappings) {
            id = attr.getId();
            index = attr.getIndex();
            if (id == null || id < 0)
                throw new IdentityException("id can't be less than 0");
            if (index == null || index < 0)
                throw new IdentityException("index can't be less than 0");

            if (workerId.equals(id)) {
                tableName = (logicTableName.intern() + PAR_CONCATENATION.identity + index).intern();
                return;
            }
        }

        throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "mappings has no worker id " + workerId);
    }

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        return tableName;
    }

}
