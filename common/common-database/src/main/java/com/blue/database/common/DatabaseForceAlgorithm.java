package com.blue.database.common;

import com.blue.base.model.exps.BlueException;
import com.blue.database.api.conf.IdentityToShardingMappingAttr;
import com.blue.identity.core.exp.IdentityException;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;
import java.util.List;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION;

/**
 * db force algorithm
 *
 * @author liuyunfei
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class DatabaseForceAlgorithm implements PreciseShardingAlgorithm<Long> {

    public final String databaseName;

    public DatabaseForceAlgorithm(String logicDataBaseName,
                                  List<IdentityToShardingMappingAttr> dataCenterToDatabaseMappings, Integer dataCenterId) {
        if (isBlank(logicDataBaseName))
            throw new IdentityException("logicDataBaseName can't be blank");
        if (isEmpty(dataCenterToDatabaseMappings))
            throw new IdentityException("dataCenterToDatabaseMappings can't be empty");
        if (isNull(dataCenterId) || dataCenterId < 0)
            throw new IdentityException("dataCenterId can't be null or less than 0");

        Integer id;
        Integer index;
        for (IdentityToShardingMappingAttr attr : dataCenterToDatabaseMappings) {
            id = attr.getId();
            index = attr.getIndex();
            if (isNull(id) || id < 0)
                throw new IdentityException("id can't be less than 0");
            if (isNull(index) || index < 0)
                throw new IdentityException("index can't be less than 0");

            if (dataCenterId.equals(id)) {
                databaseName = (logicDataBaseName.intern() + PAR_CONCATENATION.identity + index).intern();
                return;
            }
        }

        throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "mappings has no data center id " + dataCenterId);
    }


    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        return databaseName;
    }

}
