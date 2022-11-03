package com.blue.auth.service.inter;

import com.blue.auth.repository.entity.RefreshInfo;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * refresh info service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RefreshInfoService {

    /**
     * insert refresh info
     *
     * @param refreshInfo
     * @return
     */
    Mono<RefreshInfo> insertRefreshInfo(RefreshInfo refreshInfo);

    /**
     * delete refresh info
     *
     * @param id
     * @return
     */
    Mono<Void> deleteRefreshInfo(String id);

    /**
     * delete refresh info
     *
     * @param refreshInfos
     * @return
     */
    Mono<Void> deleteRefreshInfos(List<RefreshInfo> refreshInfos);

    /**
     * get refresh info by id
     *
     * @param id
     * @return
     */
    Mono<RefreshInfo> getRefreshInfo(String id);

    /**
     * select refresh info mono by probe
     *
     * @param probe
     * @return
     */
    Mono<List<RefreshInfo>> selectRefreshInfoByProbe(RefreshInfo probe);

}
