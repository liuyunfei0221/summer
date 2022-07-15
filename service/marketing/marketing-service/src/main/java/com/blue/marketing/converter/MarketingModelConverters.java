package com.blue.marketing.converter;

import com.blue.basic.model.exps.BlueException;
import com.blue.marketing.api.model.EventRecordInfo;
import com.blue.marketing.repository.entity.EventRecord;

import java.util.function.BiFunction;

import static com.blue.basic.common.base.BlueChecker.isNotBlank;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;

/**
 * model converters in marketing project
 *
 * @author liuyunfei
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class MarketingModelConverters {

    /**
     * event record -> event record info
     */
    public static final BiFunction<EventRecord, String, EventRecordInfo> EVENT_RECORD_2_EVENT_RECORD_INFO_CONVERTER = (eventRecord, creatorName) -> {
        if (isNull(eventRecord))
            throw new BlueException(EMPTY_PARAM);

        return new EventRecordInfo(eventRecord.getId(), eventRecord.getType(),
                eventRecord.getData(), eventRecord.getStatus(), eventRecord.getCreateTime(), eventRecord.getCreator(), isNotBlank(creatorName) ? creatorName : EMPTY_DATA.value);
    };

}
