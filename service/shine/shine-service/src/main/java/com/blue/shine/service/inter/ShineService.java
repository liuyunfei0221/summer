package com.blue.shine.service.inter;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.basic.model.event.IdentityEvent;
import com.blue.es.model.PitCursor;
import com.blue.shine.api.model.ShineInfo;
import com.blue.shine.model.ShineCondition;
import com.blue.shine.model.ShineInsertParam;
import com.blue.shine.model.ShineUpdateParam;
import com.blue.shine.repository.entity.Shine;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * shine service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface ShineService {

    /**
     * insert shine
     *
     * @param shineInsertParam
     * @param memberId
     * @return
     */
    Mono<ShineInfo> insertShine(ShineInsertParam shineInsertParam, Long memberId);

    /**
     * insert shine event
     *
     * @param shine
     * @return
     */
    Mono<Boolean> insertShineEvent(Shine shine);

    /**
     * update exist shine
     *
     * @param shineUpdateParam
     * @param memberId
     * @return
     */
    Mono<ShineInfo> updateShine(ShineUpdateParam shineUpdateParam, Long memberId);

    /**
     * update shine event
     *
     * @param shine
     * @return
     */
    Mono<Boolean> updateShineEvent(Shine shine);

    /**
     * delete shine
     *
     * @param id
     * @return
     */
    Mono<ShineInfo> deleteShine(Long id);

    /**
     * delete shine event
     *
     * @param identityEvent
     * @return
     */
    Mono<Boolean> deleteShineEvent(IdentityEvent identityEvent);

    /**
     * query shine mono by id
     *
     * @param id
     * @return
     */
    Mono<Shine> getShineMono(Long id);

    /**
     * query shine info mono by id with assert
     *
     * @param id
     * @return
     */
    Mono<ShineInfo> getShineInfoMonoWithAssert(Long id);

    /**
     * select shine info by ids
     *
     * @param ids
     * @return
     */
    Mono<List<ShineInfo>> selectShineInfoMonoByIds(List<Long> ids);

    /**
     * select shine info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<ShineInfo>> selectShineInfoPageMonoByPageAndCondition(PageModelRequest<ShineCondition> pageModelRequest);

    /**
     * select shine info scroll by cursor
     *
     * @param scrollModelRequest
     * @return
     */
    Mono<ScrollModelResponse<ShineInfo, String>> selectShineInfoScrollMonoByScrollAndCursor(ScrollModelRequest<ShineCondition, String> scrollModelRequest);

    /**
     * select shine info scroll by cursor with pit
     *
     * @param scrollModelRequest
     * @return
     */
    Mono<ScrollModelResponse<ShineInfo, PitCursor>> selectShineInfoScrollMonoByScrollAndCursorBaseOnSnapShot(ScrollModelRequest<ShineCondition, PitCursor> scrollModelRequest);

}
