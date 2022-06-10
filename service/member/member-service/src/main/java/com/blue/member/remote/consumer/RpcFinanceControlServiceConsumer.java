package com.blue.member.remote.consumer;

import com.blue.finance.api.inter.RpcFinanceControlService;
import com.blue.finance.api.model.MemberFinanceInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;

/**
 * rpc finance consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam", "FieldCanBeLocal"})
@Component
public class RpcFinanceControlServiceConsumer {

    @DubboReference(version = "1.0",
            providedBy = {"summer-finance"},
            methods = {
                    @Method(name = "initMemberFinanceInfo", async = false)
            })
    private RpcFinanceControlService rpcFinanceControlService;

    public RpcFinanceControlServiceConsumer() {
    }

    /**
     * init finance info for member
     *
     * @param memberFinanceInfo
     */
    public void initMemberFinanceInfo(MemberFinanceInfo memberFinanceInfo) {
        rpcFinanceControlService.initMemberFinanceInfo(memberFinanceInfo);
    }

}
