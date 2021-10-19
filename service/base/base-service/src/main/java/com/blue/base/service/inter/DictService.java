package com.blue.base.service.inter;

import com.blue.base.repository.entity.Dict;
import com.blue.base.repository.entity.DictType;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * dict service
 *
 * @author liuyunfei
 * @date 2021/9/27
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface DictService {

    /**
     * select all dict type
     *
     * @return
     */
    Mono<List<DictType>> selectDictType();

    /**
     * select all dict
     *
     * @return
     */
    Mono<List<Dict>> selectDict();

    /**
     * select dict by dict type code
     *
     * @param code
     * @return
     */
    Mono<List<Dict>> selectDictByTypeCode(String code);

}
