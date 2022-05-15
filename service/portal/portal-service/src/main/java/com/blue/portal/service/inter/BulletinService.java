package com.blue.portal.service.inter;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.portal.api.model.BulletinInfo;
import com.blue.portal.api.model.BulletinManagerInfo;
import com.blue.portal.model.BulletinCondition;
import com.blue.portal.repository.entity.Bulletin;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

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
     * @param bulletin
     * @return
     */
    int insertBulletin(Bulletin bulletin);

    /**
     * expire bulletin infos
     *
     * @return
     */
    void invalidBulletinInfosCache();

    /**
     * get bulletin by id
     *
     * @param id
     * @return
     */
    Optional<Bulletin> getBulletin(Long id);

    /**
     * get bulletin mono by id
     *
     * @param id
     * @return
     */
    Mono<Optional<Bulletin>> getBulletinMono(Long id);

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
     * list bulletin infos
     *
     * @param bulletinType
     * @return
     */
    Mono<List<BulletinInfo>> selectBulletinInfoMonoByType(Integer bulletinType);

    /**
     * select bulletin by page and condition
     *
     * @param limit
     * @param rows
     * @param bulletinCondition
     * @return
     */
    Mono<List<Bulletin>> selectBulletinMonoByLimitAndCondition(Long limit, Long rows, BulletinCondition bulletinCondition);

    /**
     * count bulletin by condition
     *
     * @param bulletinCondition
     * @return
     */
    Mono<Long> countBulletinMonoByCondition(BulletinCondition bulletinCondition);

    /**
     * select bulletin info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<BulletinManagerInfo>> selectBulletinManagerInfoPageMonoByPageAndCondition(PageModelRequest<BulletinCondition> pageModelRequest);

}
