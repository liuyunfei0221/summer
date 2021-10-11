package com.blue.finance.service.inter;

import com.blue.finance.repository.entity.DynamicResource;

import java.util.List;

/**
 * dynamic resource service
 *
 * @author liuyunfei
 * @date 2021/9/13
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public interface DynamicResourceService {

    /**
     * list all dynamic resources
     *
     * @return
     */
    List<DynamicResource> listDynamicResource();

}
