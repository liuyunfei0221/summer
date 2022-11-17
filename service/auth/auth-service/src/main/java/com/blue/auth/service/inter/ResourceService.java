package com.blue.auth.service.inter;

import com.blue.auth.api.model.ResourceInfo;
import com.blue.auth.model.ResourceCondition;
import com.blue.auth.model.ResourceInsertParam;
import com.blue.auth.model.ResourceManagerInfo;
import com.blue.auth.model.ResourceUpdateParam;
import com.blue.auth.repository.entity.Resource;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * resource service interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface ResourceService {

    /**
     * insert resource
     *
     * @param resourceInsertParam
     * @param operatorId
     * @return
     */
    ResourceInfo insertResource(ResourceInsertParam resourceInsertParam, Long operatorId);

    /**
     * update a exist resource
     *
     * @param resourceUpdateParam
     * @param operatorId
     * @return
     */
    ResourceInfo updateResource(ResourceUpdateParam resourceUpdateParam, Long operatorId);

    /**
     * delete resource
     *
     * @param id
     * @return
     */
    ResourceInfo deleteResource(Long id);

    /**
     * get resource by id
     *
     * @param id
     * @return
     */
    Optional<Resource> getResourceOpt(Long id);

    /**
     * get resource mono by resource id
     *
     * @param id
     * @return
     */
    Mono<Resource> getResource(Long id);

    /**
     * select all resources
     *
     * @return
     */
    Mono<List<Resource>> selectResource();

    /**
     * select resources mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<Resource>> selectResourceByIds(List<Long> ids);

    /**
     * select resource by page and condition
     *
     * @param limit
     * @param rows
     * @param resourceCondition
     * @return
     */
    Mono<List<Resource>> selectResourceByLimitAndCondition(Long limit, Long rows, ResourceCondition resourceCondition);

    /**
     * count resource by condition
     *
     * @param resourceCondition
     * @return
     */
    Mono<Long> countResourceByCondition(ResourceCondition resourceCondition);

    /**
     * select resource info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<ResourceManagerInfo>> selectResourceManagerInfoPageByPageAndCondition(PageModelRequest<ResourceCondition> pageModelRequest);

}
