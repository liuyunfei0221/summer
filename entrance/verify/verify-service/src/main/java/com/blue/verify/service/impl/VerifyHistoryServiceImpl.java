package com.blue.verify.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.verify.api.model.VerifyHistoryInfo;
import com.blue.verify.constant.VerifyHistorySortAttribute;
import com.blue.verify.model.VerifyHistoryCondition;
import com.blue.verify.repository.entity.VerifyHistory;
import com.blue.verify.repository.template.VerifyHistoryRepository;
import com.blue.verify.service.inter.VerifyHistoryService;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.mongo.common.SortConverter.convert;
import static com.blue.verify.constant.ColumnName.CREATE_TIME;
import static com.blue.verify.converter.VerifyModelConverters.VERIFY_HISTORIES_2_VERIFY_HISTORY_INFOS_CONVERTER;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.domain.Sort.unsorted;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;
import static reactor.util.Loggers.getLogger;

/**
 * verify history service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class VerifyHistoryServiceImpl implements VerifyHistoryService {

    private static final Logger LOGGER = getLogger(VerifyHistoryServiceImpl.class);

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private final Scheduler scheduler;

    private final VerifyHistoryRepository verifyHistoryRepository;

    public VerifyHistoryServiceImpl(ReactiveMongoTemplate reactiveMongoTemplate, Scheduler scheduler, VerifyHistoryRepository verifyHistoryRepository) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.scheduler = scheduler;
        this.verifyHistoryRepository = verifyHistoryRepository;
    }

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(VerifyHistorySortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Function<VerifyHistoryCondition, Sort> SORTER_CONVERTER = condition -> {
        if (isNull(condition))
            return unsorted();

        String sortAttribute = condition.getSortAttribute();
        if (isBlank(sortAttribute)) {
            condition.setSortAttribute(VerifyHistorySortAttribute.ID.column);
        } else {
            if (!SORT_ATTRIBUTE_MAPPING.containsValue(sortAttribute))
                throw new BlueException(INVALID_PARAM);
        }

        return convert(condition.getSortType(), singletonList(condition.getSortAttribute()));
    };

    private static final Function<VerifyHistoryCondition, Query> CONDITION_PROCESSOR = condition -> {
        Query query = new Query();

        if (condition == null)
            return query;

        VerifyHistory probe = new VerifyHistory();

        ofNullable(condition.getId()).ifPresent(probe::setId);
        ofNullable(condition.getVerifyType()).filter(BlueChecker::isNotBlank).ifPresent(probe::setVerifyType);
        ofNullable(condition.getBusinessType()).filter(BlueChecker::isNotBlank).ifPresent(probe::setBusinessType);
        ofNullable(condition.getDestination()).filter(BlueChecker::isNotBlank).ifPresent(probe::setDestination);
        ofNullable(condition.getVerify()).filter(BlueChecker::isNotBlank).ifPresent(probe::setVerify);
        ofNullable(condition.getRequestIp()).filter(BlueChecker::isNotBlank).ifPresent(probe::setRequestIp);

        ofNullable(condition.getCreateTimeBegin()).ifPresent(createTimeBegin ->
                query.addCriteria(where(CREATE_TIME.name).gte(createTimeBegin)));
        ofNullable(condition.getCreateTimeEnd()).ifPresent(createTimeEnd ->
                query.addCriteria(where(CREATE_TIME.name).lte(createTimeEnd)));

        query.addCriteria(byExample(probe));

        query.with(SORTER_CONVERTER.apply(condition));

        return query;
    };

    /**
     * insert verify history
     *
     * @param verifyHistory
     * @return
     */
    @Override
    public Mono<VerifyHistory> insertVerifyHistory(VerifyHistory verifyHistory) {
        LOGGER.info("Mono<VerifyHistory> insertVerifyHistory(VerifyHistory verifyHistory), verifyHistory = {}", verifyHistory);
        return verifyHistoryRepository.insert(verifyHistory).publishOn(scheduler);
    }

    /**
     * get verify history by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<VerifyHistory> getVerifyHistory(Long id) {
        return ofNullable(this.getVerifyHistoryMono(id).toFuture().join());
    }

    /**
     * get verify history mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<VerifyHistory> getVerifyHistoryMono(Long id) {
        LOGGER.info("Mono<VerifyHistory> getVerifyHistoryMono(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return verifyHistoryRepository.findById(id).publishOn(scheduler);
    }

    /**
     * select verify history by page and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    @Override
    public Mono<List<VerifyHistory>> selectVerifyHistoryMonoByLimitAndQuery(Long limit, Long rows, Query query) {
        LOGGER.info("Mono<List<VerifyHistory>> selectVerifyHistoryMonoByLimitAndQuery(Long limit, Long rows, Query query), " +
                "limit = {}, rows = {}, query = {}", limit, rows, query);
        if (limit == null || limit < 0 || rows == null || rows == 0)
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, VerifyHistory.class).publishOn(scheduler).collectList();
    }

    /**
     * count verify history by query
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countVerifyHistoryMonoByQuery(Query query) {
        LOGGER.info("Mono<Long> countVerifyHistoryMonoByQuery(Query query), query = {}", query);
        return reactiveMongoTemplate.count(query, VerifyHistory.class).publishOn(scheduler);
    }

    /**
     * select verify history info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<VerifyHistoryInfo>> selectVerifyHistoryInfoPageMonoByPageAndCondition(PageModelRequest<VerifyHistoryCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<VerifyHistoryInfo>> selectVerifyHistoryInfoPageMonoByPageAndCondition(PageModelRequest<VerifyHistoryCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(selectVerifyHistoryMonoByLimitAndQuery(pageModelRequest.getLimit(), pageModelRequest.getRows(), query),
                countVerifyHistoryMonoByQuery(query)
        ).flatMap(tuple2 -> {
            List<VerifyHistory> verifyHistories = tuple2.getT1();

            return isNotEmpty(verifyHistories) ?
                    just(VERIFY_HISTORIES_2_VERIFY_HISTORY_INFOS_CONVERTER.apply(verifyHistories))
                            .flatMap(verifyHistoryInfos ->
                                    just(new PageModelResponse<>(verifyHistoryInfos, tuple2.getT2())))
                    :
                    just(new PageModelResponse<>(emptyList(), tuple2.getT2()));
        });
    }

}