package com.blue.base.common.base;

import com.blue.base.constant.base.*;
import com.blue.base.constant.business.ArticleType;
import com.blue.base.constant.business.SubjectType;
import com.blue.base.constant.analyze.StatisticsRange;
import com.blue.base.constant.analyze.StatisticsType;
import com.blue.base.constant.member.Gender;
import com.blue.base.constant.portal.BulletinType;
import com.blue.base.constant.secure.DeviceType;
import com.blue.base.constant.secure.LoginType;
import com.blue.base.constant.secure.ResourceType;
import com.blue.base.model.exps.BlueException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Map;

import static com.blue.base.constant.base.ResponseElement.*;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Constant Constraint Checker
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class ConstantProcessor {

    //<editor-fold desc="constant mappings">

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
     * valid resource type identity and type mapping
     */
    private static final Map<Integer, ResourceType> RESOURCE_TYPE_MAPPING =
            of(ResourceType.values()).collect(toMap(t -> t.identity, t -> t, (a, b) -> a));

    /**
     * valid login type identity and type mapping
     */
    private static final Map<String, LoginType> LOGIN_TYPE_MAPPING =
            of(LoginType.values()).collect(toMap(e -> e.identity.toUpperCase(), e -> e, (a, b) -> a));

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
    //</editor-fold>

    //<editor-fold desc="asserter">

    /**
     * assert resource type
     *
     * @param identity
     */
    public static void assertResourceType(Integer identity, boolean nullable) {
        if (nullable && identity == null)
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
        if (nullable && value == null)
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
        if (nullable && identity == null)
            return;

        if (isBlank(identity) || !MEDIA_TYPE_MAPPING.containsKey(identity.toLowerCase()))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert login type
     *
     * @param identity
     */
    public static void assertLoginType(String identity, boolean nullable) {
        if (nullable && identity == null)
            return;

        if (isBlank(identity) || !LOGIN_TYPE_MAPPING.containsKey(identity.toUpperCase()))
            throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * assert device type
     *
     * @param identity
     */
    public static void assertDeviceType(String identity, boolean nullable) {
        if (nullable && identity == null)
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
        if (nullable && identity == null)
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
        if (nullable && identity == null)
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
        if (nullable && identity == null)
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
        if (nullable && identity == null)
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
        if (nullable && identity == null)
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
        if (nullable && identity == null)
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
        if (nullable && identity == null)
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
        if (nullable && identity == null)
            return;

        if (!ARTICLE_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(INVALID_IDENTITY);
    }
    //</editor-fold>

    //<editor-fold desc="getter">

    /**
     * get element by http status
     *
     * @param identity
     * @return
     */
    public static ResponseElement getResponseElementByStatus(Integer identity) {
        if (identity == null)
            throw new BlueException(INVALID_IDENTITY);

        ResponseElement responseElement = RESPONSE_ELEMENT_MAPPING.get(identity);
        return responseElement != null ? responseElement : INTERNAL_SERVER_ERROR;
    }

    /**
     * get resource type by identity
     *
     * @param identity
     * @return
     */
    public static ResourceType getResourceTypeByIdentity(Integer identity) {
        if (identity == null)
            throw new BlueException(INVALID_IDENTITY);

        ResourceType resourceType = RESOURCE_TYPE_MAPPING.get(identity);
        if (resourceType == null)
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
        if (httpMethod == null)
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
        if (mediaType == null)
            throw new BlueException(INVALID_IDENTITY);

        return mediaType;
    }

    /**
     * get login type by identity
     *
     * @param identity
     * @return
     */
    public static LoginType getLoginTypeByIdentity(String identity) {
        if (isBlank(identity))
            throw new BlueException(INVALID_IDENTITY);

        LoginType loginType = LOGIN_TYPE_MAPPING.get(identity.toUpperCase());
        if (loginType == null)
            throw new BlueException(INVALID_IDENTITY);

        return loginType;
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
        if (deviceType == null)
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
        if (identity == null)
            throw new BlueException(INVALID_IDENTITY);

        Gender gender = GENDER_MAPPING.get(identity);
        if (gender == null)
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
        if (identity == null)
            throw new BlueException(INVALID_IDENTITY);

        Status status = STATUS_MAPPING.get(identity);
        if (status == null)
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
        if (sortType == null)
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
        if (identity == null)
            throw new BlueException(INVALID_IDENTITY);

        BulletinType type = BULLETIN_TYPE_MAPPING.get(identity);
        if (type == null)
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
        if (range == null)
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
        if (type == null)
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
        if (identity == null)
            throw new BlueException(INVALID_IDENTITY);

        SubjectType type = SUBJECT_TYPE_MAPPING.get(identity);
        if (type == null)
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
        if (identity == null)
            throw new BlueException(INVALID_IDENTITY);

        ArticleType type = ARTICLE_TYPE_MAPPING.get(identity);
        if (type == null)
            throw new BlueException(INVALID_IDENTITY);

        return type;
    }
    //</editor-fold>

}
