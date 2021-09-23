package com.blue.portal.service.inter;

import com.blue.portal.api.model.BulletinInfo;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 门户业务接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface PortalService {

    /**
     * 获取公告信息
     *
     * @param bulletinType
     * @return
     */
    Mono<List<BulletinInfo>> listBulletin(String bulletinType);

}
