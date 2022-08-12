package com.blue.member.service.inter;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.member.api.model.CardDetailInfo;
import com.blue.member.api.model.CardInfo;
import com.blue.member.model.CardCondition;
import com.blue.member.model.CardInsertParam;
import com.blue.member.model.CardUpdateParam;
import com.blue.member.repository.entity.Card;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * card service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface CardService {

    /**
     * insert card
     *
     * @param cardInsertParam
     * @param memberId
     * @return
     */
    Mono<CardInfo> insertCard(CardInsertParam cardInsertParam, Long memberId);

    /**
     * update a exist card
     *
     * @param cardUpdateParam
     * @param memberId
     * @return
     */
    Mono<CardInfo> updateCard(CardUpdateParam cardUpdateParam, Long memberId);

    /**
     * delete card
     *
     * @param id
     * @param memberId
     * @return
     */
    Mono<CardInfo> deleteCard(Long id, Long memberId);

    /**
     * query card mono by id
     *
     * @param id
     * @return
     */
    Mono<Card> getCardMono(Long id);

    /**
     * query card mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<List<Card>> selectCardMonoByMemberId(Long memberId);

    /**
     * query card info mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<List<CardInfo>> selectCardInfoMonoByMemberId(Long memberId);

    /**
     * query card info by id with assert
     *
     * @param id
     * @return
     */
    Mono<CardInfo> getCardInfoMonoByPrimaryKeyWithAssert(Long id);

    /**
     * select card info by ids
     *
     * @param ids
     * @return
     */
    Mono<List<CardInfo>> selectCardInfoMonoByIds(List<Long> ids);

    /**
     * select card by page and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    Mono<List<Card>> selectCardMonoByLimitAndQuery(Long limit, Long rows, Query query);

    /**
     * count card by query
     *
     * @param query
     * @return
     */
    Mono<Long> countCardMonoByQuery(Query query);

    /**
     * select card detail info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<CardDetailInfo>> selectCardDetailInfoPageMonoByPageAndCondition(PageModelRequest<CardCondition> pageModelRequest);

}
