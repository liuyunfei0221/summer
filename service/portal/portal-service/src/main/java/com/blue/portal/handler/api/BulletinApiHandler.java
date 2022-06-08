package com.blue.portal.handler.api;

import com.blue.base.model.common.BlueResponse;
import com.blue.portal.service.inter.BulletinService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import static com.blue.base.common.reactive.PathVariableGetter.getIntegerVariable;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.common.PathVariable.TYPE;
import static com.blue.base.constant.common.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * portal api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Component
public final class BulletinApiHandler {

    private static final Logger LOGGER = Loggers.getLogger(BulletinApiHandler.class);

    private final BulletinService bulletinService;

    public BulletinApiHandler(BulletinService bulletinService) {
        this.bulletinService = bulletinService;
    }

    /**
     * select bulletin
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return bulletinService.selectActiveBulletinInfoMonoByTypeWithCache(getIntegerVariable(serverRequest, TYPE.key))
                .flatMap(blis -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(generate(OK.code, blis, serverRequest), BlueResponse.class)
                );
    }

}
