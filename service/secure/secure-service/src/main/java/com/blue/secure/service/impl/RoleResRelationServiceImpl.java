package com.blue.secure.service.impl;

import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.secure.repository.entity.RoleResRelation;
import com.blue.secure.repository.mapper.RoleResRelationMapper;
import com.blue.secure.service.inter.RoleResRelationService;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import java.util.List;

import static reactor.util.Loggers.getLogger;


/**
 * role resource relation service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Service
public class RoleResRelationServiceImpl implements RoleResRelationService {

    private static final Logger LOGGER = getLogger(RoleResRelationServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final RoleResRelationMapper roleResRelationMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RoleResRelationServiceImpl(BlueIdentityProcessor blueIdentityProcessor, RoleResRelationMapper roleResRelationMapper) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.roleResRelationMapper = roleResRelationMapper;
    }

    /**
     * select all role resource relation
     *
     * @return
     */
    @Override
    public List<RoleResRelation> selectRoleResRelation() {
        LOGGER.info("listRoleResRelation()");
        return roleResRelationMapper.selectRoleResRelation();
    }

    /**
     * select resources ids by role id
     *
     * @return
     */
    @Override
    public List<Long> selectResourceIdsByRoleId(Long roleId) {
        LOGGER.info("listResourceIdsByRoleId(Long roleId), roleId = {}", roleId);
        return roleResRelationMapper.selectResIdsByRoleId(roleId);
    }
}
