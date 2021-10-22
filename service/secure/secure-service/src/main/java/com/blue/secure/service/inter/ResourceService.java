package com.blue.secure.service.inter;

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

}
