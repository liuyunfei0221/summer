package com.blue.base.constant.base;

import com.blue.base.model.exps.BlueException;

import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.BlueNumericalValue.MAX_ROWS;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.ResponseMessage.*;

/**
 * @author liuyunfei
 * @date 2021/11/3
 * @apiNote
 */
@SuppressWarnings({"unused", "AlibabaEnumConstantsMustHaveComment"})
public enum CommonException {

    /**
     * TOO_MANY_REQUESTS_EXP
     */
    TOO_MANY_REQUESTS_EXP(new BlueException(TOO_MANY_REQUESTS.status, TOO_MANY_REQUESTS.code, TOO_MANY_REQUESTS.message)),

    /**
     * PARTIAL_CONTENT_EXP
     */
    PARTIAL_CONTENT_EXP(new BlueException(PARTIAL_CONTENT.status, PARTIAL_CONTENT.code, PARTIAL_CONTENT.message)),

    /**
     * UNAUTHORIZED_EXP
     */
    UNAUTHORIZED_EXP(new BlueException(UNAUTHORIZED.status, UNAUTHORIZED.code, UNAUTHORIZED.message)),

    /**
     * FORBIDDEN_EXP
     */
    FORBIDDEN_EXP(new BlueException(FORBIDDEN.status, FORBIDDEN.code, FORBIDDEN.message)),

    /**
     * NOT_FOUND_EXP
     */
    NOT_FOUND_EXP(new BlueException(NOT_FOUND.status, NOT_FOUND.code, NOT_FOUND.message)),

    /**
     * BAD_REQUEST_EXP
     */
    BAD_REQUEST_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message)),

    /**
     * UNSUPPORTED_MEDIA_TYPE
     */
    UNSUPPORTED_MEDIA_TYPE_EXP(new BlueException(UNSUPPORTED_MEDIA_TYPE.status, UNSUPPORTED_MEDIA_TYPE.code, UNSUPPORTED_MEDIA_TYPE.message)),

    /**
     * PAYLOAD_TOO_LARGE_EXP
     */
    PAYLOAD_TOO_LARGE_EXP(new BlueException(PAYLOAD_TOO_LARGE.status, PAYLOAD_TOO_LARGE.code, PAYLOAD_TOO_LARGE.message)),

    /**
     * NO_CONTENT_EXP
     */
    NO_CONTENT_EXP(new BlueException(NO_CONTENT.status, NO_CONTENT.code, NO_CONTENT.message)),

    /**
     * NOT_ACCEPTABLE_EXP
     */
    NOT_ACCEPTABLE_EXP(new BlueException(NOT_ACCEPTABLE.status, NOT_ACCEPTABLE.code, NOT_ACCEPTABLE.message)),

    /**
     * INTERNAL_SERVER_ERROR_EXP
     */
    INTERNAL_SERVER_ERROR_EXP(new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message)),

    /**
     * REQUEST_TIMEOUT_EXP
     */
    REQUEST_TIMEOUT_EXP(new BlueException(REQUEST_TIMEOUT.status, REQUEST_TIMEOUT.code, REQUEST_TIMEOUT.message)),

    /**
     * GATEWAY_TIMEOUT_EXP
     */
    GATEWAY_TIMEOUT_EXP(new BlueException(GATEWAY_TIMEOUT.status, GATEWAY_TIMEOUT.code, GATEWAY_TIMEOUT.message)),

    /**
     * INVALID_REQUEST_METHOD_EXP
     */
    INVALID_REQUEST_METHOD_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_REQUEST_METHOD.message)),

    /**
     * EMPTY_PARAM_EXP
     */
    EMPTY_PARAM_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message)),

    /**
     * INVALID_IDENTITY_EXP
     */
    INVALID_IDENTITY_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message)),

    /**
     * INVALID_IDENTITIES_EXP
     */
    INVALID_IDENTITIES_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "ids can't be empty or size can't be greater than " + DB_SELECT.value)),

    /**
     * DATA_NOT_EXIST_EXP
     */
    DATA_NOT_EXIST_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, DATA_NOT_EXIST.message)),

    /**
     * DATA_NOT_EXIST_EXP
     */
    DATA_ALREADY_EXIST_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, DATA_ALREADY_EXIST.message)),

    /**
     * INVALID_PAGE_PARAM_EXP
     */
    INVALID_PAGE_PARAM_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "page can't be less than 1, max rows per page can't be greater than " + MAX_ROWS.value)),

    /**
     * EMPTY_PARAM_EXP
     */
    INVALID_METADATA_PARAM_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_METADATA_PARAM.message)),

    /**
     * DATA_PARSED_EXP
     */
    DATA_PARSED_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "parse data failed")),

    /**
     * RSA_FAILED_EXP
     */
    CRYPT_FAILED_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, RSA_FAILED.message)),

    /**
     * DATA_WITHOUT_ALTERATION_EXP
     */
    DATA_WITHOUT_ALTERATION_EXP(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "data has no change"));

    public BlueException exp;

    CommonException(BlueException exp) {
        this.exp = exp;
    }

}
