package com.blue.base.common.reactive;

import com.blue.base.model.exps.BlueException;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * asserter
 *
 * @author liuyunfei
 * @date 2021/10/18
 * @apiNote
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused", "JavaDoc", "OptionalAssignedToNull", "OptionalUsedAsFieldOrParameterType"})
public final class BlueAsserter {

    /**
     * assert by expression
     *
     * @param data
     * @param predicate
     * @param exception
     * @param <T>
     */
    public static <T> void blueAssert(T data, Predicate<T> predicate, RuntimeException exception) {
        if (!predicate.test(data))
            if (exception != null) {
                throw exception;
            } else {
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "assert failed ");
            }
    }

    /**
     * data can't be null
     *
     * @param data
     * @param exception
     */
    public static <T> void notNullObj(T data, RuntimeException exception) {
        if (data == null)
            if (exception != null) {
                throw exception;
            } else {
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "data can't be null");
            }
    }

    /**
     * data can't be null
     *
     * @param dataOpt
     * @param exception
     */
    public static <T> void notNullObjOpt(Optional<T> dataOpt, RuntimeException exception) {
        if (dataOpt == null || dataOpt.isEmpty())
            if (exception != null) {
                throw exception;
            } else {
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "data can't be null");
            }
    }

    /**
     * data can't be blank
     *
     * @param data
     * @param exception
     */
    public static void notBlankStr(String data, RuntimeException exception) {
        if (isBlank(data))
            if (exception != null) {
                throw exception;
            } else {
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "data can't be blank");
            }
    }

    /**
     * data can't be blank
     *
     * @param dataOpt
     * @param exception
     */
    public static void notBlankStrOpt(Optional<String> dataOpt, RuntimeException exception) {
        if (dataOpt == null || dataOpt.isEmpty() || isBlank(dataOpt.get()))
            if (exception != null) {
                throw exception;
            } else {
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "data can't be blank");
            }
    }

    /**
     * collection can't be empty
     *
     * @param collection
     * @param exception
     */
    public static <T> void notEmptyCollection(Collection<T> collection, RuntimeException exception) {
        if (isEmpty(collection))
            if (exception != null) {
                throw exception;
            } else {
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "collection can't be empty");
            }
    }

    /**
     * collection can't be empty
     *
     * @param collectionOpt
     * @param exception
     */
    public static <T> void notEmptyCollectionOpt(Optional<Collection<T>> collectionOpt, RuntimeException exception) {
        if (collectionOpt == null || collectionOpt.isEmpty() || isEmpty(collectionOpt.get()))
            if (exception != null) {
                throw exception;
            } else {
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "collection can't be empty");
            }
    }

    /**
     * data only can be blank
     *
     * @param data
     * @param exception
     */
    public static void isBlankStr(String data, RuntimeException exception) {
        if (!isBlank(data))
            if (exception != null) {
                throw exception;
            } else {
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "data only can be blank");
            }
    }

    /**
     * data only can be blank
     *
     * @param dataOpt
     * @param exception
     */
    public static void isBlankStrOpt(Optional<String> dataOpt, RuntimeException exception) {
        if (dataOpt != null && dataOpt.isPresent() && !isBlank(dataOpt.get()))
            if (exception != null) {
                throw exception;
            } else {
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "data only can be blank");
            }
    }

    /**
     * data only can be null
     *
     * @param data
     * @param exception
     */
    public static <T> void isNullObj(T data, RuntimeException exception) {
        if (data != null)
            if (exception != null) {
                throw exception;
            } else {
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "data only can be null");
            }
    }

    /**
     * data only can be null
     *
     * @param dataOpt
     * @param exception
     */
    public static <T> void isNullObjOpt(Optional<T> dataOpt, RuntimeException exception) {
        if (dataOpt != null && dataOpt.isPresent())
            if (exception != null) {
                throw exception;
            } else {
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "data only can be null");
            }
    }

    /**
     * collection only can be empty
     *
     * @param collection
     * @param exception
     */
    public static <T> void isEmptyCollection(Collection<T> collection, RuntimeException exception) {
        if (!isEmpty(collection))
            if (exception != null) {
                throw exception;
            } else {
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "collection only can be empty");
            }
    }

    /**
     * data can't be blank
     *
     * @param collectionOpt
     * @param exception
     */
    public static <T> void isEmptyCollectionOpt(Optional<Collection<T>> collectionOpt, RuntimeException exception) {
        if (collectionOpt != null && collectionOpt.isPresent() && isEmpty(collectionOpt.get()))
            if (exception != null) {
                throw exception;
            } else {
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "collection only can be empty");
            }
    }

}
