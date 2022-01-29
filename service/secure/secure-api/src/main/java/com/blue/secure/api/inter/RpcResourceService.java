package com.blue.secure.api.inter;

import com.blue.secure.api.model.ResourceInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * rpc role provider
 *
 * @author DarkBlue
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
