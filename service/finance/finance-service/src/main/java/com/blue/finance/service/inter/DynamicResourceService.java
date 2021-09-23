package com.blue.finance.service.inter;

import com.blue.finance.repository.entity.DynamicResource;

import java.util.List;

/**
 * 动态资源业务接口
 *
 * @author liuyunfei
 * @date 2021/9/13
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public interface DynamicResourceService {

    /**
     * 获取全部动态资源
     *
     * @return
     */
    List<DynamicResource> listDynamicResource();

}
