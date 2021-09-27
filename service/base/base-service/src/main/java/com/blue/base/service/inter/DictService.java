package com.blue.base.service.inter;

import com.blue.base.repository.entity.Dict;
import com.blue.base.repository.entity.DictType;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 字典服务业务接口
 *
 * @author liuyunfei
 * @date 2021/9/27
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public interface DictService {

    /**
     * 查询所有字典类型
     *
     * @return
     */
    Mono<List<DictType>> selectDictType();

    /**
     * 查询所有字典数据
     *
     * @return
     */
    Mono<List<Dict>> selectDict();

    /**
     * 根据字典类型code查询字典数据
     *
     * @param code
     * @return
     */
    Mono<List<Dict>> selectDictByTypeCode(String code);

}
