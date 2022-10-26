package com.blue.finance.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.common.SortType;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.common.SortElement;
import com.blue.basic.model.exps.BlueException;
import com.blue.finance.api.model.FinanceFlowInfo;
import com.blue.finance.api.model.FinanceFlowManagerInfo;
import com.blue.finance.constant.FinanceFlowSortAttribute;
import com.blue.finance.event.producer.FinanceFlowProducer;
import com.blue.finance.model.FinanceFlowCondition;
import com.blue.finance.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.finance.repository.entity.FinanceFlow;
import com.blue.finance.repository.template.FinanceFlowRepository;
import com.blue.finance.service.inter.FinanceFlowService;
import com.blue.member.api.model.MemberBasicInfo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.finance.constant.FinanceFlowColumnName.*;
import static com.blue.finance.converter.FinanceModelConverters.FINANCE_FLOW_2_FINANCE_FLOW_INFO_CONVERTER;
import static com.blue.finance.converter.FinanceModelConverters.FINANCE_FLOW_2_FINANCE_FLOW_MANAGER_INFO_CONVERTER;
import static com.blue.mongo.common.MongoSortProcessor.process;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * finance flow service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "SpringJavaInjectionPointsAutowiringInspection", "AliControlFlowStatementWithoutBraces"})
@Service
public class FinanceFlowServiceImpl implements FinanceFlowService {

    private static final Logger LOGGER = getLogger(FinanceFlowServiceImpl.class);

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private final ExecutorService executorService;

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final FinanceFlowProducer financeFlowProducer;

    private final FinanceFlowRepository financeFlowRepository;

    public FinanceFlowServiceImpl(ReactiveMongoTemplate reactiveMongoTemplate, ExecutorService executorService, RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer,
                                  FinanceFlowProducer financeFlowProducer, FinanceFlowRepository financeFlowRepository) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.executorService = executorService;
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.financeFlowProducer = financeFlowProducer;
        this.financeFlowRepository = financeFlowRepository;
    }

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(FinanceFlowSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Function<FinanceFlowCondition, Sort> SORTER_CONVERTER = c -> {
        String sortAttribute = ofNullable(c).map(FinanceFlowCondition::getSortAttribute)
                .map(SORT_ATTRIBUTE_MAPPING::get)
                .filter(BlueChecker::isNotBlank)
                .orElse(FinanceFlowSortAttribute.CREATE_TIME.column);

        String sortType = ofNullable(c).map(FinanceFlowCondition::getSortType)
                .filter(BlueChecker::isNotBlank)
                .orElse(SortType.DESC.identity);

        return sortAttribute.equals(FinanceFlowSortAttribute.ID.column) ?
                process(singletonList(new SortElement(sortAttribute, sortType)))
                :
                process(Stream.of(sortAttribute, FinanceFlowSortAttribute.ID.column)
                        .map(attr -> new SortElement(attr, sortType)).collect(toList()));
    };

    private static final Function<FinanceFlowCondition, Query> CONDITION_PROCESSOR = c -> {
        Query query = new Query();

        if (c == null) {
            query.with(SORTER_CONVERTER.apply(new FinanceFlowCondition()));
            return query;
        }

        FinanceFlow probe = new FinanceFlow();

        ofNullable(c.getId()).ifPresent(probe::setId);
        ofNullable(c.getMemberId()).ifPresent(probe::setMemberId);
        ofNullable(c.getOrderId()).ifPresent(probe::setOrderId);
        ofNullable(c.getOrderNo()).ifPresent(probe::setOrderNo);
        ofNullable(c.getType()).ifPresent(probe::setType);
        ofNullable(c.getChangeType()).ifPresent(probe::setChangeType);
        ofNullable(c.getAmountChangedMin()).ifPresent(amountChangedMin ->
                query.addCriteria(where(AMOUNT_CHANGED.name).gte(amountChangedMin)));
        ofNullable(c.getAmountChangedMax()).ifPresent(amountChangedMax ->
                query.addCriteria(where(AMOUNT_CHANGED.name).lte(amountChangedMax)));
        ofNullable(c.getCreateTimeBegin()).ifPresent(createTimeBegin ->
                query.addCriteria(where(CREATE_TIME.name).gte(createTimeBegin)));
        ofNullable(c.getCreateTimeEnd()).ifPresent(createTimeEnd ->
                query.addCriteria(where(CREATE_TIME.name).lte(createTimeEnd)));

        query.addCriteria(byExample(probe));

        query.with(SORTER_CONVERTER.apply(c));

        return query;
    };

    /**
     * insert finance flow async
     *
     * @param financeFlow
     * @return
     */
    @Override
    public Mono<FinanceFlow> insertFinanceFlowAsync(FinanceFlow financeFlow) {
        LOGGER.info("Mono<FinanceFlow> insertFinanceFlowAsync(FinanceFlow financeFlow), financeFlow = {}", financeFlow);
        if (isNull(financeFlow))
            return error(() -> new BlueException(EMPTY_PARAM));

        return fromRunnable(() -> executorService.execute(() ->
                financeFlowProducer.send(financeFlow)))
                .then(just(financeFlow));
    }

    /**
     * insert finance flow
     *
     * @param financeFlow
     * @return
     */
    @Override
    public Mono<FinanceFlow> insertFinanceFlow(FinanceFlow financeFlow) {
        LOGGER.info("Mono<FinanceFlow> insertFinanceFlow(FinanceFlow financeFlow), financeFlow = {}", financeFlow);
        if (isNull(financeFlow))
            return error(() -> new BlueException(EMPTY_PARAM));

        return financeFlowRepository.insert(financeFlow);
    }

    /**
     * get finance flow mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<FinanceFlow> getFinanceFlowMono(Long id) {
        LOGGER.info("Mono<FinanceFlow> getFinanceFlowMono(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return financeFlowRepository.findById(id);
    }

    /**
     * get finance flow by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<FinanceFlow> getFinanceFlow(Long id) {
        LOGGER.info("Optional<FinanceFlow> getFinanceFlow(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(this.getFinanceFlowMono(id).toFuture().join());
    }

    /**
     * select finance flow by page and memberId
     *
     * @param limit
     * @param rows
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<FinanceFlow>> selectFinanceFlowMonoByLimitAndMemberId(Long limit, Long rows, Long memberId) {
        LOGGER.info("Mono<List<FinanceFlow>> selectFinanceFlowMonoByLimitAndMemberId(Long limit, Long rows, Long memberId), limit = {}, rows = {}, memberId = {}", limit, rows, memberId);
        if (isInvalidLimit(limit) || isInvalidRows(rows) || isInvalidIdentity(memberId))
            return error(() -> new BlueException(INVALID_PARAM));

        FinanceFlow probe = new FinanceFlow();
        probe.setMemberId(memberId);

        return financeFlowRepository.findAll(Example.of(probe), Sort.by(Sort.Order.desc(ID.name)))

                .skip(limit).take(rows)
                .collectList();
    }

    /**
     * count finance flow by memberId
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<Long> countFinanceFlowMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<Long> countFinanceFlowMonoByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            return error(() -> new BlueException(INVALID_PARAM));

        FinanceFlow probe = new FinanceFlow();
        probe.setMemberId(memberId);

        return financeFlowRepository.count(Example.of(probe));
    }

    /**
     * select finance flow info by page and member id
     *
     * @param pageModelRequest
     * @param memberId
     * @return
     */
    @Override
    public Mono<PageModelResponse<FinanceFlowInfo>> selectFinanceFlowInfoByPageAndMemberId(PageModelRequest<Void> pageModelRequest, Long memberId) {
        LOGGER.info("Mono<PageModelResponse<FinanceFlowInfo>> selectFinanceFlowInfoByPageAndMemberId(PageModelRequest<Void> pageModelRequest, Long memberId), pageModelDTO = {}, memberId = {}",
                pageModelRequest, memberId);
        if (isNull(pageModelRequest))
            return error(() -> new BlueException(EMPTY_PARAM));
        if (isInvalidIdentity(memberId))
            return error(() -> new BlueException(INVALID_IDENTITY));

        return zip(
                selectFinanceFlowMonoByLimitAndMemberId(pageModelRequest.getLimit(), pageModelRequest.getRows(), memberId),
                countFinanceFlowMonoByMemberId(memberId)
        ).flatMap(tuple2 -> {
            List<FinanceFlow> financeFlows = tuple2.getT1();
            Long count = tuple2.getT2();
            return isNotEmpty(financeFlows) ?
                    just(financeFlows.stream().map(FINANCE_FLOW_2_FINANCE_FLOW_INFO_CONVERTER)
                            .collect(toList()))
                            .flatMap(financeFlowInfos ->
                                    just(new PageModelResponse<>(financeFlowInfos, count)))
                    :
                    just(new PageModelResponse<>(emptyList(), count));
        });
    }

    /**
     * select finance flow by page and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    @Override
    public Mono<List<FinanceFlow>> selectFinanceFlowMonoByLimitAndQuery(Long limit, Long rows, Query query) {
        LOGGER.info("Mono<List<FinanceFlow>> selectFinanceFlowMonoByLimitAndQuery(Long limit, Long rows, Query query), " +
                "limit = {}, rows = {}, query = {}", limit, rows, query);
        if (limit == null || limit < 0 || rows == null || rows == 0)
            return error(() -> new BlueException(INVALID_PARAM));

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, FinanceFlow.class).collectList();
    }

    /**
     * count finance flow by query
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countFinanceFlowMonoByQuery(Query query) {
        LOGGER.info("Mono<Long> countFinanceFlowMonoByQuery(Query query), query = {}", query);
        return reactiveMongoTemplate.count(query, FinanceFlow.class);
    }

    /**
     * select finance flow info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<FinanceFlowManagerInfo>> selectFinanceFlowManagerInfoPageMonoByPageAndCondition(PageModelRequest<FinanceFlowCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<FinanceFlowManagerInfo>> selectFinanceFlowManagerInfoPageMonoByPageAndCondition(PageModelRequest<FinanceFlowCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            return error(() -> new BlueException(EMPTY_PARAM));

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectFinanceFlowMonoByLimitAndQuery(pageModelRequest.getLimit(), pageModelRequest.getRows(), query),
                countFinanceFlowMonoByQuery(query)
        ).flatMap(tuple2 -> {
            List<FinanceFlow> financeFlows = tuple2.getT1();
            Long count = tuple2.getT2();
            return isNotEmpty(financeFlows) ?
                    rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(financeFlows.stream().map(FinanceFlow::getMemberId).collect(toList()))
                            .flatMap(memberBasicInfos -> {
                                Map<Long, String> idAndMemberNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                return just(financeFlows.stream().map(ff ->
                                        FINANCE_FLOW_2_FINANCE_FLOW_MANAGER_INFO_CONVERTER.apply(ff, idAndMemberNameMapping)).collect(toList()));
                            }).flatMap(financeFlowManagerInfos ->
                                    just(new PageModelResponse<>(financeFlowManagerInfos, count)))
                    :
                    just(new PageModelResponse<>(emptyList(), count));
        });
    }

}
