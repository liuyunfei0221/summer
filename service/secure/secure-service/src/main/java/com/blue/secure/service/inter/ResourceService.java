package com.blue.secure.service.inter;

import com.blue.secure.repository.entity.Resource;

import java.util.List;

/**
 * resource service interface
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface ResourceService {

    /**
     * select all resources
     *
     * @return
     */
    List<Resource> selectResource();

    /**
     * select resources by ids
     *
     * @param ids
     * @return
     */
    List<Resource> selectResourceByIds(List<Long> ids);

    /**
     * select resources by role id
     *
     * @param roleId
     * @return
     */
    List<Resource> selectResourceByRoleId(Long roleId);

}
