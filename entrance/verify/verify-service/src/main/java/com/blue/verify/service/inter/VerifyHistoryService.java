package com.blue.verify.service.inter;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.verify.api.model.VerifyHistoryInfo;
import com.blue.verify.model.VerifyHistoryCondition;
import com.blue.verify.repository.entity.VerifyHistory;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * verify history service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface VerifyHistoryService {

    /**
     * insert verify history
     *
     * @param verifyHistory
     * @return
     */
    Mono<VerifyHistory> insertVerifyHistory(VerifyHistory verifyHistory);

    /**
     * get verify history by id
     *
     * @param id
     * @return
     */
    Optional<VerifyHistory> getVerifyHistoryOpt(Long id);

    /**
     * get verify history mono by id
     *
     * @param id
     * @return
     */
    Mono<VerifyHistory> getVerifyHistory(Long id);

    /**
     * select verify history by page and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    Mono<List<VerifyHistory>> selectVerifyHistoryByLimitAndQuery(Long limit, Long rows, Query query);

    /**
     * count verify history by query
     *
     * @param query
     * @return
     */
    Mono<Long> countVerifyHistoryByQuery(Query query);

    /**
     * select verify history info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<VerifyHistoryInfo>> selectVerifyHistoryInfoPageByPageAndCondition(PageModelRequest<VerifyHistoryCondition> pageModelRequest);

}
