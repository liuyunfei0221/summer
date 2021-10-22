package com.blue.captcha.router.api;

import com.blue.captcha.handler.api.CaptchaApiHandler;
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
 * captcha api route
 *
 * @author DarkBlue
 */
@Configuration
public class CaptchaApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> captchaRouter(CaptchaApiHandler captchaApiHandler) {

        RequestPredicate pathPredicate = path("/blue-captcha/captcha");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/forClientLogin", accept(APPLICATION_JSON), captchaApiHandler::generateClientLoginImageCaptcha)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
