package com.blue.media.service.impl;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.media.api.model.AttachmentInfo;
import com.blue.media.constant.AttachmentSortAttribute;
import com.blue.media.model.AttachmentCondition;
import com.blue.media.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.media.repository.entity.Attachment;
import com.blue.media.repository.mapper.AttachmentMapper;
import com.blue.media.service.inter.AttachmentService;
import com.blue.member.api.model.MemberBasicInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.ConstantProcessor.assertSortType;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static com.blue.media.converter.MediaModelConverters.ATTACHMENT_2_ATTACHMENT_INFO_CONVERTER;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;
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

    private final AttachmentMapper attachmentMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AttachmentServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, AttachmentMapper attachmentMapper) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.attachmentMapper = attachmentMapper;
    }

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(AttachmentSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Consumer<AttachmentCondition> CONDITION_REPACKAGER = condition -> {
        if (isNull(condition))
            return;

        ofNullable(condition.getSortAttribute())
                .filter(StringUtils::hasText)
                .map(SORT_ATTRIBUTE_MAPPING::get)
                .filter(StringUtils::hasText)
                .ifPresent(condition::setSortAttribute);

        assertSortType(condition.getSortType(), true);

        ofNullable(condition.getName())
                .filter(BlueChecker::isNotBlank).ifPresent(n -> condition.setName("%" + n + "%"));
    };

    /**
     * insert attachment
     *
     * @param attachment
     * @return
     */
    @Override
    public int insert(Attachment attachment) {
        LOGGER.info("insert(Attachment attachment), attachment = {}", attachment);
        return ofNullable(attachment)
                .map(attachmentMapper::insert)
                .orElse(0);
    }

    /**
     * insert attachment batch
     *
     * @param attachments
     * @return
     */
    @Override
    public int insertBatch(List<Attachment> attachments) {
        LOGGER.info("insertBatch(List<Attachment> attachments), attachments = {}", attachments);
        return ofNullable(attachments)
                .filter(as -> as.size() > 0)
                .map(attachmentMapper::insertBatch)
                .orElse(0);
    }

    /**
     * get attachment by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Attachment> getAttachment(Long id) {
        LOGGER.info("Optional<Attachment> getAttachment(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(attachmentMapper.selectByPrimaryKey(id));
    }

    /**
     * get attachment mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<Attachment>> getAttachmentMono(Long id) {
        return just(this.getAttachment(id));
    }

    /**
     * select attachments by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<Attachment> selectAttachmentByIds(List<Long> ids) {
        LOGGER.info("List<Attachment> selectAttachmentByIds(List<Long> ids), ids = {}", ids);
        return isValidIdentities(ids) ? allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(attachmentMapper::selectByIds)
                .flatMap(List::stream)
                .collect(toList())
                :
                emptyList();
    }

    /**
     * select attachments by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<Attachment>> selectAttachmentMonoByIds(List<Long> ids) {
        return just(this.selectAttachmentByIds(ids));
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
        return just(attachmentMapper.selectByLimitAndMemberId(limit, rows, memberId));
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
        return just(ofNullable(attachmentMapper.countByMemberId(memberId)).orElse(0L));
    }

    /**
     * select attachment info by page and member id
     *
     * @param pageModelRequest
     * @param memberId
     * @return
     */
    @Override
    public Mono<PageModelResponse<AttachmentInfo>> selectAttachmentInfoByPageAndMemberId(PageModelRequest<Void> pageModelRequest, Long memberId) {
        LOGGER.info("Mono<PageModelResponse<AttachmentInfo>> selectAttachmentInfoByPageAndMemberId(PageModelRequest<Void> pageModelRequest, Long memberId), pageModelDTO = {}, memberId = {}",
                pageModelRequest, memberId);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return zip(
                selectAttachmentMonoByLimitAndMemberId(pageModelRequest.getLimit(), pageModelRequest.getRows(), memberId),
                countAttachmentMonoByMemberId(memberId),
                rpcMemberBasicServiceConsumer.selectMemberBasicInfoMonoByPrimaryKey(memberId)
        ).flatMap(tuple3 -> {
            List<Attachment> attachments = tuple3.getT1();
            String memberName = tuple3.getT3().getName();

            return isNotEmpty(attachments) ?
                    just(attachments.stream().map(a -> ATTACHMENT_2_ATTACHMENT_INFO_CONVERTER.apply(a, memberName)).collect(toList()))
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
     * @param attachmentCondition
     * @return
     */
    @Override
    public Mono<List<Attachment>> selectAttachmentMonoByLimitAndCondition(Long limit, Long rows, AttachmentCondition attachmentCondition) {
        LOGGER.info("Mono<List<Attachment>> selectAttachmentMonoByLimitAndCondition(Long limit, Long rows, AttachmentCondition attachmentCondition), " +
                "limit = {}, rows = {}, attachmentCondition = {}", limit, rows, attachmentCondition);
        return just(attachmentMapper.selectByLimitAndCondition(limit, rows, attachmentCondition));
    }

    /**
     * count attachment by condition
     *
     * @param attachmentCondition
     * @return
     */
    @Override
    public Mono<Long> countAttachmentMonoByCondition(AttachmentCondition attachmentCondition) {
        LOGGER.info("Mono<Long> countAttachmentMonoByCondition(AttachmentCondition attachmentCondition), attachmentCondition = {}", attachmentCondition);
        return just(ofNullable(attachmentMapper.countByCondition(attachmentCondition)).orElse(0L));
    }

    /**
     * select attachment info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<AttachmentInfo>> selectAttachmentInfoPageMonoByPageAndCondition(PageModelRequest<AttachmentCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<RoleInfo>> selectAttachmentInfoPageMonoByPageAndCondition(PageModelRequest<AttachmentCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        AttachmentCondition attachmentCondition = pageModelRequest.getParam();
        CONDITION_REPACKAGER.accept(attachmentCondition);

        return zip(selectAttachmentMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), attachmentCondition), countAttachmentMonoByCondition(attachmentCondition))
                .flatMap(tuple2 -> {
                    List<Attachment> attachments = tuple2.getT1();
                    return isNotEmpty(attachments) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoMonoByIds(attachments.parallelStream().map(Attachment::getCreator).collect(toList()))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(attachments.stream().map(a ->
                                                        ATTACHMENT_2_ATTACHMENT_INFO_CONVERTER.apply(a, ofNullable(idAndNameMapping.get(a.getCreator())).orElse("")))
                                                .collect(toList()));
                                    }).flatMap(attachmentInfos ->
                                            just(new PageModelResponse<>(attachmentInfos, tuple2.getT2())))
                            :
                            just(new PageModelResponse<>(emptyList(), tuple2.getT2()));
                });
    }

}
