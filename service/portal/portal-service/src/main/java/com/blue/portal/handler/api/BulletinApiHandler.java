package com.blue.portal.handler.api;

import com.blue.basic.model.common.BlueResponse;
import com.blue.portal.service.inter.BulletinService;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.common.base.PathVariableGetter.getIntegerVariable;
import static com.blue.basic.constant.common.PathVariable.TYPE;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * bulletin api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Component
public final class BulletinApiHandler {

    private static final Logger LOGGER = getLogger(BulletinApiHandler.class);

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
        return bulletinService.selectActiveBulletinInfoByTypeWithCache(getIntegerVariable(serverRequest, TYPE.key))
                .flatMap(bis -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(success(bis, serverRequest), BlueResponse.class)
                );
    }

}
