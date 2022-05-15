package com.blue.portal.service.inter;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.portal.api.model.StyleInfo;
import com.blue.portal.api.model.StyleManagerInfo;
import com.blue.portal.model.StyleCondition;
import com.blue.portal.repository.entity.Style;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

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
     * @param style
     * @return
     */
    int insertStyle(Style style);

    /**
     * expire style infos
     *
     * @return
     */
    void invalidStyleInfosCache();

    /**
     * get style by id
     *
     * @param id
     * @return
     */
    Optional<Style> getStyle(Long id);

    /**
     * get style mono by id
     *
     * @param id
     * @return
     */
    Mono<Optional<Style>> getStyleMono(Long id);

    /**
     * select all style
     *
     * @return
     */
    Mono<List<Style>> selectStyle();

    /**
     * list active style by type
     *
     * @param styleType
     * @return
     */
    List<Style> selectActiveStyleByType(Integer styleType);

    /**
     * list style infos
     *
     * @param styleType
     * @return
     */
    Mono<List<StyleInfo>> selectStyleInfoMonoByType(Integer styleType);

    /**
     * select style by page and condition
     *
     * @param limit
     * @param rows
     * @param styleCondition
     * @return
     */
    Mono<List<Style>> selectStyleMonoByLimitAndCondition(Long limit, Long rows, StyleCondition styleCondition);

    /**
     * count style by condition
     *
     * @param styleCondition
     * @return
     */
    Mono<Long> countStyleMonoByCondition(StyleCondition styleCondition);

    /**
     * select style manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<StyleManagerInfo>> selectStyleManagerInfoPageMonoByPageAndCondition(PageModelRequest<StyleCondition> pageModelRequest);

}
