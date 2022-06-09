package com.blue.media.service.inter;

import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.media.api.model.DownloadHistoryInfo;
import com.blue.media.model.DownloadHistoryCondition;
import com.blue.media.repository.entity.DownloadHistory;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

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
     * get download history by id
     *
     * @param id
     * @return
     */
    Optional<DownloadHistory> getDownloadHistory(Long id);

    /**
     * get download history mono by id
     *
     * @param id
     * @return
     */
    Mono<DownloadHistory> getDownloadHistoryMono(Long id);

    /**
     * select download history by page and memberId
     *
     * @param limit
     * @param rows
     * @param memberId
     * @return
     */
    Mono<List<DownloadHistory>> selectDownloadHistoryMonoByLimitAndMemberId(Long limit, Long rows, Long memberId);

    /**
     * count download history by memberId
     *
     * @param memberId
     * @return
     */
    Mono<Long> countDownloadHistoryMonoByMemberId(Long memberId);

    /**
     * select download history info by page and member id
     *
     * @param pageModelRequest
     * @param memberId
     * @return
     */
    Mono<PageModelResponse<DownloadHistoryInfo>> selectDownloadHistoryInfoByPageAndMemberId(PageModelRequest<Void> pageModelRequest, Long memberId);

    /**
     * select download history by page and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    Mono<List<DownloadHistory>> selectDownloadHistoryMonoByLimitAndQuery(Long limit, Long rows, Query query);

    /**
     * count download history by query
     *
     * @param query
     * @return
     */
    Mono<Long> countDownloadHistoryMonoByQuery(Query query);

    /**
     * select download history info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<DownloadHistoryInfo>> selectDownloadHistoryInfoPageMonoByPageAndCondition(PageModelRequest<DownloadHistoryCondition> pageModelRequest);

}
