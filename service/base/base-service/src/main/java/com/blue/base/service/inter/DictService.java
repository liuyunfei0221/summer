package com.blue.base.service.inter;

import com.blue.base.api.model.DictInfo;
import com.blue.base.api.model.DictTypeInfo;
import com.blue.base.repository.entity.Dict;
import com.blue.base.repository.entity.DictType;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * dict service
 *
 * @author liuyunfei
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
     * select dict by dict type code
     *
     * @param code
     * @return
     */
    Mono<List<Dict>> selectDictByTypeCode(String code);

    /**
     * select all dict types
     *
     * @return
     */
    Mono<List<DictTypeInfo>> selectDictTypeInfo();

    /**
     * select dict by type code
     *
     * @param code
     * @return
     */
    Mono<List<DictInfo>> selectDictInfoByTypeCode(String code);

    /**
     * invalid cache
     *
     * @return
     */
    void invalidCache();

}
