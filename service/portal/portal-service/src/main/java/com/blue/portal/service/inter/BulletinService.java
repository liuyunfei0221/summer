package com.blue.portal.service.inter;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.portal.api.model.BulletinInfo;
import com.blue.portal.api.model.BulletinManagerInfo;
import com.blue.portal.model.BulletinCondition;
import com.blue.portal.model.BulletinInsertParam;
import com.blue.portal.model.BulletinUpdateParam;
import com.blue.portal.repository.entity.Bulletin;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * bulletin service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface BulletinService {

    /**
     * insert bulletin
     *
     * @param bulletinInsertParam
     * @param operatorId
     * @return
     */
    BulletinInfo insertBulletin(BulletinInsertParam bulletinInsertParam, Long operatorId);

    /**
     * update bulletin
     *
     * @param bulletinUpdateParam
     * @param operatorId
     * @return
     */
    BulletinInfo updateBulletin(BulletinUpdateParam bulletinUpdateParam, Long operatorId);

    /**
     * delete bulletin
     *
     * @param id
     * @return
     */
    BulletinInfo deleteBulletin(Long id);

    /**
     * expire bulletin info
     *
     * @return
     */
    void invalidBulletinInfosCache();

    /**
     * get bulletin mono by id
     *
     * @param id
     * @return
     */
    Mono<Bulletin> getBulletin(Long id);

    /**
     * select all bulletin
     *
     * @return
     */
    Mono<List<Bulletin>> selectBulletin();

    /**
     * list active bulletin by type
     *
     * @param bulletinType
     * @return
     */
    List<Bulletin> selectActiveBulletinByType(Integer bulletinType);

    /**
     * list bulletin info
     *
     * @param bulletinType
     * @return
     */
    Mono<List<BulletinInfo>> selectActiveBulletinInfoByTypeWithCache(Integer bulletinType);

    /**
     * select bulletin by page and condition
     *
     * @param limit
     * @param rows
     * @param bulletinCondition
     * @return
     */
    Mono<List<Bulletin>> selectBulletinByLimitAndCondition(Long limit, Long rows, BulletinCondition bulletinCondition);

    /**
     * count bulletin by condition
     *
     * @param bulletinCondition
     * @return
     */
    Mono<Long> countBulletinByCondition(BulletinCondition bulletinCondition);

    /**
     * select bulletin info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<BulletinManagerInfo>> selectBulletinManagerInfoPageByPageAndCondition(PageModelRequest<BulletinCondition> pageModelRequest);

}
