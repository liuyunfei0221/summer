package com.blue.member.service.impl;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.media.api.model.AttachmentInfo;
import com.blue.member.api.model.CardDetailInfo;
import com.blue.member.api.model.CardInfo;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.config.deploy.CardDeploy;
import com.blue.member.constant.CardSortAttribute;
import com.blue.member.model.CardCondition;
import com.blue.member.model.CardInsertParam;
import com.blue.member.model.CardUpdateParam;
import com.blue.member.remote.consumer.RpcAttachmentServiceConsumer;
import com.blue.member.repository.entity.Card;
import com.blue.member.repository.template.CardRepository;
import com.blue.member.service.inter.CardService;
import com.blue.member.service.inter.MemberBasicService;
import com.blue.redisson.component.SynchronizedProcessor;
import org.springframework.data.domain.Example;
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

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.constant.common.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.common.ResponseElement.*;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.base.constant.common.Status.VALID;
import static com.blue.base.constant.common.SyncKeyPrefix.CARD_UPDATE_PRE;
import static com.blue.member.constant.ColumnName.*;
import static com.blue.member.converter.MemberModelConverters.*;
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
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * card service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode", "SpringJavaInjectionPointsAutowiringInspection", "AlibabaAvoidComplexCondition"})
@Service
public class CardServiceImpl implements CardService {

    private static final Logger LOGGER = getLogger(CardServiceImpl.class);

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private BlueIdentityProcessor blueIdentityProcessor;

    private final SynchronizedProcessor synchronizedProcessor;

    private final RpcAttachmentServiceConsumer rpcAttachmentServiceConsumer;

    private final MemberBasicService memberBasicService;

    private final CardRepository cardRepository;

    public CardServiceImpl(ReactiveMongoTemplate reactiveMongoTemplate, BlueIdentityProcessor blueIdentityProcessor, SynchronizedProcessor synchronizedProcessor,
                           RpcAttachmentServiceConsumer rpcAttachmentServiceConsumer, MemberBasicService memberBasicService, CardRepository cardRepository, CardDeploy cardDeploy) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.synchronizedProcessor = synchronizedProcessor;
        this.rpcAttachmentServiceConsumer = rpcAttachmentServiceConsumer;
        this.memberBasicService = memberBasicService;
        this.cardRepository = cardRepository;

        this.MAX_ADDRESS = cardDeploy.getMax();
    }

    private final long MAX_ADDRESS;

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(CardSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private Mono<Card> packageCoverAndContent(Long coverId, Long contentId, Card card) {
        if (isInvalidIdentity(contentId) || isNull(card))
            throw new BlueException(EMPTY_PARAM);

        return rpcAttachmentServiceConsumer.selectAttachmentInfoMonoByIds(Stream.of(coverId, contentId)
                        .filter(BlueChecker::isNotNull).collect(toList()))
                .map(attachmentInfos -> attachmentInfos.stream().collect(toMap(AttachmentInfo::getId, a -> a, (a, b) -> a)))
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(attachmentMapping -> {
                    if (isValidIdentity(coverId)) {
                        AttachmentInfo cover = attachmentMapping.get(coverId);
                        if (isNull(cover))
                            return error(new BlueException(DATA_NOT_EXIST));

                        card.setCoverId(coverId);
                        card.setCoverLink(cover.getLink());
                    }

                    AttachmentInfo content = attachmentMapping.get(contentId);
                    if (isNull(content))
                        return error(new BlueException(DATA_NOT_EXIST));

                    card.setContentId(contentId);
                    card.setContentLink(content.getLink());

                    return just(card);
                });
    }

    private final BiFunction<CardInsertParam, Long, Mono<Card>> CARD_INSERT_PARAM_2_MEMBER_ADDRESS = (cardInsertParam, memberId) -> {
        if (isNull(cardInsertParam))
            throw new BlueException(EMPTY_PARAM);
        cardInsertParam.asserts();
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        Card card = new Card();

        card.setId(blueIdentityProcessor.generate(Card.class));
        card.setMemberId(memberId);
        card.setName(cardInsertParam.getName());
        card.setDetail(cardInsertParam.getDetail());
        card.setExtra(cardInsertParam.getExtra());
        card.setStatus(VALID.status);

        Long stamp = TIME_STAMP_GETTER.get();
        card.setCreateTime(stamp);
        card.setUpdateTime(stamp);

        Long coverId = cardInsertParam.getCoverId();
        Long contentId = cardInsertParam.getContentId();

        return packageCoverAndContent(coverId, contentId, card);
    };

    private final BiFunction<CardUpdateParam, Card, Mono<Card>> CARD_UPDATE_PARAM_2_MEMBER_ADDRESS = (cardUpdateParam, card) -> {
        if (isNull(cardUpdateParam) || isNull(card))
            throw new BlueException(EMPTY_PARAM);
        cardUpdateParam.asserts();

        ofNullable(cardUpdateParam.getName())
                .filter(BlueChecker::isNotBlank).ifPresent(card::setName);
        ofNullable(cardUpdateParam.getDetail())
                .filter(BlueChecker::isNotBlank).ifPresent(card::setDetail);
        ofNullable(cardUpdateParam.getExtra())
                .filter(BlueChecker::isNotBlank).ifPresent(card::setExtra);

        card.setUpdateTime(TIME_STAMP_GETTER.get());

        Long coverId = cardUpdateParam.getCoverId();
        Long contentId = cardUpdateParam.getContentId();

        if (card.getCoverId().equals(contentId) && card.getContentId().equals(contentId))
            return just(card);

        return packageCoverAndContent(coverId, contentId, card);
    };

    private static final Function<Long, String> CARD_UPDATE_SYNC_KEY_GEN = memberId -> CARD_UPDATE_PRE.prefix + memberId;

    private static final Function<CardCondition, Sort> SORTER_CONVERTER = condition -> {
        if (isNull(condition))
            return unsorted();

        String sortAttribute = condition.getSortAttribute();
        if (isBlank(sortAttribute)) {
            condition.setSortAttribute(CardSortAttribute.ID.column);
        } else {
            if (!SORT_ATTRIBUTE_MAPPING.containsValue(sortAttribute))
                throw new BlueException(INVALID_PARAM);
        }

        return convert(condition.getSortType(), singletonList(condition.getSortAttribute()));
    };

    private static final Function<CardCondition, Query> CONDITION_PROCESSOR = condition -> {
        Query query = new Query();

        if (condition == null)
            return query;

        Card probe = new Card();

        ofNullable(condition.getId()).ifPresent(probe::setId);
        ofNullable(condition.getMemberId()).ifPresent(probe::setMemberId);
        ofNullable(condition.getNameLike()).filter(BlueChecker::isNotBlank).ifPresent(nameLike ->
                query.addCriteria(where(NAME.name).regex(compile(PREFIX.element + nameLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getDetailLike()).filter(BlueChecker::isNotBlank).ifPresent(detailLike ->
                query.addCriteria(where(DETAIL.name).regex(compile(PREFIX.element + detailLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getCoverId()).ifPresent(probe::setCoverId);
        ofNullable(condition.getCoverLinkLike()).filter(BlueChecker::isNotBlank).ifPresent(coverLinkLike ->
                query.addCriteria(where(COVER_LINK.name).regex(compile(PREFIX.element + coverLinkLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getContentId()).ifPresent(probe::setContentId);
        ofNullable(condition.getContentLinkLike()).filter(BlueChecker::isNotBlank).ifPresent(contentLinkLike ->
                query.addCriteria(where(CONTENT_LINK.name).regex(compile(PREFIX.element + contentLinkLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(condition.getStatus()).ifPresent(probe::setStatus);

        ofNullable(condition.getCreateTimeBegin()).ifPresent(createTimeBegin ->
                query.addCriteria(where(CREATE_TIME.name).gte(createTimeBegin)));
        ofNullable(condition.getCreateTimeEnd()).ifPresent(createTimeEnd ->
                query.addCriteria(where(CREATE_TIME.name).lte(createTimeEnd)));

        ofNullable(condition.getUpdateTimeBegin()).ifPresent(updateTimeBegin ->
                query.addCriteria(where(UPDATE_TIME.name).gte(updateTimeBegin)));
        ofNullable(condition.getUpdateTimeEnd()).ifPresent(updateTimeEnd ->
                query.addCriteria(where(UPDATE_TIME.name).lte(updateTimeEnd)));

        query.addCriteria(byExample(probe));

        query.with(SORTER_CONVERTER.apply(condition));

        return query;
    };

    /**
     * insert card
     *
     * @param cardInsertParam
     * @param memberId
     * @return
     */
    @Override
    public Mono<CardInfo> insertCard(CardInsertParam cardInsertParam, Long memberId) {
        LOGGER.info("Mono<CardInfo> insertCard(CardInsertParam cardInsertParam, Long memberId), addressInsertParam = {}, memberId = {}",
                cardInsertParam, memberId);
        if (cardInsertParam == null)
            throw new BlueException(EMPTY_PARAM);
        cardInsertParam.asserts();
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        return synchronizedProcessor.handleSupWithLock(CARD_UPDATE_SYNC_KEY_GEN.apply(memberId), () -> {
                    Card probe = new Card();
                    probe.setMemberId(memberId);

                    return cardRepository.count(Example.of(probe))
                            .switchIfEmpty(defer(() -> just(0L)))
                            .flatMap(count -> {
                                if (count >= MAX_ADDRESS)
                                    return error(new BlueException(DATA_ALREADY_EXIST));

                                return CARD_INSERT_PARAM_2_MEMBER_ADDRESS.apply(cardInsertParam, memberId);
                            })
                            .flatMap(cardRepository::insert)
                            .flatMap(c -> just(CARD_2_CARD_INFO.apply(c)));
                }
        );
    }

    /**
     * update a exist card
     *
     * @param cardUpdateParam
     * @param memberId
     * @return
     */
    @Override
    public Mono<CardInfo> updateCard(CardUpdateParam cardUpdateParam, Long memberId) {
        LOGGER.info("Mono<CardInfo> updateCard(CardUpdateParam cardUpdateParam, Long memberId), cardUpdateParam = {}, memberId = {}", cardUpdateParam, memberId);
        if (isNull(cardUpdateParam))
            throw new BlueException(EMPTY_PARAM);
        cardUpdateParam.asserts();
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        return synchronizedProcessor.handleSupWithLock(CARD_UPDATE_SYNC_KEY_GEN.apply(memberId), () ->
                cardRepository.findById(cardUpdateParam.getId())
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                        .flatMap(card -> {
                            if (!card.getMemberId().equals(memberId))
                                return error(new BlueException(DATA_NOT_BELONG_TO_YOU));

                            return CARD_UPDATE_PARAM_2_MEMBER_ADDRESS.apply(cardUpdateParam, card);
                        })
                        .flatMap(cardRepository::save)
                        .flatMap(c -> just(CARD_2_CARD_INFO.apply(c)))
        );
    }

    /**
     * delete card
     *
     * @param id
     * @param memberId
     * @return
     */
    @Override
    public Mono<CardInfo> deleteCard(Long id, Long memberId) {
        LOGGER.info("Mono<CardInfo> deleteCard(Long id, Long memberId), id = {}, memberId = {}", id, memberId);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        return synchronizedProcessor.handleSupWithLock(CARD_UPDATE_SYNC_KEY_GEN.apply(memberId), () ->
                cardRepository.findById(id)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                        .flatMap(card -> {
                            if (!card.getMemberId().equals(memberId))
                                return error(new BlueException(DATA_NOT_BELONG_TO_YOU));

                            return cardRepository.delete(card)
                                    .then(just(card));
                        })
                        .flatMap(c -> just(CARD_2_CARD_INFO.apply(c)))
        );
    }

    /**
     * query card mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Card> getCardMono(Long id) {
        LOGGER.info("Mono<Card> getCardMono(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return cardRepository.findById(id);
    }

    /**
     * query card mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<Card>> selectCardMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<List<Card>> selectCardMonoByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        Card probe = new Card();
        probe.setMemberId(memberId);

        return cardRepository.findAll(Example.of(probe)).collectList();
    }

    /**
     * query card info mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<CardInfo>> selectCardInfoMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<List<AddressInfo>> selectAddressInfoMonoByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        return selectCardMonoByMemberId(memberId)
                .flatMap(cis -> just(CARDS_2_CARDS_INFO.apply(cis)));
    }

    /**
     * query card info by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<CardInfo> selectCardInfoMonoByPrimaryKeyWithAssert(Long id) {
        LOGGER.info("Mono<CardInfo> selectCardInfoMonoByPrimaryKeyWithAssert(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return just(id)
                .flatMap(this::getCardMono)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(c -> {
                    if (isInvalidStatus(c.getStatus()))
                        return error(() -> new BlueException(DATA_NOT_EXIST));
                    LOGGER.info("c = {}", c);
                    return just(c);
                }).flatMap(c ->
                        just(CARD_2_CARD_INFO.apply(c))
                );
    }

    /**
     * select card info by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<CardInfo>> selectCardInfoMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<CardInfo>> selectCardInfoMonoByIds(List<Long> ids), ids = {}", ids);

        return isValidIdentities(ids) ?
                fromIterable(allotByMax(ids, (int) DB_SELECT.value, false))
                        .map(shardIds -> cardRepository.findAllById(shardIds)
                                .map(CARD_2_CARD_INFO))
                        .reduce(Flux::concat)
                        .flatMap(Flux::collectList)
                :
                just(emptyList());
    }

    /**
     * select card by page and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    @Override
    public Mono<List<Card>> selectCardMonoByLimitAndQuery(Long limit, Long rows, Query query) {
        LOGGER.info("Mono<List<Card>> selectCardMonoByLimitAndQuery(Long limit, Long rows, Query query), " +
                "limit = {}, rows = {}, query = {}", limit, rows, query);

        if (isInvalidLimit(limit) || isInvalidRows(rows))
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, Card.class).collectList();
    }

    /**
     * count card by query
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countCardMonoByQuery(Query query) {
        LOGGER.info("Mono<Long> countCardMonoByQuery(Query query), query = {}", query);
        return reactiveMongoTemplate.count(query, Card.class);
    }

    /**
     * select card detail info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<CardDetailInfo>> selectCardDetailInfoPageMonoByPageAndCondition(PageModelRequest<CardCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<RoleInfo>> selectAttachmentDetailInfoPageMonoByPageAndCondition(PageModelRequest<AttachmentCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(selectCardMonoByLimitAndQuery(pageModelRequest.getLimit(), pageModelRequest.getRows(), query), countCardMonoByQuery(query))
                .flatMap(tuple2 -> {
                    List<Card> cards = tuple2.getT1();
                    return isNotEmpty(cards) ?
                            memberBasicService.selectMemberBasicInfoMonoByIds(cards.parallelStream().map(Card::getMemberId).collect(toList()))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(cards.stream().map(c ->
                                                        CARD_2_CARD_DETAIL_INFO_CONVERTER.apply(c, ofNullable(idAndNameMapping.get(c.getMemberId())).orElse(EMPTY_DATA.value)))
                                                .collect(toList()));
                                    }).flatMap(cardDetailInfos ->
                                            just(new PageModelResponse<>(cardDetailInfos, tuple2.getT2())))
                            :
                            just(new PageModelResponse<>(emptyList(), tuple2.getT2()));
                });
    }

}