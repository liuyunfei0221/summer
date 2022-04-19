package com.blue.finance.service.inter;

import com.blue.finance.repository.entity.DynamicResource;

import java.util.List;

/**
 * dynamic resource service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface DynamicResourceService {

    /**
     * list all dynamic resources
     *
     * @return
     */
    List<DynamicResource> selectDynamicResource();

}
