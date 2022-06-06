package com.blue.member.router.api;

import com.blue.member.handler.api.MemberAddressApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.blue.base.constant.base.PathVariable.ID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * member address api router
 *
 * @author liuyunfei
 */
@Configuration
public class MemberAddressApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> memberAddressApiRouter(MemberAddressApiHandler memberAddressApiHandler) {

        RequestPredicate pathPredicate = path("/blue-member/address");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("", accept(APPLICATION_JSON), memberAddressApiHandler::insert)
                .PUT("", accept(APPLICATION_JSON), memberAddressApiHandler::update)
                .DELETE("/{" + ID.key + "}", memberAddressApiHandler::delete)
                .GET("", memberAddressApiHandler::select)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
