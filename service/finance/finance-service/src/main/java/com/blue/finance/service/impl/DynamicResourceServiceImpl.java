package com.blue.finance.service.impl;

import com.blue.finance.repository.entity.DynamicResource;
import com.blue.finance.repository.mapper.DynamicResourceMapper;
import com.blue.finance.service.inter.DynamicResourceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuyunfei
 * @date 2021/9/13
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class DynamicResourceServiceImpl implements DynamicResourceService {

    private final DynamicResourceMapper dynamicResourceMapper;

    public DynamicResourceServiceImpl(DynamicResourceMapper dynamicResourceMapper) {
        this.dynamicResourceMapper = dynamicResourceMapper;
    }

    /**
     * 获取全部动态资源
     *
     * @return
     */
    @Override
    public List<DynamicResource> listDynamicResource() {
        return dynamicResourceMapper.listDynamicResource();
    }
}
