package com.blue.marketing.converter;

import com.blue.base.model.exps.BlueException;
import com.blue.marketing.api.model.EventRecordInfo;
import com.blue.marketing.repository.entity.EventRecord;

import java.util.function.BiFunction;

import static com.blue.base.common.base.BlueChecker.isNotBlank;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;

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
                eventRecord.getData(), eventRecord.getStatus(), eventRecord.getCreateTime(), eventRecord.getCreator(), isNotBlank(creatorName) ? creatorName : "");
    };

}
