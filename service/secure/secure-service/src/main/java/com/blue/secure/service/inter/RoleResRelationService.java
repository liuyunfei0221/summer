package com.blue.secure.service.inter;

import com.blue.secure.repository.entity.RoleResRelation;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * role resource relation service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface RoleResRelationService {

    /**
     * select all role resource relation
     *
     * @return
     */
    List<RoleResRelation> selectRoleResRelation();

    /**
     * select resources ids by role id
     *
     * @param roleId
     * @return
     */
    Mono<List<Long>> selectResourceIdsMonoByRoleId(Long roleId);

}
