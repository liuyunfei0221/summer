package com.blue.agreement.service.impl;

import com.blue.agreement.api.model.AgreementInfo;
import com.blue.agreement.api.model.AgreementRecordInfo;
import com.blue.agreement.constant.AgreementRecordSortAttribute;
import com.blue.agreement.model.AgreementRecordCondition;
import com.blue.agreement.model.AgreementRecordInsertParam;
import com.blue.agreement.model.AgreementRecordManagerInfo;
import com.blue.agreement.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.agreement.repository.entity.AgreementRecord;
import com.blue.agreement.repository.template.AgreementRecordRepository;
import com.blue.agreement.service.inter.AgreementRecordService;
import com.blue.agreement.service.inter.AgreementService;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.common.SortType;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.common.SortElement;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.agreement.constant.AgreementRecordColumnName.*;
import static com.blue.agreement.converter.AgreementModelConverters.AGREEMENT_RECORD_2_AGREEMENT_RECORD_INFO;
import static com.blue.agreement.converter.AgreementModelConverters.agreementRecordToAgreementRecordManagerInfo;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.mongo.common.MongoSortProcessor.process;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.domain.Example.of;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;
import static reactor.util.Loggers.getLogger;

/**
 * agreement record service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class AgreementRecordServiceImpl implements AgreementRecordService {

    private static final Logger LOGGER = getLogger(AgreementRecordServiceImpl.class);

    private BlueIdentityProcessor blueIdentityProcessor;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final AgreementService agreementService;

    private AgreementRecordRepository agreementRecordRepository;

    public AgreementRecordServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ReactiveMongoTemplate reactiveMongoTemplate, RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer,
                                      AgreementService agreementService, AgreementRecordRepository agreementRecordRepository) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.agreementService = agreementService;
        this.agreementRecordRepository = agreementRecordRepository;
    }

    private final BiConsumer<AgreementRecordInsertParam, Long> INSERT_ITEM_VALIDATOR = (p, mid) -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(mid))
            throw new BlueException(UNAUTHORIZED);
        p.asserts();

        AgreementRecord probe = new AgreementRecord();
        probe.setMemberId(mid);
        probe.setAgreementId(p.getAgreementId());

        if (ofNullable(agreementRecordRepository.count(of(probe)).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(DATA_ALREADY_EXIST);
    };

    private final BiFunction<AgreementRecordInsertParam, Long, AgreementRecord> AGREEMENT_INSERT_PARAM_2_AGREEMENT_CONVERTER = (p, mid) -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(mid))
            throw new BlueException(UNAUTHORIZED);
        p.asserts();

        AgreementRecord agreementRecord = new AgreementRecord();

        agreementRecord.setId(blueIdentityProcessor.generate(AgreementRecord.class));
        agreementRecord.setMemberId(mid);
        agreementRecord.setAgreementId(p.getAgreementId());
        agreementRecord.setCreateTime(TIME_STAMP_GETTER.get());

        return agreementRecord;
    };

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(AgreementRecordSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Function<AgreementRecordCondition, Sort> SORTER_CONVERTER = c -> {
        String sortAttribute = ofNullable(c).map(AgreementRecordCondition::getSortAttribute)
                .map(SORT_ATTRIBUTE_MAPPING::get)
                .filter(BlueChecker::isNotBlank)
                .orElse(AgreementRecordSortAttribute.CREATE_TIME.column);

        String sortType = ofNullable(c).map(AgreementRecordCondition::getSortType)
                .filter(BlueChecker::isNotBlank)
                .orElse(SortType.DESC.identity);

        return sortAttribute.equals(AgreementRecordSortAttribute.ID.column) ?
                process(singletonList(new SortElement(sortAttribute, sortType)))
                :
                process(Stream.of(sortAttribute, AgreementRecordSortAttribute.ID.column)
                        .map(attr -> new SortElement(attr, sortType)).collect(toList()));
    };

    private static final Function<AgreementRecordCondition, Query> CONDITION_PROCESSOR = c -> {
        Query query = new Query();

        if (c == null) {
            query.with(SORTER_CONVERTER.apply(new AgreementRecordCondition()));
            return query;
        }

        AgreementRecord probe = new AgreementRecord();

        ofNullable(c.getId()).ifPresent(probe::setId);
        ofNullable(c.getMemberId()).ifPresent(probe::setMemberId);
        ofNullable(c.getAgreementId()).ifPresent(probe::setAgreementId);

        ofNullable(c.getCreateTimeBegin()).ifPresent(createTimeBegin ->
                query.addCriteria(where(CREATE_TIME.name).gte(createTimeBegin)));
        ofNullable(c.getCreateTimeEnd()).ifPresent(createTimeEnd ->
                query.addCriteria(where(CREATE_TIME.name).lte(createTimeEnd)));

        query.with(SORTER_CONVERTER.apply(c));

        return query;
    };

    /**
     * select agreement unsigned
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<AgreementInfo>> selectNewestAgreementInfosUnsigned(Long memberId) {
        LOGGER.info("memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        return agreementService.selectNewestAgreementInfosByAllTypeWithCache()
                .map(ais -> ais.stream().collect(toMap(AgreementInfo::getId, identity(), (a, b) -> a)))
                .flatMap(idAndAgreementInfoMapping ->
                        reactiveMongoTemplate.find(query(where(MEMBER_ID.name).is(memberId)
                                                .and(AGREEMENT_ID.name).in(idAndAgreementInfoMapping.keySet())),
                                        AgreementRecord.class)
                                .map(AgreementRecord::getAgreementId)
                                .collectList()
                                .map(HashSet::new)
                                .map(existAids -> idAndAgreementInfoMapping.keySet().stream()
                                        .filter(aid -> !existAids.contains(aid))
                                        .map(idAndAgreementInfoMapping::get).collect(toList()))
                );
    }

    /**
     * sign agreement record
     *
     * @param agreementRecordInsertParam
     * @param memberId
     * @return
     */
    @Override
    public Mono<AgreementRecordInfo> insertAgreementRecord(AgreementRecordInsertParam agreementRecordInsertParam, Long memberId) {
        LOGGER.info("agreementRecordInsertParam = {}, memberId = {}",
                agreementRecordInsertParam, memberId);
        if (isNull(agreementRecordInsertParam))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        INSERT_ITEM_VALIDATOR.accept(agreementRecordInsertParam, memberId);
        AgreementRecord agreementRecord = AGREEMENT_INSERT_PARAM_2_AGREEMENT_CONVERTER.apply(agreementRecordInsertParam, memberId);

        return agreementRecordRepository.insert(agreementRecord).map(AGREEMENT_RECORD_2_AGREEMENT_RECORD_INFO);
    }

    /**
     * select agreement record by limit and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    @Override
    public Mono<List<AgreementRecord>> selectAgreementRecordMonoByLimitAndCondition(Long limit, Long rows, Query query) {
        LOGGER.info("limit = {}, rows = {}, query = {}", limit, rows, query);
        if (isInvalidLimit(limit) || isInvalidRows(rows))
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, AgreementRecord.class).collectList();
    }

    /**
     * count agreement record by query
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countAgreementRecordMonoByCondition(Query query) {
        LOGGER.info("query = {}", query);

        return reactiveMongoTemplate.count(query, AgreementRecord.class);
    }

    /**
     * select agreement record manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<AgreementRecordManagerInfo>> selectAgreementRecordManagerInfoPageMonoByPageAndCondition(PageModelRequest<AgreementRecordCondition> pageModelRequest) {
        LOGGER.info("pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectAgreementRecordMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), query),
                countAgreementRecordMonoByCondition(query)
        ).flatMap(tuple2 -> {
            List<AgreementRecord> agreementRecords = tuple2.getT1();
            Long count = tuple2.getT2();
            return isNotEmpty(agreementRecords) ?
                    zip(rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(agreementRecords.stream().map(AgreementRecord::getMemberId).collect(toList()))
                                    .map(memberBasicInfos -> memberBasicInfos.stream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a)))
                            ,
                            agreementService.selectAgreementInfoByIds(agreementRecords.stream().map(AgreementRecord::getAgreementId).collect(toList()))
                                    .map(agreementInfos -> agreementInfos.stream().collect(toMap(AgreementInfo::getId, identity(), (a, b) -> a)))
                    ).flatMap(t2 -> {
                        Map<Long, String> memberIdAndNameMapping = t2.getT1();
                        Map<Long, AgreementInfo> agreementIdAndAgreementInfoMapping = t2.getT2();

                        return just(agreementRecords.stream().map(ar ->
                                        agreementRecordToAgreementRecordManagerInfo(ar, ofNullable(memberIdAndNameMapping.get(ar.getMemberId())).orElse(EMPTY_VALUE.value),
                                                agreementIdAndAgreementInfoMapping.get(ar.getAgreementId())))
                                .collect(toList()))
                                .flatMap(downloadHistoryInfos ->
                                        just(new PageModelResponse<>(downloadHistoryInfos, count)));
                    })
                    :
                    just(new PageModelResponse<>(emptyList(), count));
        });
    }

}
