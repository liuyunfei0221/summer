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
import org.springframework.http.MediaType;

import java.util.Map;

import static com.blue.base.constant.base.CommonException.INVALID_IDENTITY_EXP;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
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
    public static void assertResourceType(Integer identity, boolean nullable) {
        if (nullable && identity == null)
            return;

        if (!RESOURCE_TYPE_MAPPING.containsKey(identity))
            throw INVALID_IDENTITY_EXP.exp;
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
            throw INVALID_IDENTITY_EXP.exp;
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
            throw INVALID_IDENTITY_EXP.exp;
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
            throw INVALID_IDENTITY_EXP.exp;
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
            throw INVALID_IDENTITY_EXP.exp;
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
            throw INVALID_IDENTITY_EXP.exp;
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
            throw INVALID_IDENTITY_EXP.exp;
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
            throw INVALID_IDENTITY_EXP.exp;
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
            throw INVALID_IDENTITY_EXP.exp;
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
            throw INVALID_IDENTITY_EXP.exp;
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
            throw INVALID_IDENTITY_EXP.exp;
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
            throw INVALID_IDENTITY_EXP.exp;
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
            throw INVALID_IDENTITY_EXP.exp;

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
            throw INVALID_IDENTITY_EXP.exp;

        ResourceType resourceType = RESOURCE_TYPE_MAPPING.get(identity);
        if (resourceType == null)
            throw INVALID_IDENTITY_EXP.exp;

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
            throw INVALID_IDENTITY_EXP.exp;

        MediaType mediaType = MEDIA_TYPE_MAPPING.get(identity.toLowerCase());
        if (mediaType == null)
            throw INVALID_IDENTITY_EXP.exp;

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
            throw INVALID_IDENTITY_EXP.exp;

        LoginType loginType = LOGIN_TYPE_MAPPING.get(identity.toUpperCase());
        if (loginType == null)
            throw INVALID_IDENTITY_EXP.exp;

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
            throw INVALID_IDENTITY_EXP.exp;

        DeviceType deviceType = DEVICE_TYPE_MAPPING.get(identity.toUpperCase());
        if (deviceType == null)
            throw INVALID_IDENTITY_EXP.exp;

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
            throw INVALID_IDENTITY_EXP.exp;

        Gender gender = GENDER_MAPPING.get(identity);
        if (gender == null)
            throw INVALID_IDENTITY_EXP.exp;

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
            throw INVALID_IDENTITY_EXP.exp;

        Status status = STATUS_MAPPING.get(identity);
        if (status == null)
            throw INVALID_IDENTITY_EXP.exp;

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
            throw INVALID_IDENTITY_EXP.exp;

        SortType sortType = SORT_TYPE_MAPPING.get(identity);
        if (sortType == null)
            throw INVALID_IDENTITY_EXP.exp;

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
            throw INVALID_IDENTITY_EXP.exp;

        BulletinType type = BULLETIN_TYPE_MAPPING.get(identity);
        if (type == null)
            throw INVALID_IDENTITY_EXP.exp;

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
            throw INVALID_IDENTITY_EXP.exp;

        StatisticsRange range = STATISTICS_RANGE_MAPPING.get(identity.toUpperCase());
        if (range == null)
            throw INVALID_IDENTITY_EXP.exp;

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
            throw INVALID_IDENTITY_EXP.exp;

        StatisticsType type = STATISTICS_TYPE_MAPPING.get(identity.toUpperCase());
        if (type == null)
            throw INVALID_IDENTITY_EXP.exp;

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
            throw INVALID_IDENTITY_EXP.exp;

        SubjectType type = SUBJECT_TYPE_MAPPING.get(identity);
        if (type == null)
            throw INVALID_IDENTITY_EXP.exp;

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
            throw INVALID_IDENTITY_EXP.exp;

        ArticleType type = ARTICLE_TYPE_MAPPING.get(identity);
        if (type == null)
            throw INVALID_IDENTITY_EXP.exp;

        return type;
    }
    //</editor-fold>

}
