package com.blue.portal.service.inter;

import com.blue.base.constant.portal.BulletinType;
import com.blue.portal.repository.entity.Bulletin;

import java.util.List;

/**
 * 公告相关业务接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface BulletinService {

    /**
     * 获取公告信息
     *
     * @param bulletinType
     * @return
     */
    List<Bulletin> listBulletin(BulletinType bulletinType);


}
