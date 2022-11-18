package com.blue.media.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.common.SortType;
import com.blue.basic.model.common.*;
import com.blue.basic.model.exps.BlueException;
import com.blue.media.api.model.AttachmentDetailInfo;
import com.blue.media.api.model.AttachmentInfo;
import com.blue.media.constant.AttachmentSortAttribute;
import com.blue.media.model.AttachmentCondition;
import com.blue.media.model.AttachmentManagerCondition;
import com.blue.media.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.media.repository.entity.Attachment;
import com.blue.media.repository.template.AttachmentRepository;
import com.blue.media.service.inter.AttachmentService;
import com.blue.member.api.model.MemberBasicInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.media.constant.AttachmentColumnName.*;
import static com.blue.media.converter.MediaModelConverters.ATTACHMENT_2_ATTACHMENT_DETAIL_INFO_CONVERTER;
import static com.blue.media.converter.MediaModelConverters.ATTACHMENT_2_ATTACHMENT_INFO_CONVERTER;
import static com.blue.mongo.common.MongoSearchAfterProcessor.packageSearchAfter;
import static com.blue.mongo.common.MongoSearchAfterProcessor.parseSearchAfter;
import static com.blue.mongo.common.MongoSortProcessor.process;
import static com.blue.mongo.constant.LikeElement.PREFIX;
import static com.blue.mongo.constant.LikeElement.SUFFIX;
import static com.blue.mongo.constant.SortSchema.DESC;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
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
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class AttachmentServiceImpl implements AttachmentService {

    private static final Logger LOGGER = getLogger(AttachmentServiceImpl.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private final AttachmentRepository attachmentRepository;

    public AttachmentServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, ReactiveMongoTemplate reactiveMongoTemplate,
                                 AttachmentRepository attachmentRepository) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.attachmentRepository = attachmentRepository;
    }

    private static final BiFunction<ScrollModelRequest<AttachmentCondition, Long>, Long, Query> SCROLL_MODEL_REQUEST_PROCESSOR = (smr, mid) -> {
        if (isInvalidIdentity(mid))
            throw new BlueException(UNAUTHORIZED);
        if (isNull(smr))
            throw new BlueException(EMPTY_PARAM);

        AttachmentCondition c = smr.getCondition();

        Query query = new Query();

        Attachment probe = new Attachment();

        probe.setCreator(mid);

        if (isNotNull(c)) {
            ofNullable(c.getLinkLike()).filter(BlueChecker::isNotBlank).ifPresent(linkLike ->
                    query.addCriteria(where(LINK.name).regex(compile(PREFIX.element + linkLike + SUFFIX.element, CASE_INSENSITIVE))));
            ofNullable(c.getNameLike()).filter(BlueChecker::isNotBlank).ifPresent(nameLike ->
                    query.addCriteria(where(NAME.name).regex(compile(PREFIX.element + nameLike + SUFFIX.element, CASE_INSENSITIVE))));
            ofNullable(c.getFileType()).filter(BlueChecker::isNotBlank).ifPresent(probe::setFileType);
            ofNullable(c.getCreateTimeBegin()).ifPresent(createTimeBegin ->
                    query.addCriteria(where(CREATE_TIME.name).gte(createTimeBegin)));
            ofNullable(c.getCreateTimeEnd()).ifPresent(createTimeEnd ->
                    query.addCriteria(where(CREATE_TIME.name).lte(createTimeEnd)));
        }

        query.addCriteria(byExample(probe));

        packageSearchAfter(query, DESC.sortType.identity, AttachmentSortAttribute.ID.column, smr.getCursor());
        query.with(process(List.of(new SortElement(AttachmentSortAttribute.CREATE_TIME.column, DESC.sortType.identity),
                new SortElement(AttachmentSortAttribute.ID.column, DESC.sortType.identity))));

        query.limit(smr.getRows().intValue());

        return query;
    };

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(AttachmentSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Function<AttachmentManagerCondition, Sort> SORTER_CONVERTER = c -> {
        String sortAttribute = ofNullable(c).map(AttachmentManagerCondition::getSortAttribute)
                .map(SORT_ATTRIBUTE_MAPPING::get).filter(BlueChecker::isNotBlank)
                .orElse(AttachmentSortAttribute.CREATE_TIME.column);

        String sortType = ofNullable(c).map(AttachmentManagerCondition::getSortType)
                .filter(BlueChecker::isNotBlank).orElse(SortType.DESC.identity);

        return process(
                sortAttribute.equals(AttachmentSortAttribute.ID.column) ?
                        singletonList(new SortElement(sortAttribute, sortType))
                        :
                        Stream.of(sortAttribute, AttachmentSortAttribute.ID.column)
                                .map(attr -> new SortElement(attr, sortType)).collect(toList())
        );
    };

    private static final Function<AttachmentManagerCondition, Query> CONDITION_PROCESSOR = c -> {
        Query query = new Query();

        if (isNull(c)) {
            query.with(SORTER_CONVERTER.apply(new AttachmentManagerCondition()));
            return query;
        }

        Attachment probe = new Attachment();

        ofNullable(c.getId()).ifPresent(probe::setId);
        ofNullable(c.getLinkLike()).filter(BlueChecker::isNotBlank).ifPresent(linkLike ->
                query.addCriteria(where(LINK.name).regex(compile(PREFIX.element + linkLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getNameLike()).filter(BlueChecker::isNotBlank).ifPresent(nameLike ->
                query.addCriteria(where(NAME.name).regex(compile(PREFIX.element + nameLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getFileType()).filter(BlueChecker::isNotBlank).ifPresent(probe::setFileType);
        ofNullable(c.getCreateTimeBegin()).ifPresent(createTimeBegin ->
                query.addCriteria(where(CREATE_TIME.name).gte(createTimeBegin)));
        ofNullable(c.getCreateTimeEnd()).ifPresent(createTimeEnd ->
                query.addCriteria(where(CREATE_TIME.name).lte(createTimeEnd)));
        ofNullable(c.getCreator()).ifPresent(probe::setCreator);

        query.addCriteria(byExample(probe));

        query.with(SORTER_CONVERTER.apply(c));

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
        LOGGER.info("attachment = {}", attachment);
        if (isNull(attachment))
            throw new BlueException(EMPTY_PARAM);

        return attachmentRepository.insert(attachment);
    }

    /**
     * insert attachment batch
     *
     * @param attachments
     * @return
     */
    @Override
    public Mono<List<Attachment>> insertAttachments(List<Attachment> attachments) {
        LOGGER.info("attachments = {}", attachments);

        return isNotEmpty(attachments) ?
                fromIterable(allotByMax(attachments, (int) DB_WRITE.value, false))
                        .map(attachmentRepository::saveAll)
                        .reduce(Flux::concat)
                        .flatMap(Flux::collectList)
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
    public Mono<Attachment> getAttachment(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return attachmentRepository.findById(id);
    }

    /**
     * get attachment info mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<AttachmentInfo> getAttachmentInfo(Long id) {
        return getAttachment(id).map(ATTACHMENT_2_ATTACHMENT_INFO_CONVERTER);
    }

    /**
     * select attachment info mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<AttachmentInfo>> selectAttachmentInfoByIds(List<Long> ids) {
        LOGGER.info("ids = {}", ids);
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            return error(() -> new BlueException(PAYLOAD_TOO_LARGE));

        return fromIterable(allotByMax(ids, (int) DB_SELECT.value, false))
                .map(shardIds -> attachmentRepository.findAllById(shardIds)
                        .map(ATTACHMENT_2_ATTACHMENT_INFO_CONVERTER))
                .reduce(Flux::concat)
                .flatMap(Flux::collectList);
    }

    /**
     * select attachments by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<AttachmentDetailInfo>> selectAttachmentDetailInfoByIds(List<Long> ids) {
        LOGGER.info("ids = {}", ids);
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            return error(() -> new BlueException(PAYLOAD_TOO_LARGE));

        return fromIterable(allotByMax(ids, (int) DB_SELECT.value, false))
                .map(shardIds -> attachmentRepository.findAllById(shardIds)
                        .collectList()
                        .flatMap(attachments ->
                                zip(rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(attachments.stream().map(Attachment::getCreator).collect(toList()))
                                                .flatMap(memberBasicInfos -> just(memberBasicInfos.stream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a)))),
                                        just(attachments))
                        ).flatMapMany(tuple2 -> {
                            Map<Long, String> idAndNameMapping = tuple2.getT1();
                            return fromStream(tuple2.getT2().stream().map(attachment -> ATTACHMENT_2_ATTACHMENT_DETAIL_INFO_CONVERTER.apply(attachment, idAndNameMapping.get(attachment.getCreator()))));
                        }))
                .reduce(Flux::concat)
                .flatMap(Flux::collectList);
    }

    /**
     * select attachment detail info by scroll and member id
     *
     * @param scrollModelRequest
     * @param memberId
     * @return
     */
    @Override
    public Mono<ScrollModelResponse<AttachmentDetailInfo, String>> selectAttachmentDetailInfoScrollByConditionAndCursorBaseOnMemberId(ScrollModelRequest<AttachmentCondition, Long> scrollModelRequest, Long memberId) {
        LOGGER.info("scrollModelRequest = {}, memberId = {}", scrollModelRequest, memberId);
        if (isNull(scrollModelRequest))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return zip(reactiveMongoTemplate.find(SCROLL_MODEL_REQUEST_PROCESSOR.apply(scrollModelRequest, memberId), Attachment.class).collectList(),
                rpcMemberBasicServiceConsumer.getMemberBasicInfo(memberId)
        ).map(tuple2 -> {
            List<Attachment> attachments = tuple2.getT1();
            String memberName = ofNullable(tuple2.getT2().getName()).filter(BlueChecker::isNotBlank).orElse(EMPTY_VALUE.value);

            return isNotEmpty(attachments) ?
                    new ScrollModelResponse<>(attachments.stream().map(a -> ATTACHMENT_2_ATTACHMENT_DETAIL_INFO_CONVERTER.apply(a, memberName)).collect(toList()),
                            parseSearchAfter(attachments, ofNullable(scrollModelRequest.getCondition()).map(AttachmentCondition::getSortType).orElse(DESC.sortType.identity),
                                    attachment -> String.valueOf(attachment.getId())))
                    :
                    new ScrollModelResponse<>(emptyList());
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
    public Mono<List<Attachment>> selectAttachmentByLimitAndQuery(Long limit, Long rows, Query query) {
        LOGGER.info("limit = {}, rows = {}, query = {}", limit, rows, query);
        if (isInvalidLimit(limit) || isInvalidRows(rows))
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, Attachment.class).collectList();
    }

    /**
     * count attachment by condition
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countAttachmentByQuery(Query query) {
        LOGGER.info("query = {}", query);
        if (isNull(query))
            return error(() -> new BlueException(EMPTY_PARAM));

        return reactiveMongoTemplate.count(query, Attachment.class);
    }

    /**
     * select attachment info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<AttachmentDetailInfo>> selectAttachmentDetailInfoPageByPageAndCondition(PageModelRequest<AttachmentManagerCondition> pageModelRequest) {
        LOGGER.info("pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectAttachmentByLimitAndQuery(pageModelRequest.getLimit(), pageModelRequest.getRows(), query), countAttachmentByQuery(query))
                .flatMap(tuple2 -> {
                    List<Attachment> attachments = tuple2.getT1();
                    Long count = tuple2.getT2();
                    return isNotEmpty(attachments) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(attachments.parallelStream().map(Attachment::getCreator).collect(toList()))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(attachments.stream().map(a ->
                                                        ATTACHMENT_2_ATTACHMENT_DETAIL_INFO_CONVERTER.apply(a, ofNullable(idAndNameMapping.get(a.getCreator())).orElse(EMPTY_VALUE.value)))
                                                .collect(toList()));
                                    }).flatMap(attachmentDetailInfos ->
                                            just(new PageModelResponse<>(attachmentDetailInfos, count)))
                            :
                            just(new PageModelResponse<>(emptyList(), count));
                });
    }

}
