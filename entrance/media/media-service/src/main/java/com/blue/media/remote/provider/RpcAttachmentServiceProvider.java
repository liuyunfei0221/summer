package com.blue.media.remote.provider;

import com.blue.media.api.inter.RpcAttachmentService;
import com.blue.media.api.model.AttachmentInfo;
import com.blue.media.service.inter.AttachmentService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * rpc attachment provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcAttachmentService.class,
        version = "1.0",
        methods = {
                @Method(name = "getAttachmentInfoByPrimaryKey", async = true),
                @Method(name = "selectAttachmentInfoByIds", async = true)
        })
public class RpcAttachmentServiceProvider implements RpcAttachmentService {

    private final AttachmentService attachmentService;

    public RpcAttachmentServiceProvider(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    /**
     * get attachment by id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<AttachmentInfo> getAttachmentInfoByPrimaryKey(Long id) {
        return attachmentService.getAttachmentInfo(id).toFuture();
    }

    /**
     * select attachment by ids
     *
     * @param ids
     * @return
     */
    @Override
    public CompletableFuture<List<AttachmentInfo>> selectAttachmentInfoByIds(List<Long> ids) {
        return attachmentService.selectAttachmentInfoByIds(ids).toFuture();
    }

}
