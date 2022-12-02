package com.blue.basic.common.base;

import com.blue.basic.constant.common.Status;
import com.blue.basic.model.common.IdentityParam;
import com.blue.basic.model.exps.BlueException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Status.VALID;
import static java.util.stream.Collectors.toList;

/**
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "unused"})
public final class BlueChecker {

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
     * @param array
     * @param <T>
     * @return
     */
    public static <T> boolean isNotEmpty(T[] array) {
        return array != null && array.length > 0;
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
     * check an empty set
     *
     * @param set
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(Set<T> set) {
        return set == null || set.size() < 1;
    }

    /**
     * check not an empty set
     *
     * @param set
     * @param <T>
     * @return
     */
    public static <T> boolean isNotEmpty(Set<T> set) {
        return set != null && set.size() > 0;
    }

    /**
     * check an empty list
     *
     * @param map
     * @param <T,A>
     * @return
     */
    public static <T, A> boolean isEmpty(Map<T, A> map) {
        return map == null || map.size() < 1;
    }

    /**
     * check not an empty map
     *
     * @param map
     * @param <T,A>
     * @return
     */
    public static <T, A> boolean isNotEmpty(Map<T, A> map) {
        return map != null && map.size() > 0;
    }

    /**
     * is same list
     *
     * @param a
     * @param b
     * @param <T>
     * @return
     */
    public static <T> boolean isEquals(List<T> a, List<T> b) {
        boolean isNullA = isNull(a);
        boolean isNullB = isNull(b);

        if (isNullA && isNullB)
            return true;

        if (isNullA != isNullB)
            return false;

        List<T> sortedA = a.stream().sorted().filter(Objects::nonNull).collect(toList());
        List<T> sortedB = b.stream().sorted().filter(Objects::nonNull).collect(toList());

        int size = a.size();
        if (size != b.size())
            return false;

        for (int i = 0; i < size; i++)
            if (!sortedA.get(i).equals(sortedB.get(i)))
                return false;

        return true;
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
     * a num is less than 0?
     *
     * @param arg
     * @return
     */
    public static boolean isLessThanZero(Long arg) {
        return arg != null && arg < 0L;
    }

    /**
     * a num is less than 0?
     *
     * @param arg
     * @return
     */
    public static boolean isLessThanZero(Integer arg) {
        return arg != null && arg < 0;
    }

    /**
     * a num is less or equals than 0?
     *
     * @param arg
     * @return
     */
    public static boolean isLessThanOrEqualsZero(Long arg) {
        return arg != null && arg <= 0L;
    }

    /**
     * a num is less or equals than 0?
     *
     * @param arg
     * @return
     */
    public static boolean isLessThanOrEqualsZero(Integer arg) {
        return arg != null && arg <= 0;
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
    public static boolean isInvalidIdentities(Collection<Long> identities) {
        return identities == null || identities.size() == 0 || identities.parallelStream().anyMatch(BlueChecker::isInvalidIdentity);
    }

    /**
     * check identities is valid
     *
     * @param identities
     */
    public static boolean isValidIdentities(Collection<Long> identities) {
        return identities != null && identities.size() > 0 && identities.parallelStream().anyMatch(BlueChecker::isValidIdentity);
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
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        Long id = identityParam.getId();
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return id;
    }

}
