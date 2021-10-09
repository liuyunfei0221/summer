package com.blue.base.service.impl;

import com.blue.base.repository.entity.Dict;
import com.blue.base.repository.entity.DictType;
import com.blue.base.repository.mapper.DictMapper;
import com.blue.base.repository.mapper.DictTypeMapper;
import com.blue.base.service.inter.DictService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static reactor.core.publisher.Mono.just;

/**
 * 字典服务业务实现
 *
 * @author liuyunfei
 * @date 2021/9/27
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
@Service
public class DictServiceImpl implements DictService {

    private final DictTypeMapper dictTypeMapper;

    private final DictMapper dictMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DictServiceImpl(DictTypeMapper dictTypeMapper, DictMapper dictMapper) {
        this.dictTypeMapper = dictTypeMapper;
        this.dictMapper = dictMapper;
    }

    /**
     * 查询所有字典类型
     *
     * @return
     */
    @Override
    public Mono<List<DictType>> selectDictType() {
        return just(dictTypeMapper.listDictType());
    }

    /**
     * 查询所有字典数据
     *
     * @return
     */
    @Override
    public Mono<List<Dict>> selectDict() {
        return just(dictMapper.listDict());
    }

    /**
     * 根据字典类型code查询字典数据
     *
     * @param code
     * @return
     */
    @Override
    public Mono<List<Dict>> selectDictByTypeCode(String code) {
        //return just(dictMapper.selectByExample(null));

        return null;
    }
}
