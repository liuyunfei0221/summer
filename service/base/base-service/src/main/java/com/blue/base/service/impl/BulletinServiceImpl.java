package com.blue.base.service.impl;

import com.blue.base.constant.base.Status;
import com.blue.base.constant.portal.BulletinType;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.Bulletin;
import com.blue.base.repository.mapper.BulletinMapper;
import com.blue.base.service.inter.BulletinService;
import org.springframework.stereotype.Service;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.List;

import static com.blue.base.common.base.Asserter.isNull;
import static com.blue.base.constant.base.BlueNumericalValue.ROWS;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;

/**
 * bulletin service
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
     * select bulletins
     *
     * @param bulletinType
     * @return
     */
    @Override
    public List<Bulletin> selectBulletin(BulletinType bulletinType) {
        LOGGER.info("listBulletin(BulletinType bulletinType), bulletinType = {}", bulletinType);
        if (isNull(bulletinType))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        List<Bulletin> bulletins = bulletinMapper.selectBulletin(bulletinType.identity, Status.VALID.status, ROWS.value);
        LOGGER.info("bulletins = {}", bulletins);

        return bulletins;
    }

}
