package com.blue.member.remote.consumer;

import com.blue.media.api.inter.RpcAttachmentService;
import com.blue.media.api.model.AttachmentInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;

import static reactor.core.publisher.Mono.fromFuture;

/**
 * rpc attachment consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
public class RpcAttachmentServiceConsumer {

    @DubboReference(version = "1.0",
            providedBy = {"summer-media"},
            methods = {
                    @Method(name = "getAttachmentInfoMonoByPrimaryKey", async = true),
                    @Method(name = "selectAttachmentInfoMonoByIds", async = true)
            })
    private RpcAttachmentService rpcAttachmentService;

    private final Scheduler scheduler;

    public RpcAttachmentServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * get attachment by id
     *
     * @param id
     * @return
     */
    public Mono<AttachmentInfo> getAttachmentInfoMonoByPrimaryKey(Long id) {
        return fromFuture(rpcAttachmentService.getAttachmentInfoMonoByPrimaryKey(id)).subscribeOn(scheduler);
    }

    /**
     * select attachment by ids
     *
     * @param ids
     * @return
     */
    public Mono<List<AttachmentInfo>> selectAttachmentInfoMonoByIds(List<Long> ids) {
        return fromFuture(rpcAttachmentService.selectAttachmentInfoMonoByIds(ids)).subscribeOn(scheduler);
    }

}
