package com.blue.auth.handler.api;

import com.blue.auth.service.inter.ControlService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * credential api handler
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Component
public final class CredentialApiHandler {

    private final ControlService controlService;

    public CredentialApiHandler(ControlService controlService) {
        this.controlService = controlService;
    }

    /**
     * setting up phone credential
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> phoneCredentialSettingUp(ServerRequest serverRequest) {
        return controlService.login(serverRequest);
    }

    /**
     * setting up email credential
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> emailCredentialSettingUp(ServerRequest serverRequest) {
        return controlService.login(serverRequest);
    }

}
