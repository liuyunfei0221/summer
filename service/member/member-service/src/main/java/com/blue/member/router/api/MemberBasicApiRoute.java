package com.blue.member.router.api;

import com.blue.member.handler.api.MemberBasicApiHandler;
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
 * member basic api router
 *
 * @author liuyunfei
 */
@Configuration
public class MemberBasicApiRoute {

    @Bean
    RouterFunction<ServerResponse> memberBasicApiRouter(MemberBasicApiHandler memberBasicApiHandler) {

        RequestPredicate pathPredicate = path("/blue-member/basic");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("", memberBasicApiHandler::get)
                .PATCH("/icon", accept(APPLICATION_JSON), memberBasicApiHandler::updateIcon)
                .PATCH("/qrCode", accept(APPLICATION_JSON), memberBasicApiHandler::updateQrCode)
                .PATCH("/profile", accept(APPLICATION_JSON), memberBasicApiHandler::updateProfile)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
