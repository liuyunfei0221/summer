package com.blue.media.converter;

import com.blue.auth.api.model.RoleInfo;
import com.blue.base.model.exps.BlueException;
import com.blue.media.api.model.*;
import com.blue.media.model.QrCodeConfigManagerInfo;
import com.blue.media.repository.entity.Attachment;
import com.blue.media.repository.entity.DownloadHistory;
import com.blue.media.repository.entity.QrCodeConfig;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isNotBlank;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * model converters in media project
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavadocDeclaration"})
public final class MediaModelConverters {

    /**
     * attachment -> attachment upload info
     */
    public static final Function<Attachment, AttachmentUploadInfo> ATTACHMENT_2_ATTACHMENT_UPLOAD_INFO_CONVERTER = attachment -> {
        if (isNull(attachment))
            throw new BlueException(EMPTY_PARAM);

        return new AttachmentUploadInfo(attachment.getId(), attachment.getType(), attachment.getName(), attachment.getSize());
    };

    /**
     * attachments -> attachment upload infos
     */
    public static final Function<List<Attachment>, List<AttachmentUploadInfo>> ATTACHMENTS_2_ATTACHMENT_UPLOAD_INFOS_CONVERTER = attachments ->
            attachments != null && attachments.size() > 0 ? attachments.stream()
                    .map(ATTACHMENT_2_ATTACHMENT_UPLOAD_INFO_CONVERTER)
                    .collect(toList()) : emptyList();

    /**
     * attachment -> attachment info
     */
    public static final Function<Attachment, AttachmentInfo> ATTACHMENT_2_ATTACHMENT_INFO_CONVERTER = attachment -> {
        if (isNull(attachment))
            throw new BlueException(EMPTY_PARAM);

        return new AttachmentInfo(attachment.getId(), attachment.getType(), attachment.getLink(), attachment.getName(), attachment.getFileType(), attachment.getSize(), attachment.getStatus(), attachment.getCreateTime(), attachment.getCreator());
    };

    /**
     * attachment -> attachment detail info
     */
    public static final BiFunction<Attachment, String, AttachmentDetailInfo> ATTACHMENT_2_ATTACHMENT_DETAIL_INFO_CONVERTER = (attachment, creatorName) -> {
        if (isNull(attachment))
            throw new BlueException(EMPTY_PARAM);

        return new AttachmentDetailInfo(attachment.getId(), attachment.getType(), attachment.getLink(), attachment.getName(), attachment.getSize(),
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

    /**
     * qr code config -> qr code config info
     */
    public static final Function<QrCodeConfig, QrCodeConfigInfo> QR_CODE_CONFIG_2_QR_CODE_CONFIG_INFO_CONVERTER = qrCodeConfig -> {
        if (isNull(qrCodeConfig))
            throw new BlueException(EMPTY_PARAM);

        return new QrCodeConfigInfo(qrCodeConfig.getId(), qrCodeConfig.getName(), qrCodeConfig.getDescription(), qrCodeConfig.getType(), qrCodeConfig.getGenHandlerType(),
                qrCodeConfig.getDomain(), qrCodeConfig.getPathToBeFilled(), qrCodeConfig.getPlaceholderCount(), qrCodeConfig.getAllowedRoles(), qrCodeConfig.getStatus());
    };

    /**
     * config -> config manager indo
     *
     * @param qrCodeConfig
     * @param idAndRoleInfoMapping
     * @param idAndMemberNameMapping
     * @return
     */
    public static QrCodeConfigManagerInfo qrCodeConfigToQrCodeConfigManagerInfo(QrCodeConfig qrCodeConfig, Map<Long, RoleInfo> idAndRoleInfoMapping, Map<Long, String> idAndMemberNameMapping) {
        if (isNull(qrCodeConfig) || isNull(idAndRoleInfoMapping) || isNull(idAndMemberNameMapping))
            throw new BlueException(EMPTY_PARAM);

        return new QrCodeConfigManagerInfo(
                qrCodeConfig.getId(), qrCodeConfig.getName(), qrCodeConfig.getDescription(), qrCodeConfig.getType(), qrCodeConfig.getGenHandlerType(),
                qrCodeConfig.getDomain(), qrCodeConfig.getPathToBeFilled(), qrCodeConfig.getPlaceholderCount(), qrCodeConfig.getAllowedRoles().stream().map(idAndRoleInfoMapping::get).collect(toList()), qrCodeConfig.getStatus(),
                qrCodeConfig.getCreateTime(), qrCodeConfig.getUpdateTime(), qrCodeConfig.getCreator(), ofNullable(idAndMemberNameMapping.get(qrCodeConfig.getCreator())).orElse(EMPTY_DATA.value),
                qrCodeConfig.getUpdater(), ofNullable(idAndMemberNameMapping.get(qrCodeConfig.getUpdater())).orElse(EMPTY_DATA.value));
    }

}
