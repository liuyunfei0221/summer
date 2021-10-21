package com.blue.base.common.base;

import com.blue.base.constant.base.BlueMediaType;
import com.blue.base.constant.base.ResponseElement;
import com.blue.base.constant.base.SortType;
import com.blue.base.constant.base.Status;
import com.blue.base.constant.business.ArticleType;
import com.blue.base.constant.business.SubjectType;
import com.blue.base.constant.data.StatisticsRange;
import com.blue.base.constant.data.StatisticsType;
import com.blue.base.constant.member.Gender;
import com.blue.base.constant.portal.BulletinType;
import com.blue.base.constant.secure.DeviceType;
import com.blue.base.constant.secure.LoginType;
import com.blue.base.constant.secure.ResourceType;
import com.blue.base.model.exps.BlueException;
import org.springframework.http.MediaType;

import java.util.Map;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.ResponseMessage.INVALID_CONSTANT_IDENTITY;
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
    public static void assertResourceType(Integer identity) {
        if (identity == null || !RESOURCE_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid resource type identity");
    }

    /**
     * assert media type
     *
     * @param identity
     * @return
     */
    public static void assertMediaType(String identity) {
        if (isBlank(identity) || !MEDIA_TYPE_MAPPING.containsKey(identity.toLowerCase()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid media type identity");
    }

    /**
     * assert login type
     *
     * @param identity
     */
    public static void assertLoginType(String identity) {
        if (isBlank(identity) || !LOGIN_TYPE_MAPPING.containsKey(identity.toUpperCase()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid login type identity");
    }

    /**
     * assert device type
     *
     * @param identity
     */
    public static void assertDeviceType(String identity) {
        if (isBlank(identity) || !DEVICE_TYPE_MAPPING.containsKey(identity.toUpperCase()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid device type identity");
    }

    /**
     * assert gender type
     *
     * @param identity
     */
    public static void assertGenderIdentity(Integer identity) {
        if (identity == null || !GENDER_MAPPING.containsKey(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid gender type identity");
    }

    /**
     * assert status type
     *
     * @param identity
     */
    public static void assertStatus(Integer identity) {
        if (identity == null || !STATUS_MAPPING.containsKey(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid status type identity");
    }

    /**
     * assert sort type
     *
     * @param identity
     */
    public static void assertSortType(String identity) {
        if (isBlank(identity) || !SORT_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid sort type identity");
    }

    /**
     * assert bulletin type
     *
     * @param identity
     */
    public static void assertBulletinType(Integer identity) {
        if (identity == null || !BULLETIN_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid bulletin type identity");
    }

    /**
     * assert statistics range
     *
     * @param identity
     */
    public static void assertStatisticsRange(String identity) {
        if (isBlank(identity) || !STATISTICS_RANGE_MAPPING.containsKey(identity.toUpperCase()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid statistics range identity");
    }

    /**
     * assert statistics type
     *
     * @param identity
     */
    public static void assertStatisticsType(String identity) {
        if (isBlank(identity) || !STATISTICS_TYPE_MAPPING.containsKey(identity.toUpperCase()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid statistics type identity");
    }

    /**
     * assert subject type
     *
     * @param identity
     */
    public static void assertSubjectType(Integer identity) {
        if (identity == null || !SUBJECT_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid subject type identity");
    }

    /**
     * assert article type
     *
     * @param identity
     */
    public static void assertArticleType(Integer identity) {
        if (identity == null || !ARTICLE_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid article type identity");
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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        ResourceType resourceType = RESOURCE_TYPE_MAPPING.get(identity);
        if (resourceType == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid resource type identity");

        return resourceType;
    }

    /**
     * get media type by identity
     *
     * @param identity
     * @return
     */
    public static MediaType getMediaTypeByIdentity(String identity) {
        if (isBlank(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        MediaType mediaType = MEDIA_TYPE_MAPPING.get(identity.toLowerCase());
        if (mediaType == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid media type identity");

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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        LoginType loginType = LOGIN_TYPE_MAPPING.get(identity.toUpperCase());
        if (loginType == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid login type identity");

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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        DeviceType deviceType = DEVICE_TYPE_MAPPING.get(identity.toUpperCase());
        if (deviceType == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid device type identity");

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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        Gender gender = GENDER_MAPPING.get(identity);
        if (gender == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid gender type identity");

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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        Status status = STATUS_MAPPING.get(identity);
        if (status == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid status identity");

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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        SortType sortType = SORT_TYPE_MAPPING.get(identity);
        if (sortType == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid sort Type identity");

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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        BulletinType type = BULLETIN_TYPE_MAPPING.get(identity);
        if (type == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid bulletin type identity");

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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        StatisticsRange range = STATISTICS_RANGE_MAPPING.get(identity.toUpperCase());
        if (range == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid statistics range identity");

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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        StatisticsType type = STATISTICS_TYPE_MAPPING.get(identity.toUpperCase());
        if (type == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid statistics type identity");

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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        SubjectType type = SUBJECT_TYPE_MAPPING.get(identity);
        if (type == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid subject type identity");

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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        ArticleType type = ARTICLE_TYPE_MAPPING.get(identity);
        if (type == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid article type identity");

        return type;
    }
    //</editor-fold>

}
