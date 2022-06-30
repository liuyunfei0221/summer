package com.blue.risk.remote.consumer;

import com.blue.auth.api.inter.RpcRoleService;
import com.blue.auth.api.model.RoleInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;

import static reactor.core.publisher.Mono.fromFuture;

/**
 * rpc role reference
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Component
public class RpcRoleServiceConsumer {

    @DubboReference(version = "1.0",
            providedBy = {"summer-auth"},
            methods = {
                    @Method(name = "selectRoleInfo", async = true, retries = 2)
            })
    private RpcRoleService rpcRoleService;

    private final Scheduler scheduler;

    public RpcRoleServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * select all role info
     *
     * @return
     */
    public Mono<List<RoleInfo>> selectRoleInfo() {
        return fromFuture(rpcRoleService.selectRoleInfo()).subscribeOn(scheduler);
    }

}
