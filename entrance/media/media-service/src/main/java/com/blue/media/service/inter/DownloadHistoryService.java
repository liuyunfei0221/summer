package com.blue.media.service.inter;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.media.api.model.DownloadHistoryInfo;
import com.blue.media.model.DownloadHistoryCondition;
import com.blue.media.repository.entity.DownloadHistory;
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
    Mono<DownloadHistory> insert(DownloadHistory downloadHistory);

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
    Mono<Optional<DownloadHistory>> getDownloadHistoryMono(Long id);

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
     * select download history by page and condition
     *
     * @param limit
     * @param rows
     * @param downloadHistoryCondition
     * @return
     */
    Mono<List<DownloadHistory>> selectDownloadHistoryMonoByLimitAndCondition(Long limit, Long rows, DownloadHistoryCondition downloadHistoryCondition);

    /**
     * count download history by condition
     *
     * @param downloadHistoryCondition
     * @return
     */
    Mono<Long> countDownloadHistoryMonoByCondition(DownloadHistoryCondition downloadHistoryCondition);

    /**
     * select download history info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<DownloadHistoryInfo>> selectDownloadHistoryInfoPageMonoByPageAndCondition(PageModelRequest<DownloadHistoryCondition> pageModelRequest);

}
