package com.blue.finance.service.inter;

import com.blue.finance.repository.entity.DynamicHandler;

import java.util.List;

/**
 * 动态处理器业务接口
 *
 * @author liuyunfei
 * @date 2021/9/14
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public interface DynamicHandlerService {

    /**
     * 获取全部动态处理器
     *
     * @return
     */
    List<DynamicHandler> listDynamicHandler();

}
