package com.blue.media.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.common.SortType;
import com.blue.basic.model.common.*;
import com.blue.basic.model.exps.BlueException;
import com.blue.media.api.model.AttachmentDetailInfo;
import com.blue.media.api.model.DownloadHistoryInfo;
import com.blue.media.constant.DownloadHistorySortAttribute;
import com.blue.media.model.DownloadHistoryCondition;
import com.blue.media.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.media.repository.entity.DownloadHistory;
import com.blue.media.repository.template.DownloadHistoryRepository;
import com.blue.media.service.inter.AttachmentService;
import com.blue.media.service.inter.DownloadHistoryService;
import com.blue.member.api.model.MemberBasicInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.media.constant.DownloadHistoryColumnName.CREATE_TIME;
import static com.blue.media.converter.MediaModelConverters.downloadHistoryToDownloadHistoryInfo;
import static com.blue.mongo.common.MongoSearchAfterProcessor.packageSearchAfter;
import static com.blue.mongo.common.MongoSearchAfterProcessor.parseSearchAfter;
import static com.blue.mongo.common.MongoSortProcessor.process;
import static com.blue.mongo.constant.SortSchema.DESC;
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
 * download history service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class DownloadHistoryServiceImpl implements DownloadHistoryService {

    private static final Logger LOGGER = getLogger(DownloadHistoryServiceImpl.class);

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final AttachmentService attachmentService;

    private final DownloadHistoryRepository downloadHistoryRepository;

    public DownloadHistoryServiceImpl(ReactiveMongoTemplate reactiveMongoTemplate, RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer,
                                      AttachmentService attachmentService, DownloadHistoryRepository downloadHistoryRepository) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.attachmentService = attachmentService;
        this.downloadHistoryRepository = downloadHistoryRepository;
    }

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(DownloadHistorySortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Function<DownloadHistoryCondition, Sort> SORTER_CONVERTER = c -> {
        String sortAttribute = ofNullable(c).map(DownloadHistoryCondition::getSortAttribute)
                .map(SORT_ATTRIBUTE_MAPPING::get)
                .filter(BlueChecker::isNotBlank)
                .orElse(DownloadHistorySortAttribute.CREATE_TIME.column);

        String sortType = ofNullable(c).map(DownloadHistoryCondition::getSortType)
                .filter(BlueChecker::isNotBlank)
                .orElse(SortType.DESC.identity);

        return process(
                sortAttribute.equals(DownloadHistorySortAttribute.ID.column) ?
                        singletonList(new SortElement(sortAttribute, sortType))
                        :
                        Stream.of(sortAttribute, DownloadHistorySortAttribute.ID.column)
                                .map(attr -> new SortElement(attr, sortType)).collect(toList())
        );
    };

    private static final Function<DownloadHistoryCondition, Query> CONDITION_PROCESSOR = c -> {
        Query query = new Query();

        if (c == null) {
            query.with(SORTER_CONVERTER.apply(new DownloadHistoryCondition()));
            return query;
        }

        DownloadHistory probe = new DownloadHistory();

        ofNullable(c.getId()).ifPresent(probe::setId);
        ofNullable(c.getAttachmentId()).ifPresent(probe::setAttachmentId);
        ofNullable(c.getCreator()).ifPresent(probe::setCreator);
        ofNullable(c.getCreateTimeBegin()).ifPresent(createTimeBegin ->
                query.addCriteria(where(CREATE_TIME.name).gte(createTimeBegin)));
        ofNullable(c.getCreateTimeEnd()).ifPresent(createTimeEnd ->
                query.addCriteria(where(CREATE_TIME.name).lte(createTimeEnd)));

        query.addCriteria(byExample(probe));

        query.with(SORTER_CONVERTER.apply(c));

        return query;
    };

    /**
     * insert download history
     *
     * @param downloadHistory
     * @return
     */
    @Override
    public Mono<DownloadHistory> insertDownloadHistory(DownloadHistory downloadHistory) {
        LOGGER.info("downloadHistory = {}", downloadHistory);
        if (isNull(downloadHistory))
            return error(() -> new BlueException(EMPTY_PARAM));

        return downloadHistoryRepository.insert(downloadHistory);
    }

    /**
     * get download history mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<DownloadHistory> getDownloadHistory(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return downloadHistoryRepository.findById(id);
    }

    /**
     * select attachment detail info by scroll and member id
     *
     * @param scrollModelRequest
     * @param memberId
     * @return
     */
    @Override
    public Mono<ScrollModelResponse<DownloadHistoryInfo, String>> selectShineInfoScrollByScrollAndCursorBaseOnMemberId(ScrollModelRequest<Void, Long> scrollModelRequest, Long memberId) {
        LOGGER.info("scrollModelRequest = {}, memberId = {}", scrollModelRequest, memberId);
        if (isNull(scrollModelRequest))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        Query query = new Query();

        DownloadHistory probe = new DownloadHistory();
        probe.setCreator(memberId);

        query.addCriteria(byExample(probe));
        packageSearchAfter(query, DESC.sortType.identity, DownloadHistorySortAttribute.ID.column, scrollModelRequest.getCursor());
        query.with(process(List.of(new SortElement(DownloadHistorySortAttribute.CREATE_TIME.column, DESC.sortType.identity),
                new SortElement(DownloadHistorySortAttribute.ID.column, DESC.sortType.identity))));

        query.limit(scrollModelRequest.getRows().intValue());

        return zip(reactiveMongoTemplate.find(query, DownloadHistory.class).collectList(),
                rpcMemberBasicServiceConsumer.getMemberBasicInfo(memberId)
        ).flatMap(tuple2 -> {
            List<DownloadHistory> downloadHistories = tuple2.getT1();
            String memberName = ofNullable(tuple2.getT2().getName()).filter(BlueChecker::isNotBlank).orElse(EMPTY_VALUE.value);

            return isNotEmpty(downloadHistories) ?
                    attachmentService.selectAttachmentDetailInfoByIds(downloadHistories.parallelStream().map(DownloadHistory::getAttachmentId).collect(toList()))
                            .flatMap(attachments -> {
                                Map<Long, String> attachmentIdAndAttachmentNameMapping = attachments.parallelStream().collect(toMap(AttachmentDetailInfo::getId, AttachmentDetailInfo::getName, (a, b) -> a));
                                return just(downloadHistories.stream().map(dh ->
                                                downloadHistoryToDownloadHistoryInfo(dh, ofNullable(attachmentIdAndAttachmentNameMapping.get(dh.getAttachmentId())).orElse(EMPTY_VALUE.value), memberName))
                                        .collect(toList()));
                            }).flatMap(downloadHistoryInfo ->
                                    just(new ScrollModelResponse<>(downloadHistoryInfo,
                                            parseSearchAfter(downloadHistories, DESC.sortType.identity, downloadHistory -> String.valueOf(downloadHistory.getId())))))
                    :
                    just(new ScrollModelResponse<>(emptyList()));
        });
    }

    /**
     * select download history by page and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    @Override
    public Mono<List<DownloadHistory>> selectDownloadHistoryByLimitAndQuery(Long limit, Long rows, Query query) {
        LOGGER.info("limit = {}, rows = {}, query = {}", limit, rows, query);
        if (limit == null || limit < 0 || rows == null || rows == 0)
            return error(() -> new BlueException(INVALID_PARAM));

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, DownloadHistory.class).collectList();
    }

    /**
     * count download history by query
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countDownloadHistoryByQuery(Query query) {
        LOGGER.info("query = {}", query);
        return reactiveMongoTemplate.count(query, DownloadHistory.class);
    }

    /**
     * select download history info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<DownloadHistoryInfo>> selectDownloadHistoryInfoPageByPageAndCondition(PageModelRequest<DownloadHistoryCondition> pageModelRequest) {
        LOGGER.info("pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            return error(() -> new BlueException(EMPTY_PARAM));

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectDownloadHistoryByLimitAndQuery(pageModelRequest.getLimit(), pageModelRequest.getRows(), query),
                countDownloadHistoryByQuery(query)
        ).flatMap(tuple2 -> {
            List<DownloadHistory> downloadHistories = tuple2.getT1();
            Long count = tuple2.getT2();
            return isNotEmpty(downloadHistories) ?
                    zip(attachmentService.selectAttachmentDetailInfoByIds(downloadHistories.stream().map(DownloadHistory::getAttachmentId).collect(toList()))
                                    .map(attachments -> attachments.stream().collect(toMap(AttachmentDetailInfo::getId, AttachmentDetailInfo::getName, (a, b) -> a)))
                            ,
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(downloadHistories.stream().map(DownloadHistory::getCreator).collect(toList()))
                                    .map(memberBasicInfos -> memberBasicInfos.stream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a)))
                    ).flatMap(t2 -> {
                        Map<Long, String> attachmentIdAndNameMapping = t2.getT1();
                        Map<Long, String> memberIdAndNameMapping = t2.getT2();

                        return just(downloadHistories.stream().map(dh ->
                                        downloadHistoryToDownloadHistoryInfo(dh, ofNullable(attachmentIdAndNameMapping.get(dh.getAttachmentId())).orElse(EMPTY_VALUE.value),
                                                ofNullable(memberIdAndNameMapping.get(dh.getCreator())).orElse(EMPTY_VALUE.value)))
                                .collect(toList()))
                                .flatMap(downloadHistoryInfos ->
                                        just(new PageModelResponse<>(downloadHistoryInfos, count)));
                    })
                    :
                    just(new PageModelResponse<>(emptyList(), count));
        });
    }

}
