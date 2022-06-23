package com.blue.finance.router.dynamic;

import com.blue.finance.config.deploy.DynamicApiDeploy;
import com.blue.finance.handler.dynamic.BlueDynamicHandler;
import com.blue.finance.repository.entity.DynamicResource;
import com.blue.finance.service.inter.DynamicResourceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.util.Logger;

import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.common.base.ConstantProcessor.getMediaTypeByIdentity;
import static com.blue.base.constant.common.Symbol.PATH_SEPARATOR;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static reactor.util.Loggers.getLogger;

/**
 * dynamic routers
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "NullableProblems"})
@Configuration
public class BlueDynamicRoute {

    private static final Logger LOGGER = getLogger(BlueDynamicRoute.class);

    private final String PATH;

    public BlueDynamicRoute(DynamicApiDeploy dynamicApiDeploy) {
        String path = dynamicApiDeploy.getPath();
        if (isBlank(path))
            throw new RuntimeException("path can't be null or ''");

        this.PATH = PATH_PARSER.apply(path);
    }

    private static final UnaryOperator<String> PATH_PARSER = path -> path;

    private static final Function<String, RequestPredicate> MEDIA_TYPE_PREDICATE_GENERATOR = str -> accept(getMediaTypeByIdentity(str.toLowerCase().intern()));

    /**
     * generate endPoint
     *
     * @param routeBuilder
     * @param dynamicResource
     * @param handlerFunction
     */
    private static void generateEndPointAttr(RouterFunctions.Builder routeBuilder, DynamicResource dynamicResource, HandlerFunction<ServerResponse> handlerFunction) {

        String requestMethod = dynamicResource.getRequestMethod().intern().toUpperCase().intern();
        Long uriPlaceholder = dynamicResource.getUriPlaceholder();

        if (isNull(uriPlaceholder))
            throw new RuntimeException("uriPlaceholder can't be null");

        switch (requestMethod) {
            case "GET":
                routeBuilder.GET((PATH_SEPARATOR.identity + uriPlaceholder).intern(), handlerFunction);
                break;

            case "HEAD":
                routeBuilder.HEAD((PATH_SEPARATOR.identity + uriPlaceholder).intern(), handlerFunction);
                break;

            case "POST":
                routeBuilder.POST((PATH_SEPARATOR.identity + uriPlaceholder).intern(), MEDIA_TYPE_PREDICATE_GENERATOR.apply(dynamicResource.getContentType().intern()), handlerFunction);
                break;

            case "PUT":
                routeBuilder.PUT((PATH_SEPARATOR.identity + uriPlaceholder).intern(), MEDIA_TYPE_PREDICATE_GENERATOR.apply(dynamicResource.getContentType().intern()), handlerFunction);
                break;

            case "PATCH":
                routeBuilder.PATCH((PATH_SEPARATOR.identity + uriPlaceholder).intern(), MEDIA_TYPE_PREDICATE_GENERATOR.apply(dynamicResource.getContentType().intern()), handlerFunction);
                break;

            case "DELETE":
                routeBuilder.DELETE((PATH_SEPARATOR.identity + uriPlaceholder).intern(), MEDIA_TYPE_PREDICATE_GENERATOR.apply(dynamicResource.getContentType().intern()), handlerFunction);
                break;

            case "OPTIONS":
                routeBuilder.OPTIONS((PATH_SEPARATOR.identity + uriPlaceholder).intern(), handlerFunction);
                break;

            default:
                throw new RuntimeException("not support method -> " + requestMethod);
        }

        LOGGER.info("generate endPoint, dynamicResource = {}", dynamicResource);
    }

    @Bean
    RouterFunction<ServerResponse> dynamicRouter(BlueDynamicHandler blueDynamicHandler, DynamicResourceService dynamicResourceService) {
        List<DynamicResource> dynamicResources = dynamicResourceService.selectDynamicResource();

        RouterFunctions.Builder routeBuilder = route();
        dynamicResources.forEach(resource -> generateEndPointAttr(routeBuilder, resource, blueDynamicHandler::handle));

        RequestPredicate pathPredicate = path(PATH);
        return nest(pathPredicate, routeBuilder.build());
    }

}
