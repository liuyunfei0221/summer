package com.blue.portal.handler.api;

import com.blue.base.model.base.BlueResponse;
import com.blue.portal.service.inter.PortalService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import static com.blue.base.common.reactive.PathVariableGetter.getIntegerVariable;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.PathVariable.TYPE;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * portal api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Component
public final class PortalApiHandler {

    private static final Logger LOGGER = Loggers.getLogger(PortalApiHandler.class);

    private final PortalService portalService;

    public PortalApiHandler(PortalService portalService) {
        this.portalService = portalService;
    }

    /**
     * select bulletin
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectBulletin(ServerRequest serverRequest) {
        return portalService.selectBulletin(getIntegerVariable(serverRequest, TYPE.key))
                .flatMap(bl -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(generate(OK.code, bl, serverRequest), BlueResponse.class)
                );
    }

}
