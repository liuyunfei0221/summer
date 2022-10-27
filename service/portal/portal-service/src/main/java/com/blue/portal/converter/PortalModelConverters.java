package com.blue.portal.converter;

import com.blue.basic.model.exps.BlueException;
import com.blue.portal.api.model.BulletinInfo;
import com.blue.portal.api.model.BulletinManagerInfo;
import com.blue.portal.api.model.NoticeInfo;
import com.blue.portal.api.model.NoticeManagerInfo;
import com.blue.portal.model.BulletinInsertParam;
import com.blue.portal.model.NoticeInsertParam;
import com.blue.portal.repository.entity.Bulletin;
import com.blue.portal.repository.entity.Notice;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.basic.constant.common.Status.VALID;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * model converters in portal project
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused"})
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
     * notice -> noticeInfo
     */
    public static final Function<Notice, NoticeInfo> NOTICE_2_NOTICE_INFO_CONVERTER = notice -> {
        if (isNull(notice))
            throw new BlueException(EMPTY_PARAM);

        return new NoticeInfo(notice.getId(), notice.getTitle(), notice.getContent(), notice.getLink(), notice.getType());
    };

    /**
     * bulletins -> bulletinInfos
     */
    public static final Function<List<Bulletin>, List<BulletinInfo>> BULLETINS_2_BULLETIN_INFOS_CONVERTER = bls ->
            isNotNull(bls) && bls.size() > 0 ? bls.stream()
                    .map(BULLETIN_2_BULLETIN_INFO_CONVERTER)
                    .collect(toList()) : emptyList();

    /**
     * notices -> noticeInfos
     */
    public static final Function<List<Notice>, List<NoticeInfo>> NOTICES_2_NOTICE_INFOS_CONVERTER = nls ->
            isNotNull(nls) && nls.size() > 0 ? nls.stream()
                    .map(NOTICE_2_NOTICE_INFO_CONVERTER)
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
     * notice insert param -> notice
     */
    public static final Function<NoticeInsertParam, Notice> NOTICE_INSERT_PARAM_2_NOTICE_CONVERTER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Notice notice = new Notice();

        notice.setTitle(param.getTitle());
        notice.setContent(param.getContent());
        notice.setLink(param.getLink());
        notice.setType(param.getType());

        Long stamp = TIME_STAMP_GETTER.get();

        notice.setCreateTime(stamp);
        notice.setUpdateTime(stamp);

        return notice;
    };

    public static final BiFunction<Bulletin, Map<Long, String>, BulletinManagerInfo> BULLETIN_2_BULLETIN_MANAGER_INFO_CONVERTER = (bulletin, idAndMemberNameMapping) -> {
        if (isNull(bulletin))
            throw new BlueException(EMPTY_PARAM);

        return new BulletinManagerInfo(bulletin.getId(), bulletin.getTitle(), bulletin.getContent(), bulletin.getLink(), bulletin.getType(), bulletin.getStatus(),
                bulletin.getPriority(), bulletin.getActiveTime(), bulletin.getExpireTime(), bulletin.getCreateTime(), bulletin.getUpdateTime(),
                bulletin.getCreator(), ofNullable(idAndMemberNameMapping).map(m -> m.get(bulletin.getCreator())).orElse(EMPTY_VALUE.value), bulletin.getUpdater(),
                ofNullable(idAndMemberNameMapping).map(m -> m.get(bulletin.getUpdater())).orElse(EMPTY_VALUE.value));
    };

    public static final BiFunction<Notice, Map<Long, String>, NoticeManagerInfo> NOTICES_2_NOTICE_MANAGER_INFOS_CONVERTER = (notice, idAndMemberNameMapping) -> {
        if (isNull(notice))
            throw new BlueException(EMPTY_PARAM);

        return new NoticeManagerInfo(notice.getId(), notice.getTitle(), notice.getContent(), notice.getLink(), notice.getType(), notice.getCreateTime(), notice.getUpdateTime(),
                notice.getCreator(), ofNullable(idAndMemberNameMapping).map(m -> m.get(notice.getCreator())).orElse(EMPTY_VALUE.value), notice.getUpdater(),
                ofNullable(idAndMemberNameMapping).map(m -> m.get(notice.getUpdater())).orElse(EMPTY_VALUE.value));
    };

}
