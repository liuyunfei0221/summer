package com.blue.secure.service.inter;

import com.blue.secure.repository.entity.Resource;

import java.util.List;

/**
 * 资源接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface ResourceService {

    /**
     * 获取全部资源
     *
     * @return
     */
    List<Resource> listResource();

    /**
     * 根据ids批量查询成员资源列表
     *
     * @param ids
     * @return
     */
    List<Resource> listResourceByIds(List<Long> ids);

    /**
     * 根据roleId查询所有的资源信息
     *
     * @param roleId
     * @return
     */
    List<Resource> listResourceByRoleId(Long roleId);

}
