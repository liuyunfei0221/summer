package com.blue.secure.service.impl;

import com.blue.secure.repository.entity.Resource;
import com.blue.secure.repository.mapper.ResourceMapper;
import com.blue.secure.service.inter.ResourceService;
import com.blue.secure.service.inter.RoleResRelationService;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.util.Loggers.getLogger;


/**
 * 资源实现
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class ResourceServiceImpl implements ResourceService {

    private static final Logger LOGGER = getLogger(ResourceServiceImpl.class);

    private final ResourceMapper resourceMapper;

    private final RoleResRelationService roleResRelationService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ResourceServiceImpl(ResourceMapper resourceMapper, RoleResRelationService roleResRelationService) {
        this.resourceMapper = resourceMapper;
        this.roleResRelationService = roleResRelationService;
    }

    /**
     * 获取全部资源
     *
     * @return
     */
    @Override
    public List<Resource> listResource() {
        LOGGER.info("listResource()");
        return resourceMapper.listResource();
    }

    /**
     * 根据ids批量查询成员资源列表
     *
     * @param ids
     * @return
     */
    @Override
    public List<Resource> listResourceByIds(List<Long> ids) {
        LOGGER.info("listResourceByIds(List<Long> ids), ids = {}", ids);
        return resourceMapper.listResourceByIds(ids);
    }

    /**
     * 根据roleId查询所有的资源信息
     *
     * @param roleId
     * @return
     */
    @Override
    public List<Resource> listResourceByRoleId(Long roleId) {
        LOGGER.info("listResourceByRoleId(Long roleId), roleId = {}", roleId);

        List<Long> ids = roleResRelationService.listResourceIdsByRoleId(roleId);

        if (isEmpty(ids))
            return emptyList();

        return resourceMapper.listResourceByIds(ids);
    }
}
