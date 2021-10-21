package com.blue.secure.service.impl;

import com.blue.secure.event.producer.SystemAuthorityInfosRefreshProducer;
import com.blue.secure.service.inter.*;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;

/**
 * Business interface impl used to configure role-resource permissions and user-role associations
 *
 * @author liuyunfei
 * @date 2021/10/14
 * @apiNote
 */
@Service
public class RelationServiceImpl implements RelationService {

    private static final Logger LOGGER = getLogger(RelationServiceImpl.class);

    private final ResourceService resourceService;

    private final RoleService roleService;

    private final MemberService memberService;

    private final RoleResRelationService roleResRelationService;

    private final MemberRoleRelationService memberRoleRelationService;

    private final SecureService secureService;

    private final SystemAuthorityInfosRefreshProducer systemAuthorityInfosRefreshProducer;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")

    public RelationServiceImpl(ResourceService resourceService, RoleService roleService, MemberService memberService, RoleResRelationService roleResRelationService,
                               MemberRoleRelationService memberRoleRelationService, SecureService secureService, SystemAuthorityInfosRefreshProducer systemAuthorityInfosRefreshProducer) {
        this.resourceService = resourceService;
        this.roleService = roleService;
        this.memberService = memberService;
        this.roleResRelationService = roleResRelationService;
        this.memberRoleRelationService = memberRoleRelationService;
        this.secureService = secureService;
        this.systemAuthorityInfosRefreshProducer = systemAuthorityInfosRefreshProducer;
    }

    /**
     * refresh resource key/info or role-resource-relation
     */
    @Override
    public void refreshSystemAuthorityInfos() {
        LOGGER.info("void refreshSystemAuthorityInfos()");
        secureService.refreshSystemAuthorityInfos();
    }
}
