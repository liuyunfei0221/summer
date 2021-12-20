package com.blue.base.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.Dict;
import com.blue.base.repository.entity.DictType;
import com.blue.base.repository.mapper.DictMapper;
import com.blue.base.repository.mapper.DictTypeMapper;
import com.blue.base.service.inter.DictService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;

import static com.blue.base.common.base.Asserter.*;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * dict service
 *
 * @author liuyunfei
 * @date 2021/9/27
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class DictServiceImpl implements DictService {

    private static final Logger LOGGER = getLogger(DictServiceImpl.class);

    private final DictTypeMapper dictTypeMapper;

    private final DictMapper dictMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DictServiceImpl(DictTypeMapper dictTypeMapper, DictMapper dictMapper) {
        this.dictTypeMapper = dictTypeMapper;
        this.dictMapper = dictMapper;
    }

    /**
     * select all dict type
     *
     * @return
     */
    @Override
    public Mono<List<DictType>> selectDictType() {
        return just(dictTypeMapper.select());
    }

    /**
     * select all dict
     *
     * @return
     */
    @Override
    public Mono<List<Dict>> selectDict() {
        return just(dictMapper.select());
    }

    /**
     * select dict by dict type code
     *
     * @param code
     * @return
     */
    @Override
    public Mono<List<Dict>> selectDictByTypeCode(String code) {
        LOGGER.info("Mono<List<Dict>> selectDictByTypeCode(String code), code = {}", code);

        if (isBlank(code))
            throw new BlueException(BAD_REQUEST);

        DictType dictType = dictTypeMapper.getByCode(code);
        if (isNull(dictType))
            throw new BlueException(BAD_REQUEST);

        Long dictTypeId = dictType.getId();
        if (isInvalidIdentity(dictTypeId))
            throw new BlueException(BAD_REQUEST);

        return just(dictMapper.selectByDictTypeId(dictTypeId));
    }
}
