package com.blue.secure.service.inter;

/**
 * Business interface used to configure role-resource permissions and user-role associations
 *
 * @author liuyunfei
 * @date 2021/10/14
 * @apiNote
 */
public interface RelationService {

    /**
     * refresh resource key/info or role-resource-relation
     */
    void refreshSystemAuthorityInfos();

}
