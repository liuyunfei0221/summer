package com.blue.portal.service.inter;

import com.blue.base.constant.portal.BulletinType;
import com.blue.portal.repository.entity.Bulletin;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * bulletin service
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface BulletinService {

    /**
     * select all bulletins
     *
     * @return
     */
    Mono<List<Bulletin>> selectBulletin();

    /**
     * list active bulletins by type
     *
     * @param bulletinType
     * @return
     */
    List<Bulletin> selectActiveBulletinByType(BulletinType bulletinType);

}
