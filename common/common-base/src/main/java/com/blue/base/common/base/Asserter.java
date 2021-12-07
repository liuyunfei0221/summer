package com.blue.base.common.base;

import com.blue.base.constant.base.Status;
import com.blue.base.model.base.IdentityParam;
import com.blue.base.model.exps.BlueException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.UNAUTHORIZED;
import static com.blue.base.constant.base.ResponseMessage.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;
import static com.blue.base.constant.base.Status.VALID;

/**
 * @author liuyunfei
 * @date 2021/10/29
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "unused"})
public final class Asserter {

    /**
     * check a null value
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> boolean isNull(T t) {
        return t == null;
    }

    /**
     * check not a null value
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> boolean isNotNull(T t) {
        return t != null;
    }

    /**
     * @param s
     * @return
     */
    public static boolean isBlank(String s) {
        return StringUtils.isBlank(s);
    }

    /**
     * @param s
     * @return
     */
    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    /**
     * check an empty list
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() < 1;
    }

    /**
     * check not an empty list
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> boolean isNotEmpty(List<T> list) {
        return list != null && list.size() > 0;
    }


    /**
     * a num is greater than 0?
     *
     * @param arg
     * @return
     */
    public static boolean isGreaterThanZero(Long arg) {
        return arg != null && arg > 0L;
    }

    /**
     * a num is greater than 0?
     *
     * @param arg
     * @return
     */
    public static boolean isGreaterThanZero(Integer arg) {
        return arg != null && arg > 0;
    }

    /**
     * a num is greater or equals than 0?
     *
     * @param arg
     * @return
     */
    public static boolean isGreaterThanOrEqualsZero(Long arg) {
        return arg != null && arg >= 0L;
    }

    /**
     * a num is greater or equals than 0?
     *
     * @param arg
     * @return
     */
    public static boolean isGreaterThanOrEqualsZero(Integer arg) {
        return arg != null && arg >= 0;
    }

    /**
     * check identity is invalid
     *
     * @param identity
     */
    public static boolean isInvalidIdentity(Long identity) {
        return identity == null || identity < 0L;
    }

    /**
     * check identity is valid
     *
     * @param identity
     */
    public static boolean isValidIdentity(Long identity) {
        return identity != null && identity >= 0L;
    }

    /**
     * check identities is valid
     *
     * @param identities
     */
    public static boolean isInvalidIdentities(List<Long> identities) {
        return identities == null || identities.size() < 1;
    }

    /**
     * check identities is valid
     *
     * @param identities
     */
    public static boolean isValidIdentities(List<Long> identities) {
        return identities != null && identities.size() > 0;
    }

    /**
     * check identities with max rows
     *
     * @param identities
     */
    public static boolean isInvalidIdentitiesWithMaxRows(List<Long> identities, int maxRows) {
        if (identities == null)
            return true;

        int size = identities.size();
        return size < 1 || size > maxRows;
    }

    /**
     * check identities with max rows
     *
     * @param identities
     */
    public static boolean isValidIdentitiesWithMaxRows(List<Long> identities, int maxRows) {
        if (identities == null)
            return false;

        int size = identities.size();
        return size > 0 || size <= maxRows;
    }

    /**
     * check identities with max rows
     *
     * @param identities
     */
    public static boolean isInvalidIdentitiesWithMaxRows(List<Long> identities, long maxRows) {
        return isInvalidIdentitiesWithMaxRows(identities, (int) maxRows);
    }

    /**
     * check identities with max rows
     *
     * @param identities
     */
    public static boolean isValidIdentitiesWithMaxRows(List<Long> identities, long maxRows) {
        return isValidIdentitiesWithMaxRows(identities, (int) maxRows);
    }

    /**
     * check page is invalid
     *
     * @param page
     */
    public static boolean isInvalidPage(Long page) {
        return page == null || page < 1L;
    }

    /**
     * check page is valid
     *
     * @param page
     */
    public static boolean isValidPage(Long page) {
        return page != null && page > 0L;
    }

    /**
     * check limit is invalid
     *
     * @param limit
     */
    public static boolean isInvalidLimit(Long limit) {
        return limit == null || limit < 0L;
    }

    /**
     * check limit is valid
     *
     * @param limit
     */
    public static boolean isValidLimit(Long limit) {
        return limit != null && limit >= 0L;
    }

    /**
     * check rows is invalid
     *
     * @param rows
     */
    public static boolean isInvalidRows(Long rows) {
        return rows == null || rows < 1L;
    }

    /**
     * check rows is valid
     *
     * @param rows
     */
    public static boolean isValidRows(Long rows) {
        return rows != null && rows > 0L;
    }

    /**
     * check status is valid
     *
     * @param status
     */
    public static boolean isValidStatus(Integer status) {
        return status != null && VALID.status == status;
    }

    /**
     * check status is invalid
     *
     * @param status
     */
    public static boolean isInvalidStatus(Integer status) {
        return status == null || VALID.status != status;
    }

    /**
     * check status is targeting
     *
     * @param status
     */
    public static boolean isNotTargetStatus(Integer status, Status targetStatus) {
        return status == null || targetStatus.status != status;
    }

    /**
     * assert params for delete data by data id and operator id
     *
     * @param identityParam
     * @param operatorId
     * @return data id
     */
    @SuppressWarnings("AlibabaMethodReturnWrapperType")
    public static long assertIdentityParamsAndReturnIdForOperate(IdentityParam identityParam, Long operatorId) {
        if (isNull(identityParam))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED.status, UNAUTHORIZED.code, UNAUTHORIZED.message);

        Long id = identityParam.getId();
        if (isInvalidIdentity(id))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return id;
    }

}
