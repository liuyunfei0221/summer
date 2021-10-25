package com.blue.secure.service.inter;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.secure.api.model.ResourceInfo;
import com.blue.secure.model.ResourceCondition;
import com.blue.secure.model.ResourceInsertParam;
import com.blue.secure.repository.entity.Resource;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * resource service interface
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface ResourceService {

    /**
     * insert resource
     *
     * @param resourceInsertParam
     */
    void insertResource(ResourceInsertParam resourceInsertParam);

    /**
     * get resource by role id
     *
     * @param id
     * @return
     */
    Mono<Optional<Resource>> getResourceMonoById(Long id);

    /**
     * select all resources
     *
     * @return
     */
    Mono<List<Resource>> selectResource();

    /**
     * select resources by ids
     *
     * @param ids
     * @return
     */
    Mono<List<Resource>> selectResourceMonoByIds(List<Long> ids);

    /**
     * select resource by page and condition
     *
     * @param limit
     * @param rows
     * @param resourceCondition
     * @return
     */
    Mono<List<Resource>> selectResourceMonoByLimitAndCondition(Long limit, Long rows, ResourceCondition resourceCondition);

    /**
     * count resource by condition
     *
     * @param resourceCondition
     * @return
     */
    Mono<Long> countResourceMonoByCondition(ResourceCondition resourceCondition);

    /**
     * select resource info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<ResourceInfo>> selectResourceInfoPageMonoByPageAndCondition(PageModelRequest<ResourceCondition> pageModelRequest);

}
