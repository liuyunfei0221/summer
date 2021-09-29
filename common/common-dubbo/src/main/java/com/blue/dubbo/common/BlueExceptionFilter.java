package com.blue.dubbo.common;

import com.blue.base.model.exps.BlueException;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static org.apache.dubbo.common.logger.LoggerFactory.getLogger;
import static org.apache.dubbo.rpc.RpcContext.getServiceContext;

/**
 * dubbo exception filter
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaClassNamingShouldBeCamel", "unused", "AlibabaUndefineMagicConstant"})
@Activate(group = {"provider"})
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
            LOGGER.error("dubbo catch exception,exception = {}", exception);

            appResponse.setException(
                    exception instanceof BlueException ? exception :
                            new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message));
        }
    }

    @Override
    public void onError(Throwable e, Invoker<?> invoker, Invocation invocation) {
        LOGGER.error("Got unchecked and undeclared exception which called by " + getServiceContext().getRemoteHost() + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
    }

}