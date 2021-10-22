package com.blue.secure.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.secure.repository.entity.Resource;
import com.blue.secure.repository.mapper.ResourceMapper;
import com.blue.secure.service.inter.ResourceService;
import com.blue.secure.service.inter.RoleResRelationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;


/**
 * resource service interface impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class ResourceServiceImpl implements ResourceService {

    private static final Logger LOGGER = getLogger(ResourceServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final ResourceMapper resourceMapper;

    private final RoleResRelationService roleResRelationService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ResourceServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ResourceMapper resourceMapper, RoleResRelationService roleResRelationService) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.resourceMapper = resourceMapper;
        this.roleResRelationService = roleResRelationService;
    }

    /**
     * select resources by ids
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<Resource>> getResourceMonoById(Long id) {
        LOGGER.info("ono<Optional<Role>> getRoleMonoById(Long id), id = {}", id);
        if (id == null || id < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return just(ofNullable(resourceMapper.selectByPrimaryKey(id)));
    }

    /**
     * select all resources
     *
     * @return
     */
    @Override
    public Mono<List<Resource>> selectResource() {
        LOGGER.info("Mono<List<Resource>> selectResource()");
        return just(resourceMapper.select());
    }

    /**
     * select resources by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<Resource>> selectResourceMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<Resource>> selectResourceMonoByIds(List<Long> ids), ids = {}", ids);
        return just(resourceMapper.selectByIds(ids));
    }

}
