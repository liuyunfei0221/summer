package com.blue.media.remote.provider;

import com.blue.base.model.exps.BlueException;
import com.blue.media.api.inter.RpcAttachmentService;
import com.blue.media.api.model.AttachmentInfo;
import com.blue.media.service.inter.AttachmentService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.blue.base.constant.common.ResponseElement.DATA_NOT_EXIST;
import static reactor.core.publisher.Mono.*;

/**
 * rpc attachment provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcAttachmentService.class,
        version = "1.0",
        methods = {
                @Method(name = "getAttachmentInfoMonoByPrimaryKey", async = true),
                @Method(name = "selectAttachmentInfoMonoByIds", async = true)
        })
public class RpcAttachmentServiceProvider implements RpcAttachmentService {

    private final AttachmentService attachmentService;

    private final Scheduler scheduler;

    public RpcAttachmentServiceProvider(AttachmentService attachmentService, Scheduler scheduler) {
        this.attachmentService = attachmentService;
        this.scheduler = scheduler;
    }

    /**
     * get attachment by id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<AttachmentInfo> getAttachmentInfoMonoByPrimaryKey(Long id) {
        return just(id).publishOn(scheduler).flatMap(attachmentService::getAttachmentInfoMono)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .toFuture();
    }

    /**
     * select attachment by ids
     *
     * @param ids
     * @return
     */
    @Override
    public CompletableFuture<List<AttachmentInfo>> selectAttachmentInfoMonoByIds(List<Long> ids) {
        return just(ids).publishOn(scheduler).flatMap(attachmentService::selectAttachmentInfoMonoByIds)
                .toFuture();
    }

}
