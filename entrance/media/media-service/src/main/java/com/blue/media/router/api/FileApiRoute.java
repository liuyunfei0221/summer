package com.blue.media.router.api;

import com.blue.media.handler.api.FileApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.blue.basic.constant.common.PathVariable.ID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * media api route
 *
 * @author liuyunfei
 */
@Configuration
public class FileApiRoute {

    @Bean
    RouterFunction<ServerResponse> fileApiRouter(FileApiHandler fileApiHandler) {

        RequestPredicate pathPredicate = path("/blue-media");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/file", accept(MULTIPART_FORM_DATA), fileApiHandler::upload)
                .GET("/file/{" + ID.key + "}", accept(APPLICATION_JSON), fileApiHandler::download)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
