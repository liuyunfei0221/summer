package com.blue.base.common.base;

import com.blue.base.constant.business.ArticleType;
import com.blue.base.constant.business.SubjectType;
import com.blue.base.constant.base.BlueMediaType;
import com.blue.base.constant.base.ResponseElement;
import com.blue.base.constant.base.Status;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.ResponseMessage.INVALID_CONSTANT_IDENTITY;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 常量约束校验器
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class ConstantProcessor {

    //<editor-fold desc="数据维护">

    /**
     * 已知响应状态信息
     */
    private static final Map<Integer, ResponseElement> RESPONSE_ELEMENT_MAPPING =
            of(ResponseElement.values()).collect(toMap(e -> e.status, e -> e, (a, b) -> a));

    /**
     * 媒体类型
     */
    private static final Map<String, MediaType> MEDIA_TYPE_MAPPING = Stream.of(BlueMediaType.values())
            .collect(Collectors.toMap(bmt -> bmt.identity.toLowerCase(), bmt -> bmt.mediaType));

    /**
     * 合法资源类型
     */
    private static final Map<Integer, ResourceType> RESOURCE_TYPE_MAPPING =
            of(ResourceType.values()).collect(toMap(t -> t.identity, t -> t, (a, b) -> a));

    /**
     * 合法登录类型
     */
    private static final Map<String, LoginType> LOGIN_TYPE_MAPPING =
            of(LoginType.values()).collect(toMap(e -> e.identity.toUpperCase(), e -> e, (a, b) -> a));

    /**
     * 合法设备类型
     */
    private static final Map<String, DeviceType> DEVICE_TYPE_MAPPING =
            of(DeviceType.values()).collect(toMap(e -> e.identity.toUpperCase(), e -> e, (a, b) -> a));

    /**
     * 合法性别标识
     */
    private static final Map<Integer, Gender> GENDER_MAPPING =
            of(Gender.values()).collect(toMap(g -> g.identity, g -> g, (a, b) -> a));

    /**
     * 合法状态标识
     */
    private static final Map<Integer, Status> STATUS_MAPPING =
            of(Status.values()).collect(toMap(s -> s.status, s -> s, (a, b) -> a));

    /**
     * 合法公告类型标识
     */
    private static final Map<Integer, BulletinType> BULLETIN_TYPE_MAPPING =
            of(BulletinType.values()).collect(toMap(t -> t.identity, t -> t, (a, b) -> a));

    /**
     * 统计范围标识
     */
    private static final Map<String, StatisticsRange> STATISTICS_RANGE_MAPPING =
            of(StatisticsRange.values()).collect(toMap(t -> t.identity.toUpperCase(), t -> t, (a, b) -> a));

    /**
     * 统计类型标识
     */
    private static final Map<String, StatisticsType> STATISTICS_TYPE_MAPPING =
            of(StatisticsType.values()).collect(toMap(t -> t.identity.toUpperCase(), t -> t, (a, b) -> a));

    /**
     * 合法内容类型标识
     */
    private static final Map<Integer, SubjectType> SUBJECT_TYPE_MAPPING =
            of(SubjectType.values()).collect(toMap(t -> t.identity, t -> t, (a, b) -> a));

    /**
     * 合法文章类型标识
     */
    private static final Map<Integer, ArticleType> ARTICLE_TYPE_MAPPING =
            of(ArticleType.values()).collect(toMap(t -> t.identity, t -> t, (a, b) -> a));
    //</editor-fold>

    //<editor-fold desc="校验项">

    /**
     * 校验资源类型
     *
     * @param identity
     */
    public static void assertResourceType(Integer identity) {
        if (identity == null || !RESOURCE_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "资源类型不合法");
    }

    /**
     * 校验媒体类型
     *
     * @param identity
     * @return
     */
    public static void assertMediaType(String identity) {
        if (isBlank(identity) || !MEDIA_TYPE_MAPPING.containsKey(identity.toLowerCase()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "媒体类型不合法");
    }

    /**
     * 校验登录类型
     *
     * @param identity
     */
    public static void assertLoginType(String identity) {
        if (isBlank(identity) || !LOGIN_TYPE_MAPPING.containsKey(identity.toUpperCase()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "登录类型不合法");
    }

    /**
     * 校验设备类型
     *
     * @param identity
     */
    public static void assertDeviceType(String identity) {
        if (isBlank(identity) || !DEVICE_TYPE_MAPPING.containsKey(identity.toUpperCase()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "设备类型不合法");
    }

    /**
     * 校验性别标识
     *
     * @param identity
     */
    public static void assertGenderIdentity(Integer identity) {
        if (identity == null || !GENDER_MAPPING.containsKey(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "性别不合法");
    }

    /**
     * 校验状态标识
     *
     * @param identity
     */
    public static void assertStatus(Integer identity) {
        if (identity == null || !STATUS_MAPPING.containsKey(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "状态不合法");
    }

    /**
     * 校验公告类型
     *
     * @param identity
     */
    public static void assertBulletinType(Integer identity) {
        if (identity == null || !BULLETIN_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "公告类型不合法");
    }

    /**
     * 校验统计范围
     *
     * @param identity
     */
    public static void assertStatisticsRange(String identity) {
        if (isBlank(identity) || !STATISTICS_RANGE_MAPPING.containsKey(identity.toUpperCase()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "统计范围不合法");
    }

    /**
     * 校验统计类型
     *
     * @param identity
     */
    public static void assertStatisticsType(String identity) {
        if (isBlank(identity) || !STATISTICS_TYPE_MAPPING.containsKey(identity.toUpperCase()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "统计类型不合法");
    }

    /**
     * 校验主题类型
     *
     * @param identity
     */
    public static void assertSubjectType(Integer identity) {
        if (identity == null || !SUBJECT_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "主题类型不合法");
    }

    /**
     * 校验文章类型
     *
     * @param identity
     */
    public static void assertArticleType(Integer identity) {
        if (identity == null || !ARTICLE_TYPE_MAPPING.containsKey(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "文章类型不合法");
    }
    //</editor-fold>

    //<editor-fold desc="获取项">

    /**
     * 根据响应状态码获取响应信息
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
     * 根据媒体类型字符串获取媒体类型
     *
     * @param identity
     * @return
     */
    public static MediaType getMediaTypeByIdentity(String identity) {
        if (isBlank(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        MediaType mediaType = MEDIA_TYPE_MAPPING.get(identity.toLowerCase());
        if (mediaType == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "媒体类型标识不合法");

        return mediaType;
    }

    /**
     * 根据标识获取资源类型
     *
     * @param identity
     * @return
     */
    public static ResourceType getResourceTypeByIdentity(Integer identity) {
        if (identity == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        ResourceType resourceType = RESOURCE_TYPE_MAPPING.get(identity);
        if (resourceType == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "资源类型不合法");

        return resourceType;
    }

    /**
     * 根据标识获取登录类型
     *
     * @param identity
     * @return
     */
    public static LoginType getLoginTypeByIdentity(String identity) {
        if (isBlank(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        LoginType loginType = LOGIN_TYPE_MAPPING.get(identity.toUpperCase());
        if (loginType == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "登录类型不合法");

        return loginType;
    }

    /**
     * 根据标识获取设备类型
     *
     * @param identity
     * @return
     */
    public static DeviceType getDeviceTypeByIdentity(String identity) {
        if (isBlank(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        DeviceType deviceType = DEVICE_TYPE_MAPPING.get(identity.toUpperCase());
        if (deviceType == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "设备类型不合法");

        return deviceType;
    }

    /**
     * 根据标识获取性别
     *
     * @param identity
     * @return
     */
    public static Gender getGenderTypeByIdentity(Integer identity) {
        if (identity == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        Gender gender = GENDER_MAPPING.get(identity);
        if (gender == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "性别不合法");

        return gender;
    }

    /**
     * 根据标识获取状态
     *
     * @param identity
     * @return
     */
    public static Status getStatusTypeByIdentity(Integer identity) {
        if (identity == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        Status status = STATUS_MAPPING.get(identity);
        if (status == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "状态不合法");

        return status;
    }

    /**
     * 根据标识获取公告类型
     *
     * @param identity
     * @return
     */
    public static BulletinType getBulletinTypeByIdentity(Integer identity) {
        if (identity == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        BulletinType type = BULLETIN_TYPE_MAPPING.get(identity);
        if (type == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "公告类型不合法");

        return type;
    }

    /**
     * 根据标识获取统计范围类型
     *
     * @param identity
     * @return
     */
    public static StatisticsRange getStatisticsRangeByIdentity(String identity) {
        if (isBlank(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        StatisticsRange range = STATISTICS_RANGE_MAPPING.get(identity.toUpperCase());
        if (range == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "统计范围不合法");

        return range;
    }

    /**
     * 根据标识获取统计范围类型
     *
     * @param identity
     * @return
     */
    public static StatisticsType getStatisticsTypeByIdentity(String identity) {
        if (isBlank(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        StatisticsType type = STATISTICS_TYPE_MAPPING.get(identity.toUpperCase());
        if (type == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "统计类型不合法");

        return type;
    }

    /**
     * 根据标识获取主题类型
     *
     * @param identity
     * @return
     */
    public static SubjectType getSubjectTypeByIdentity(Integer identity) {
        if (identity == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        SubjectType type = SUBJECT_TYPE_MAPPING.get(identity);
        if (type == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "主题类型不合法");

        return type;
    }

    /**
     * 根据标识获取文章类型
     *
     * @param identity
     * @return
     */
    public static ArticleType getArticleTypeByIdentity(Integer identity) {
        if (identity == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_CONSTANT_IDENTITY.message);

        ArticleType type = ARTICLE_TYPE_MAPPING.get(identity);
        if (type == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "文章类型不合法");

        return type;
    }
    //</editor-fold>

}
