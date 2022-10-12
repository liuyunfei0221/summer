package com.blue.finance.handler.dynamic;

import com.blue.basic.constant.common.Symbol;
import com.blue.basic.model.exps.BlueException;
import com.blue.finance.component.dynamic.inter.DynamicEndPointHandler;
import com.blue.finance.config.deploy.DynamicApiDeploy;
import com.blue.finance.repository.entity.DynamicHandler;
import com.blue.finance.repository.entity.DynamicResource;
import com.blue.finance.service.inter.DynamicHandlerService;
import com.blue.finance.service.inter.DynamicResourceService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.constant.common.ResponseElement.*;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static reactor.util.Loggers.getLogger;

/**
 * dynamic handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
@Order(HIGHEST_PRECEDENCE)
public final class BlueDynamicHandler implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = getLogger(BlueDynamicHandler.class);

    private DynamicHandlerService dynamicHandlerService;

    private DynamicResourceService dynamicResourceService;

    private Map<String, DynamicEndPointHandler> clzWithHandlerMapping;

    private Map<String, DynamicEndPointHandler> placeHolderHandlerMapping;

    private static volatile boolean dynamicInfoRefreshing = true;

    private long maxWaitingForRefresh;

    private static final String PATH_SEPARATOR = Symbol.SLASH.identity;

    private static final String PAR_CONCATENATION = Symbol.PAR_CONCATENATION.identity;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();

        Map<String, DynamicHandlerService> beansOfDynamicHandlerService = applicationContext.getBeansOfType(DynamicHandlerService.class);
        if (beansOfDynamicHandlerService.values().size() != 1)
            throw new RuntimeException("beans of dynamicHandlerService can't be empty or more than 1");

        Map<String, DynamicResourceService> beansOfDynamicResourceService = applicationContext.getBeansOfType(DynamicResourceService.class);
        if (beansOfDynamicResourceService.values().size() != 1)
            throw new RuntimeException("beans of dynamicResourceService can't be empty or more than 1");

        Map<String, DynamicApiDeploy> beansOfDynamicApiDeploy = applicationContext.getBeansOfType(DynamicApiDeploy.class);
        if (beansOfDynamicApiDeploy.values().size() != 1)
            throw new RuntimeException("beans of dynamicApiDeploy can't be empty or more than 1");

        this.dynamicHandlerService = new ArrayList<>(beansOfDynamicHandlerService.values()).get(0);
        this.dynamicResourceService = new ArrayList<>(beansOfDynamicResourceService.values()).get(0);
        DynamicApiDeploy dynamicApiDeploy = new ArrayList<>(beansOfDynamicApiDeploy.values()).get(0);
        maxWaitingForRefresh = dynamicApiDeploy.getBlockingMillis();

        this.clzWithHandlerMapping = applicationContext.getBeansOfType(DynamicEndPointHandler.class)
                .values().stream().collect(toMap(h -> h.getClass().getName(), h -> h, (a, b) -> a));

        refreshHandlers();

        LOGGER.info("void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) success");
    }

    private final Supplier<Boolean> DYNAMIC_INFO_REFRESH_BLOCKER = () -> {
        if (dynamicInfoRefreshing) {
            long start = currentTimeMillis();
            while (dynamicInfoRefreshing) {
                if (currentTimeMillis() - start > maxWaitingForRefresh)
                    throw new BlueException(REQUEST_TIMEOUT);
                onSpinWait();
            }
        }
        return true;
    };

    private static final BinaryOperator<String> DYNAMIC_KEY_GENERATOR = (placeholder, method) -> {
        if (isBlank(placeholder) || isBlank(method))
            throw new RuntimeException("placeholder or method can't be blank");

        return placeholder + PAR_CONCATENATION + method.toUpperCase();
    };

    private final Function<ServerRequest, DynamicEndPointHandler> DYNAMIC_HANDLER_GETTER = serverRequest -> {
        String path = serverRequest.path();

        int index = lastIndexOf(path, PATH_SEPARATOR);
        if (index == -1)
            throw new BlueException(NOT_FOUND);

        String maybePlaceholder = substring(path, index + 1);

        DYNAMIC_INFO_REFRESH_BLOCKER.get();

        DynamicEndPointHandler dynamicEndPointHandler = placeHolderHandlerMapping.get(DYNAMIC_KEY_GENERATOR.apply(maybePlaceholder, serverRequest.methodName()));

        if (isNotNull(dynamicEndPointHandler))
            return dynamicEndPointHandler;

        throw new BlueException(NOT_FOUND);
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
                                .orElseThrow(() -> new RuntimeException("the handler with resource = {} not exist, r = " + r)), (a, b) -> a));

        dynamicInfoRefreshing = true;
        placeHolderHandlerMapping = tempPlaceHolderHandlerMapping;
        dynamicInfoRefreshing = false;

        LOGGER.info("void refreshHandlers(), placeHolderHandlerMapping = {}", placeHolderHandlerMapping);
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
