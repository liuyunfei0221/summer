package com.blue.portal.service.inter;

import com.blue.portal.api.model.BulletinInfo;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * portal service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface PortalService {

    /**
     * list bulletin infos
     *
     * @param bulletinType
     * @return
     */
    Mono<List<BulletinInfo>> listBulletinInfo(String bulletinType);

}
