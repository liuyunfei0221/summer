package com.blue.agreement.converter;

import com.blue.agreement.api.model.AgreementInfo;
import com.blue.agreement.model.AgreementInsertParam;
import com.blue.agreement.model.AgreementManagerInfo;
import com.blue.agreement.repository.entity.Agreement;
import com.blue.basic.model.exps.BlueException;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static java.util.Optional.ofNullable;

/**
 * model converters in agreement project
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces"})
public final class AgreementModelConverters {

    public static final Function<Agreement, AgreementInfo> AGREEMENT_2_AGREEMENT_INFO = agreement -> {
        if (isNull(agreement))
            throw new BlueException(EMPTY_PARAM);

        return new AgreementInfo(agreement.getId(), agreement.getTitle(), agreement.getContent(), agreement.getLink(), agreement.getType(),
                agreement.getCreateTime(), agreement.getCreator());
    };

    public static final Function<AgreementInsertParam, Agreement> AGREEMENT_INSERT_PARAM_2_AGREEMENT_CONVERTER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Agreement agreement = new Agreement();

        agreement.setTitle(param.getTitle());
        agreement.setContent(param.getContent());
        agreement.setLink(param.getLink());
        agreement.setType(param.getType());
        Long stamp = TIME_STAMP_GETTER.get();

        agreement.setCreateTime(stamp);

        return agreement;
    };

    public static final BiFunction<Agreement, Map<Long, String>, AgreementManagerInfo> AGREEMENT_2_AGREEMENT_MANAGER_INFO = (agreement, idAndMemberNameMapping) -> {
        if (isNull(agreement))
            throw new BlueException(EMPTY_PARAM);

        return new AgreementManagerInfo(agreement.getId(), agreement.getTitle(), agreement.getContent(), agreement.getLink(), agreement.getType(), agreement.getCreateTime(),
                agreement.getCreator(), ofNullable(idAndMemberNameMapping).map(m -> m.get(agreement.getCreator())).orElse(EMPTY_VALUE.value));
    };

}
