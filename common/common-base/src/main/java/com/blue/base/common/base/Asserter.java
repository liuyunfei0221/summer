package com.blue.base.common.base;

import com.blue.base.constant.base.Status;

import java.util.List;

import static com.blue.base.constant.base.Status.VALID;

/**
 * @author liuyunfei
 * @date 2021/10/29
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
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
        return s == null || "".equals(s);
    }

    /**
     * @param s
     * @return
     */
    public static boolean isNotBlank(String s) {
        return s != null && !"".equals(s);
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
     * check status is valid
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

}
