package com.blue.base.service.inter;

import com.blue.base.api.model.StyleInfo;
import com.blue.base.api.model.StyleManagerInfo;
import com.blue.base.model.StyleCondition;
import com.blue.base.model.StyleInsertParam;
import com.blue.base.model.StyleUpdateParam;
import com.blue.base.repository.entity.Style;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * style service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface StyleService {

    /**
     * insert style
     *
     * @param styleInsertParam
     * @param operatorId
     * @return
     */
    StyleInfo insertStyle(StyleInsertParam styleInsertParam, Long operatorId);

    /**
     * update style
     *
     * @param styleUpdateParam
     * @param operatorId
     * @return
     */
    StyleInfo updateStyle(StyleUpdateParam styleUpdateParam, Long operatorId);

    /**
     * delete style
     *
     * @param id
     * @return
     */
    StyleInfo deleteStyle(Long id);

    /**
     * update active style by id
     *
     * @param id
     * @param operatorId
     * @return
     */
    StyleManagerInfo updateActiveStyle(Long id, Long operatorId);

    /**
     * expire style info
     *
     * @return
     */
    void invalidStyleInfosCache();

    /**
     * get style mono by id
     *
     * @param id
     * @return
     */
    Mono<Style> getStyle(Long id);

    /**
     * select all style
     *
     * @return
     */
    Mono<List<Style>> selectStyle();

    /**
     * list style by type and active
     *
     * @param styleType
     * @param isActive
     * @return
     */
    List<Style> selectStyleByTypeAndActive(Integer styleType, Boolean isActive);

    /**
     * get active style
     *
     * @param styleType
     * @return
     */
    Mono<StyleInfo> getActiveStyleInfoByTypeWithCache(Integer styleType);

    /**
     * select style by page and condition
     *
     * @param limit
     * @param rows
     * @param styleCondition
     * @return
     */
    Mono<List<Style>> selectStyleByLimitAndCondition(Long limit, Long rows, StyleCondition styleCondition);

    /**
     * count style by condition
     *
     * @param styleCondition
     * @return
     */
    Mono<Long> countStyleByCondition(StyleCondition styleCondition);

    /**
     * select style manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<StyleManagerInfo>> selectStyleManagerInfoPageByPageAndCondition(PageModelRequest<StyleCondition> pageModelRequest);

}
