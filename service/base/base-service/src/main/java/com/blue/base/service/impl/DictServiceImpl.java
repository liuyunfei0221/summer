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

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * 字典服务业务实现
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
        return just(dictTypeMapper.selectDictType());
    }

    /**
     * select all dict
     *
     * @return
     */
    @Override
    public Mono<List<Dict>> selectDict() {
        return just(dictMapper.selectDict());
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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "code can't be blank");

        DictType dictType = dictTypeMapper.getDictTypeByCode(code);
        if (dictType == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid code, dictType not exist");

        return just(dictMapper.selectDictByDictTypeId(dictType.getId()));
    }
}
