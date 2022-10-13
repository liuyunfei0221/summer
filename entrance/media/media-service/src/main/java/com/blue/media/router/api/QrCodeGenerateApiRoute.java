package com.blue.media.router.api;

import com.blue.media.handler.api.QrCodeGenerateApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * qr code generate api route
 *
 * @author liuyunfei
 */
@Configuration
public class QrCodeGenerateApiRoute {

    @Bean
    RouterFunction<ServerResponse> qrCodeGenerateApiRouter(QrCodeGenerateApiHandler qrCodeGenerateApiHandler) {

        RequestPredicate pathPredicate = path("/blue-media/qrCode");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("", accept(APPLICATION_JSON), qrCodeGenerateApiHandler::generate)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
