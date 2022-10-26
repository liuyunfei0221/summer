package com.blue.agreement.converter;

import com.blue.agreement.api.model.AgreementInfo;
import com.blue.agreement.repository.entity.Agreement;
import com.blue.basic.model.exps.BlueException;

import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;

/**
 * model converters in agreement project
 *
 * @author liuyunfei
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class AgreementModelConverters {

    public static final Function<Agreement, AgreementInfo> AGREEMENT_2_AGREEMENT_INFO = agreement -> {
        if (isNull(agreement))
            throw new BlueException(EMPTY_PARAM);

        return new AgreementInfo(agreement.getId(), agreement.getTitle(), agreement.getContent(), agreement.getLink(), agreement.getType(),
                agreement.getStatus(), agreement.getPriority(), agreement.getCreateTime(), agreement.getUpdateTime(), agreement.getCreator(), agreement.getUpdater());
    };
//
//    public static final Function<LinkInsertParam, Link> LINK_INSERT_PARAM_2_LINK = linkInsertParam -> {
//        if (isNull(linkInsertParam))
//            throw new BlueException(EMPTY_PARAM);
//        linkInsertParam.asserts();
//
//        Long stamp = TIME_STAMP_GETTER.get();
//        Link link = new Link();
//
//        link.setLinkUrl(linkInsertParam.getLinkUrl());
//        link.setContent(ofNullable(linkInsertParam.getContent()).orElse(EMPTY_VALUE.value));
//
//        link.setFavorites(0L);
//        link.setReadings(0L);
//        link.setComments(0L);
//        link.setLikes(0L);
//        link.setBoring(0L);
//
//        link.setStatus(VALID.status);
//        link.setCreateTime(stamp);
//
//        return link;
//    };

}
