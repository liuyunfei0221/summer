package com.blue.portal.converter;

import com.blue.base.model.exps.BlueException;
import com.blue.portal.api.model.BulletinInfo;
import com.blue.portal.repository.entity.Bulletin;

import java.util.List;
import java.util.function.Function;

import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * model converters in portal project
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class PortalModelConverters {

    /**
     * bulletin -> bulletinInfo
     */
    public static final Function<Bulletin, BulletinInfo> BULLETIN_2_BULLETIN_INFO_CONVERTER = bulletin -> {
        if (bulletin == null)
            throw new BlueException(EMPTY_PARAM);

        return new BulletinInfo(bulletin.getId(), bulletin.getTitle(), bulletin.getContent(), bulletin.getLink(), bulletin.getType());
    };

    /**
     * bulletins -> bulletinInfos
     */
    public static final Function<List<Bulletin>, List<BulletinInfo>> BULLETINS_2_BULLETIN_INFOS_CONVERTER = bls ->
            bls != null && bls.size() > 0 ? bls.stream()
                    .map(BULLETIN_2_BULLETIN_INFO_CONVERTER)
                    .collect(toList()) : emptyList();

}
