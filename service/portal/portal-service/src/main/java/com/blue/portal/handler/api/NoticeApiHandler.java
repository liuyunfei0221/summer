package com.blue.portal.handler.api;

import com.blue.basic.model.common.BlueResponse;
import com.blue.portal.service.inter.NoticeService;
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
 * notice api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Component
public final class NoticeApiHandler {

    private static final Logger LOGGER = getLogger(NoticeApiHandler.class);

    private final NoticeService noticeService;

    public NoticeApiHandler(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    /**
     * get notice
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        return noticeService.getNoticeInfoByTypeWithCache(getIntegerVariable(serverRequest, TYPE.key))
                .flatMap(ni -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(success(ni, serverRequest), BlueResponse.class)
                );
    }

}
