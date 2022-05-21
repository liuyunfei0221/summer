package com.blue.portal.converter;

import com.blue.base.model.exps.BlueException;
import com.blue.portal.api.model.BulletinInfo;
import com.blue.portal.api.model.BulletinManagerInfo;
import com.blue.portal.api.model.StyleInfo;
import com.blue.portal.api.model.StyleManagerInfo;
import com.blue.portal.model.BulletinInsertParam;
import com.blue.portal.model.StyleInsertParam;
import com.blue.portal.repository.entity.Bulletin;
import com.blue.portal.repository.entity.Style;

import java.util.List;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.constant.base.BlueBoolean.FALSE;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.Status.VALID;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * model converters in portal project
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavadocDeclaration", "unused"})
public final class PortalModelConverters {

    /**
     * bulletin -> bulletinInfo
     */
    public static final Function<Bulletin, BulletinInfo> BULLETIN_2_BULLETIN_INFO_CONVERTER = bulletin -> {
        if (isNull(bulletin))
            throw new BlueException(EMPTY_PARAM);

        return new BulletinInfo(bulletin.getId(), bulletin.getTitle(), bulletin.getContent(), bulletin.getLink(),
                bulletin.getType(), bulletin.getPriority(), bulletin.getActiveTime(), bulletin.getExpireTime());
    };

    /**
     * bulletins -> bulletinInfos
     */
    public static final Function<List<Bulletin>, List<BulletinInfo>> BULLETINS_2_BULLETIN_INFOS_CONVERTER = bls ->
            isNotNull(bls) && bls.size() > 0 ? bls.stream()
                    .map(BULLETIN_2_BULLETIN_INFO_CONVERTER)
                    .collect(toList()) : emptyList();

    /**
     * style -> styleInfo
     */
    public static final Function<Style, StyleInfo> STYLE_2_STYLE_INFO_CONVERTER = style -> {
        if (isNull(style))
            throw new BlueException(EMPTY_PARAM);

        return new StyleInfo(style.getId(), style.getName(), style.getAttributes(), style.getType(), style.getStatus());
    };

    /**
     * styles -> styleInfos
     */
    public static final Function<List<Style>, List<StyleInfo>> STYLES_2_STYLE_INFOS_CONVERTER = sts ->
            isNotNull(sts) && sts.size() > 0 ? sts.stream()
                    .map(STYLE_2_STYLE_INFO_CONVERTER)
                    .collect(toList()) : emptyList();

    /**
     * bulletin insert param -> bulletin
     */
    public static final Function<BulletinInsertParam, Bulletin> BULLETIN_INSERT_PARAM_2_BULLETIN_CONVERTER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Long stamp = TIME_STAMP_GETTER.get();

        Bulletin bulletin = new Bulletin();

        bulletin.setTitle(param.getTitle());
        bulletin.setContent(param.getContent());
        bulletin.setLink(param.getLink());
        bulletin.setType(param.getType());
        bulletin.setStatus(VALID.status);
        bulletin.setPriority(param.getPriority());
        bulletin.setActiveTime(param.getActiveTime());
        bulletin.setExpireTime(param.getExpireTime());
        bulletin.setCreateTime(stamp);
        bulletin.setUpdateTime(stamp);

        return bulletin;
    };

    /**
     * style insert param -> style
     */
    public static final Function<StyleInsertParam, Style> STYLE_INSERT_PARAM_2_STYLE_CONVERTER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Long stamp = TIME_STAMP_GETTER.get();

        Style style = new Style();

        style.setName(param.getName());
        style.setAttributes(param.getAttributes());
        style.setType(param.getType());
        style.setIsActive(FALSE.bool);
        style.setStatus(VALID.status);
        style.setCreateTime(stamp);
        style.setUpdateTime(stamp);

        return style;
    };

    /**
     * bulletin -> bulletin manager indo
     *
     * @param bulletin
     * @param creatorName
     * @param updaterName
     * @return
     */
    public static BulletinManagerInfo bulletinToBulletinManagerInfo(Bulletin bulletin, String creatorName, String updaterName) {
        if (isNull(bulletin))
            throw new BlueException(EMPTY_PARAM);

        return new BulletinManagerInfo(bulletin.getId(), bulletin.getTitle(), bulletin.getContent(), bulletin.getLink(), bulletin.getType(), bulletin.getStatus(),
                bulletin.getPriority(), bulletin.getActiveTime(), bulletin.getExpireTime(), bulletin.getCreateTime(), bulletin.getUpdateTime(),
                bulletin.getCreator(), isNotBlank(creatorName) ? creatorName : "", bulletin.getUpdater(), isNotBlank(updaterName) ? updaterName : "");

    }

    /**
     * style -> style manager indo
     *
     * @param style
     * @param creatorName
     * @param updaterName
     * @return
     */
    public static StyleManagerInfo styleToStyleManagerInfo(Style style, String creatorName, String updaterName) {
        if (isNull(style))
            throw new BlueException(EMPTY_PARAM);

        return new StyleManagerInfo(style.getId(), style.getName(), style.getAttributes(), style.getType(), style.getIsActive(), style.getStatus(),
                style.getCreateTime(), style.getUpdateTime(), style.getCreator(), isNotBlank(creatorName) ? creatorName : "",
                style.getUpdater(), isNotBlank(updaterName) ? updaterName : "");
    }

}
