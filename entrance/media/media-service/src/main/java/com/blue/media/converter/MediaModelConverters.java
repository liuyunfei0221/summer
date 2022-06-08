package com.blue.media.converter;

import com.blue.base.model.exps.BlueException;
import com.blue.media.api.model.AttachmentInfo;
import com.blue.media.api.model.DownloadHistoryInfo;
import com.blue.media.repository.entity.Attachment;
import com.blue.media.repository.entity.DownloadHistory;

import java.util.function.BiFunction;

import static com.blue.base.common.base.BlueChecker.isNotBlank;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;

/**
 * model converters in media project
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavadocDeclaration"})
public final class MediaModelConverters {

    /**
     * attachment -> attachment info
     */
    public static final BiFunction<Attachment, String, AttachmentInfo> ATTACHMENT_2_ATTACHMENT_INFO_CONVERTER = (attachment, creatorName) -> {
        if (isNull(attachment))
            throw new BlueException(EMPTY_PARAM);

        return new AttachmentInfo(attachment.getId(), attachment.getName(), attachment.getSize(),
                attachment.getStatus(), attachment.getCreateTime(), attachment.getCreator(), isNotBlank(creatorName) ? creatorName : EMPTY_DATA.value);
    };

    /**
     * downloadHistory -> downloadHistory info
     *
     * @param downloadHistory
     * @param attachmentName
     * @param creatorName
     * @return
     */
    public static DownloadHistoryInfo downloadHistoryToDownloadHistoryInfo(DownloadHistory downloadHistory, String attachmentName, String creatorName) {
        if (isNull(downloadHistory))
            throw new BlueException(EMPTY_PARAM);

        return new DownloadHistoryInfo(downloadHistory.getId(), downloadHistory.getAttachmentId(), isNotBlank(attachmentName) ? attachmentName : EMPTY_DATA.value, downloadHistory.getCreateTime(),
                downloadHistory.getCreator(), isNotBlank(creatorName) ? creatorName : EMPTY_DATA.value);
    }

}
