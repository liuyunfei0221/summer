package com.blue.portal.service.inter;

import com.blue.base.constant.portal.BulletinType;
import com.blue.portal.repository.entity.Bulletin;

import java.util.List;

/**
 * bulletin service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface BulletinService {

    /**
     * list active bulletins by type
     *
     * @param bulletinType
     * @return
     */
    List<Bulletin> listBulletin(BulletinType bulletinType);

}
