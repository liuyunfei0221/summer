package com.blue.secure.component.login.impl;

import com.blue.base.constant.secure.LoginType;
import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.secure.component.login.inter.LoginHandler;
import com.blue.secure.model.LoginParam;
import com.blue.secure.repository.entity.Credential;
import com.blue.secure.service.inter.CredentialService;
import com.blue.secure.service.inter.MemberService;
import com.blue.secure.service.inter.SecureService;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.function.BiPredicate;

import static com.blue.base.common.base.BlueChecker.isNotBlank;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.BlueHeader.AUTHORIZATION;
import static com.blue.base.constant.base.BlueHeader.SECRET;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.secure.LoginType.EMAIL_PWD;
import static com.blue.secure.constant.LoginAttribute.ACCESS;
import static com.blue.secure.constant.LoginAttribute.IDENTITY;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * email and password login handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Component
@Order(LOWEST_PRECEDENCE - 1)
public class EmailAndPwdLoginHandler implements LoginHandler {

    private static final Logger LOGGER = getLogger(EmailAndPwdLoginHandler.class);

    private final CredentialService credentialService;

    private final MemberService memberService;

    private final SecureService secureService;

    public EmailAndPwdLoginHandler(CredentialService credentialService, MemberService memberService, SecureService secureService) {
        this.credentialService = credentialService;
        this.memberService = memberService;
        this.secureService = secureService;
    }

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private static final BiPredicate<String, String> PWD_PRE = (access, originalAccess) ->
            isNotBlank(access) && isNotBlank(originalAccess) && ENCODER.matches(access, originalAccess);

    @Override
    public Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest) {
        LOGGER.info("Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest), loginParam = {}", loginParam);
        if (loginParam == null)
            throw new BlueException(EMPTY_PARAM);

        String email = loginParam.getData(IDENTITY.key);

        //TODO verify

        return credentialService.getCredentialByCredentialAndType(email, EMAIL_PWD.identity)
                .flatMap(credentialOpt ->
                        just(credentialOpt
                                .filter(c -> PWD_PRE.test(loginParam.getData(ACCESS.key), c.getAccess()))
                                .map(Credential::getMemberId).orElseThrow(() -> new BlueException(INVALID_ACCT_OR_PWD)))
                ).flatMap(memberService::selectMemberBasicById)
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
        return EMAIL_PWD;
    }

}
