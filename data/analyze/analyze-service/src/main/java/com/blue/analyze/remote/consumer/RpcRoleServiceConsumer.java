package com.blue.analyze.remote.consumer;

import com.blue.secure.api.inter.RpcRoleService;
import com.blue.secure.api.model.RoleInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.List;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * rpc secure reference
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
public class RpcRoleServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcRoleServiceConsumer.class);

    @DubboReference(version = "1.0",
            providedBy = {"summer-secure"},
            methods = {
                    @Method(name = "selectRoleInfo", async = true, retries = 2)
            })
    private RpcRoleService rpcRoleService;

    private final Scheduler scheduler;

    public RpcRoleServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * select all role infos
     *
     * @return
     */
    public Mono<List<RoleInfo>> selectRoleInfo() {
        LOGGER.info("Mono<List<RoleInfo>> selectRoleInfo()");
        return fromFuture(rpcRoleService.selectRoleInfo()).publishOn(scheduler);
    }

}
