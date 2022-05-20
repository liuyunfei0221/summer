package com.blue.shine.service.inter;

import com.blue.shine.api.model.ShineInfo;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * shine service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface ShineService {

    /**
     * list shine info
     *
     * @param ip
     * @return
     */
    Mono<List<ShineInfo>> selectShineInfo(String ip);

}
