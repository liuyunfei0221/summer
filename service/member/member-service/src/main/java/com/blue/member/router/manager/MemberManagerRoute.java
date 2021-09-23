package com.blue.member.router.manager;

import com.blue.member.handler.manager.MemberManagerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * 用户后台路由
 *
 * @author liuyunfei
 * @date 2021/8/31
 * @apiNote
 */
@Configuration
public class MemberManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> memberManagerRouter(MemberManagerHandler memberManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-member/manager/member");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/list", accept(APPLICATION_JSON), memberManagerHandler::select)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
