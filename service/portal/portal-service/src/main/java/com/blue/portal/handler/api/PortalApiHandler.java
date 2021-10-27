package com.blue.portal.handler.api;

import com.blue.base.common.reactive.ReactiveCommonFunctions;
import com.blue.base.model.base.BlueResponse;
import com.blue.portal.service.inter.PortalService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.Map;

import static com.blue.base.common.reactive.MetadataGetterForReactive.getMetadata;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * portal api handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Component
public final class PortalApiHandler {

    private static final Logger LOGGER = Loggers.getLogger(PortalApiHandler.class);

    private final PortalService portalService;

    public PortalApiHandler(PortalService portalService) {
        this.portalService = portalService;
    }

    private static final String TYPE_PAR = "bulletinType";

    /**
     * get bulletin
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> getBulletin(ServerRequest serverRequest) {
        String ip = ReactiveCommonFunctions.getIp(serverRequest);
        LOGGER.warn("client ip = {}", ip);
        Map<String, String> metadata = getMetadata(serverRequest);
        LOGGER.warn("metadata = {}", metadata);

        return portalService.selectBulletinInfo(serverRequest.pathVariable(TYPE_PAR))
                .flatMap(bl -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(generate(OK.code, bl, OK.message), BlueResponse.class)
                );
    }

}
