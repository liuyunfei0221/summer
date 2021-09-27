package com.blue.base.service.inter;

import com.blue.base.constant.portal.BulletinType;
import com.blue.base.repository.entity.Bulletin;

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
