package com.blue.finance.service.impl;

import com.blue.finance.repository.entity.DynamicHandler;
import com.blue.finance.repository.mapper.DynamicHandlerMapper;
import com.blue.finance.service.inter.DynamicHandlerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * dynamic handler service impl
 *
 * @author liuyunfei
 * @date 2021/9/14
 * @apiNote
 */
@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "JavaDoc"})
@Service
public class DynamicHandlerServiceImpl implements DynamicHandlerService {

    private final DynamicHandlerMapper dynamicHandlerMapper;

    public DynamicHandlerServiceImpl(DynamicHandlerMapper dynamicHandlerMapper) {
        this.dynamicHandlerMapper = dynamicHandlerMapper;
    }

    /**
     * list all dynamic handlers
     *
     * @return
     */
    @Override
    public List<DynamicHandler> selectDynamicHandler() {
        return dynamicHandlerMapper.select();
    }

}
