package com.blue.auth.service.impl;

import com.blue.auth.api.model.ResourceInfo;
import com.blue.auth.constant.ResourceSortAttribute;
import com.blue.auth.model.ResourceCondition;
import com.blue.auth.model.ResourceInsertParam;
import com.blue.auth.model.ResourceManagerInfo;
import com.blue.auth.model.ResourceUpdateParam;
import com.blue.auth.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.auth.repository.entity.Resource;
import com.blue.auth.repository.mapper.ResourceMapper;
import com.blue.auth.service.inter.ResourceService;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.redisson.component.SynchronizedProcessor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

import static com.blue.auth.converter.AuthModelConverters.*;
import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.*;
import static com.blue.basic.common.base.ConstantProcessor.assertResourceType;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_SELECT;
import static com.blue.basic.constant.common.CacheKey.RESOURCES;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Symbol.PERCENT;
import static com.blue.basic.constant.common.SyncKey.RESOURCES_REFRESH_SYNC;
import static com.blue.database.common.ConditionSortProcessor.process;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * resource service interface impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class ResourceServiceImpl implements ResourceService {

    private static final Logger LOGGER = getLogger(ResourceServiceImpl.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private ResourceMapper resourceMapper;

    private StringRedisTemplate stringRedisTemplate;

    private SynchronizedProcessor synchronizedProcessor;

    public ResourceServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, BlueIdentityProcessor blueIdentityProcessor, ResourceMapper resourceMapper,
                               StringRedisTemplate stringRedisTemplate, SynchronizedProcessor synchronizedProcessor) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.resourceMapper = resourceMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.synchronizedProcessor = synchronizedProcessor;
    }

    private static final int
            REDIS_LIST_START = 0,
            REDIS_LIST_END = -1;

    private final Consumer<String> CACHE_DELETER = key ->
            stringRedisTemplate.delete(key);

    private final Supplier<List<Resource>> RESOURCES_DB_SUP = () ->
            resourceMapper.select();

    private final Supplier<List<Resource>> RESOURCES_REDIS_SUP = () ->
            ofNullable(stringRedisTemplate.opsForList().range(RESOURCES.key, REDIS_LIST_START, REDIS_LIST_END))
                    .orElseGet(Collections::emptyList).stream().map(s -> GSON.fromJson(s, Resource.class)).collect(toList());

    private final Consumer<List<Resource>> RESOURCES_REDIS_SETTER = resources -> {
        CACHE_DELETER.accept(RESOURCES.key);
        stringRedisTemplate.opsForList().rightPushAll(RESOURCES.key,
                resources.stream().map(GSON::toJson).collect(toList()));
    };

    private final Supplier<List<Resource>> RESOURCES_WITH_CACHE_SUP = () ->
            synchronizedProcessor.handleSupByOrderedWithSetter(RESOURCES_REFRESH_SYNC.key, RESOURCES_REDIS_SUP, RESOURCES_DB_SUP, RESOURCES_REDIS_SETTER, BlueChecker::isNotEmpty);

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(ResourceSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<ResourceCondition> CONDITION_PROCESSOR = c -> {
        ResourceCondition rc = isNotNull(c) ? c : new ResourceCondition();

        process(rc, SORT_ATTRIBUTE_MAPPING, ResourceSortAttribute.CREATE_TIME.column);

        ofNullable(rc.getRequestMethod())
                .filter(StringUtils::hasText).map(String::toUpperCase).ifPresent(rc::setRequestMethod);
        ofNullable(rc.getModule())
                .filter(StringUtils::hasText).map(String::toLowerCase).ifPresent(rc::setModule);

        ofNullable(rc.getUriLike())
                .filter(StringUtils::hasText).map(String::toLowerCase).ifPresent(uriLike -> rc.setUriLike(PERCENT.identity + uriLike + PERCENT.identity));
        ofNullable(rc.getNameLike())
                .filter(StringUtils::hasText).ifPresent(nameLike -> rc.setNameLike(PERCENT.identity + nameLike + PERCENT.identity));

        return rc;
    };

    private static final Function<List<Resource>, List<Long>> OPERATORS_GETTER = rs -> {
        Set<Long> operatorIds = new HashSet<>(rs.size());

        for (Resource r : rs) {
            operatorIds.add(r.getCreator());
            operatorIds.add(r.getUpdater());
        }

        return new ArrayList<>(operatorIds);
    };

    private final Consumer<ResourceInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        if (isNotNull(resourceMapper.selectByName(p.getName())))
            throw new BlueException(RESOURCE_NAME_ALREADY_EXIST);

        if (isNotNull(resourceMapper.selectByUnique(p.getRequestMethod().toUpperCase(),
                p.getModule().toLowerCase(), p.getName().toLowerCase())))
            throw new BlueException(RESOURCE_FEATURE_ALREADY_EXIST);
    };

    private final Function<ResourceUpdateParam, Resource> UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long id = p.getId();

        ofNullable(p.getName())
                .filter(BlueChecker::isNotBlank)
                .map(resourceMapper::selectByName)
                .map(Resource::getId)
                .ifPresent(eid -> {
                    if (!id.equals(eid))
                        throw new BlueException(RESOURCE_NAME_ALREADY_EXIST);
                });

        Resource resource = resourceMapper.selectByPrimaryKey(id);
        if (isNull(resource))
            throw new BlueException(DATA_NOT_EXIST);

        ofNullable(resourceMapper.selectByUnique(
                ofNullable(p.getRequestMethod()).filter(BlueChecker::isNotBlank).map(String::trim).map(String::toUpperCase).orElseGet(resource::getRequestMethod),
                ofNullable(p.getModule()).filter(BlueChecker::isNotBlank).map(String::trim).map(String::toLowerCase).orElseGet(resource::getModule),
                ofNullable(p.getUri()).filter(BlueChecker::isNotBlank).map(String::trim).map(String::toLowerCase).map(uri -> {
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

    public static final BiConsumer<ResourceUpdateParam, Resource> UPDATE_ITEM_WITH_ASSERT_PACKAGER = (p, t) -> {
        if (isNull(p) || isNull(t))
            throw new BlueException(BAD_REQUEST);

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

        if (!alteration)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        t.setUpdateTime(TIME_STAMP_GETTER.get());
    };

    /**
     * insert resource
     *
     * @param resourceInsertParam
     * @param operatorId
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public ResourceInfo insertResource(ResourceInsertParam resourceInsertParam, Long operatorId) {
        LOGGER.info("resourceInsertParam = {}, operatorId = {}",
                resourceInsertParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        INSERT_ITEM_VALIDATOR.accept(resourceInsertParam);
        Resource resource = RESOURCE_INSERT_PARAM_2_RESOURCE_CONVERTER.apply(resourceInsertParam);

        resource.setId(blueIdentityProcessor.generate(Resource.class));
        resource.setCreator(operatorId);
        resource.setUpdater(operatorId);

        CACHE_DELETER.accept(RESOURCES.key);
        resourceMapper.insert(resource);
        CACHE_DELETER.accept(RESOURCES.key);

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
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public ResourceInfo updateResource(ResourceUpdateParam resourceUpdateParam, Long operatorId) {
        LOGGER.info("resourceUpdateParam = {}, operatorId = {}",
                resourceUpdateParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        Resource resource = UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER.apply(resourceUpdateParam);

        UPDATE_ITEM_WITH_ASSERT_PACKAGER.accept(resourceUpdateParam, resource);
        resource.setUpdater(operatorId);

        CACHE_DELETER.accept(RESOURCES.key);
        resourceMapper.updateByPrimaryKeySelective(resource);
        CACHE_DELETER.accept(RESOURCES.key);

        return RESOURCE_2_RESOURCE_INFO_CONVERTER.apply(resource);
    }

    /**
     * delete resource
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public ResourceInfo deleteResource(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        Resource resource = resourceMapper.selectByPrimaryKey(id);
        if (isNull(resource))
            throw new BlueException(DATA_NOT_EXIST);

        CACHE_DELETER.accept(RESOURCES.key);
        resourceMapper.deleteByPrimaryKey(id);
        CACHE_DELETER.accept(RESOURCES.key);

        return RESOURCE_2_RESOURCE_INFO_CONVERTER.apply(resource);
    }

    /**
     * get resource by role id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Resource> getResourceOpt(Long id) {
        LOGGER.info("id = {}", id);
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
    public Mono<Resource> getResource(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return justOrEmpty(resourceMapper.selectByPrimaryKey(id));
    }

    /**
     * select all resources
     *
     * @return
     */
    @Override
    public Mono<List<Resource>> selectResource() {
        return just(RESOURCES_WITH_CACHE_SUP.get());
    }

    /**
     * select resources mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<Resource>> selectResourceByIds(List<Long> ids) {
        LOGGER.info("ids = {}", ids);

        return just(allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(resourceMapper::selectByIds)
                .flatMap(List::stream)
                .collect(toList()));
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
    public Mono<List<Resource>> selectResourceByLimitAndCondition(Long limit, Long rows, ResourceCondition resourceCondition) {
        LOGGER.info("limit = {}, rows = {}, resourceCondition = {}", limit, rows, resourceCondition);
        if (isInvalidLimit(limit) || isInvalidRows(rows))
            throw new BlueException(INVALID_PARAM);

        return just(resourceMapper.selectByLimitAndCondition(limit, rows, resourceCondition));
    }

    /**
     * count resource by condition
     *
     * @param resourceCondition
     * @return
     */
    @Override
    public Mono<Long> countResourceByCondition(ResourceCondition resourceCondition) {
        LOGGER.info("resourceCondition = {}", resourceCondition);
        return just(ofNullable(resourceMapper.countByCondition(resourceCondition)).orElse(0L));
    }

    /**
     * select resource info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<ResourceManagerInfo>> selectResourceManagerInfoPageByPageAndCondition(PageModelRequest<ResourceCondition> pageModelRequest) {
        LOGGER.info("pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        ResourceCondition resourceCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectResourceByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), resourceCondition), countResourceByCondition(resourceCondition))
                .flatMap(tuple2 -> {
                    List<Resource> resources = tuple2.getT1();
                    Long count = tuple2.getT2();
                    return isNotEmpty(resources) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(OPERATORS_GETTER.apply(resources))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndMemberNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(resources.stream().map(r ->
                                                RESOURCE_2_RESOURCE_MANAGER_INFO_CONVERTER.apply(r, idAndMemberNameMapping)).collect(toList()));
                                    }).flatMap(resourceManagerInfos ->
                                            just(new PageModelResponse<>(resourceManagerInfos, count)))
                            :
                            just(new PageModelResponse<>(emptyList(), count));
                });
    }
}
