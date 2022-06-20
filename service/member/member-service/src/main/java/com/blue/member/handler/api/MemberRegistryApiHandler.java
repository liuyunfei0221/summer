package com.blue.member.handler.api;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.common.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.MemberRegistryParam;
import com.blue.member.service.inter.MemberAuthService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.base.ConstantProcessor.assertSource;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.common.reactive.SourceGetterForReactive.getSource;
import static com.blue.base.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.common.ResponseElement.OK;
import static com.blue.base.constant.member.SourceType.APP;
import static java.util.Optional.ofNullable;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * member registry handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class MemberRegistryApiHandler {

    private final MemberAuthService memberAuthService;

    public MemberRegistryApiHandler(MemberAuthService memberAuthService) {
        this.memberAuthService = memberAuthService;
    }

    /**
     * registry member
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> registry(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(MemberRegistryParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(mrp -> {
                    String source = ofNullable(getSource(serverRequest))
                            .filter(BlueChecker::isNotBlank).orElse(APP.identity);
                    assertSource(source, false);

                    mrp.setSource(source);
                    return just(memberAuthService.registerMemberBasic(mrp));
                })
                .flatMap(mbi ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(generate(OK.code, mbi, serverRequest), BlueResponse.class));
    }

}
