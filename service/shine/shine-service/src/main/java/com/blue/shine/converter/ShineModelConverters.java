package com.blue.shine.converter;

import com.blue.basic.model.exps.BlueException;
import com.blue.shine.api.model.ShineInfo;
import com.blue.shine.repository.entity.Shine;

import java.util.List;
import java.util.function.Function;

import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * model converters in shine project
 *
 * @author liuyunfei
 */
public final class ShineModelConverters {

    public static final Function<Shine, ShineInfo> SHINE_2_SHINE_INFO = shine -> {
        if (shine != null)
            return new ShineInfo(shine.getId(), shine.getTitle(), shine.getContent(), shine.getDetail(), shine.getContact(), shine.getContactDetail(), shine.getCountryId(), shine.getCountry(),
                    shine.getStateId(), shine.getState(), shine.getCityId(), shine.getCity(), shine.getAddressDetail(), shine.getExtra(), shine.getPriority(), shine.getCreateTime());

        throw new BlueException(EMPTY_PARAM);
    };

    public static final Function<List<Shine>, List<ShineInfo>> SHINES_2_SHINES_INFO = ss ->
            ss != null && ss.size() > 0 ? ss.stream()
                    .map(SHINE_2_SHINE_INFO)
                    .collect(toList()) : emptyList();

}
