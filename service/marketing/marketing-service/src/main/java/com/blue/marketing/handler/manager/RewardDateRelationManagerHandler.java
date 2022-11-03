package com.blue.marketing.handler.manager;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.common.MonthParam;
import com.blue.basic.model.exps.BlueException;
import com.blue.marketing.model.RewardDateRelationBatchInsertParam;
import com.blue.marketing.model.RewardDateRelationInsertParam;
import com.blue.marketing.model.RewardDateRelationUpdateParam;
import com.blue.marketing.service.inter.MarketingControlService;
import com.blue.marketing.service.inter.RewardDateRelationService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.common.base.PathVariableGetter.getLongVariableReact;
import static com.blue.basic.constant.common.PathVariable.ID;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.marketing.constant.MarketingTypeReference.PAGE_MODEL_FOR_REWARD_DATE_RELATION_CONDITION_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * reward date relation manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class RewardDateRelationManagerHandler {

    private final MarketingControlService marketingControlService;

    private final RewardDateRelationService rewardDateRelationService;

    public RewardDateRelationManagerHandler(MarketingControlService marketingControlService, RewardDateRelationService rewardDateRelationService) {
        this.marketingControlService = marketingControlService;
        this.rewardDateRelationService = rewardDateRelationService;
    }

    /**
     * create a new relation
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(RewardDateRelationInsertParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> marketingControlService.insertRewardDateRelation(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ri, serverRequest), BlueResponse.class));
    }

    /**
     * create new relations by date
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insertByDate(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(RewardDateRelationBatchInsertParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> marketingControlService.insertRewardDateRelationMonoByYearAndMonth(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ri, serverRequest), BlueResponse.class));
    }

    /**
     * update relation
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(RewardDateRelationUpdateParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> marketingControlService.updateRewardDateRelation(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ri, serverRequest), BlueResponse.class));
    }

    /**
     * delete relation
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return getLongVariableReact(serverRequest, ID.key)
                .flatMap(marketingControlService::deleteRewardDateRelation)
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ri, serverRequest), BlueResponse.class));
    }

    /**
     * select relation
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> page(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_REWARD_DATE_RELATION_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(rewardDateRelationService::selectRewardManagerInfoPageByPageAndCondition)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(pmr, serverRequest), BlueResponse.class));
    }

    /**
     * select relation by date
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectByDate(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(MonthParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(rewardDateRelationService::selectRewardDateRelationByYearAndMonth)
                .flatMap(l ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(l, serverRequest), BlueResponse.class));
    }

}
