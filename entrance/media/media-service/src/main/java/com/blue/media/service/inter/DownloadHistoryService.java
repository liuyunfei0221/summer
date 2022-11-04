package com.blue.media.service.inter;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.media.api.model.DownloadHistoryInfo;
import com.blue.media.model.DownloadHistoryCondition;
import com.blue.media.repository.entity.DownloadHistory;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * download history service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface DownloadHistoryService {

    /**
     * insert download history
     *
     * @param downloadHistory
     * @return
     */
    Mono<DownloadHistory> insertDownloadHistory(DownloadHistory downloadHistory);

    /**
     * get download history mono by id
     *
     * @param id
     * @return
     */
    Mono<DownloadHistory> getDownloadHistory(Long id);

    /**
     * select attachment detail info by scroll and member id
     *
     * @param scrollModelRequest
     * @param memberId
     * @return
     */
    Mono<ScrollModelResponse<DownloadHistoryInfo, String>> selectShineInfoScrollByScrollAndCursorBaseOnMemberId(ScrollModelRequest<Void, Long> scrollModelRequest, Long memberId);

    /**
     * select download history by page and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    Mono<List<DownloadHistory>> selectDownloadHistoryByLimitAndQuery(Long limit, Long rows, Query query);

    /**
     * count download history by query
     *
     * @param query
     * @return
     */
    Mono<Long> countDownloadHistoryByQuery(Query query);

    /**
     * select download history info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<DownloadHistoryInfo>> selectDownloadHistoryInfoPageByPageAndCondition(PageModelRequest<DownloadHistoryCondition> pageModelRequest);

}
