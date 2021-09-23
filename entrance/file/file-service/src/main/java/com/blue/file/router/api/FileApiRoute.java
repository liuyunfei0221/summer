package com.blue.file.router.api;

import com.blue.file.handler.api.FileApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * 文件映射器
 *
 * @author DarkBlue
 */
@Configuration
public class FileApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> fileRouter(FileApiHandler fileApiHandler) {

        RequestPredicate pathPredicate = path("/blue-file/file");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/upload", accept(MULTIPART_FORM_DATA), fileApiHandler::upload)
                .POST("/download", accept(APPLICATION_JSON), fileApiHandler::download)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
