package com.blue.shine.service.inter;

import com.blue.shine.api.model.ShineInfo;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * shine service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface ShineService {

    /**
     * list shine infos
     *
     * @param ip
     * @return
     */
    Mono<List<ShineInfo>> listShineInfo(String ip);

}
