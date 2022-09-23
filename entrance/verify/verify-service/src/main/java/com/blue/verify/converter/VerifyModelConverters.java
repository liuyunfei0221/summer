package com.blue.verify.converter;

import com.blue.basic.model.exps.BlueException;
import com.blue.verify.api.model.VerifyHistoryInfo;
import com.blue.verify.api.model.VerifyTemplateInfo;
import com.blue.verify.model.VerifyTemplateInsertParam;
import com.blue.verify.model.VerifyTemplateManagerInfo;
import com.blue.verify.repository.entity.VerifyHistory;
import com.blue.verify.repository.entity.VerifyTemplate;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * model converters in verify project
 *
 * @author liuyunfei
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class VerifyModelConverters {

    /**
     * verify template insert param -> verify template
     */
    public static final Function<VerifyTemplateInsertParam, VerifyTemplate> VERIFY_TEMPLATE_INSERT_PARAM_2_VERIFY_TEMPLATE_CONVERTER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        VerifyTemplate verifyTemplate = new VerifyTemplate();

        verifyTemplate.setName(param.getName());
        verifyTemplate.setDescription(param.getDescription());
        verifyTemplate.setType(param.getType());
        verifyTemplate.setBusinessType(param.getBusinessType());
        verifyTemplate.setTitle(param.getTitle());
        verifyTemplate.setContent(param.getContent());
        
        Long stamp = TIME_STAMP_GETTER.get();
        verifyTemplate.setCreateTime(stamp);
        verifyTemplate.setUpdateTime(stamp);

        return verifyTemplate;
    };

    /**
     * verify template -> verify template info
     */
    public static final Function<VerifyTemplate, VerifyTemplateInfo> VERIFY_TEMPLATE_2_VERIFY_TEMPLATE_INFO_CONVERTER = verifyTemplate -> {
        if (isNull(verifyTemplate))
            throw new BlueException(EMPTY_PARAM);

        return new VerifyTemplateInfo(verifyTemplate.getId(), verifyTemplate.getName(), verifyTemplate.getDescription(), verifyTemplate.getType(),
                verifyTemplate.getBusinessType(), verifyTemplate.getTitle(), verifyTemplate.getContent());
    };

    /**
     * verify template -> verify template manager info
     */
    public static final BiFunction<VerifyTemplate, Map<Long, String>, VerifyTemplateManagerInfo> VERIFY_TEMPLATE_2_VERIFY_TEMPLATE_MANAGER_INFO_CONVERTER = (verifyTemplate, idAndMemberNameMapping) -> {
        if (isNull(verifyTemplate) || isNull(idAndMemberNameMapping))
            throw new BlueException(EMPTY_PARAM);

        return new VerifyTemplateManagerInfo(verifyTemplate.getId(), verifyTemplate.getName(), verifyTemplate.getDescription(), verifyTemplate.getType(), verifyTemplate.getBusinessType(), verifyTemplate.getTitle(), verifyTemplate.getContent(),
                verifyTemplate.getCreateTime(), verifyTemplate.getUpdateTime(), verifyTemplate.getCreator(), ofNullable(idAndMemberNameMapping.get(verifyTemplate.getCreator())).orElse(EMPTY_VALUE.value), verifyTemplate.getUpdater(),
                ofNullable(idAndMemberNameMapping.get(verifyTemplate.getUpdater())).orElse(EMPTY_VALUE.value));
    };

    /**
     * verify history -> verify history info
     */
    public static final Function<VerifyHistory, VerifyHistoryInfo> VERIFY_HISTORY_2_VERIFY_HISTORY_INFO_CONVERTER = verifyHistory -> {
        if (isNull(verifyHistory))
            throw new BlueException(EMPTY_PARAM);

        return new VerifyHistoryInfo(verifyHistory.getId(), verifyHistory.getVerifyType(), verifyHistory.getBusinessType(),
                verifyHistory.getDestination(), verifyHistory.getVerify(), verifyHistory.getRequestIp(), verifyHistory.getCreateTime());
    };

    /**
     * verify histories -> verify history infos
     */
    public static final Function<List<VerifyHistory>, List<VerifyHistoryInfo>> VERIFY_HISTORIES_2_VERIFY_HISTORY_INFOS_CONVERTER = verifyHistories ->
            verifyHistories != null && verifyHistories.size() > 0 ? verifyHistories.stream()
                    .map(VERIFY_HISTORY_2_VERIFY_HISTORY_INFO_CONVERTER)
                    .collect(toList()) : emptyList();

}
