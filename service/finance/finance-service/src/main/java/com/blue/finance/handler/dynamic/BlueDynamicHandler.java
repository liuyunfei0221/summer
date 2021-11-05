package com.blue.finance.handler.dynamic;

import com.blue.base.component.common.BlueBeanDefinitionScanner;
import com.blue.base.constant.base.Symbol;
import com.blue.base.model.exps.BlueException;
import com.blue.finance.component.dynamic.inter.DynamicEndPointHandler;
import com.blue.finance.config.deploy.DynamicApiDeploy;
import com.blue.finance.repository.entity.DynamicHandler;
import com.blue.finance.repository.entity.DynamicResource;
import com.blue.finance.service.inter.DynamicHandlerService;
import com.blue.finance.service.inter.DynamicResourceService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.*;
import static reactor.util.Loggers.getLogger;

/**
 * dynamic handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
public final class BlueDynamicHandler implements ResourceLoaderAware, ApplicationContextAware, ImportBeanDefinitionRegistrar {

    private static final Logger LOGGER = getLogger(BlueDynamicHandler.class);

    private ResourceLoader resourceLoader;

    private ApplicationContext applicationContext;

    private DynamicHandlerService dynamicHandlerService;

    private DynamicResourceService dynamicResourceService;

    private Map<String, DynamicEndPointHandler> clzWithHandlerMapping;

    private Map<String, DynamicEndPointHandler> placeHolderHandlerMapping;

    private static volatile boolean dynamicInfoRefreshing = true;

    private long maxWaitingForRefresh;

    private static final String[] SCAN_PACKAGES = new String[]{"com.blue.finance.component.dynamic.impl"};

    private static final boolean USE_DEFAULT_FILTERS = false;

    private static final String PATH_SEPARATOR = Symbol.PATH_SEPARATOR.identity;

    private static final String PAR_CONCATENATION = Symbol.PAR_CONCATENATION.identity;

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        BlueBeanDefinitionScanner scanner = new BlueBeanDefinitionScanner(registry, USE_DEFAULT_FILTERS);

        scanner.setResourceLoader(resourceLoader);
        scanner.addIncludeFilter(new AssignableTypeFilter(DynamicEndPointHandler.class));
        scanner.doScan(SCAN_PACKAGES);

        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
    }

    private final Supplier<Boolean> DYNAMIC_INFO_REFRESH_BLOCKER = () -> {
        if (dynamicInfoRefreshing) {
            long start = currentTimeMillis();
            while (dynamicInfoRefreshing) {
                if (currentTimeMillis() - start > maxWaitingForRefresh)
                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message);
                onSpinWait();
            }
        }
        return true;
    };

    private static final BinaryOperator<String> DYNAMIC_KEY_GENERATOR = (placeholder, method) -> {
        if (isBlank(placeholder) || isBlank(method))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message);

        return placeholder + PAR_CONCATENATION + method.toUpperCase();
    };

    private final Function<ServerRequest, DynamicEndPointHandler> DYNAMIC_HANDLER_GETTER = serverRequest -> {
        String path = serverRequest.path();

        int index = lastIndexOf(path, PATH_SEPARATOR);
        if (index == -1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message);

        String maybePlaceholder = substring(path, index + 1);

        DYNAMIC_INFO_REFRESH_BLOCKER.get();

        DynamicEndPointHandler dynamicEndPointHandler = placeHolderHandlerMapping.get(DYNAMIC_KEY_GENERATOR.apply(maybePlaceholder, serverRequest.methodName()));

        if (dynamicEndPointHandler != null)
            return dynamicEndPointHandler;

        throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message);
    };


    public void refreshHandlers() {
        List<DynamicHandler> dynamicHandlers = dynamicHandlerService.selectDynamicHandler();
        List<DynamicResource> dynamicResources = dynamicResourceService.selectDynamicResource();

        Map<Long, String> handlerIdWithBeanNameMapping = dynamicHandlers.stream()
                .collect(toMap(DynamicHandler::getId, DynamicHandler::getHandlerBean, (a, b) -> a));

        Map<String, DynamicEndPointHandler> tempPlaceHolderHandlerMapping = dynamicResources.stream()
                .collect(toMap(r -> DYNAMIC_KEY_GENERATOR.apply(String.valueOf(r.getUriPlaceholder()), r.getRequestMethod()), r ->
                        ofNullable(r.getHandlerId())
                                .map(handlerIdWithBeanNameMapping::get)
                                .map(clzWithHandlerMapping::get)
                                .orElseThrow(() -> new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "the handler with resource = {} not exist, r = " + r)), (a, b) -> a));

        dynamicInfoRefreshing = true;
        placeHolderHandlerMapping = tempPlaceHolderHandlerMapping;
        dynamicInfoRefreshing = false;

        LOGGER.info("refreshHandlers(), placeHolderHandlerMapping = {}", placeHolderHandlerMapping);
    }

    @PostConstruct
    public void init() {
        Map<String, DynamicHandlerService> beansOfDynamicHandlerService = applicationContext.getBeansOfType(DynamicHandlerService.class);
        if (beansOfDynamicHandlerService.values().size() != 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "beans of dynamicHandlerService can't be empty or more than 1");

        Map<String, DynamicResourceService> beansOfDynamicResourceService = applicationContext.getBeansOfType(DynamicResourceService.class);
        if (beansOfDynamicResourceService.values().size() != 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "beans of dynamicResourceService can't be empty or more than 1");

        Map<String, DynamicApiDeploy> beansOfDynamicApiDeploy = applicationContext.getBeansOfType(DynamicApiDeploy.class);
        if (beansOfDynamicApiDeploy.values().size() != 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "beans of dynamicApiDeploy can't be empty or more than 1");

        this.dynamicHandlerService = new ArrayList<>(beansOfDynamicHandlerService.values()).get(0);
        this.dynamicResourceService = new ArrayList<>(beansOfDynamicResourceService.values()).get(0);
        DynamicApiDeploy dynamicApiDeploy = new ArrayList<>(beansOfDynamicApiDeploy.values()).get(0);
        maxWaitingForRefresh = dynamicApiDeploy.getBlockingMillis();

        this.clzWithHandlerMapping = applicationContext.getBeansOfType(DynamicEndPointHandler.class)
                .values().stream().collect(toMap(h -> h.getClass().getName(), h -> h, (a, b) -> a));

        refreshHandlers();

        LOGGER.info("init() success");
    }


    /**
     * handle request
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        return DYNAMIC_HANDLER_GETTER.apply(serverRequest).handle(serverRequest);
    }

}
