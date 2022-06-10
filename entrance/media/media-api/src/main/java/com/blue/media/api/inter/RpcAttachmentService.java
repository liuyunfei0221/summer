package com.blue.media.api.inter;

import com.blue.media.api.model.AttachmentInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * rpc attachment interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcAttachmentService {

    /**
     * get attachment by id
     *
     * @param id
     * @return
     */
    CompletableFuture<AttachmentInfo> getAttachmentInfoMonoByPrimaryKey(Long id);

    /**
     * select attachment by ids
     *
     * @param ids
     * @return
     */
    CompletableFuture<List<AttachmentInfo>> selectAttachmentInfoMonoByIds(List<Long> ids);

}
