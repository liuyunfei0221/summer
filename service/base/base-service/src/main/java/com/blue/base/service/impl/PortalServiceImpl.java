package com.blue.base.service.impl;

import com.blue.base.api.model.BulletinInfo;
import com.blue.base.constant.portal.BulletinType;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.Bulletin;
import com.blue.base.service.inter.BulletinService;
import com.blue.base.service.inter.PortalService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static com.blue.base.common.base.Check.isBlank;
import static com.blue.base.common.base.ConstantProcessor.getBulletinTypeByIdentity;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static java.lang.Integer.parseInt;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.just;

/**
 * test impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class PortalServiceImpl implements PortalService {

    private static final Logger LOGGER = Loggers.getLogger(PortalServiceImpl.class);

    private final BulletinService bulletinService;

    public PortalServiceImpl(BulletinService bulletinService) {
        this.bulletinService = bulletinService;
    }

    private static final Function<List<Bulletin>, List<BulletinInfo>> VO_LIST_CONVERTER = bl ->
            bl != null && bl.size() > 0 ? bl.stream()
                    .map(b -> new BulletinInfo(b.getId(), b.getTitle(), b.getContent(), b.getLink(), b.getType()))
                    .collect(toList()) : emptyList();

    private static final Function<String, BulletinType> TYPE_CONVERTER = typeStr -> {
        if (isBlank(typeStr)) {
            throw new BlueException(BAD_REQUEST);
        }

        int type;
        try {
            type = parseInt(typeStr);
        } catch (NumberFormatException e) {
            throw new BlueException(BAD_REQUEST);
        }

        return getBulletinTypeByIdentity(type);
    };

    /**
     * select bulletin
     *
     * @param bulletinType
     * @return
     */
    @Override
    public Mono<List<BulletinInfo>> selectBulletin(String bulletinType) {
        LOGGER.info("Mono<List<BulletinInfo>> selectBulletin(String bulletinType), bulletinType = {}", bulletinType);

        List<BulletinInfo> vos = VO_LIST_CONVERTER.apply(ofNullable(bulletinService.selectBulletin(TYPE_CONVERTER.apply(bulletinType))).orElseGet(Collections::emptyList));
        LOGGER.info("vos = {}", vos);
        return just(vos);
    }

}
