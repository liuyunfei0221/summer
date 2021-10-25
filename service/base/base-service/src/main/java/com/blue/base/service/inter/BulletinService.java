package com.blue.base.service.inter;

import com.blue.base.constant.portal.BulletinType;
import com.blue.base.repository.entity.Bulletin;

import java.util.List;

/**
 * bulletin service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface BulletinService {

    /**
     * select bulletin
     *
     * @param bulletinType
     * @return
     */
    List<Bulletin> selectBulletin(BulletinType bulletinType);


}
