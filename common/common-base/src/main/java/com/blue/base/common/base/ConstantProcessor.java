package com.blue.base.common.base;

import com.blue.base.constant.analyze.StatisticsRange;
import com.blue.base.constant.analyze.StatisticsType;
import com.blue.base.constant.base.*;
import com.blue.base.constant.article.ArticleType;
import com.blue.base.constant.article.SubjectType;
import com.blue.base.constant.member.Gender;
import com.blue.base.constant.portal.BulletinType;
import com.blue.base.constant.auth.DeviceType;
import com.blue.base.constant.auth.CredentialType;
import com.blue.base.constant.auth.ResourceType;
import com.blue.base.constant.verify.BusinessType;
import com.blue.base.constant.verify.VerifyType;
import com.blue.base.model.exps.BlueException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Map;

import static com.blue.base.common.base.BlueChecker.isNotNull;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
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
            of(BlueBoolean.values()).collect(toMap(b -> b.status, b -> b, (a, b) -> a));

    private static final Map<Boolean, BlueBoolean> STATUS_BOOL_MAPPING =
            of(BlueBoolean.values()).collect(toMap(b -> b.bool, b -> b, (a, b) -> a));

    /**
     * http response status and response element mapping
     */
    private static final Map<Integer, ResponseElement> RESPONSE_ELEMENT_MAPPING =
            of(ResponseElement.values()).collect(toMap(e -> e.status, e -> e, (a, b) -> a));

    /**
     * http method name and method mapping
     */
    private static final Map<String, HttpMethod> HTTP_METHOD_MAPPING =
            of(BlueHttpMethod.values()).collect(toMap(bhm -> bhm.value.toUpperCase(), bhm -> bhm.method));

    /**
     * media type identity and media mapping
     */
    private static final Map<String, MediaType> MEDIA_TYPE_MAPPING =
            of(BlueMediaType.values()).collect(toMap(bmt -> bmt.identity.toLowerCase(), bmt -> bmt.mediaType));

    /**
     * file type identity and media mapping
     */
    private static final Map<String, MediaType> FILE_MEDIA_TYPE_MAPPING =
            of(BlueFileType.values()).collect(toMap(bft -> bft.identity.toLowerCase(), bft -> bft.mediaType));

    /**
     * valid resource type identity and type mapping
     */
    private static final Map<Integer, ResourceType> RESOURCE_TYPE_MAPPING =
            of(ResourceType.values()).collect(toMap(t -> t.identity, t -> t, (a, b) -> a));

    /**
     * valid credential type identity and type mapping
     */
    private static final Map<String, CredentialType> CREDENTIAL_TYPE_MAPPING =
            of(CredentialType.values()).collect(toMap(e -> e.identity.toUpperCase(), e -> e, (a, b) -> a));

    /**
     * valid device type identity and type mapping
     */
    private static final Map<String, DeviceType> DEVICE_TYPE_MAPPING =
            of(DeviceType.values()).collect(toMap(e -> e.identity.toUpperCase(), e -> e, (a, b) -> a));

    /**
     * valid gender type identity and type mapping
     */
    private static final Map<Integer, Gender> GENDER_MAPPING =
            of(Gender.values()).collect(toMap(g -> g.identity, g -> g, (a, b) -> a));

    /**
     * valid status type identity and type mapping
     */
    private static final Map<Integer, Status> STATUS_MAPPING =
            of(Status.values()).collect(toMap(s -> s.status, s -> s, (a, b) -> a));

    /**
     * valid sort type identity and type mapping
     */
    private static final Map<String, SortType> SORT_TYPE_MAPPING =
            of(SortType.values()).collect(toMap(t -> t.identity, t -> t, (a, b) -> a));

    /**
     * valid bulletin type identity and type mapping
     */
    private static final Map<Integer, BulletinType> BULLETIN_TYPE_MAPPING =
            of(BulletinType.values()).collect(toMap(t -> t.identity, t -> t, (a, b) -> a));

    /**
     * valid statistics range identity and range mapping
     */
    private static final Map<String, StatisticsRange> STATISTICS_RANGE_MAPPING =
            of(StatisticsRange.values()).collect(toMap(t -> t.identity.toUpperCase(), t -> t, (a, b) -> a));

    /**
     * valid statistics type identity and type mapping
     */
    private static final Map<String, StatisticsType> STATISTICS_TYPE_MAPPING =
            of(StatisticsType.values()).collect(toMap(t -> t.identity.toUpperCase(), t -> t, (a, b) -> a));

    /**
     * valid subject type identity and type mapping
     */
    private static final Map<Integer, SubjectType> SUBJECT_TYPE_MAPPING =
            of(SubjectType.values()).collect(toMap(t -> t.identity, t -> t, (a, b) -> a));

    /**
     * valid article type identity and type mapping
     */
    private static final Map<Integer, ArticleType> ARTICLE_TYPE_MAPPING =
            of(ArticleType.values()).collect(toMap(t -> t.identity, t -> t, (a, b) -> a));

    /**
     * business verify type identity and type mapping
     */
    private static final Map<String, BusinessType> BUSINESS_TYPE_MAPPING =
            of(BusinessType.values()).collect(toMap(t -> t.identity, t -> t, (a, b) -> a));

    /**
     * verify type identity and type mapping
     */
    private static final Map<String, VerifyType> VERIFY_TYPE_MAPPING =
            of(VerifyType.values()).collect(toMap(t -> t.identity, t -> t, (a, b) -> a));
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
     * assert gender type
     *
     * @param identity
     */
    public static void assertGenderIdentity(Integer identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!GENDER_MAPPING.containsKey(identity))
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
    public static void assertBusinessType(String identity, boolean nullable) {
        if (nullable && isNull(identity))
            return;

        if (!BUSINESS_TYPE_MAPPING.containsKey(identity))
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
        return isNotNull(responseElement) ? responseElement : INTERNAL_SERVER_ERROR;
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
    public static BusinessType getBusinessTypeByIdentity(String identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);

        BusinessType type = BUSINESS_TYPE_MAPPING.get(identity);
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

    //</editor-fold>

}
