//package com.blue.rdatabase.algorithm;
//
//import com.blue.identity.core.exp.IdentityException;
//import com.blue.rdatabase.api.conf.IdentityToShardingMappingAttr;
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static com.blue.basic.common.base.BlueChecker.*;
//import static com.blue.basic.constant.common.Symbol.PAR_CONCATENATION;
//import static com.blue.identity.core.SnowflakeIdentityParser.parse;
//import static java.lang.Long.valueOf;
//
///**
// * database sharding algorithm
// *
// * @author liuyunfei
// */
//@SuppressWarnings("AliControlFlowStatementWithoutBraces")
//public final class SnowflakeDatabaseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {
//
//    private final Map<Long, String> mapping;
//
//    public SnowflakeDatabaseShardingAlgorithm(String logicDataBaseName, List<IdentityToShardingMappingAttr> dataCenterToDatabaseMappings) {
//        if (isBlank(logicDataBaseName))
//            throw new IdentityException("logicDataBaseName can't be blank");
//        if (isEmpty(dataCenterToDatabaseMappings))
//            throw new IdentityException("dataCenterToDatabaseMappings can't be empty");
//
//        Map<Long, String> dataCenterIdAndDatabaseIndexMapping = new HashMap<>(dataCenterToDatabaseMappings.size(), 2.0f);
//        Integer id;
//        Integer index;
//        for (IdentityToShardingMappingAttr attr : dataCenterToDatabaseMappings) {
//            id = attr.getId();
//            index = attr.getIndex();
//            if (isNull(id) || id < 0)
//                throw new IdentityException("id can't be less than 0");
//            if (isNull(index) || index < 0)
//                throw new IdentityException("index can't be less than 0");
//
//            dataCenterIdAndDatabaseIndexMapping.put(valueOf(id), (logicDataBaseName.intern() + PAR_CONCATENATION.identity + index).intern());
//        }
//
//        mapping = dataCenterIdAndDatabaseIndexMapping;
//    }
//
//
//    @Override
//    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
//        return mapping.get(parse(shardingValue.getValue()).getDataCenter()).intern();
//    }
//
//}
