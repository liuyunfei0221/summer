package com.blue.base.service.inter;

import com.blue.base.api.model.BulletinInfo;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * test
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface PortalService {

    /**
     * test
     *
     * @param bulletinType
     * @return
     */
    Mono<List<BulletinInfo>> selectBulletin(String bulletinType);

}
