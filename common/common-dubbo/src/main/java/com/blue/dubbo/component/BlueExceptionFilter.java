package com.blue.dubbo.component;

import com.blue.basic.model.exps.BlueException;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;

import static org.apache.dubbo.registry.Constants.CONSUMER_PROTOCOL;
import static org.apache.dubbo.registry.Constants.PROVIDER_PROTOCOL;
import static org.apache.dubbo.rpc.RpcContext.getServiceContext;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * dubbo exception filter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaClassNamingShouldBeCamel", "unused", "AlibabaUndefineMagicConstant", "AliControlFlowStatementWithoutBraces"})
@Activate(group = {PROVIDER_PROTOCOL, CONSUMER_PROTOCOL})
public final class BlueExceptionFilter implements Filter, Filter.Listener {

    private static final Logger LOGGER = getLogger(BlueExceptionFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }

    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        if (appResponse.hasException() && GenericService.class != invoker.getInterface()) {
            Throwable exception = appResponse.getException();
            LOGGER.error("dubbo catch exception, exception = {}, message = {}", exception, exception.getMessage());
            if (exception instanceof BlueException)
                return;

            appResponse.setException(exception);
        }
    }

    @Override
    public void onError(Throwable e, Invoker<?> invoker, Invocation invocation) {
        LOGGER.error("Got unchecked and undeclared exception which called by " + getServiceContext().getRemoteHost() + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
    }

}