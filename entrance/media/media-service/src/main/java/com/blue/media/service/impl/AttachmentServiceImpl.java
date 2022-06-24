package com.blue.media.service.impl;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.media.api.model.AttachmentDetailInfo;
import com.blue.media.api.model.AttachmentInfo;
import com.blue.media.constant.AttachmentSortAttribute;
import com.blue.media.model.AttachmentCondition;
import com.blue.media.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.media.repository.entity.Attachment;
import com.blue.media.repository.template.AttachmentRepository;
import com.blue.media.service.inter.AttachmentService;
import com.blue.member.api.model.MemberBasicInfo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.common.BlueNumericalValue.*;
import static com.blue.base.constant.common.ResponseElement.*;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.media.constant.ColumnName.*;
import static com.blue.media.converter.MediaModelConverters.ATTACHMENT_2_ATTACHMENT_DETAIL_INFO_CONVERTER;
import static com.blue.media.converter.MediaModelConverters.ATTACHMENT_2_ATTACHMENT_INFO_CONVERTER;
import static com.blue.mongo.common.SortConverter.convert;
import static com.blue.mongo.constant.LikeElement.PREFIX;
import static com.blue.mongo.constant.LikeElement.SUFFIX;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.domain.Sort.unsorted;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Flux.fromStream;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * attachment service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
@Service
public class AttachmentServiceImpl implements AttachmentService {

    private static final Logger LOGGER = getLogger(AttachmentServiceImpl.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private final Scheduler scheduler;

    private final AttachmentRepository attachmentRepository;

    public AttachmentServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, ReactiveMongoTemplate reactiveMongoTemplate, Scheduler scheduler,
                                 AttachmentRepository attachmentRepository) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.scheduler = scheduler;
        this.attachmentRepository = attachmentRepository;
    }

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(AttachmentSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Function<AttachmentCondition, Sort> SORTER_CONVERTER = condition -> {
        if (isNull(condition))
            return unsorted();

        String sortAttribute = condition.getSortAttribute();
        if (isBlank(sortAttribute)) {
            condition.setSortAttribute(AttachmentSortAttribute.ID.column);
        } else {
            if (!SORT_ATTRIBUTE_MAPPING.containsValue(sortAttribute))
                throw new BlueException(INVALID_PARAM);
        }

        return convert(condition.getSortType(), singletonList(condition.getSortAttribute()));
    };

    private static final Function<AttachmentCondition, Query> CONDITION_PROCESSOR = condition -> {
        Query query = new Query();

        if (condition == null)
            return query;

        Attachment probe = new Attachment();

        ofNullable(condition.getId()).ifPresent(probe::setId);
        ofNullable(condition.getLinkLike()).filter(BlueChecker::isNotBlank).ifPresent(linkLike ->
                query.addCriteria(where(LINK.name).regex(compile(PREFIX.element + linkLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getNameLike()).filter(BlueChecker::isNotBlank).ifPresent(nameLike ->
                query.addCriteria(where(NAME.name).regex(compile(PREFIX.element + nameLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getFileType()).filter(BlueChecker::isNotBlank).ifPresent(probe::setFileType);
        ofNullable(condition.getCreateTimeBegin()).ifPresent(createTimeBegin ->
                query.addCriteria(where(CREATE_TIME.name).gte(createTimeBegin)));
        ofNullable(condition.getCreateTimeEnd()).ifPresent(createTimeEnd ->
                query.addCriteria(where(CREATE_TIME.name).lte(createTimeEnd)));

        query.addCriteria(byExample(probe));

        query.with(SORTER_CONVERTER.apply(condition));

        return query;
    };


    /**
     * insert attachment
     *
     * @param attachment
     * @return
     */
    @Override
    public Mono<Attachment> insertAttachment(Attachment attachment) {
        LOGGER.info("Mono<Attachment> insert(Attachment attachment), attachment = {}", attachment);
        if (attachment == null)
            throw new BlueException(EMPTY_PARAM);

        return attachmentRepository.insert(attachment).subscribeOn(scheduler);
    }

    /**
     * insert attachment batch
     *
     * @param attachments
     * @return
     */
    @Override
    public Mono<List<Attachment>> insertAttachments(List<Attachment> attachments) {
        LOGGER.info("Mono<List<Attachment>> insertBatch(List<Attachment> attachments), attachments = {}", attachments);

        return isNotEmpty(attachments) ?
                fromIterable(allotByMax(attachments, (int) DB_WRITE.value, false))
                        .map(attachmentRepository::saveAll)
                        .reduce(Flux::concat)
                        .flatMap(Flux::collectList).subscribeOn(scheduler)
                :
                just(emptyList());
    }

    /**
     * get attachment mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Attachment> getAttachmentMono(Long id) {
        LOGGER.info("Mono<Attachment> getAttachmentMono(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return attachmentRepository.findById(id).subscribeOn(scheduler);
    }

    /**
     * get attachment info mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<AttachmentInfo> getAttachmentInfoMono(Long id) {
        return getAttachmentMono(id).map(ATTACHMENT_2_ATTACHMENT_INFO_CONVERTER);
    }

    /**
     * select attachment info mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<AttachmentInfo>> selectAttachmentInfoMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<AttachmentInfo>> selectAttachmentInfoMonoByIds(List<Long> ids), ids = {}", ids);
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            return error(() -> new BlueException(PAYLOAD_TOO_LARGE));

        return fromIterable(allotByMax(ids, (int) DB_SELECT.value, false))
                .map(shardIds -> attachmentRepository.findAllById(shardIds).map(ATTACHMENT_2_ATTACHMENT_INFO_CONVERTER))
                .reduce(Flux::concat)
                .flatMap(Flux::collectList)
                .subscribeOn(scheduler);
    }

    /**
     * select attachments by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<AttachmentDetailInfo>> selectAttachmentDetailInfoMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<Attachment>> selectAttachmentDetailInfoMonoByIds(List<Long> ids), ids = {}", ids);
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            return error(() -> new BlueException(PAYLOAD_TOO_LARGE));

        return fromIterable(allotByMax(ids, (int) DB_SELECT.value, false))
                .map(shardIds -> attachmentRepository.findAllById(shardIds)
                        .collectList()
                        .flatMap(attachments ->
                                zip(rpcMemberBasicServiceConsumer.selectMemberBasicInfoMonoByIds(attachments.stream().map(Attachment::getCreator).collect(toList()))
                                                .flatMap(memberBasicInfos -> just(memberBasicInfos.stream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a)))),
                                        just(attachments))
                        ).flatMapMany(tuple2 -> {
                            Map<Long, String> idAndNameMapping = tuple2.getT1();
                            return fromStream(tuple2.getT2().stream().map(attachment -> ATTACHMENT_2_ATTACHMENT_DETAIL_INFO_CONVERTER.apply(attachment, idAndNameMapping.get(attachment.getCreator()))));
                        }))
                .reduce(Flux::concat)
                .flatMap(Flux::collectList).subscribeOn(scheduler);
    }

    /**
     * select attachment by page and memberId
     *
     * @param limit
     * @param rows
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<Attachment>> selectAttachmentMonoByLimitAndMemberId(Long limit, Long rows, Long memberId) {
        LOGGER.info("Mono<List<Attachment>> selectAttachmentMonoByLimitAndMemberId(Long limit, Long rows, Long memberId), " +
                "limit = {}, rows = {}, memberId = {}", limit, rows, memberId);
        if (isInvalidLimit(limit) || isInvalidRows(rows) || isInvalidIdentity(memberId))
            throw new BlueException(INVALID_PARAM);

        Attachment probe = new Attachment();
        probe.setCreator(memberId);

        return attachmentRepository.findAll(Example.of(probe), Sort.by(Sort.Order.desc(ID.name)))
                .skip(limit).take(rows)
                .collectList().subscribeOn(scheduler);
    }

    /**
     * count attachment by memberId
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<Long> countAttachmentMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<Long> countAttachmentMonoByMemberId(Long memberId), memberId = {}", memberId);

        Attachment probe = new Attachment();
        probe.setCreator(memberId);

        return attachmentRepository.count(Example.of(probe)).subscribeOn(scheduler);
    }

    /**
     * select attachment info by page and member id
     *
     * @param pageModelRequest
     * @param memberId
     * @return
     */
    @Override
    public Mono<PageModelResponse<AttachmentDetailInfo>> selectAttachmentDetailInfoByPageAndMemberId(PageModelRequest<Void> pageModelRequest, Long memberId) {
        LOGGER.info("Mono<PageModelResponse<AttachmentInfo>> selectAttachmentDetailInfoByPageAndMemberId(PageModelRequest<Void> pageModelRequest, Long memberId), " +
                "pageModelRequest = {}, memberId = {}", pageModelRequest, memberId);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return zip(
                selectAttachmentMonoByLimitAndMemberId(pageModelRequest.getLimit(), pageModelRequest.getRows(), memberId),
                countAttachmentMonoByMemberId(memberId),
                rpcMemberBasicServiceConsumer.getMemberBasicInfoMonoByPrimaryKey(memberId)
        ).flatMap(tuple3 -> {
            List<Attachment> attachments = tuple3.getT1();
            String memberName = tuple3.getT3().getName();

            return isNotEmpty(attachments) ?
                    just(attachments.stream().map(a -> ATTACHMENT_2_ATTACHMENT_DETAIL_INFO_CONVERTER.apply(a, memberName)).collect(toList()))
                            .flatMap(attachmentInfos ->
                                    just(new PageModelResponse<>(attachmentInfos, tuple3.getT2())))
                    :
                    just(new PageModelResponse<>(emptyList(), tuple3.getT2()));
        });
    }

    /**
     * select attachment by page and condition
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    @Override
    public Mono<List<Attachment>> selectAttachmentMonoByLimitAndQuery(Long limit, Long rows, Query query) {
        LOGGER.info("Mono<List<Attachment>> selectAttachmentMonoByLimitAndCondition(Long limit, Long rows, Query query)," +
                " limit = {}, rows = {}, query = {}", limit, rows, query);

        if (isInvalidLimit(limit) || isInvalidRows(rows))
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, Attachment.class).collectList().subscribeOn(scheduler);
    }

    /**
     * count attachment by condition
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countAttachmentMonoByQuery(Query query) {
        LOGGER.info("Mono<Long> countAttachmentMonoByCondition(Query query), query = {}", query);
        return reactiveMongoTemplate.count(query, Attachment.class).subscribeOn(scheduler);
    }

    /**
     * select attachment info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<AttachmentDetailInfo>> selectAttachmentDetailInfoPageMonoByPageAndCondition(PageModelRequest<AttachmentCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<RoleInfo>> selectAttachmentDetailInfoPageMonoByPageAndCondition(PageModelRequest<AttachmentCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(selectAttachmentMonoByLimitAndQuery(pageModelRequest.getLimit(), pageModelRequest.getRows(), query), countAttachmentMonoByQuery(query))
                .flatMap(tuple2 -> {
                    List<Attachment> attachments = tuple2.getT1();
                    return isNotEmpty(attachments) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoMonoByIds(attachments.parallelStream().map(Attachment::getCreator).collect(toList()))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(attachments.stream().map(a ->
                                                        ATTACHMENT_2_ATTACHMENT_DETAIL_INFO_CONVERTER.apply(a, ofNullable(idAndNameMapping.get(a.getCreator())).orElse(EMPTY_DATA.value)))
                                                .collect(toList()));
                                    }).flatMap(attachmentDetailInfos ->
                                            just(new PageModelResponse<>(attachmentDetailInfos, tuple2.getT2())))
                            :
                            just(new PageModelResponse<>(emptyList(), tuple2.getT2()));
                });
    }

}
