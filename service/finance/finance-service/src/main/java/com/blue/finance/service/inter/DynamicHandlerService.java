package com.blue.finance.service.inter;

import com.blue.finance.repository.entity.DynamicHandler;

import java.util.List;

/**
 * dynamic handler service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface DynamicHandlerService {

    /**
     * list all dynamic handlers
     *
     * @return
     */
    List<DynamicHandler> selectDynamicHandler();

}
