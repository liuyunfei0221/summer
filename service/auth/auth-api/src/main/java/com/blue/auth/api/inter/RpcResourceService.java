package com.blue.auth.api.inter;

import com.blue.auth.api.model.ResourceInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * rpc resource interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcResourceService {

    /**
     * select all resource infos
     *
     * @return
     */
    CompletableFuture<List<ResourceInfo>> selectResourceInfo();

}
