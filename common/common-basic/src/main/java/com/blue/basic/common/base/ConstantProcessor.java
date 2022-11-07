package com.blue.basic.common.base;

import com.blue.basic.constant.agreement.AgreementType;
import com.blue.basic.constant.analyze.StatisticsRange;
import com.blue.basic.constant.analyze.StatisticsType;
import com.blue.basic.constant.article.ArticleType;
import com.blue.basic.constant.article.SubjectType;
import com.blue.basic.constant.auth.CredentialType;
import com.blue.basic.constant.auth.DeviceType;
import com.blue.basic.constant.auth.ResourceType;
import com.blue.basic.constant.auth.RoleType;
import com.blue.basic.constant.common.*;
import com.blue.basic.constant.finance.OrderArticleStatus;
import com.blue.basic.constant.finance.OrderStatus;
import com.blue.basic.constant.finance.ReferenceAmountStatus;
import com.blue.basic.constant.marketing.RewardType;
import com.blue.basic.constant.media.AttachmentType;
import com.blue.basic.constant.media.MessageBusinessType;
import com.blue.basic.constant.media.MessageType;
import com.blue.basic.constant.media.QrCodeType;
import com.blue.basic.constant.member.ChineseZodiac;
import com.blue.basic.constant.member.Gender;
import com.blue.basic.constant.member.SourceType;
import com.blue.basic.constant.member.ZodiacSign;
import com.blue.basic.constant.portal.BulletinType;
import com.blue.basic.constant.portal.NoticeType;
import com.blue.basic.constant.portal.StyleType;
import com.blue.basic.constant.risk.RiskType;
import com.blue.basic.constant.verify.VerifyBusinessType;
import com.blue.basic.constant.verify.VerifyType;
import com.blue.basic.model.exps.BlueException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Map;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Constant Constraint Checker
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class ConstantProcessor {

    //<editor-fold desc="constant mappings">

    /**
     * boolean
     */
    private static final Map<Integer, BlueBoolean> BOOL_STATUS_MAPPING =
            of(BlueBoolean.values()).collect(toMap(b -> b.status, identity(), (a, b) -> a));

    private static final Map<Boolean, BlueBoolean> STATUS_BOOL_MAPPING =
            of(BlueBoolean.values()).collect(toMap(b -> b.bool, identity(), (a, b) -> a));

    /**
     * http response status and response element mapping
     */
    private static final Map<Integer, ResponseElement> RESPONSE_ELEMENT_MAPPING =
            of(ResponseElement.values()).collect(toMap(e -> e.status, identity(), (a, b) -> a));

    /**
     * http method name and method mapping
     */
    private static final Map<String, HttpMethod> HTTP_METHOD_MAPPING =
            of(BlueHttpMethod.values()).collect(toMap(bhm -> bhm.value.toUpperCase(), bhm -> bhm.method, (a, b) -> a));

    /**
     * media type identity and media mapping
     */
    private static final Map<String, MediaType> MEDIA_TYPE_MAPPING =
            of(BlueMediaType.values()).collect(toMap(t -> t.identity.toLowerCase(), t -> t.mediaType, (a, b) -> a));

    /**
     * file type identity and media mapping
     */
    private static final Map<String, MediaType> FILE_MEDIA_TYPE_MAPPING =
            of(BlueFileType.values()).collect(toMap(t -> t.identity.toLowerCase(), t -> t.mediaType, (a, b) -> a));

    /**
     * attachment type identity and attachment type mapping
     */
    private static final Map<Integer, AttachmentType> ATTACHMENT_TYPE_MAPPING =
            of(AttachmentType.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * message type identity and message type mapping
     */
    private static final Map<Integer, MessageType> MESSAGE_TYPE_MAPPING =
            of(MessageType.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * message business type identity and message business type mapping
     */
    private static final Map<Integer, MessageBusinessType> MESSAGE_BUSINESS_TYPE_MAPPING =
            of(MessageBusinessType.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * qr code type identity and qr code type mapping
     */
    private static final Map<Integer, QrCodeType> QR_CODE_TYPE_MAPPING =
            of(QrCodeType.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * valid resource type identity and type mapping
     */
    private static final Map<Integer, ResourceType> RESOURCE_TYPE_MAPPING =
            of(ResourceType.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * valid role type identity and type mapping
     */
    private static final Map<Integer, RoleType> ROLE_TYPE_MAPPING =
            of(RoleType.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * valid credential type identity and type mapping
     */
    private static final Map<String, CredentialType> CREDENTIAL_TYPE_MAPPING =
            of(CredentialType.values()).collect(toMap(e -> e.identity.toUpperCase(), identity(), (a, b) -> a));

    /**
     * valid device type identity and type mapping
     */
    private static final Map<String, DeviceType> DEVICE_TYPE_MAPPING =
            of(DeviceType.values()).collect(toMap(e -> e.identity.toUpperCase(), identity(), (a, b) -> a));

    /**
     * valid order status identity and order status mapping
     */
    private static final Map<Integer, OrderStatus> ORDER_STATUS_MAPPING =
            of(OrderStatus.values()).collect(toMap(o -> o.identity, identity(), (a, b) -> a));

    /**
     * valid order article status identity and order article status mapping
     */
    private static final Map<Integer, OrderArticleStatus> ORDER_ARTICLE_STATUS_MAPPING =
            of(OrderArticleStatus.values()).collect(toMap(oa -> oa.identity, identity(), (a, b) -> a));

    /**
     * valid reference amount status identity and reference amount status mapping
     */
    private static final Map<Integer, ReferenceAmountStatus> REFERENCE_AMOUNT_STATUS_MAPPING =
            of(ReferenceAmountStatus.values()).collect(toMap(oa -> oa.identity, identity(), (a, b) -> a));

    /**
     * valid gender type identity and type mapping
     */
    private static final Map<Integer, Gender> GENDER_MAPPING =
            of(Gender.values()).collect(toMap(g -> g.identity, identity(), (a, b) -> a));

    /**
     * valid source type identity and type mapping
     */
    private static final Map<String, SourceType> SOURCE_MAPPING =
            of(SourceType.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * valid chinese zodiac identity and type mapping
     */
    private static final Map<Integer, ChineseZodiac> CHINESE_ZODIAC_MAPPING =
            of(ChineseZodiac.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * valid zodiac sign identity and type mapping
     */
    private static final Map<Integer, ZodiacSign> ZODIAC_SIGN_MAPPING =
            of(ZodiacSign.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * valid status type identity and type mapping
     */
    private static final Map<Integer, Status> STATUS_MAPPING =
            of(Status.values()).collect(toMap(s -> s.status, identity(), (a, b) -> a));

    /**
     * valid sort type identity and type mapping
     */
    private static final Map<String, SortType> SORT_TYPE_MAPPING =
            of(SortType.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * valid bulletin type identity and type mapping
     */
    private static final Map<Integer, BulletinType> BULLETIN_TYPE_MAPPING =
            of(BulletinType.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * valid notice type identity and type mapping
     */
    private static final Map<Integer, NoticeType> NOTICE_TYPE_MAPPING =
            of(NoticeType.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * valid reward type identity and type mapping
     */
    private static final Map<Integer, RewardType> REWARD_TYPE_MAPPING =
            of(RewardType.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * valid style type identity and type mapping
     */
    private static final Map<Integer, StyleType> STYLE_TYPE_MAPPING =
            of(StyleType.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * valid statistics range identity and range mapping
     */
    private static final Map<String, StatisticsRange> STATISTICS_RANGE_MAPPING =
            of(StatisticsRange.values()).collect(toMap(t -> t.identity.toUpperCase(), identity(), (a, b) -> a));

    /**
     * valid statistics type identity and type mapping
     */
    private static final Map<String, StatisticsType> STATISTICS_TYPE_MAPPING =
            of(StatisticsType.values()).collect(toMap(t -> t.identity.toUpperCase(), identity(), (a, b) -> a));

    /**
     * valid subject type identity and type mapping
     */
    private static final Map<Integer, SubjectType> SUBJECT_TYPE_MAPPING =
            of(SubjectType.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * valid article type identity and type mapping
     */
    private static final Map<Integer, ArticleType> ARTICLE_TYPE_MAPPING =
            of(ArticleType.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * business verify type identity and type mapping
     */
    private static final Map<String, VerifyBusinessType> VERIFY_BUSINESS_TYPE_MAPPING =
            of(VerifyBusinessType.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * verify type identity and type mapping
     */
    private static final Map<String, VerifyType> VERIFY_TYPE_MAPPING =
            of(VerifyType.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * agreement type identity and type mapping
     */
    private static final Map<Integer, AgreementType> AGREEMENT_TYPE_MAPPING =
            of(AgreementType.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * rsa decrypt mode identity and rsa decrypt mode mapping
     */
    private static final Map<Integer, RsaDecryptMode> RSA_DECRYPT_MODE_MAPPING =
            of(RsaDecryptMode.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));

    /**
     * risk type identity and type mapping
     */
    private static final Map<Integer, RiskType> RISK_TYPE_MAPPING =
            of(RiskType.values()).collect(toMap(t -> t.identity, identity(), (a, b) -> a));
    //</editor-fold>

    //<editor-fold desc="asserter">

    /**
     * assert bool status
     *
     * @param status
     */
    public static void assertBoolStatus(Integer status, boolean nullable) {
        if (nullable && isNull(status))
            return;

        if (!BOOL_STATUS_MAPPING.containsKey(status))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert resource type
     *
     * @param identity
     */
    public static void assertResourceType(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!RESOURCE_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert role type
     *
     * @param identity
     */
    public static void assertRoleType(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!ROLE_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert http method
     *
     * @param value
     * @return
     */
    public static void assertHttpMethod(String value, boolean nullable) {
        if (nullable && isNull(value))
            return;

        if (isBlank(value) || !HTTP_METHOD_MAPPING.containsKey(value.toUpperCase()))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert media type
     *
     * @param identity
     * @return
     */
    public static void assertMediaType(String identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (isBlank(identity) || !MEDIA_TYPE_MAPPING.containsKey(identity.toLowerCase()))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert file type
     *
     * @param identity
     * @return
     */
    public static void assertFileType(String identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (isBlank(identity) || !FILE_MEDIA_TYPE_MAPPING.containsKey(identity.toLowerCase()))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert attachment type
     *
     * @param identity
     * @return
     */
    public static void assertAttachmentType(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!ATTACHMENT_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert message type
     *
     * @param identity
     * @return
     */
    public static void assertMessageType(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!MESSAGE_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert message business type
     *
     * @param identity
     * @return
     */
    public static void assertMessageBusinessType(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!MESSAGE_BUSINESS_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert qr code type
     *
     * @param identity
     * @return
     */
    public static void assertQrCodeType(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!QR_CODE_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert credential type
     *
     * @param identity
     */
    public static void assertCredentialType(String identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (isBlank(identity) || !CREDENTIAL_TYPE_MAPPING.containsKey(identity.toUpperCase()))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert device type
     *
     * @param identity
     */
    public static void assertDeviceType(String identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (isBlank(identity) || !DEVICE_TYPE_MAPPING.containsKey(identity.toUpperCase()))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert order status
     *
     * @param identity
     */
    public static void assertOrderStatus(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!ORDER_STATUS_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert order article status
     *
     * @param identity
     */
    public static void assertOrderArticleStatus(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!ORDER_ARTICLE_STATUS_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert reference amount status
     *
     * @param identity
     */
    public static void assertReferenceAmountStatus(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!REFERENCE_AMOUNT_STATUS_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert gender type
     *
     * @param identity
     */
    public static void assertGender(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!GENDER_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert source type
     *
     * @param identity
     */
    public static void assertSource(String identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!SOURCE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert chinese zodiac
     *
     * @param identity
     */
    public static void assertChineseZodiac(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!CHINESE_ZODIAC_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert zodiac sign
     *
     * @param identity
     */
    public static void assertZodiacSign(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!ZODIAC_SIGN_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert status type
     *
     * @param identity
     */
    public static void assertStatus(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!STATUS_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert sort type
     *
     * @param identity
     */
    public static void assertSortType(String identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (isBlank(identity) || !SORT_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert bulletin type
     *
     * @param identity
     */
    public static void assertBulletinType(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!BULLETIN_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert notice type
     *
     * @param identity
     */
    public static void assertNoticeType(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!NOTICE_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert reward type
     *
     * @param identity
     */
    public static void assertRewardType(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!REWARD_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert style type
     *
     * @param identity
     */
    public static void assertStyleType(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!STYLE_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert statistics range
     *
     * @param identity
     */
    public static void assertStatisticsRange(String identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (isBlank(identity) || !STATISTICS_RANGE_MAPPING.containsKey(identity.toUpperCase()))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert statistics type
     *
     * @param identity
     */
    public static void assertStatisticsType(String identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (isBlank(identity) || !STATISTICS_TYPE_MAPPING.containsKey(identity.toUpperCase()))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert subject type
     *
     * @param identity
     */
    public static void assertSubjectType(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!SUBJECT_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert article type
     *
     * @param identity
     */
    public static void assertArticleType(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!ARTICLE_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert verify business type
     *
     * @param identity
     */
    public static void assertVerifyBusinessType(String identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!VERIFY_BUSINESS_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert verify type
     *
     * @param identity
     */
    public static void assertVerifyType(String identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!VERIFY_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert agreement type
     *
     * @param identity
     */
    public static void assertAgreementType(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!AGREEMENT_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert rsa decrypt mode
     *
     * @param identity
     */
    public static void assertRsaDecryptMode(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!RSA_DECRYPT_MODE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert rsa decrypt mode
     *
     * @param identity
     */
    public static void assertRiskType(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!RISK_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }
    //</editor-fold>

    //<editor-fold desc="getter">

    /**
     * get bool by int status
     *
     * @param status
     * @return
     */
    public static BlueBoolean getBoolByStatus(Integer status) {
        if (isNull(status))
            throw new BlueException(INVALID_IDENTITY);

        BlueBoolean blueBoolean = BOOL_STATUS_MAPPING.get(status);
        if (isNull(blueBoolean))
            throw new BlueException(INVALID_IDENTITY);

        return blueBoolean;
    }

    /**
     * get bool by int bool
     *
     * @param bool
     * @return
     */
    public static BlueBoolean getBoolByBool(Boolean bool) {
        if (isNull(bool))
            throw new BlueException(INVALID_IDENTITY);

        BlueBoolean blueBoolean = STATUS_BOOL_MAPPING.get(bool);
        if (isNull(blueBoolean))
            throw new BlueException(INVALID_IDENTITY);

        return blueBoolean;
    }

    /**
     * get element by http status
     *
     * @param identity
     * @return
     */
    public static ResponseElement getResponseElementByStatus(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        ResponseElement responseElement = RESPONSE_ELEMENT_MAPPING.get(identity);
        return isNotNull(responseElement) ? responseElement : BAD_REQUEST;
    }

    /**
     * get resource type by identity
     *
     * @param identity
     * @return
     */
    public static ResourceType getResourceTypeByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        ResourceType resourceType = RESOURCE_TYPE_MAPPING.get(identity);
        if (isNull(resourceType))
            throw new BlueException(INVALID_IDENTITY);

        return resourceType;
    }

    /**
     * get role type by identity
     *
     * @param identity
     * @return
     */
    public static RoleType getRoleTypeByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        RoleType roleType = ROLE_TYPE_MAPPING.get(identity);
        if (isNull(roleType))
            throw new BlueException(INVALID_IDENTITY);

        return roleType;
    }

    /**
     * get http method by value
     *
     * @param value
     * @return
     */
    public static HttpMethod getHttpMethodByValue(String value) {
        if (isBlank(value))
            throw new BlueException(INVALID_IDENTITY);

        HttpMethod httpMethod = HTTP_METHOD_MAPPING.get(value.toUpperCase());
        if (isNull(httpMethod))
            throw new BlueException(INVALID_IDENTITY);

        return httpMethod;
    }

    /**
     * get media type by identity
     *
     * @param identity
     * @return
     */
    public static MediaType getMediaTypeByIdentity(String identity) {
        if (isBlank(identity))
            throw new BlueException(INVALID_IDENTITY);

        MediaType mediaType = MEDIA_TYPE_MAPPING.get(identity.toLowerCase());
        if (isNull(mediaType))
            throw new BlueException(INVALID_IDENTITY);

        return mediaType;
    }

    /**
     * get media type by file suffix
     *
     * @param identity
     * @return
     */
    public static MediaType getMediaTypeByFileIdentity(String identity) {
        if (isBlank(identity))
            throw new BlueException(INVALID_IDENTITY);

        MediaType mediaType = FILE_MEDIA_TYPE_MAPPING.get(identity.toLowerCase());
        if (isNull(mediaType))
            throw new BlueException(INVALID_IDENTITY);

        return mediaType;
    }

    /**
     * get attachment type by attachment identity
     *
     * @param identity
     * @return
     */
    public static AttachmentType getAttachmentTypeByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        AttachmentType attachmentType = ATTACHMENT_TYPE_MAPPING.get(identity);
        if (isNull(attachmentType))
            throw new BlueException(INVALID_IDENTITY);

        return attachmentType;
    }

    /**
     * get message type by identity
     *
     * @param identity
     * @return
     */
    public static MessageType getMessageTypeByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        MessageType messageType = MESSAGE_TYPE_MAPPING.get(identity);
        if (isNull(messageType))
            throw new BlueException(INVALID_IDENTITY);

        return messageType;
    }

    /**
     * get message business type by identity
     *
     * @param identity
     * @return
     */
    public static MessageBusinessType getMessageBusinessTypeByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        MessageBusinessType messageBusinessType = MESSAGE_BUSINESS_TYPE_MAPPING.get(identity);
        if (isNull(messageBusinessType))
            throw new BlueException(INVALID_IDENTITY);

        return messageBusinessType;
    }

    /**
     * get qr code type by identity
     *
     * @param identity
     * @return
     */
    public static QrCodeType getQrCodeTypeByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        QrCodeType qrCodeType = QR_CODE_TYPE_MAPPING.get(identity);
        if (isNull(qrCodeType))
            throw new BlueException(INVALID_IDENTITY);

        return qrCodeType;
    }

    /**
     * get credential type by identity
     *
     * @param identity
     * @return
     */
    public static CredentialType getCredentialTypeByIdentity(String identity) {
        if (isBlank(identity))
            throw new BlueException(INVALID_IDENTITY);

        CredentialType credentialType = CREDENTIAL_TYPE_MAPPING.get(identity.toUpperCase());
        if (isNull(credentialType))
            throw new BlueException(INVALID_IDENTITY);

        return credentialType;
    }

    /**
     * get device type by identity
     *
     * @param identity
     * @return
     */
    public static DeviceType getDeviceTypeByIdentity(String identity) {
        if (isBlank(identity))
            throw new BlueException(INVALID_IDENTITY);

        DeviceType deviceType = DEVICE_TYPE_MAPPING.get(identity.toUpperCase());
        if (isNull(deviceType))
            throw new BlueException(INVALID_IDENTITY);

        return deviceType;
    }

    /**
     * get order status by identity
     *
     * @param identity
     * @return
     */
    public static OrderStatus getOrderStatusByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        OrderStatus orderStatus = ORDER_STATUS_MAPPING.get(identity);
        if (isNull(orderStatus))
            throw new BlueException(INVALID_IDENTITY);

        return orderStatus;
    }

    /**
     * get order article status by identity
     *
     * @param identity
     * @return
     */
    public static OrderArticleStatus getOrderArticleStatusByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        OrderArticleStatus orderArticleStatus = ORDER_ARTICLE_STATUS_MAPPING.get(identity);
        if (isNull(orderArticleStatus))
            throw new BlueException(INVALID_IDENTITY);

        return orderArticleStatus;
    }

    /**
     * get reference amount status by identity
     *
     * @param identity
     * @return
     */
    public static ReferenceAmountStatus getReferenceAmountStatusByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        ReferenceAmountStatus referenceAmountStatus = REFERENCE_AMOUNT_STATUS_MAPPING.get(identity);
        if (isNull(referenceAmountStatus))
            throw new BlueException(INVALID_IDENTITY);

        return referenceAmountStatus;
    }

    /**
     * get gender type by identity
     *
     * @param identity
     * @return
     */
    public static Gender getGenderTypeByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        Gender gender = GENDER_MAPPING.get(identity);
        if (isNull(gender))
            throw new BlueException(INVALID_IDENTITY);

        return gender;
    }

    /**
     * get source type by identity
     *
     * @param identity
     * @return
     */
    public static SourceType getSourceTypeByIdentity(String identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        SourceType sourceType = SOURCE_MAPPING.get(identity);
        if (isNull(sourceType))
            throw new BlueException(INVALID_IDENTITY);

        return sourceType;
    }

    /**
     * get zodiac sign by identity
     *
     * @param identity
     * @return
     */
    public static ChineseZodiac getChineseZodiacByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        ChineseZodiac chineseZodiac = CHINESE_ZODIAC_MAPPING.get(identity);
        if (isNull(chineseZodiac))
            throw new BlueException(INVALID_IDENTITY);

        return chineseZodiac;
    }

    /**
     * get zodiac sign by identity
     *
     * @param identity
     * @return
     */
    public static ZodiacSign getZodiacSignByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        ZodiacSign zodiacSign = ZODIAC_SIGN_MAPPING.get(identity);
        if (isNull(zodiacSign))
            throw new BlueException(INVALID_IDENTITY);

        return zodiacSign;
    }

    /**
     * get status type by identity
     *
     * @param identity
     * @return
     */
    public static Status getStatusByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        Status status = STATUS_MAPPING.get(identity);
        if (isNull(status))
            throw new BlueException(INVALID_IDENTITY);

        return status;
    }

    /**
     * get sort type by identity
     *
     * @param identity
     * @return
     */
    public static SortType getSortTypeByIdentity(String identity) {
        if (isBlank(identity))
            throw new BlueException(INVALID_IDENTITY);

        SortType sortType = SORT_TYPE_MAPPING.get(identity);
        if (isNull(sortType))
            throw new BlueException(INVALID_IDENTITY);

        return sortType;
    }

    /**
     * get bulletin type by identity
     *
     * @param identity
     * @return
     */
    public static BulletinType getBulletinTypeByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        BulletinType type = BULLETIN_TYPE_MAPPING.get(identity);
        if (isNull(type))
            throw new BlueException(INVALID_IDENTITY);

        return type;
    }

    /**
     * get notice type by identity
     *
     * @param identity
     * @return
     */
    public static NoticeType getNoticeTypeByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        NoticeType type = NOTICE_TYPE_MAPPING.get(identity);
        if (isNull(type))
            throw new BlueException(INVALID_IDENTITY);

        return type;
    }

    /**
     * get bulletin type by identity
     *
     * @param identity
     * @return
     */
    public static RewardType getRewardTypeByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        RewardType type = REWARD_TYPE_MAPPING.get(identity);
        if (isNull(type))
            throw new BlueException(INVALID_IDENTITY);

        return type;
    }

    /**
     * get style type by identity
     *
     * @param identity
     * @return
     */
    public static StyleType getStyleTypeByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        StyleType type = STYLE_TYPE_MAPPING.get(identity);
        if (isNull(type))
            throw new BlueException(INVALID_IDENTITY);

        return type;
    }

    /**
     * get statistics range by identity
     *
     * @param identity
     * @return
     */
    public static StatisticsRange getStatisticsRangeByIdentity(String identity) {
        if (isBlank(identity))
            throw new BlueException(INVALID_IDENTITY);

        StatisticsRange range = STATISTICS_RANGE_MAPPING.get(identity.toUpperCase());
        if (isNull(range))
            throw new BlueException(INVALID_IDENTITY);

        return range;
    }

    /**
     * get statistics type by identity
     *
     * @param identity
     * @return
     */
    public static StatisticsType getStatisticsTypeByIdentity(String identity) {
        if (isBlank(identity))
            throw new BlueException(INVALID_IDENTITY);

        StatisticsType type = STATISTICS_TYPE_MAPPING.get(identity.toUpperCase());
        if (isNull(type))
            throw new BlueException(INVALID_IDENTITY);

        return type;
    }

    /**
     * get subject type by identity
     *
     * @param identity
     * @return
     */
    public static SubjectType getSubjectTypeByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        SubjectType type = SUBJECT_TYPE_MAPPING.get(identity);
        if (isNull(type))
            throw new BlueException(INVALID_IDENTITY);

        return type;
    }

    /**
     * get article type by identity
     *
     * @param identity
     * @return
     */
    public static ArticleType getArticleTypeByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        ArticleType type = ARTICLE_TYPE_MAPPING.get(identity);
        if (isNull(type))
            throw new BlueException(INVALID_IDENTITY);

        return type;
    }

    /**
     * get business verify type by identity
     *
     * @param identity
     * @return
     */
    public static VerifyBusinessType getVerifyBusinessTypeByIdentity(String identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        VerifyBusinessType type = VERIFY_BUSINESS_TYPE_MAPPING.get(identity);
        if (isNull(type))
            throw new BlueException(INVALID_IDENTITY);

        return type;
    }

    /**
     * get verify type by identity
     *
     * @param identity
     * @return
     */
    public static VerifyType getVerifyTypeByIdentity(String identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        VerifyType type = VERIFY_TYPE_MAPPING.get(identity);
        if (isNull(type))
            throw new BlueException(INVALID_IDENTITY);

        return type;
    }

    /**
     * get agreement type by identity
     *
     * @param identity
     * @return
     */
    public static AgreementType getAgreementTypeByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        AgreementType type = AGREEMENT_TYPE_MAPPING.get(identity);
        if (isNull(type))
            throw new BlueException(INVALID_IDENTITY);

        return type;
    }

    /**
     * get rsa decrypt mode by identity
     *
     * @param identity
     * @return
     */
    public static RsaDecryptMode getRsaDecryptModeByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        RsaDecryptMode rsaDecryptMode = RSA_DECRYPT_MODE_MAPPING.get(identity);
        if (isNull(rsaDecryptMode))
            throw new BlueException(INVALID_IDENTITY);

        return rsaDecryptMode;
    }

    /**
     * get risk type by identity
     *
     * @param identity
     * @return
     */
    public static RiskType getRiskTypeByIdentity(Integer identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        RiskType riskType = RISK_TYPE_MAPPING.get(identity);
        if (isNull(riskType))
            throw new BlueException(INVALID_IDENTITY);

        return riskType;
    }
    //</editor-fold>

}
