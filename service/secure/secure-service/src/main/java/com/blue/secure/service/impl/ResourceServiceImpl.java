package com.blue.secure.service.impl;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.secure.api.model.ResourceInfo;
import com.blue.secure.constant.RoleSortAttribute;
import com.blue.secure.model.ResourceCondition;
import com.blue.secure.model.ResourceInsertParam;
import com.blue.secure.repository.entity.Resource;
import com.blue.secure.repository.mapper.ResourceMapper;
import com.blue.secure.service.inter.ResourceService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.base.common.base.ConstantProcessor.assertSortType;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;
import static com.blue.secure.converter.SecureModelConverters.RESOURCE_2_RESOURCE_INFO_CONVERTER;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;
import static reactor.util.Loggers.getLogger;

/**
 * resource service interface impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "CatchMayIgnoreException"})
@Service
public class ResourceServiceImpl implements ResourceService {

    private static final Logger LOGGER = getLogger(ResourceServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final ResourceMapper resourceMapper;

    private final RedissonClient redissonClient;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ResourceServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ResourceMapper resourceMapper, RedissonClient redissonClient) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.resourceMapper = resourceMapper;
        this.redissonClient = redissonClient;
    }

    /**
     * sort attr - sort column mapping
     */
    private static final Map<String, String> RESOURCE_SORT_ATTRIBUTE_MAPPING = Stream.of(RoleSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    /**
     * sort attr -> sort column
     */
    private static final UnaryOperator<String> RESOURCE_SORT_ATTRIBUTE_CONVERTER = attr ->
            ofNullable(attr)
                    .map(RESOURCE_SORT_ATTRIBUTE_MAPPING::get)
                    .filter(StringUtils::hasText)
                    .orElse("");

    /**
     * process columns
     */
    private static final Consumer<ResourceCondition> RESOURCE_COLUMN_REPACKAGER = condition -> {
        if (condition != null) {
            condition.setSortAttribute(RESOURCE_SORT_ATTRIBUTE_CONVERTER.apply(condition.getSortAttribute()));
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

    private static final String RESOURCE_INSERT_SYNC_KEY = "INSERT_RESROUCE";

    /**
     * insert resource
     *
     * @param resourceInsertParam
     */
    @Override
    public void insertResource(ResourceInsertParam resourceInsertParam) {

        RLock resourceInsertLock = redissonClient.getLock(RESOURCE_INSERT_SYNC_KEY);

        try {
            //TODO
            resourceInsertLock.lock(10L, TimeUnit.SECONDS);


        } catch (Exception exception) {
            LOGGER.error("lock on business failed, e = {}", exception);
        } finally {
            try {
                resourceInsertLock.unlock();
            } catch (Exception e) {
            }
        }


    }

    /**
     * select resources by ids
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<Resource>> getResourceMonoById(Long id) {
        LOGGER.info("Mono<Optional<Resource>> getResourceMonoById(Long id), id = {}", id);
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
        RESOURCE_COLUMN_REPACKAGER.accept(resourceCondition);

        return this.countResourceMonoByCondition(resourceCondition)
                .flatMap(roleCount -> {
                    Mono<List<Resource>> listMono = roleCount > 0L ? this.selectResourceMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), resourceCondition) : just(emptyList());
                    return zip(listMono, just(roleCount));
                })
                .flatMap(tuple2 -> {
                    List<Resource> resources = tuple2.getT1();
                    Mono<List<ResourceInfo>> resourceInfosMono = resources.size() > 0 ?
                            just(resources.stream()
                                    .map(RESOURCE_2_RESOURCE_INFO_CONVERTER).collect(toList()))
                            :
                            just(emptyList());

                    return resourceInfosMono
                            .flatMap(roleInfos ->
                                    just(new PageModelResponse<>(roleInfos, tuple2.getT2())));
                });
    }
}
