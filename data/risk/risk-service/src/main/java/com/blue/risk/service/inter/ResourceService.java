package com.blue.risk.service.inter;

import com.blue.auth.api.model.ResourceInfo;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * resource service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface ResourceService {

    /**
     * select all resource info
     *
     * @return
     */
    Mono<List<ResourceInfo>> selectResourceInfo();

    /**
     * select all identity with resource info
     *
     * @return
     */
    Mono<Map<String, ResourceInfo>> selectIdentityWithResourceInfo();

    /**
     * get resource info by key
     *
     * @param resourceKey
     * @return
     */
    Mono<ResourceInfo> getResourceInfoByResourceKey(String resourceKey);

}
