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
    List<DictType> selectDictType();

    /**
     * select all dict
     *
     * @return
     */
    List<Dict> selectDict();

    /**
     * select dict by dict type code
     *
     * @param code
     * @return
     */
    List<Dict> selectDictByTypeCode(String code);

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
     * invalid dict type info
     *
     * @return
     */
    void invalidDictTypeInfosCache();

    /**
     * invalid dict info
     *
     * @return
     */
    void invalidDictInfosCache();

}
