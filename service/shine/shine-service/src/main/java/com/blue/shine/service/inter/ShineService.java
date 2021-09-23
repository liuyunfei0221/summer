package com.blue.shine.service.inter;

import com.blue.shine.api.model.ShineInfo;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 公益功能接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface ShineService {

    /**
     * 获取公益信息
     *
     * @param ip
     * @return
     */
    Mono<List<ShineInfo>> getShineInfo(String ip);

}
