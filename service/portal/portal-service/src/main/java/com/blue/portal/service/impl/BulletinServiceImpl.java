package com.blue.portal.service.impl;

import com.blue.base.constant.base.Status;
import com.blue.base.constant.portal.BulletinType;
import com.blue.base.model.exps.BlueException;
import com.blue.portal.repository.entity.Bulletin;
import com.blue.portal.repository.mapper.BulletinMapper;
import com.blue.portal.service.inter.BulletinService;
import org.springframework.stereotype.Service;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.List;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ThresholdNumericalValue.ROWS;

/**
 * 公告相关业务实现
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
@Service
public class BulletinServiceImpl implements BulletinService {

    private static final Logger LOGGER = Loggers.getLogger(BulletinServiceImpl.class);

    private final BulletinMapper bulletinMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public BulletinServiceImpl(BulletinMapper bulletinMapper) {
        this.bulletinMapper = bulletinMapper;
    }

    /**
     * 获取公告信息
     *
     * @param bulletinType
     * @return
     */
    @Override
    public List<Bulletin> listBulletin(BulletinType bulletinType) {
        LOGGER.info("listBulletin(BulletinType bulletinType), bulletinType = {}", bulletinType);
        if (bulletinType == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "公告类型不能为空");

        List<Bulletin> bulletins = bulletinMapper.listBulletin(bulletinType.identity, Status.VALID.status, ROWS.value);
        LOGGER.info("bulletins = {}", bulletins);

        return bulletins;
    }

}
