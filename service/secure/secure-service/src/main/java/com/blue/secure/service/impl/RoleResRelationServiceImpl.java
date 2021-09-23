package com.blue.secure.service.impl;

import com.blue.secure.repository.entity.RoleResRelation;
import com.blue.secure.repository.mapper.RoleResRelationMapper;
import com.blue.secure.service.inter.RoleResRelationService;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import java.util.List;

import static reactor.util.Loggers.getLogger;


/**
 * 角色资源关联实现
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Service
public class RoleResRelationServiceImpl implements RoleResRelationService {

    private static final Logger LOGGER = getLogger(RoleResRelationServiceImpl.class);

    private final RoleResRelationMapper roleResRelationMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RoleResRelationServiceImpl(RoleResRelationMapper roleResRelationMapper) {
        this.roleResRelationMapper = roleResRelationMapper;
    }

    /**
     * 获取全部资源角色关联
     *
     * @return
     */
    @Override
    public List<RoleResRelation> listRoleResRelation() {
        LOGGER.info("listRoleResRelation()");
        return roleResRelationMapper.listRoleResRelation();
    }

    /**
     * 根据角色id查询角色对应的资源id
     *
     * @return
     */
    @Override
    public List<Long> listResourceIdsByRoleId(Long roleId) {
        LOGGER.info("listResourceIdsByRoleId(Long roleId), roleId = {}", roleId);
        return roleResRelationMapper.listResIdsByRoleId(roleId);
    }
}
