package com.blue.media.service.impl;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.media.api.model.DownloadHistoryInfo;
import com.blue.media.constant.DownloadHistorySortAttribute;
import com.blue.media.model.DownloadHistoryCondition;
import com.blue.media.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.media.repository.entity.Attachment;
import com.blue.media.repository.entity.DownloadHistory;
import com.blue.media.repository.template.DownloadHistoryRepository;
import com.blue.media.service.inter.AttachmentService;
import com.blue.media.service.inter.DownloadHistoryService;
import com.blue.member.api.model.MemberBasicInfo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.media.converter.MediaModelConverters.downloadHistoryToDownloadHistoryInfo;
import static com.blue.mongo.common.SortConverter.convert;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.domain.Sort.unsorted;
import static org.springframework.data.mongodb.core.query.Query.query;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;
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

    private static final Function<DownloadHistoryCondition, Sort> SORTER_CONVERTER = condition -> {
        if (isNull(condition))
            return unsorted();

        String sortAttribute = condition.getSortAttribute();
        if (isBlank(sortAttribute)) {
            condition.setSortAttribute(DownloadHistorySortAttribute.ID.column);
        } else {
            if (!SORT_ATTRIBUTE_MAPPING.containsValue(sortAttribute))
                throw new BlueException(INVALID_PARAM);
        }

        return convert(condition.getSortType(), singletonList(condition.getSortAttribute()));
    };


    private static final Function<DownloadHistoryCondition, Query> CONDITION_PROCESSOR = condition -> {
        if (condition == null)
            return query(new Criteria());

        DownloadHistory ex = new DownloadHistory();

        ofNullable(condition.getId()).ifPresent(ex::setId);
        ofNullable(condition.getAttachmentId()).ifPresent(ex::setAttachmentId);
        ofNullable(condition.getCreator()).ifPresent(ex::setCreator);

        Criteria criteria = Criteria.byExample(ex);

        ofNullable(condition.getCreateTimeBegin())
                .ifPresent(ctb -> criteria.andOperator(Criteria.where("createTime").gte(ctb)));
        ofNullable(condition.getCreateTimeEnd())
                .ifPresent(cte -> criteria.andOperator(Criteria.where("createTime").lte(cte)));

        Query query = query(criteria);

        query.with(SORTER_CONVERTER.apply(condition));

        return query(criteria);
    };

    /**
     * insert download history
     *
     * @param downloadHistory
     * @return
     */
    @Override
    public Mono<DownloadHistory> insert(DownloadHistory downloadHistory) {
        LOGGER.info("Mono<DownloadHistory> insert(DownloadHistory downloadHistory), downloadHistory = {}", downloadHistory);
        return downloadHistoryRepository.insert(downloadHistory);
    }

    /**
     * get download history by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<DownloadHistory> getDownloadHistory(Long id) {
        return this.getDownloadHistoryMono(id).toFuture().join();
    }

    /**
     * get download history mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<DownloadHistory>> getDownloadHistoryMono(Long id) {
        LOGGER.info("Mono<Optional<DownloadHistory>> getDownloadHistoryMono(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return downloadHistoryRepository.findById(id).map(Optional::ofNullable);
    }

    /**
     * select download history by page and memberId
     *
     * @param limit
     * @param rows
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<DownloadHistory>> selectDownloadHistoryMonoByLimitAndMemberId(Long limit, Long rows, Long memberId) {
        LOGGER.info("Mono<List<DownloadHistory>> selectDownloadHistoryMonoByLimitAndMemberId(Long limit, Long rows, Long memberId), limit = {}, rows = {}, memberId = {}", limit, rows, memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        DownloadHistory ex = new DownloadHistory();
        ex.setCreator(memberId);

        return downloadHistoryRepository.findAll(Example.of(ex), Sort.by(Sort.Order.desc("id")))
                .collectList();
    }

    /**
     * count download history by memberId
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<Long> countDownloadHistoryMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<Long> countDownloadHistoryMonoByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        DownloadHistory ex = new DownloadHistory();
        ex.setCreator(memberId);

        return downloadHistoryRepository.count(Example.of(ex));
    }

    /**
     * select download history info by page and member id
     *
     * @param pageModelRequest
     * @param memberId
     * @return
     */
    @Override
    public Mono<PageModelResponse<DownloadHistoryInfo>> selectDownloadHistoryInfoByPageAndMemberId(PageModelRequest<Void> pageModelRequest, Long memberId) {
        LOGGER.info("Mono<PageModelResponse<DownloadHistoryInfo>> selectDownloadHistoryInfoByPageAndMemberId(PageModelRequest<Void> pageModelRequest, Long memberId), pageModelDTO = {}, memberId = {}",
                pageModelRequest, memberId);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return zip(
                selectDownloadHistoryMonoByLimitAndMemberId(pageModelRequest.getLimit(), pageModelRequest.getRows(), memberId),
                countDownloadHistoryMonoByMemberId(memberId),
                rpcMemberBasicServiceConsumer.selectMemberBasicInfoMonoByPrimaryKey(memberId)
        ).flatMap(tuple3 -> {
            List<DownloadHistory> downloadHistories = tuple3.getT1();
            String memberName = tuple3.getT3().getName();

            return isNotEmpty(downloadHistories) ?
                    attachmentService.selectAttachmentMonoByIds(downloadHistories.parallelStream().map(DownloadHistory::getAttachmentId).collect(toList()))
                            .flatMap(attachments -> {
                                Map<Long, String> idAndNameMapping = attachments.parallelStream().collect(toMap(Attachment::getId, Attachment::getName, (a, b) -> a));
                                return just(downloadHistories.stream().map(dh ->
                                                downloadHistoryToDownloadHistoryInfo(dh, ofNullable(idAndNameMapping.get(dh.getAttachmentId())).orElse(""), memberName))
                                        .collect(toList()));
                            }).flatMap(downloadHistoryInfo ->
                                    just(new PageModelResponse<>(downloadHistoryInfo, tuple3.getT2())))
                    :
                    just(new PageModelResponse<>(emptyList(), tuple3.getT2()));
        });
    }

    /**
     * select download history by page and condition
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    @Override
    public Mono<List<DownloadHistory>> selectDownloadHistoryMonoByLimitAndCondition(Long limit, Long rows, Query query) {
        LOGGER.info("Mono<List<DownloadHistory>> selectDownloadHistoryMonoByLimitAndCondition(Long limit, Long rows, Query query), " +
                "limit = {}, rows = {}, query = {}", limit, rows, query);
        return reactiveMongoTemplate.find(query, DownloadHistory.class).collectList();
    }

    /**
     * count download history by condition
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countDownloadHistoryMonoByCondition(Query query) {
        LOGGER.info("Mono<Long> countDownloadHistoryMonoByCondition(Query query), query = {}", query);
        return reactiveMongoTemplate.count(query, DownloadHistory.class);
    }

    /**
     * select download history info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<DownloadHistoryInfo>> selectDownloadHistoryInfoPageMonoByPageAndCondition(PageModelRequest<DownloadHistoryCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<DownloadHistoryInfo>> selectDownloadHistoryInfoPageMonoByPageAndCondition(PageModelRequest<DownloadHistoryCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(
                selectDownloadHistoryMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), query),
                countDownloadHistoryMonoByCondition(query)
        ).flatMap(tuple2 -> {
            List<DownloadHistory> downloadHistories = tuple2.getT1();

            return isNotEmpty(downloadHistories) ?
                    zip(attachmentService.selectAttachmentMonoByIds(downloadHistories.parallelStream().map(DownloadHistory::getAttachmentId).collect(toList()))
                                    .map(attachments -> attachments.parallelStream().collect(toMap(Attachment::getId, Attachment::getName, (a, b) -> a)))
                            ,
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoMonoByIds(downloadHistories.parallelStream().map(DownloadHistory::getCreator).collect(toList()))
                                    .map(memberBasicInfos -> memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a)))
                    ).flatMap(t2 -> {
                        Map<Long, String> attachmentIdAndNameMapping = t2.getT1();
                        Map<Long, String> memberIdAndNameMapping = t2.getT2();

                        return just(downloadHistories.stream().map(dh ->
                                        downloadHistoryToDownloadHistoryInfo(dh, ofNullable(attachmentIdAndNameMapping.get(dh.getAttachmentId())).orElse(""),
                                                ofNullable(memberIdAndNameMapping.get(dh.getCreator())).orElse("")))
                                .collect(toList()))
                                .flatMap(downloadHistoryInfo ->
                                        just(new PageModelResponse<>(downloadHistoryInfo, tuple2.getT2())));
                    })
                    :
                    just(new PageModelResponse<>(emptyList(), tuple2.getT2()));
        });
    }

}
