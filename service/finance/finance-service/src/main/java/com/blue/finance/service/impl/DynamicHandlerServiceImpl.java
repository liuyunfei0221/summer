package com.blue.finance.service.impl;

import com.blue.finance.repository.entity.DynamicHandler;
import com.blue.finance.repository.mapper.DynamicHandlerMapper;
import com.blue.finance.service.inter.DynamicHandlerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 动态处理器业务实现
 *
 * @author liuyunfei
 * @date 2021/9/14
 * @apiNote
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class DynamicHandlerServiceImpl implements DynamicHandlerService {

    private final DynamicHandlerMapper dynamicHandlerMapper;

    public DynamicHandlerServiceImpl(DynamicHandlerMapper dynamicHandlerMapper) {
        this.dynamicHandlerMapper = dynamicHandlerMapper;
    }

    @Override
    public List<DynamicHandler> listDynamicHandler() {
        return dynamicHandlerMapper.listDynamicHandler();
    }
}
