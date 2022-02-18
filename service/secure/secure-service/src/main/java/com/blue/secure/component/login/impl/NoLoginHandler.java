package com.blue.secure.component.login.impl;

import com.blue.base.constant.secure.LoginType;
import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.secure.component.login.inter.LoginHandler;
import com.blue.secure.model.LoginParam;
import com.blue.secure.service.inter.MemberService;
import com.blue.secure.service.inter.SecureService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.BlueHeader.AUTHORIZATION;
import static com.blue.base.constant.base.BlueHeader.SECRET;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.OK;
import static com.blue.base.constant.secure.LoginType.EMAIL_PWD;
import static com.blue.base.constant.secure.LoginType.NOT_LOGGED_IN;
import static com.blue.secure.constant.LoginAttribute.IDENTITY;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.util.Loggers.getLogger;

/**
 * no login handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"DuplicatedCode", "AliControlFlowStatementWithoutBraces"})
@Component
@Order(LOWEST_PRECEDENCE - 1)
public class NoLoginHandler implements LoginHandler {

    private static final Logger LOGGER = getLogger(NoLoginHandler.class);

    private final MemberService memberService;

    private final SecureService secureService;

    public NoLoginHandler(MemberService memberService, SecureService secureService) {
        this.memberService = memberService;
        this.secureService = secureService;
    }

    @Override
    public Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest) {
        LOGGER.info("Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest), loginParam = {}", loginParam);
        if (loginParam == null)
            throw new BlueException(EMPTY_PARAM);

        String email = loginParam.getData(IDENTITY.key);

        //TODO verify

        return memberService.selectMemberBasicInfoMonoByEmailWithAssertPwd(email, loginParam.getData(IDENTITY.key))
                .flatMap(mbi -> secureService.generateAuthMono(mbi.getId(), EMAIL_PWD.identity, loginParam.getDeviceType().intern())
                )
                .flatMap(ma ->
                        ok().contentType(APPLICATION_JSON)
                                .header(AUTHORIZATION.name, ma.getAuth())
                                .header(SECRET.name, ma.getSecKey())
                                .body(generate(OK.code, serverRequest)
                                        , BlueResponse.class));
    }

    @Override
    public LoginType targetType() {
        return NOT_LOGGED_IN;
    }

}
