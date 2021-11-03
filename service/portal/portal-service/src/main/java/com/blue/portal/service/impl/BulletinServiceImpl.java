package com.blue.portal.service.impl;

import com.blue.base.constant.base.Status;
import com.blue.base.constant.portal.BulletinType;
import com.blue.portal.repository.entity.Bulletin;
import com.blue.portal.repository.mapper.BulletinMapper;
import com.blue.portal.service.inter.BulletinService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.List;

import static com.blue.base.constant.base.BlueNumericalValue.ROWS;
import static com.blue.base.constant.base.CommonException.INVALID_IDENTITY_EXP;
import static reactor.core.publisher.Mono.just;

/**
 * bulletin service impl
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
     * select all bulletins
     *
     * @return
     */
    @Override
    public Mono<List<Bulletin>> selectBulletin() {
        LOGGER.info("Mono<List<Bulletin>> selectBulletin()");
        return just(bulletinMapper.select());
    }

    /**
     * list active bulletins by type
     *
     * @param bulletinType
     * @return
     */
    @Override
    public List<Bulletin> selectTargetActiveBulletinByType(BulletinType bulletinType) {
        LOGGER.info("List<Bulletin> selectActiveBulletinByType(BulletinType bulletinType), bulletinType = {}", bulletinType);
        if (bulletinType == null)
            throw INVALID_IDENTITY_EXP.exp;

        List<Bulletin> bulletins = bulletinMapper.selectByRowsAndCondition(bulletinType.identity, Status.VALID.status, ROWS.value);
        LOGGER.info("List<Bulletin> selectActiveBulletinByType(BulletinType bulletinType), bulletins = {}", bulletins);

        return bulletins;
    }

}
