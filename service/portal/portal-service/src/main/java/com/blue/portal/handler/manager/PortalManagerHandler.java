package com.blue.portal.handler.manager;

import com.blue.portal.service.inter.PortalService;
import org.springframework.stereotype.Component;
import reactor.util.Logger;
import reactor.util.Loggers;

/**
 * @author liuyunfei
 * @date 2021/8/31
 * @apiNote
 */
@Component
public class PortalManagerHandler {

    private static final Logger LOGGER = Loggers.getLogger(PortalManagerHandler.class);

    private final PortalService portalService;

    public PortalManagerHandler(PortalService portalService) {
        this.portalService = portalService;
    }



}
