package com.blue.portal.converter;

import com.blue.base.model.exps.BlueException;
import com.blue.portal.api.model.BulletinInfo;
import com.blue.portal.api.model.BulletinManagerInfo;
import com.blue.portal.repository.entity.Bulletin;

import java.util.List;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * model converters in portal project
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavadocDeclaration"})
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

}
