package com.blue.secure.service.impl;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.secure.api.model.ResourceInfo;
import com.blue.secure.constant.RoleSortAttribute;
import com.blue.secure.model.ResourceCondition;
import com.blue.secure.model.ResourceInsertParam;
import com.blue.secure.model.ResourceUpdateParam;
import com.blue.secure.repository.entity.Resource;
import com.blue.secure.repository.mapper.ResourceMapper;
import com.blue.secure.service.inter.ResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.REST_URI_ASSERTER;
import static com.blue.base.common.base.ConstantProcessor.*;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.secure.converter.SecureModelConverters.RESOURCE_2_RESOURCE_INFO_CONVERTER;
import static com.blue.secure.converter.SecureModelConverters.RESOURCE_INSERT_PARAM_2_RESOURCE_CONVERTER;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;
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

    private ResourceMapper resourceMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ResourceServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ResourceMapper resourceMapper) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.resourceMapper = resourceMapper;
    }

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(RoleSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Consumer<ResourceCondition> CONDITION_REPACKAGER = condition -> {
        if (isNotNull(condition)) {
            ofNullable(condition.getSortAttribute())
                    .filter(StringUtils::hasText)
                    .map(SORT_ATTRIBUTE_MAPPING::get)
                    .ifPresent(condition::setSortAttribute);

            assertSortType(condition.getSortType(), true);

            ofNullable(condition.getRequestMethod())
                    .filter(rm -> !isBlank(rm)).map(String::toUpperCase).ifPresent(condition::setRequestMethod);
            ofNullable(condition.getModule())
                    .filter(m -> !isBlank(m)).map(String::toLowerCase).ifPresent(m -> condition.setModule("%" + m + "%"));
            ofNullable(condition.getUri())
                    .filter(uri -> !isBlank(uri)).map(String::toLowerCase).ifPresent(uri -> condition.setUri("%" + uri + "%"));
            ofNullable(condition.getName())
                    .filter(n -> !isBlank(n)).ifPresent(n -> condition.setName("%" + n + "%"));
        }
    };

    /**
     * is a resource exist?
     */
    private final Consumer<ResourceInsertParam> INSERT_RESOURCE_VALIDATOR = rip -> {
        if (rip == null)
            throw new BlueException(EMPTY_PARAM);

        String requestMethod = rip.getRequestMethod();
        assertHttpMethod(requestMethod, false);

        String module = rip.getModule();
        if (isBlank(module))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "module can't be null");

        String uri = rip.getUri();
        REST_URI_ASSERTER.accept(uri);

        Boolean authenticate = rip.getAuthenticate();
        if (authenticate == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "authenticate can't be null");

        Boolean requestUnDecryption = rip.getRequestUnDecryption();
        if (requestUnDecryption == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "requestUnDecryption can't be null");

        Boolean responseUnEncryption = rip.getResponseUnEncryption();
        if (responseUnEncryption == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "responseUnEncryption can't be null");

        Boolean existenceRequestBody = rip.getExistenceRequestBody();
        if (existenceRequestBody == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "existenceRequestBody can't be null");

        Boolean existenceResponseBody = rip.getExistenceResponseBody();
        if (existenceResponseBody == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "existenceResponseBody can't be null");

        Integer type = rip.getType();
        assertResourceType(type, false);

        String name = rip.getName();
        if (isBlank(name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");

        String description = rip.getDescription();
        if (isBlank(description))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "description can't be blank");

        if (isNotNull(resourceMapper.selectByName(name)))
            throw new BlueException(RESOURCE_NAME_ALREADY_EXIST);

        if (isNotNull(resourceMapper.selectByUnique(requestMethod.toUpperCase(), module.toLowerCase(), uri.toLowerCase())))
            throw new BlueException(RESOURCE_FEATURE_ALREADY_EXIST);
    };

    /**
     * is a resource exist?
     */
    private final Function<ResourceUpdateParam, Resource> UPDATE_RESOURCE_VALIDATOR_AND_ORIGIN_RETURNER = rup -> {
        Long id = rup.getId();
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        ofNullable(rup.getName())
                .filter(BlueChecker::isNotBlank)
                .map(resourceMapper::selectByName)
                .map(Resource::getId)
                .ifPresent(eid -> {
                    if (!id.equals(eid))
                        throw new BlueException(RESOURCE_NAME_ALREADY_EXIST);
                });

        Resource resource = resourceMapper.selectByPrimaryKey(id);
        if (resource == null)
            throw new BlueException(DATA_NOT_EXIST);

        ofNullable(resourceMapper.selectByUnique(
                ofNullable(rup.getRequestMethod()).filter(BlueChecker::isNotBlank).map(String::trim).map(String::toUpperCase).orElseGet(resource::getRequestMethod),
                ofNullable(rup.getModule()).filter(BlueChecker::isNotBlank).map(String::trim).map(String::toLowerCase).orElseGet(resource::getModule),
                ofNullable(rup.getUri()).filter(BlueChecker::isNotBlank).map(String::trim).map(String::toLowerCase).map(uri -> {
                    REST_URI_ASSERTER.accept(uri);
                    return uri;
                }).orElseGet(resource::getUri))
        )
                .map(Resource::getId)
                .ifPresent(rid -> {
                    if (!id.equals(rid))
                        throw new BlueException(RESOURCE_FEATURE_ALREADY_EXIST);
                });

        return resource;
    };

    /**
     * for resource
     */
    public static final BiFunction<ResourceUpdateParam, Resource, Boolean> RESOURCE_UPDATE_PARAM_AND_ROLE_COMPARER = (p, t) -> {
        if (!p.getId().equals(t.getId()))
            throw new BlueException(BAD_REQUEST);

        boolean alteration = false;

        String requestMethod = p.getRequestMethod();
        if (isNotBlank(requestMethod) && !requestMethod.equals(t.getRequestMethod())) {
            t.setRequestMethod(requestMethod);
            alteration = true;
        }

        String module = p.getModule();
        if (isNotBlank(module) && !module.equals(t.getModule())) {
            t.setModule(module);
            alteration = true;
        }

        String uri = p.getUri();
        if (isNotBlank(uri) && !uri.equals(t.getUri())) {
            REST_URI_ASSERTER.accept(uri);
            t.setUri(uri);
            alteration = true;
        }

        Boolean authenticate = p.getAuthenticate();
        if (authenticate != null && !authenticate.equals(t.getAuthenticate())) {
            t.setAuthenticate(authenticate);
            alteration = true;
        }

        Boolean requestUnDecryption = p.getRequestUnDecryption();
        if (requestUnDecryption != null && !requestUnDecryption.equals(t.getRequestUnDecryption())) {
            t.setRequestUnDecryption(requestUnDecryption);
            alteration = true;
        }

        Boolean responseUnEncryption = p.getResponseUnEncryption();
        if (responseUnEncryption != null && !responseUnEncryption.equals(t.getResponseUnEncryption())) {
            t.setResponseUnEncryption(responseUnEncryption);
            alteration = true;
        }

        Boolean existenceRequestBody = p.getExistenceRequestBody();
        if (existenceRequestBody != null && !existenceRequestBody.equals(t.getExistenceRequestBody())) {
            t.setExistenceRequestBody(existenceRequestBody);
            alteration = true;
        }

        Boolean existenceResponseBody = p.getExistenceResponseBody();
        if (existenceResponseBody != null && !existenceResponseBody.equals(t.getExistenceResponseBody())) {
            t.setExistenceResponseBody(existenceResponseBody);
            alteration = true;
        }

        Integer type = p.getType();
        assertResourceType(type, true);
        if (type != null && !type.equals(t.getType())) {
            t.setType(type);
            alteration = true;
        }

        String name = p.getName();
        if (isNotBlank(name) && !name.equals(t.getName())) {
            t.setName(name);
            alteration = true;
        }

        String description = p.getDescription();
        if (isNotBlank(description) && !description.equals(t.getDescription())) {
            t.setDescription(description);
            alteration = true;
        }

        return alteration;
    };

    /**
     * insert resource
     *
     * @param resourceInsertParam
     * @param operatorId
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public ResourceInfo insertResource(ResourceInsertParam resourceInsertParam, Long operatorId) {
        LOGGER.info("ResourceInfo insertResource(ResourceInsertParam resourceInsertParam), resourceInsertParam = {}", resourceInsertParam);
        if (isNull(resourceInsertParam))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        INSERT_RESOURCE_VALIDATOR.accept(resourceInsertParam);
        Resource resource = RESOURCE_INSERT_PARAM_2_RESOURCE_CONVERTER.apply(resourceInsertParam);

        long id = blueIdentityProcessor.generate(Resource.class);
        resource.setId(id);
        resource.setCreator(operatorId);
        resource.setUpdater(operatorId);

        resourceMapper.insert(resource);

        return RESOURCE_2_RESOURCE_INFO_CONVERTER.apply(resource);
    }

    /**
     * update a exist resource
     *
     * @param resourceUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public ResourceInfo updateResource(ResourceUpdateParam resourceUpdateParam, Long operatorId) {
        LOGGER.info("ResourceInfo updateResource(ResourceUpdateParam resourceUpdateParam, Long operatorId), resourceUpdateParam = {}", resourceUpdateParam);
        if (isNull(resourceUpdateParam))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        Resource resource = UPDATE_RESOURCE_VALIDATOR_AND_ORIGIN_RETURNER.apply(resourceUpdateParam);
        if (RESOURCE_UPDATE_PARAM_AND_ROLE_COMPARER.apply(resourceUpdateParam, resource)) {
            resourceMapper.updateByPrimaryKeySelective(resource);
            return RESOURCE_2_RESOURCE_INFO_CONVERTER.apply(resource);
        }

        throw new BlueException(DATA_HAS_NOT_CHANGED);
    }

    /**
     * delete resource
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public ResourceInfo deleteResourceById(Long id) {
        LOGGER.info("ResourceInfo deleteResourceById(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        Resource resource = resourceMapper.selectByPrimaryKey(id);
        if (resource != null) {
            resourceMapper.deleteByPrimaryKey(id);
            return RESOURCE_2_RESOURCE_INFO_CONVERTER.apply(resource);
        }

        throw new BlueException(DATA_NOT_EXIST);
    }

    /**
     * get resource by role id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Resource> getResourceById(Long id) {
        LOGGER.info("Optional<Resource> getResourceById(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(resourceMapper.selectByPrimaryKey(id));
    }

    /**
     * select resources mono by ids
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<Resource>> getResourceMonoById(Long id) {
        LOGGER.info("Mono<Optional<Resource>> getResourceMonoById(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

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
    public List<Resource> selectResourceByIds(List<Long> ids) {
        LOGGER.info("List<Resource> selectResourceByIds(List<Long> ids), ids = {}", ids);

        return isValidIdentities(ids) ? allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(resourceMapper::selectByIds)
                .flatMap(List::stream)
                .collect(toList())
                :
                emptyList();
    }

    /**
     * select resources mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<Resource>> selectResourceMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<Resource>> selectResourceMonoByIds(List<Long> ids), ids = {}", ids);

        return just(this.selectResourceByIds(ids));
    }

    /**
     * select resource by page and condition
     *
     * @param limit
     * @param rows
     * @param resourceCondition
     * @return
     */
    @Override
    public Mono<List<Resource>> selectResourceMonoByLimitAndCondition(Long limit, Long rows, ResourceCondition resourceCondition) {
        LOGGER.info("Mono<List<Resource>> selectResourceMonoByLimitAndCondition(Long limit, Long rows, ResourceCondition resourceCondition), " +
                "limit = {}, rows = {}, resourceCondition = {}", limit, rows, resourceCondition);
        return just(resourceMapper.selectByLimitAndCondition(limit, rows, resourceCondition));
    }

    /**
     * count resource by condition
     *
     * @param resourceCondition
     * @return
     */
    @Override
    public Mono<Long> countResourceMonoByCondition(ResourceCondition resourceCondition) {
        LOGGER.info("Mono<Long> countResourceMonoByCondition(ResourceCondition resourceCondition), resourceCondition = {}", resourceCondition);
        return just(ofNullable(resourceMapper.countByCondition(resourceCondition)).orElse(0L));
    }

    /**
     * select resource info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<ResourceInfo>> selectResourceInfoPageMonoByPageAndCondition(PageModelRequest<ResourceCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<ResourceInfo>> selectResourceInfoPageMonoByPageAndCondition(PageModelRequest<ResourceCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);

        ResourceCondition resourceCondition = pageModelRequest.getParam();
        CONDITION_REPACKAGER.accept(resourceCondition);

        return zip(selectResourceMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), resourceCondition), countResourceMonoByCondition(resourceCondition))
                .flatMap(tuple2 -> {
                    List<Resource> resources = tuple2.getT1();
                    Mono<List<ResourceInfo>> resourceInfosMono = resources.size() > 0 ?
                            just(resources.stream().map(RESOURCE_2_RESOURCE_INFO_CONVERTER).collect(toList()))
                            :
                            just(emptyList());

                    return resourceInfosMono
                            .flatMap(roleInfos ->
                                    just(new PageModelResponse<>(roleInfos, tuple2.getT2())));
                });
    }
}
